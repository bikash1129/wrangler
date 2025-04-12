package io.cdap.wrangler.api.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

import java.util.Locale;

public class ByteSize implements Token {
  private final String original;
  private final long bytes;

  public ByteSize(String value) {
    this.original = value;
    this.bytes = parseBytes(value);
  }

  private long parseBytes(String value) {
    value = value.trim().toLowerCase(Locale.ENGLISH);
    double number;
    if (value.endsWith("kb")) {
      number = Double.parseDouble(value.replace("kb", ""));
      return (long) (number * 1024);
    } else if (value.endsWith("mb")) {
      number = Double.parseDouble(value.replace("mb", ""));
      return (long) (number * 1024 * 1024);
    } else if (value.endsWith("gb")) {
      number = Double.parseDouble(value.replace("gb", ""));
      return (long) (number * 1024 * 1024 * 1024);
    } else if (value.endsWith("tb")) {
      number = Double.parseDouble(value.replace("tb", ""));
      return (long) (number * 1024L * 1024 * 1024 * 1024);
    } else if (value.endsWith("b")) {
      number = Double.parseDouble(value.replace("b", ""));
      return (long) number;
    } else {
      throw new IllegalArgumentException("Unsupported byte size format: " + value);
    }
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("type", type().name());
    object.addProperty("value", original);
    object.addProperty("bytes", bytes);
    return object;
  }
}
