package io.cmartinezs.authboot.commons;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * @author Carlos
 * @version 1.0
 */
@UtilityClass
public class DateUtils {

  public static boolean between(
      @NonNull LocalDateTime dateTime, @NonNull LocalDateTime from, @NonNull LocalDateTime to) {
    return dateTime.isAfter(from) && dateTime.isBefore(to);
  }

  public static boolean between(
      @NonNull LocalDate date, @NonNull LocalDate from, @NonNull LocalDate to) {
    return date.isAfter(from) && date.isBefore(to);
  }

  public static LocalDate toLocalDate(@NonNull LocalDateTime date) {
    return toLocalDate(date, ZoneId.systemDefault());
  }

  public static LocalDate toLocalDate(@NonNull LocalDateTime date, @NonNull ZoneId zoneId) {
    return date.atZone(zoneId).toLocalDate();
  }

  public static LocalTime toLocalTime(@NonNull LocalDateTime time) {
    return toLocalTime(time, ZoneId.systemDefault());
  }

  public static LocalTime toLocalTime(@NonNull LocalDateTime time, @NonNull ZoneId zoneId) {
    return time.atZone(zoneId).toLocalTime();
  }

  public static LocalDate toLocalDate(@NonNull Date date) {
    return toLocalDate(date, ZoneId.systemDefault());
  }

  public static LocalDate toLocalDate(@NonNull Date date, @NonNull ZoneId zoneId) {
    return toLocalDateTime(date, zoneId).toLocalDate();
  }

  public static LocalDateTime toLocalDateTime(@NonNull Date date) {
    return toLocalDateTime(date, ZoneId.systemDefault());
  }

  public static LocalDateTime toLocalDateTime(@NonNull Date date, @NonNull ZoneId zoneId) {
    return date.toInstant().atZone(zoneId).toLocalDateTime();
  }

  public static String format(@NonNull LocalDate date) {
    return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
  }

  public static String format(@NonNull LocalDateTime date) {
    return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date);
  }

  public static String format(@NonNull LocalDate date, @NonNull String format) {
    return DateTimeFormatter.ofPattern(format).format(date);
  }

  public static String format(@NonNull LocalDateTime date, @NonNull String format) {
    return DateTimeFormatter.ofPattern(format).format(date);
  }

  public static LocalDate parseLocalDate(@NonNull String formattedDate) {
    return LocalDate.parse(formattedDate, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static LocalDateTime parseLocalDateTime(@NonNull String formattedDate) {
    return LocalDateTime.parse(formattedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public static LocalDate parseLocalDate(@NonNull String formattedDate, @NonNull String format) {
    return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern(format));
  }

  public static LocalDateTime parseLocalDateTime(
      @NonNull String formattedDate, @NonNull String format) {
    return LocalDateTime.parse(formattedDate, DateTimeFormatter.ofPattern(format));
  }

  public static Date toDate(@NonNull LocalDate localDate) {
    return toDate(localDate, ZoneId.systemDefault());
  }

  public static Date toDate(@NonNull LocalDateTime localDateTime) {
    return toDate(localDateTime, ZoneId.systemDefault());
  }

  public static Date toDate(@NonNull LocalDate localDate, @NonNull ZoneId zoneId) {
    return Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant());
  }

  public static Date toDate(@NonNull LocalDateTime localDateTime, @NonNull ZoneId zoneId) {
    return Date.from(localDateTime.atZone(zoneId).toInstant());
  }
}
