package io.cdap.wrangler.parser;

import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {

  @Test
  public void testTimeDurationParsing() {
    Assert.assertEquals(5, new TimeDuration("5ms").getMilliseconds());
    Assert.assertEquals(2000, new TimeDuration("2s").getMilliseconds());
    Assert.assertEquals((long)(2.1 * 1000), new TimeDuration("2.1s").getMilliseconds());
    Assert.assertEquals(60000, new TimeDuration("1min").getMilliseconds());
    Assert.assertEquals(3600000, new TimeDuration("1h").getMilliseconds());
  }
}
