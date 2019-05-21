/*
 * Copyright 2019 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.core.util;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import java.util.Date;

/**
 * The <code>ISO8601Util</code> class provides a helper class for handling ISO 8601 strings of the
 * following format: "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public final class ISO8601Util
{
  private static final ThreadLocal<DateTimeFormatter> localDateTimeFormatter =
      ThreadLocal.withInitial(() -> (new DateTimeFormatterBuilder().appendPattern(
      "yyyy-MM-dd'T'HH:mm:ss").parseLenient().optionalStart().appendPattern(".SSS").optionalEnd()
      .optionalStart().appendOffset("+HH:MM", "Z").optionalEnd().toFormatter()));
  private static final ThreadLocal<DateTimeFormatter> localDateFormatter = ThreadLocal.withInitial(
      () -> DateTimeFormatter.ISO_DATE);
  private static final ThreadLocal<DateTimeFormatter> localTimeFormatter = ThreadLocal.withInitial(
      () -> DateTimeFormatter.ISO_TIME);
  private static final ThreadLocal<SimpleDateFormat> dateTimeFormatter = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
  private static final ThreadLocal<DateTimeFormatter> dateInputFormatter = ThreadLocal.withInitial(
      () -> (new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").parseLenient()
      .optionalStart().appendPattern("'T'HH:mm:ss").optionalEnd().optionalStart().appendPattern(
      ".SSS").optionalEnd().optionalStart().appendOffset("+HH:MM", "Z").optionalEnd()
      .toFormatter()));
  private static final ThreadLocal<SimpleDateFormat> dateFormatter = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd"));

  /**
   * Transform the <code>Date</code> instance into an ISO 8601 string.
   *
   * @param date the <code>Date</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>Date</code> instance
   */
  public static String fromDate(Date date)
  {
    return dateFormatter.get().format(date);
  }

  /**
   * Transform the <code>Date</code> instance into an ISO 8601 string.
   *
   * @param date the <code>Date</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>Date</code> instance
   */
  public static String fromDateTime(Date date)
  {
    return dateTimeFormatter.get().format(date);
  }

  /**
   * Transform the <code>LocalDate</code> instance into an ISO 8601 string.
   *
   * @param localDate the <code>LocalDate</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>LocalDate</code> instance
   */
  public static String fromLocalDate(LocalDate localDate)
  {
    return localDate.format(localDateFormatter.get());
  }

  /**
   * Transform the <code>LocalDateTime</code> instance into an ISO 8601 string.
   *
   * @param localDateTime the <code>LocalDateTime</code> instance to transform into an ISO 8601
   *                      string
   *
   * @return the ISO 8601 string for the <code>LocalDateTime</code> instance
   */
  public static String fromLocalDateTime(LocalDateTime localDateTime)
  {
    return ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).format(
        localDateTimeFormatter.get());
  }

  /**
   * Transform the <code>LocalTime</code> instance into an ISO 8601 string.
   *
   * @param localTime the <code>LocalTime</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>LocalTime</code> instance
   */
  public static String fromLocalTime(LocalTime localTime)
  {
    return localTime.format(localTimeFormatter.get());
  }

  /**
   * Transform the <code>ZonedDateTime</code> instance into an ISO 8601 string.
   *
   * @param zonedDateTime the <code>ZonedDateTime</code> instance to transform into an ISO 8601
   *                      string
   *
   * @return the ISO 8601 string for the <code>ZonedDateTime</code> instance
   */
  public static String fromZonedDateTime(ZonedDateTime zonedDateTime)
  {
    return zonedDateTime.format(localDateTimeFormatter.get());
  }

  /**
   * Main.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    System.out.println("fromDate(new Date()) = " + fromDate(new Date()));

    System.out.println("fromDateTime(new Date()) = " + fromDateTime(new Date()));

    System.out.println("fromLocalDate(LocalDate.now()) = " + fromLocalDate(LocalDate.now()));

    System.out.println("fromLocalDateTime(LocalDateTime.now()) = " + fromLocalDateTime(
        LocalDateTime.now()));

    System.out.println("fromZonedDateTime(ZonedDateTime.now(ZoneOffset.UTC)) = "
        + fromZonedDateTime(ZonedDateTime.now(ZoneOffset.UTC)));

    System.out.println("fromZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault())) = "
        + fromZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault())));

    System.out.println("Date 2017-08-14 = " + toDate("2017-08-14"));

    System.out.println("Date 2017-08-14T19:14:53.120Z = " + toDate("2017-08-14T19:14:53.120Z"));

    System.out.println("Date 2017-08-14T22:14:53.120+02:00 = " + toDate(
        "2017-08-14T22:14:53.120+02:00"));

    System.out.println("Date 2017-08-14T19:14:53.120 = " + toDate("2017-08-14T19:14:53.120"));

    System.out.println("Date 2017-08-14T19:14:53 = " + toDate("2017-08-14T19:14:53"));

    System.out.println("Local Date Time 2017-08-14T19:14:53.120Z = " + toLocalDateTime(
        "2017-08-14T19:14:53.120Z"));

    System.out.println("Local Date Time 2017-08-14T22:14:53.120+02:00 = " + toLocalDateTime(
        "2017-08-14T22:14:53.120+02:00"));

    System.out.println("Local Date Time 2017-08-14T19:14:53.120 = " + toLocalDateTime(
        "2017-08-14T19:14:53.120"));

    System.out.println("Local Date Time 2017-08-14T19:14:53 = " + toLocalDateTime(
        "2017-08-14T19:14:53"));

    System.out.println("Zoned Date Time 2017-08-14T19:14:53.120Z = " + toZonedDateTime(
        "2017-08-14T19:14:53.120Z"));

    System.out.println("Zoned Date Time 2017-08-14T22:14:53.120+02:00 = " + toZonedDateTime(
        "2017-08-14T22:14:53.120+02:00"));

    System.out.println("Zoned Date Time 2017-08-14T19:14:53.120 = " + toZonedDateTime(
        "2017-08-14T19:14:53.120"));

    System.out.println("Zoned Date Time 2017-08-14T19:14:53 = " + toZonedDateTime(
        "2017-08-14T19:14:53"));
  }

  /**
   * Get current date and time formatted as ISO 8601 string.
   *
   * @return the current date and time formatted as ISO 8601 string
   */
  public static String now()
  {
    return fromLocalDateTime(LocalDateTime.now());
  }

  /**
   * Transform ISO 8601 string into a <code>LocalDate</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalDate</code> instance for the ISO 8601 string
   */
  public static Date toDate(String iso8601string)
  {
    TemporalAccessor temporalAccessor = dateInputFormatter.get().parse(iso8601string);

    if (!temporalAccessor.isSupported(ChronoField.HOUR_OF_DAY))
    {
      LocalDate localDate = LocalDate.parse(iso8601string, localDateFormatter.get());

      return java.sql.Date.valueOf(localDate);
    }
    else if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS))
    {
      ZonedDateTime zonedDateTime = ZonedDateTime.parse(iso8601string, localDateTimeFormatter.get())
          .withZoneSameInstant(ZoneId.systemDefault());

      return Date.from(zonedDateTime.toInstant());
    }
    else
    {
      LocalDateTime localDateTime = LocalDateTime.parse(iso8601string,
          localDateTimeFormatter.get());

      return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
  }

  /**
   * Transform ISO 8601 string into a <code>LocalDate</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalDate</code> instance for the ISO 8601 string
   */
  public static LocalDate toLocalDate(String iso8601string)
  {
    return LocalDate.parse(iso8601string, localDateFormatter.get());
  }

  /**
   * Transform ISO 8601 string into a <code>LocalDateTime</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalDateTime</code> instance for the ISO 8601 string
   */
  public static LocalDateTime toLocalDateTime(String iso8601string)
  {
    TemporalAccessor temporalAccessor = localDateTimeFormatter.get().parse(iso8601string);

    if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS))
    {
      return ZonedDateTime.parse(iso8601string, localDateTimeFormatter.get()).withZoneSameInstant(
          ZoneId.systemDefault()).toLocalDateTime();
    }
    else
    {
      return LocalDateTime.parse(iso8601string, localDateTimeFormatter.get());
    }
  }

  /**
   * Transform ISO 8601 string into a <code>LocalTime</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalTime</code> instance for the ISO 8601 string
   */
  public static LocalTime toLocalTime(String iso8601string)
  {
    return LocalTime.parse(iso8601string, localTimeFormatter.get());
  }

  /**
   * Transform ISO 8601 string into a <code>ZonedDateTime</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>ZonedDateTime</code> instance for the ISO 8601 string
   */
  public static ZonedDateTime toZonedDateTime(String iso8601string)
  {
    TemporalAccessor temporalAccessor = localDateTimeFormatter.get().parse(iso8601string);

    if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS))
    {
      return ZonedDateTime.parse(iso8601string, localDateTimeFormatter.get()).withZoneSameInstant(
          ZoneId.systemDefault());
    }
    else
    {
      return LocalDateTime.parse(iso8601string, localDateTimeFormatter.get()).atZone(
          ZoneId.systemDefault());
    }
  }
}
