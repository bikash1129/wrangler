package io.cdap.wrangler.executor.directives;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.parser.UsageDefinition;
import java.util.Collections;
import java.util.List;

/**
 * A directive to aggregate byte size and time duration fields across rows.
 */
@Public
public class AggregateStats implements Directive {
  private String byteColumn;
  private String timeColumn;
  private String outputByteColumn;
  private String outputTimeColumn;
  private long totalBytes = 0L;
  private long totalMillis = 0L;
  private int rowCount = 0;
  private boolean isFinal = false;

  @Override
  public UsageDefinition define() {
    return UsageDefinition.builder("aggregate-stats")
      .define("byte_column", TokenType.COLUMN)
      .define("time_column", TokenType.COLUMN)
      .define("output_byte_column", TokenType.IDENTIFIER)
      .define("output_time_column", TokenType.IDENTIFIER)
      .build();
  }

  @Override
  public void initialize(ExecutorContext context, List<Token> args) throws Exception {
    this.byteColumn = ((ColumnName) args.get(0)).value().toString();
    this.timeColumn = ((ColumnName) args.get(1)).value().toString();
    this.outputByteColumn = ((Text) args.get(2)).value().toString();
    this.outputTimeColumn = ((Text) args.get(3)).value().toString();
  }

  @Override
  public List<Row> execute(Row row, ExecutorContext context) throws Exception {
    if (isFinal) {
      throw new SkipRowException();
    }

    Object byteObj = row.getValue(byteColumn);
    Object timeObj = row.getValue(timeColumn);

    long bytes = parseByteValue(byteObj);
    long millis = parseTimeValue(timeObj);

    totalBytes += bytes;
    totalMillis += millis;
    rowCount++;

    throw new SkipRowException(); // Skip individual row outputs; we only return 1 row in finalize
  }

  @Override
  public List<Row> finalize(ExecutorContext context) {
    isFinal = true;

    Row result = new Row();
    result.add(outputByteColumn, convertBytesToMB(totalBytes)); // Or keep as bytes
    result.add(outputTimeColumn, convertMillisToSeconds(totalMillis));

    return Collections.singletonList(result);
  }

  private long parseByteValue(Object val) {
    if (val instanceof Number) return ((Number) val).longValue();
    String str = val.toString().trim().toLowerCase();
    if (str.endsWith("kb")) return (long) (Double.parseDouble(str.replace("kb", "")) * 1024);
    if (str.endsWith("mb")) return (long) (Double.parseDouble(str.replace("mb", "")) * 1024 * 1024);
    if (str.endsWith("gb")) return (long) (Double.parseDouble(str.replace("gb", "")) * 1024 * 1024 * 1024);
    if (str.endsWith("b")) return (long) Double.parseDouble(str.replace("b", ""));
    return Long.parseLong(str);
  }

  private long parseTimeValue(Object val) {
    if (val instanceof Number) return ((Number) val).longValue();
    String str = val.toString().trim().toLowerCase();
    if (str.endsWith("ms")) return (long) Double.parseDouble(str.replace("ms", ""));
    if (str.endsWith("s")) return (long) (Double.parseDouble(str.replace("s", "")) * 1000);
    if (str.endsWith("min")) return (long) (Double.parseDouble(str.replace("min", "")) * 60000);
    if (str.endsWith("h")) return (long) (Double.parseDouble(str.replace("h", "")) * 3600000);
    return Long.parseLong(str);
  }

  private double convertBytesToMB(long bytes) {
    return bytes / (1024.0 * 1024.0);
  }

  private double convertMillisToSeconds(long millis) {
    return millis / 1000.0;
  }
}
