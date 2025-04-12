package io.cdap.wrangler.api.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

import java.util.Locale;

public class TimeDuration implements Token {
  private final String original;
  private final long milliseconds;

  public TimeDuration(String value) {
    this.original = value;
    this.milliseconds = parseDuration(value);
  }

  private long parseDuration(String value) {
    value = value.trim().toLowerCase(Locale.ENGLISH);
    double number;
    if (value.endsWith("ms")) {
      number = Double.parseDouble(value.replace("ms", ""));
      return (long) number;
    } else if (value.endsWith("s")) {
      number = Double.parseDouble(value.replace("s", ""));
      return (long) (number * 1000);
    } else if (value.endsWith("min")) {
      number = Double.parseDouble(value.replace("min", ""));
      return (long) (number * 60 * 1000);
    } else if (value.endsWith("h")) {
      number = Double.parseDouble(value.replace("h", ""));
      return (long) (number * 60 * 60 * 1000);
    } else {
      throw new IllegalArgumentException("Unsupported time duration format: " + value);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("type", type().name());
    object.addProperty("value", original);
    object.addProperty("milliseconds", milliseconds);
    return object;
  }
}
