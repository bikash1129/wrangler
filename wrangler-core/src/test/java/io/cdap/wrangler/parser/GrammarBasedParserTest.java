/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under
 *  the License.
 */

package io.cdap.wrangler.parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.CompileStatus;
import io.cdap.wrangler.api.Compiler;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.RecipeParser;
import io.cdap.wrangler.api.RecipeSymbol;

/**
 * Tests {@link GrammarBasedParser}
 */
public class GrammarBasedParserTest {

  @Test
  public void testBasic() throws Exception {
    String[] recipe = new String[] {
      "#pragma version 2.0;",
      "rename :col1 :col2",
      "parse-as-csv :body ',' true;",
      "#pragma load-directives text-reverse, text-exchange;",
      "${macro} ${macro_2}",
      "${macro_${test}}",
      "aggregate-stats :size :duration 'total_bytes' 'total_time';"
    };

    RecipeParser parser = TestingRig.parse(recipe);
    List<Directive> directives = parser.parse();
    Assert.assertEquals(3, directives.size()); // rename, parse-as-csv, aggregate-stats
  }

  @Test
  public void testLoadableDirectives() throws Exception {
    String[] recipe = new String[] {
      "#pragma version 2.0;",
      "#pragma load-directives text-reverse, text-exchange;",
      "rename col1 col2",
      "parse-as-csv body , true",
      "text-reverse :body;",
      "test prop: { a='b', b=1.0, c=true};",
      "#pragma load-directives test-change,text-exchange, test1,test2,test3,test4;"
    };

    Compiler compiler = new RecipeCompiler();
    CompileStatus status = compiler.compile(new MigrateToV2(recipe).migrate());
    Assert.assertEquals(7, status.getSymbols().getLoadableDirectives().size());
  }

  @Test
  public void testCommentOnlyRecipe() throws Exception {
    String[] recipe = new String[] {
      "// test"
    };

    RecipeParser parser = TestingRig.parse(recipe);
    List<Directive> directives = parser.parse();
    Assert.assertEquals(0, directives.size());
  }

  // ✅ NEW TEST: Valid BYTE_SIZE and TIME_DURATION
  @Test
  public void testValidByteSizeAndTimeDurationParsing() throws Exception {
    String[] recipe = {
      "aggregate-stats :size :duration 'total_size_mb' 'total_time_sec';"
    };

    RecipeSymbol parsed = TestingRig.parse(recipe).compile();
    Assert.assertNotNull(parsed);
    Assert.assertTrue(parsed.toString().contains("aggregate-stats"));
    Assert.assertTrue(parsed.toString().contains("total_size_mb"));
    Assert.assertTrue(parsed.toString().contains("total_time_sec"));
  }

  // ✅ NEW TEST: Invalid syntax should fail
  @Test(expected = Exception.class)
  public void testInvalidByteSizeFails() throws Exception {
    String[] recipe = {
      "aggregate-stats :size 123XY 'bad_bytes' 'bad_time';"
    };

    TestingRig.parse(recipe).compile();
  }
}
