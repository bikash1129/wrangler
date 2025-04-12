package io.cdap.wrangler.executor.directives;

import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.Row;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

  @Test
  public void testAggregateStatsInMBAndSeconds() throws Exception {
    List<Row> input = Arrays.asList(
      new Row("size", "10KB").add("time", "500ms"),
      new Row("size", "5MB").add("time", "2s"),
      new Row("size", "1.5MB").add("time", "1.2s")
    );

    String[] recipe = new String[] {
      "aggregate-stats :size :time 'total_size_mb' 'total_time_sec';"
    };

    List<Row> results = TestingRig.execute(recipe, input);

    Assert.assertEquals(1, results.size());
    Row result = results.get(0);

    double expectedMB = (10 * 1024 + 5 * 1024 * 1024 + (long)(1.5 * 1024 * 1024)) / (1024.0 * 1024.0);
    double expectedSec = (500 + 2000 + 1200) / 1000.0;

    Assert.assertEquals(expectedMB, (Double) result.getValue("total_size_mb"), 0.001);
    Assert.assertEquals(expectedSec, (Double) result.getValue("total_time_sec"), 0.001);
  }
}
