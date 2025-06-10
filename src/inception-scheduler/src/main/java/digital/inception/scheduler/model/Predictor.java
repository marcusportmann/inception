/*
 * Copyright Marcus Portmann
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

package digital.inception.scheduler.model;

import digital.inception.scheduler.exception.InvalidSchedulingPatternException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * The {@code Predictor} class provides the capability to predict when a scheduling pattern will be
 * matched.
 *
 * <p>Suppose you want to know when the scheduler will execute a job scheduled with the pattern
 * <em>0 3 * jan-jun,sep-dec mon-fri</em>. You can predict the next <em>n</em> execution of the job
 * using a Predictor instance:
 *
 * <pre>
 * String pattern = &quot;0 3 * jan-jun,sep-dec mon-fri&quot;;
 * Predictor p = new Predictor(pattern);
 * for (int i = 0; i &lt; n; i++) {
 *  System.out.println(p.nextMatchingDate());
 * }
 * </pre>
 *
 * @author Carlo Pelliccia
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Predictor {

  /** The scheduling pattern on which the predictor works. */
  private final SchedulingPattern schedulingPattern;

  /** The start time for the next prediction. */
  private long time;

  /** The time zone for the prediction. */
  private TimeZone timeZone = TimeZone.getDefault();

  /**
   * Constructs a new {@code SchedulingPattern}.
   *
   * @param schedulingPattern the scheduling pattern on which the prediction will be based
   */
  public Predictor(SchedulingPattern schedulingPattern) {
    this(schedulingPattern, System.currentTimeMillis());
  }

  /**
   * Constructs a new {@code SchedulingPattern}.
   *
   * @param schedulingPattern the scheduling pattern on which the prediction will be based
   * @throws InvalidSchedulingPatternException if the scheduling pattern is invalid
   */
  public Predictor(String schedulingPattern) throws InvalidSchedulingPatternException {
    this(schedulingPattern, System.currentTimeMillis());
  }

  /**
   * Constructs a new {@code SchedulingPattern}.
   *
   * @param schedulingPattern the scheduling pattern on which the prediction will be based
   * @param start the start time of the prediction
   */
  public Predictor(SchedulingPattern schedulingPattern, Date start) {
    this(schedulingPattern, start.getTime());
  }

  /**
   * It builds a predictor with the given scheduling pattern and start time.
   *
   * @param schedulingPattern the scheduling pattern on which the prediction will be based
   * @param start the start time of the prediction as a milliseconds value
   */
  public Predictor(SchedulingPattern schedulingPattern, long start) {
    this.schedulingPattern = schedulingPattern;
    this.time = (start / (1000 * 60)) * 1000 * 60;
  }

  /**
   * It builds a predictor with the given scheduling pattern and start time.
   *
   * @param schedulingPattern the pattern on which the prediction will be based
   * @param start the start time of the prediction
   * @throws InvalidSchedulingPatternException if the scheduling pattern is invalid
   */
  public Predictor(String schedulingPattern, Date start) throws InvalidSchedulingPatternException {
    this(schedulingPattern, start.getTime());
  }

  /**
   * It builds a predictor with the given scheduling pattern and start time.
   *
   * @param schedulingPattern the pattern on which the prediction will be based
   * @param start the start time of the prediction
   * @throws InvalidSchedulingPatternException if the scheduling pattern is invalid
   */
  public Predictor(String schedulingPattern, long start) throws InvalidSchedulingPatternException {
    this.schedulingPattern = new SchedulingPattern(schedulingPattern);
    this.time = (start / (1000 * 60)) * 1000 * 60;
  }

  /**
   * Returns the next matching moment as a {@link Date} object.
   *
   * @return the next matching moment as a {@link Date} object
   */
  public synchronized Date nextMatchingDate() {
    return new Date(nextMatchingTime());
  }

  /**
   * Returns the next matching moment as a {@link OffsetDateTime} object.
   *
   * @return the next matching moment as a {@link OffsetDateTime} object
   */
  public synchronized OffsetDateTime nextMatchingOffsetDateTime() {
    return OffsetDateTime.ofInstant(
        Instant.ofEpochMilli(nextMatchingTime()), ZoneId.systemDefault());
  }

  /**
   * Returns the next matching moment as a milliseconds value.
   *
   * @return the next matching moment as a milliseconds value
   */
  public synchronized long nextMatchingTime() {
    // Go a minute ahead.
    time += 60000;

    // Is it matching?
    if (schedulingPattern.match(time)) {
      return time;
    }

    // Go through the matcher groups.
    int size = schedulingPattern.matcherSize;
    long[] times = new long[size];

    for (int k = 0; k < size; k++) {
      // Ok, split the time!
      GregorianCalendar c = new GregorianCalendar();

      c.setTimeInMillis(time);
      c.setTimeZone(timeZone);

      int minute = c.get(Calendar.MINUTE);
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
      int month = c.get(Calendar.MONTH);
      int year = c.get(Calendar.YEAR);

      // Gets the matchers.
      ValueMatcher minuteMatcher = schedulingPattern.minuteMatchers.get(k);
      ValueMatcher hourMatcher = schedulingPattern.hourMatchers.get(k);
      ValueMatcher dayOfMonthMatcher = schedulingPattern.dayOfMonthMatchers.get(k);
      ValueMatcher dayOfWeekMatcher = schedulingPattern.dayOfWeekMatchers.get(k);
      ValueMatcher monthMatcher = schedulingPattern.monthMatchers.get(k);

      for (; ; ) { // day of week
        for (; ; ) { // month
          for (; ; ) { // day of month
            for (; ; ) { // hour
              for (; ; ) { // minutes
                if (minuteMatcher.match(minute)) {
                  break;
                } else {
                  minute++;

                  if (minute > 59) {
                    minute = 0;
                    hour++;
                  }
                }
              }

              if (hour > 23) {
                hour = 0;
                dayOfMonth++;
              }

              if (hourMatcher.match(hour)) {
                break;
              } else {
                hour++;
                minute = 0;
              }
            }

            if (dayOfMonth > 31) {
              dayOfMonth = 1;
              month++;
            }

            if (month > Calendar.DECEMBER) {
              month = Calendar.JANUARY;
              year++;
            }

            if (dayOfMonthMatcher instanceof DayOfMonthValueMatcher aux) {

              if (aux.match(dayOfMonth, month + 1, c.isLeapYear(year))) {
                break;
              } else {
                dayOfMonth++;
                hour = 0;
                minute = 0;
              }
            } else if (dayOfMonthMatcher.match(dayOfMonth)) {
              break;
            } else {
              dayOfMonth++;
              hour = 0;
              minute = 0;
            }
          }

          if (monthMatcher.match(month + 1)) {
            break;
          } else {
            month++;
            dayOfMonth = 1;
            hour = 0;
            minute = 0;
          }
        }

        // Is this ok?
        c = new GregorianCalendar();
        c.setTimeZone(timeZone);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);

        // Day-of-month/month/year compatibility check.
        int oldDayOfMonth = dayOfMonth;
        int oldMonth = month;
        int oldYear = year;

        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        if ((month != oldMonth) || (dayOfMonth != oldDayOfMonth) || (year != oldYear)) {
          // Take another spin!
          continue;
        }

        // Day of week.
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeekMatcher.match(dayOfWeek - 1)) {
          break;
        } else {
          dayOfMonth++;
          hour = 0;
          minute = 0;

          if (dayOfMonth > 31) {
            dayOfMonth = 1;
            month++;

            if (month > Calendar.DECEMBER) {
              month = Calendar.JANUARY;
              year++;
            }
          }
        }
      }

      // Seems it matches!
      times[k] = (c.getTimeInMillis() / (1000 * 60)) * 1000 * 60;
    }

    // Which one?
    long min = Long.MAX_VALUE;

    for (int k = 0; k < size; k++) {
      if (times[k] < min) {
        min = times[k];
      }
    }

    // Updates the object current time value.
    time = min;

    // Here it is.
    return time;
  }

  /**
   * Set the time zone for predictions.
   *
   * @param timeZone the time zone to user for predictions
   */
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * The {@code ValueMatcher} describes the behavior that must be implemented by a ValueMatcher.
   *
   * <p>A ValueMatcher is an object that validate an integer value against a set of rules.
   *
   * @author Carlo Pelliccia
   * @author Marcus Portmann
   */
  public interface ValueMatcher {

    /**
     * Validate the given integer value against a set of rules.
     *
     * @param value the value
     * @return {@code true} if the given value matches the rules of the {@code ValueMatcher} ,
     *     {@code false} otherwise
     */
    boolean match(int value);
  }

  /**
   * The {@code AlwaysTrueValueMatcher} class implements a {@code ValueMatcher} that always returns
   * {@code true}.
   *
   * @author Carlo Pelliccia
   * @author Marcus Portmann
   */
  public static class AlwaysTrueValueMatcher implements ValueMatcher {

    /** Constructs a new {@code AlwaysTrueValueMatcher}. */
    public AlwaysTrueValueMatcher() {}

    /**
     * Validate the given integer value against a set of rules.
     *
     * @param value the value
     * @return {@code true} if the given value matches the rules of the {@code ValueMatcher} ,
     *     {@code false} otherwise
     */
    public boolean match(int value) {
      return true;
    }
  }

  /**
   * The {@code DayOfMonthValueMatcher} class implements a {@code ValueMatcher} whose rules are in a
   * plain array of integer values.
   *
   * <p>When asked to validate a value, this ValueMatcher checks if it is in the array and, if not,
   * checks whether the last-day-of-month setting applies.
   *
   * @author Paul Fernley
   * @author Marcus Portmann
   */
  @SuppressWarnings({"unused", "WeakerAccess"})
  public static class DayOfMonthValueMatcher extends IntArrayValueMatcher {

    private static final int[] lastDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * Constructs a new {@code DayOfMonthValueMatcher}.
     *
     * @param integers The Integer elements, one for every value accepted by the matcher. The
     *     match() method will return {@code true} only if its parameter will be one of this list or
     *     the last-day-of-month setting applies.
     */
    public DayOfMonthValueMatcher(List<Integer> integers) {
      super(integers);
    }

    /**
     * Returns {@code true} if the given value is included in the matcher list or the
     * last-day-of-month setting applies, otherwise {@code false}.
     *
     * @param value the value
     * @param month the month
     * @param isLeapYear {@code true} if this is a leap year {@code false} otherwise
     * @return {@code true} if the given value matches the rules of the {@code ValueMatcher} ,
     *     {@code false} otherwise
     */
    public boolean match(int value, int month, boolean isLeapYear) {
      return (super.match(value)
          || ((value > 27) && match(32) && isLastDayOfMonth(value, month, isLeapYear)));
    }

    private boolean isLastDayOfMonth(int value, int month, boolean isLeapYear) {
      if (isLeapYear && (month == 2)) {
        return value == 29;
      } else {
        return value == lastDays[month - 1];
      }
    }
  }

  /**
   * The {@code IntArrayValueMatcher} class implements a {@code ValueMatcher} whose rules are in a
   * plain array of integer values.
   *
   * <p>When asked to validate a value, this ValueMatcher checks if it is in the array.
   *
   * @author Carlo Pelliccia
   * @author Marcus Portmann
   */
  public static class IntArrayValueMatcher implements ValueMatcher {

    /** The accepted values. */
    private final int[] values;

    /**
     * Builds the ValueMatcher.
     *
     * @param integers The Integer elements, one for every value accepted by the matcher. The
     *     match() method will return true only if its parameter will be one of this list.
     */
    IntArrayValueMatcher(List<Integer> integers) {
      int size = integers.size();

      values = new int[size];

      for (int i = 0; i < size; i++) {
        try {
          values[i] = integers.get(i);
        } catch (Exception e) {
          throw new IllegalArgumentException(e.getMessage());
        }
      }
    }

    /**
     * Validate the given integer value against a set of rules.
     *
     * <p>Returns {@code true} if the given value is included in the matcher list.
     *
     * @param value the value
     * @return {@code true} if the given value matches the rules of the {@code ValueMatcher} ,
     *     {@code false} otherwise
     */
    public boolean match(int value) {
      for (int value1 : values) {
        if (value1 == value) {
          return true;
        }
      }

      return false;
    }
  }

  /**
   * The {@code SchedulingPattern} class supports a UNIX crontab-like pattern is a string split in
   * five space separated parts.
   *
   * <p>Each part is intended as:
   *
   * <ol>
   *   <li><strong>Minutes sub-pattern</strong>. During which minutes of the hour should the job
   *       been launched? The values range is from 0 to 59.
   *   <li><strong>Hours sub-pattern</strong>. During which hours of the day should the job been
   *       launched? The values range is from 0 to 23.
   *   <li><strong>Days of month sub-pattern</strong>. During which days of the month should the job
   *       been launched? The values range is from 1 to 31. The special value L can be used to
   *       recognize the last day of month.
   *   <li><strong>Months sub-pattern</strong>. During which months of the year should the job been
   *       launched? The values range is from 1 (January) to 12 (December), otherwise this
   *       sub-pattern allows the aliases &quot;jan&quot;, &quot;feb&quot;, &quot;mar&quot;,
   *       &quot;apr&quot;, &quot;may&quot;, &quot;jun&quot;, &quot;jul&quot;, &quot;aug&quot;,
   *       &quot;sep&quot;, &quot;oct&quot;, &quot;nov&quot; and &quot;dec&quot;.
   *   <li><strong>Days of week sub-pattern</strong>. During which days of the week should the job
   *       been launched? The values range is from 0 (Sunday) to 6 (Saturday), otherwise this
   *       sub-pattern allows the aliases &quot;sun&quot;, &quot;mon&quot;, &quot;tue&quot;,
   *       &quot;wed&quot;, &quot;thu&quot;, &quot;fri&quot; and &quot;sat&quot;.
   * </ol>
   *
   * <p>The star wildcard character is also admitted, indicating &quot;every minute of the
   * hour&quot;, &quot;every hour of the day&quot;, &quot;every day of the month&quot;, &quot;every
   * month of the year&quot; and &quot;every day of the week&quot;, according to the sub-pattern in
   * which it is used.
   *
   * <p>Once the scheduler is started, a job will be launched when the five parts in its scheduling
   * pattern will be true at the same time.
   *
   * <p>Some examples:
   *
   * <p><strong>5 * * * *</strong><br>
   * This pattern causes a job to be launched once every hour, at the begin of the fifth minute
   * (00:05, 01:05, 02:05 etc.).
   *
   * <p><strong>* * * * *</strong><br>
   * This pattern causes a job to be launched every minute.
   *
   * <p><strong>* 12 * * Mon</strong><br>
   * This pattern causes a job to be launched every minute during the 12th hour of Monday.
   *
   * <p><strong>* 12 16 * Mon</strong><br>
   * This pattern causes a job to be launched every minute during the 12th hour of Monday, 16th, but
   * only if the day is the 16th of the month.
   *
   * <p>Every sub-pattern can contain two or more comma separated values.
   *
   * <p><strong>59 11 * * 1,2,3,4,5</strong><br>
   * This pattern causes a job to be launched at 11:59AM on Monday, Tuesday, Wednesday, Thursday and
   * Friday.
   *
   * <p>Values intervals are admitted and defined using the minus character.
   *
   * <p><strong>59 11 * * 1-5</strong><br>
   * This pattern is equivalent to the previous one.
   *
   * <p>The slash character can be used to identify step values within a range. It can be used both
   * in the form <em>*&#47;c</em> and <em>a-b/c</em>. The subpattern is matched every <em>c</em>
   * values of the range <em>0,maxvalue</em> or <em>a-b</em>.
   *
   * <p><strong>*&#47;5 * * * *</strong><br>
   * This pattern causes a job to be launched every 5 minutes (0:00, 0:05, 0:10, 0:15 and so on).
   *
   * <p><strong>3-18&#47;5 * * * *</strong><br>
   * This pattern causes a job to be launched every 5 minutes starting from the third minute of the
   * hour, up to the 18th (0:03, 0:08, 0:13, 0:18, 1:03, 1:08 and so on).
   *
   * <p><strong>*&#47;15 9-17 * * *</strong><br>
   * This pattern causes a job to be launched every 15 minutes between the 9th and 17th hour of the
   * day (9:00, 9:15, 9:30, 9:45 and so on... note that the last execution will be at 17:45).
   *
   * <p>All the fresh described syntax rules can be used together.
   *
   * <p><strong>* 12 10-16&#47;2 * *</strong><br>
   * This pattern causes a job to be launched every minute during the 12th hour of the day, but only
   * if the day is the 10th, the 12th, the 14th or the 16th of the month.
   *
   * <p><strong>* 12 1-15,17,20-25 * *</strong><br>
   * This pattern causes a job to be launched every minute during the 12th hour of the day, but the
   * day of the month must be between the 1st and the 15th, the 20th and the 25, or at least it must
   * be the 17th.
   *
   * <p>Finally cron4j lets you combine more scheduling patterns into one, with the pipe character:
   *
   * <p><strong>0 5 * * *|8 10 * * *|22 17 * * *</strong><br>
   * This pattern causes a job to be launched every day at 05:00, 10:08 and 17:22.
   *
   * @author Carlo Pelliccia
   * @author Marcus Portmann
   */
  @SuppressWarnings({"unused", "WeakerAccess"})
  public static class SchedulingPattern {

    /** The parser for the day of month values. */
    private static final ValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();

    /** The parser for the day of week values. */
    private static final ValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();

    /** The parser for the hour values. */
    private static final ValueParser HOUR_VALUE_PARSER = new HourValueParser();

    /** The parser for the minute values. */
    private static final ValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();

    /** The parser for the month values. */
    private static final ValueParser MONTH_VALUE_PARSER = new MonthValueParser();

    /** The pattern as a string. */
    private final String asString;

    /** The ValueMatcher list for the "day of month" field. */
    protected List<ValueMatcher> dayOfMonthMatchers = new ArrayList<>();

    /** The ValueMatcher list for the "day of week" field. */
    protected List<ValueMatcher> dayOfWeekMatchers = new ArrayList<>();

    /** The ValueMatcher list for the "hour" field. */
    protected List<ValueMatcher> hourMatchers = new ArrayList<>();

    /** How many matcher groups in this pattern? */
    protected int matcherSize = 0;

    /** The ValueMatcher list for the "minute" field. */
    protected List<ValueMatcher> minuteMatchers = new ArrayList<>();

    /** The ValueMatcher list for the "month" field. */
    protected List<ValueMatcher> monthMatchers = new ArrayList<>();

    /**
     * Builds a SchedulingPattern by parsing it from a string.
     *
     * @param pattern the pattern as a crontab-like string
     * @throws InvalidSchedulingPatternException if the scheduling pattern is invalid
     */
    public SchedulingPattern(String pattern) throws InvalidSchedulingPatternException {
      this.asString = pattern;

      StringTokenizer st1 = new StringTokenizer(pattern, "|");

      if (st1.countTokens() < 1) {
        throw new InvalidSchedulingPatternException("Invalid pattern: \"%s\"".formatted(pattern));
      }

      while (st1.hasMoreTokens()) {
        String localPattern = st1.nextToken();
        StringTokenizer st2 = new StringTokenizer(localPattern, " \t");

        if (st2.countTokens() != 5) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern: \"%s\"".formatted(localPattern));
        }

        try {
          minuteMatchers.add(buildValueMatcher(st2.nextToken(), MINUTE_VALUE_PARSER));
        } catch (Exception e) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern \"%s\". Error parsing minutes field".formatted(localPattern), e);
        }

        try {
          hourMatchers.add(buildValueMatcher(st2.nextToken(), HOUR_VALUE_PARSER));
        } catch (Exception e) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern \"%s\". Error parsing hours field".formatted(localPattern), e);
        }

        try {
          dayOfMonthMatchers.add(buildValueMatcher(st2.nextToken(), DAY_OF_MONTH_VALUE_PARSER));
        } catch (Exception e) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern \"%s\". Error parsing days of month field".formatted(localPattern),
              e);
        }

        try {
          monthMatchers.add(buildValueMatcher(st2.nextToken(), MONTH_VALUE_PARSER));
        } catch (Exception e) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern \"%s\". Error parsing months field".formatted(localPattern), e);
        }

        try {
          dayOfWeekMatchers.add(buildValueMatcher(st2.nextToken(), DAY_OF_WEEK_VALUE_PARSER));
        } catch (Exception e) {
          throw new InvalidSchedulingPatternException(
              "Invalid pattern \"%s\". Error parsing days of week field".formatted(localPattern),
              e);
        }

        matcherSize++;
      }
    }

    /**
     * Validates a string as a scheduling pattern.
     *
     * @param schedulingPattern the pattern to validate
     * @return {@code true} if the given string represents a valid scheduling pattern or {@code
     *     false} otherwise
     */
    @SuppressWarnings("unused")
    public static boolean validate(String schedulingPattern) {
      try {
        new SchedulingPattern(schedulingPattern);
      } catch (InvalidSchedulingPatternException e) {
        return false;
      }

      return true;
    }

    /**
     * This utility method changes an alias to an integer value.
     *
     * @param value the value
     * @param aliases the aliases list
     * @param offset the offset applied to the aliases list indices
     * @return the parsed value
     */
    private static int parseAlias(String value, String[] aliases, int offset) throws Exception {
      for (int i = 0; i < aliases.length; i++) {
        if (aliases[i].equalsIgnoreCase(value)) {
          return offset + i;
        }
      }

      throw new Exception("Invalid alias \"%s\"".formatted(value));
    }

    /**
     * Returns {@code true} if the EPOCH timestamp in milliseconds matches the pattern, according to
     * the given time zone.
     *
     * @param timezone the time zone
     * @param timestamp the EPOCH timestamp in milliseconds
     * @return {@code true} if the given timestamp matches the pattern or {@code false} otherwise
     */
    public boolean match(TimeZone timezone, long timestamp) {
      GregorianCalendar gc = new GregorianCalendar();

      gc.setTimeInMillis(timestamp);
      gc.setTimeZone(timezone);

      int minute = gc.get(Calendar.MINUTE);
      int hour = gc.get(Calendar.HOUR_OF_DAY);
      int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
      int month = gc.get(Calendar.MONTH) + 1;
      int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK) - 1;
      int year = gc.get(Calendar.YEAR);

      for (int i = 0; i < matcherSize; i++) {
        ValueMatcher minuteMatcher = minuteMatchers.get(i);
        ValueMatcher hourMatcher = hourMatchers.get(i);
        ValueMatcher dayOfMonthMatcher = dayOfMonthMatchers.get(i);
        ValueMatcher monthMatcher = monthMatchers.get(i);
        ValueMatcher dayOfWeekMatcher = dayOfWeekMatchers.get(i);
        boolean eval =
            minuteMatcher.match(minute)
                && hourMatcher.match(hour)
                && ((dayOfMonthMatcher instanceof DayOfMonthValueMatcher)
                    ? ((DayOfMonthValueMatcher) dayOfMonthMatcher)
                        .match(dayOfMonth, month, gc.isLeapYear(year))
                    : dayOfMonthMatcher.match(dayOfMonth))
                && monthMatcher.match(month)
                && dayOfWeekMatcher.match(dayOfWeek);

        if (eval) {
          return true;
        }
      }

      return false;
    }

    /**
     * Returns {@code true} if the given EPOCH timestamp in milliseconds matches the pattern,
     * according to the system default time zone.
     *
     * @param timestamp the EPOCH timestamp in milliseconds
     * @return {@code true} if the given timestamp matches the pattern or {@code false} otherwise
     */
    public boolean match(long timestamp) {
      return match(TimeZone.getDefault(), timestamp);
    }

    /**
     * Returns the pattern as a string.
     *
     * @return The pattern as a string.
     */
    public String toString() {
      return asString;
    }

    /**
     * A {@code ValueMatcher} utility builder.
     *
     * @param str the pattern part for the {@code ValueMatcher} creation
     * @param parser the parser used to parse the values
     * @return the requested {@code ValueMatcher}
     */
    private ValueMatcher buildValueMatcher(String str, ValueParser parser) throws Exception {
      if (str.equals("*")) {
        return new AlwaysTrueValueMatcher();
      }

      List<Integer> values = new ArrayList<>();
      StringTokenizer st = new StringTokenizer(str, ",");

      while (st.hasMoreTokens()) {
        String element = st.nextToken();
        List<Integer> local;

        try {
          local = parseListElement(element, parser);
        } catch (Exception e) {
          throw new Exception(
              "Invalid field \"%s\", invalid element \"%s\": %s"
                  .formatted(str, element, e.getMessage()));
        }

        for (Integer value : local) {
          if (!values.contains(value)) {
            values.add(value);
          }
        }
      }

      if (values.isEmpty()) {
        throw new Exception("Invalid field \"%s\"".formatted(str));
      }

      if (parser == DAY_OF_MONTH_VALUE_PARSER) {
        return new DayOfMonthValueMatcher(values);
      } else {
        return new IntArrayValueMatcher(values);
      }
    }

    /**
     * Parses an element to retrieve the values for the pattern.
     *
     * @param str the element string
     * @param parser the parser used to parse the values
     * @return the integers representing the allowed values
     */
    private List<Integer> parseListElement(String str, ValueParser parser) throws Exception {
      StringTokenizer st = new StringTokenizer(str, "/");
      int size = st.countTokens();

      if ((size < 1) || (size > 2)) {
        throw new Exception("Syntax error");
      }

      List<Integer> values;

      try {
        values = parseRange(st.nextToken(), parser);
      } catch (Exception e) {
        throw new Exception("Invalid range: %s".formatted(e.getMessage()));
      }

      if (size == 2) {
        String dStr = st.nextToken();
        int div;

        try {
          div = Integer.parseInt(dStr);
        } catch (NumberFormatException e) {
          throw new Exception("Invalid divisor \"%s\"".formatted(dStr));
        }

        if (div < 1) {
          throw new Exception("Non positive divisor \"%d\"".formatted(div));
        }

        List<Integer> values2 = new ArrayList<>();

        for (int i = 0; i < values.size(); i += div) {
          values2.add(values.get(i));
        }

        return values2;
      } else {
        return values;
      }
    }

    /**
     * Parses a range of values.
     *
     * @param str the range string.
     * @param parser the parser used to parse the values
     * @return the integers representing the allowed values
     */
    private List<Integer> parseRange(String str, ValueParser parser) throws Exception {
      if (str.equals("*")) {
        int min = parser.getMinValue();
        int max = parser.getMaxValue();
        List<Integer> values = new ArrayList<>();

        for (int i = min; i <= max; i++) {
          values.add(i);
        }

        return values;
      }

      StringTokenizer st = new StringTokenizer(str, "-");
      int size = st.countTokens();

      if ((size < 1) || (size > 2)) {
        throw new Exception("Syntax error");
      }

      String v1Str = st.nextToken();
      int v1;

      try {
        v1 = parser.parse(v1Str);
      } catch (Exception e) {
        throw new Exception("Invalid value \"%s\": %s".formatted(v1Str, e.getMessage()));
      }

      if (size == 1) {
        List<Integer> values = new ArrayList<>();

        values.add(v1);

        return values;
      } else {
        String v2Str = st.nextToken();
        int v2;

        try {
          v2 = parser.parse(v2Str);
        } catch (Exception e) {
          throw new Exception("Invalid value \"%s\": %s".formatted(v2Str, e.getMessage()));
        }

        List<Integer> values = new ArrayList<>();

        if (v1 < v2) {
          for (int i = v1; i <= v2; i++) {
            values.add(i);
          }
        } else if (v1 > v2) {
          int min = parser.getMinValue();
          int max = parser.getMaxValue();

          for (int i = v1; i <= max; i++) {
            values.add(i);
          }

          for (int i = min; i <= v2; i++) {
            values.add(i);
          }
        } else {
          // v1 == v2
          values.add(v1);
        }

        return values;
      }
    }

    /** Definition for a value parser. */
    private interface ValueParser {

      /**
       * Returns the maximum value accepted by the parser.
       *
       * @return the maximum value accepted by the parser
       */
      int getMaxValue();

      /**
       * Returns the minimum value accepted by the parser.
       *
       * @return the minimum value accepted by the parser
       */
      int getMinValue();

      /**
       * Attempts to parse a value.
       *
       * @param value the value
       * @return the parsed value
       * @throws Exception If the value can't be parsed.
       */
      int parse(String value) throws Exception;
    }

    /** The days of month value parser. */
    private static class DayOfMonthValueParser extends SimpleValueParser {

      /** Builds the value parser. */
      public DayOfMonthValueParser() {
        super(1, 31);
      }

      /**
       * Added to support last-day-of-month.
       *
       * @param value the value to be parsed
       * @return the integer day of the month or 32 for last day of the month
       */
      public int parse(String value) throws Exception {
        if (value.equalsIgnoreCase("L")) {
          return 32;
        } else {
          return super.parse(value);
        }
      }
    }

    /** The value parser for the months field. */
    private static class DayOfWeekValueParser extends SimpleValueParser {

      /** Days of week aliases. */
      private static final String[] ALIASES = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

      /** Builds the months value parser. */
      public DayOfWeekValueParser() {
        super(0, 7);
      }

      /**
       * Parse the value.
       *
       * @param value the value to parse
       * @return the parsed value
       */
      public int parse(String value) throws Exception {
        try {
          // try as a simple value
          return super.parse(value) % 7;
        } catch (Exception e) {
          // try as an alias
          return parseAlias(value, ALIASES, 0);
        }
      }
    }

    /** The hours value parser. */
    private static class HourValueParser extends SimpleValueParser {

      /** Builds the value parser. */
      public HourValueParser() {
        super(0, 23);
      }
    }

    /** The minutes value parser. */
    private static class MinuteValueParser extends SimpleValueParser {

      /** Builds the value parser. */
      public MinuteValueParser() {
        super(0, 59);
      }
    }

    /** The value parser for the months field. */
    private static class MonthValueParser extends SimpleValueParser {

      /** Months aliases. */
      private static final String[] ALIASES = {
        "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"
      };

      /** Builds the months value parser. */
      public MonthValueParser() {
        super(1, 12);
      }

      /**
       * Parse the value.
       *
       * @param value the value to parse
       * @return the parsed value
       */
      public int parse(String value) throws Exception {
        try {
          // try as a simple value
          return super.parse(value);
        } catch (Exception e) {
          // try as an alias
          return parseAlias(value, ALIASES, 1);
        }
      }
    }

    /** A simple value parser. */
    private static class SimpleValueParser implements ValueParser {

      /** The maximum allowed value. */
      protected int maxValue;

      /** The minimum allowed value. */
      protected int minValue;

      /**
       * Builds the value parser.
       *
       * @param minValue the minimum allowed value
       * @param maxValue the maximum allowed value
       */
      public SimpleValueParser(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
      }

      /**
       * Returns the maximum value.
       *
       * @return the maximum value
       */
      public int getMaxValue() {
        return maxValue;
      }

      /**
       * Returns the minimum value.
       *
       * @return the minimum value
       */
      public int getMinValue() {
        return minValue;
      }

      /**
       * Parse the value
       *
       * @param value the value to parse
       * @return the parsed value
       */
      public int parse(String value) throws Exception {
        int i;

        try {
          i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
          throw new Exception("invalid integer value");
        }

        if ((i < minValue) || (i > maxValue)) {
          throw new Exception("value out of range");
        }

        return i;
      }
    }
  }
}
