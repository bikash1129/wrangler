package io.cdap.wrangler.parser;

import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {

  @Test
  public void testByteSizeParsing() {
    Assert.assertEquals(10 * 1024, new ByteSize("10KB").getBytes());
    Assert.assertEquals(1024 * 1024, new ByteSize("1MB").getBytes());
    Assert.assertEquals((long)(1.5 * 1024 * 1024), new ByteSize("1.5MB").getBytes());
    Assert.assertEquals(1L, new ByteSize("1b").getBytes());
    Assert.assertEquals(1024L * 1024 * 1024, new ByteSize("1GB").getBytes());
  }
}
