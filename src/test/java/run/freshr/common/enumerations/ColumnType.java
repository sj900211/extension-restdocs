package run.freshr.common.enumerations;

import lombok.Getter;

/**
 * Database Column 유형
 *
 * @author FreshR
 * @apiNote Database Column 유형 정의
 * @since 2024. 3. 29. 오후 1:49:37
 */
@Getter
public enum ColumnType {

  UNKNOWN(null, null),
  FLOAT("-9999999999 to 9999999999", null),
  DOUBLE("-9999999999 to 9999999999", null),
  DECIMAL("-9999999999 to 9999999999", null),
  DATE(null, "yyyy-MM-dd"),
  TIME(null, "hh:mm:ss.f"),
  DATETIME(null, "yyyy-MM-dd hh:mm:ss.f"),
  TINYINT(null, "TRUE or FALSE"),
  SMALLINT("-32768 to 32767", null),
  VARCHAR("255 characters", null),
  BIGINT("-9223372036854775808 to 9223372036854775807", null),
  INT("-2147483648 to 2147483647", null),
  BIT(null, "TRUE or FALSE"),
  LONGTEXT("4GB or 4,294,967,295 characters", null),
  BLOB("65,535 byte", null);

  /**
   * 기본 크기
   *
   * @apiNote 해당 유형의 기본 length
   * @since 2024. 3. 29. 오후 1:49:37
   */
  private final String size;
  /**
   * 포맷
   *
   * @apiNote 해당 유형의 기본 format pattern
   * @since 2024. 3. 29. 오후 1:49:37
   */
  private final String format;

  ColumnType(String size, String format) {
    this.size = size;
    this.format = format;
  }

}
