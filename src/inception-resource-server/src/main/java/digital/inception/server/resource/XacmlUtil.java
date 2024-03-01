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

package digital.inception.server.resource;

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.ow2.authzforce.core.pdp.api.AttributeFqn;
import org.ow2.authzforce.core.pdp.api.AttributeFqns;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.core.pdp.api.policy.PrimaryPolicyMetadata;
import org.ow2.authzforce.core.pdp.api.value.AttributeBag;
import org.ow2.authzforce.core.pdp.api.value.Bags;
import org.ow2.authzforce.core.pdp.api.value.BooleanValue;
import org.ow2.authzforce.core.pdp.api.value.DateTimeValue;
import org.ow2.authzforce.core.pdp.api.value.DateValue;
import org.ow2.authzforce.core.pdp.api.value.DoubleValue;
import org.ow2.authzforce.core.pdp.api.value.IntegerValue;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;

/**
 * The <b>XacmlUtil</b> class provides utility methods that are useful when working with XACML.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class XacmlUtil {

  /** Private constructor to prevent instantiation. */
  private XacmlUtil() {}

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValue the attribute value
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      String attributeCategory,
      String attributeId,
      String attributeValue) {
    addAttributeToRequest(
        decisionRequestBuilder, Optional.empty(), attributeCategory, attributeId, attributeValue);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValueArray the attribute value array
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      String attributeCategory,
      String attributeId,
      String[] attributeValueArray) {
    addAttributeToRequest(
        decisionRequestBuilder,
        Optional.empty(),
        attributeCategory,
        attributeId,
        attributeValueArray);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeIssuer the attribute issuer
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValue the attribute value
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      Optional<String> attributeIssuer,
      String attributeCategory,
      String attributeId,
      String attributeValue) {
    AttributeFqn attributeFqn =
        AttributeFqns.newInstance(attributeCategory, attributeIssuer, attributeId);

    AttributeBag<?> attributeValues =
        Bags.singletonAttributeBag(StandardDatatypes.STRING, new StringValue(attributeValue));

    decisionRequestBuilder.putNamedAttributeIfAbsent(attributeFqn, attributeValues);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeIssuer the attribute issuer
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValueArray the attribute value array
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      Optional<String> attributeIssuer,
      String attributeCategory,
      String attributeId,
      String[] attributeValueArray) {
    if ((attributeValueArray != null) && (attributeValueArray.length > 0)) {
      AttributeFqn attributeFqn =
          AttributeFqns.newInstance(attributeCategory, attributeIssuer, attributeId);

      AttributeBag<?> attributeValues =
          Bags.newAttributeBag(
              StandardDatatypes.STRING,
              Arrays.stream(attributeValueArray)
                  .map(StringValue::new)
                  .collect(Collectors.toList()));

      decisionRequestBuilder.putNamedAttributeIfAbsent(attributeFqn, attributeValues);
    }
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValues the XACML attribute bag containing the attribute values
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      String attributeCategory,
      String attributeId,
      AttributeBag<?> attributeValues) {
    addAttributeToRequest(
        decisionRequestBuilder, Optional.empty(), attributeCategory, attributeId, attributeValues);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeIssuer the attribute issuer
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValues the XACML attribute bag containing the attribute values
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      Optional<String> attributeIssuer,
      String attributeCategory,
      String attributeId,
      AttributeBag<?> attributeValues) {
    AttributeFqn attributeFqn =
        AttributeFqns.newInstance(attributeCategory, attributeIssuer, attributeId);

    decisionRequestBuilder.putNamedAttributeIfAbsent(attributeFqn, attributeValues);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValueList the attribute value list
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      String attributeCategory,
      String attributeId,
      List<String> attributeValueList) {
    addAttributeToRequest(
        decisionRequestBuilder,
        Optional.empty(),
        attributeCategory,
        attributeId,
        attributeValueList);
  }

  /**
   * Add the attribute to the request.
   *
   * @param decisionRequestBuilder the decision request builder
   * @param attributeIssuer the attribute issuer
   * @param attributeCategory the attribute category
   * @param attributeId the attribute ID
   * @param attributeValueList the attribute value list
   */
  public static void addAttributeToRequest(
      DecisionRequestBuilder<?> decisionRequestBuilder,
      Optional<String> attributeIssuer,
      String attributeCategory,
      String attributeId,
      List<String> attributeValueList) {
    if ((attributeValueList != null) && (!attributeValueList.isEmpty())) {
      AttributeFqn attributeFqn =
          AttributeFqns.newInstance(attributeCategory, attributeIssuer, attributeId);

      AttributeBag<?> attributeValues =
          Bags.newAttributeBag(
              StandardDatatypes.STRING,
              attributeValueList.stream().map(StringValue::new).collect(Collectors.toList()));

      decisionRequestBuilder.putNamedAttributeIfAbsent(attributeFqn, attributeValues);
    }
  }

  /**
   * Retrieve the XACML attribute bag containing the list of specified value.
   *
   * @param values the list of value
   * @param valueType the type of values in the list
   * @return the XACML attribute bag containing the list of specified values
   */
  public static AttributeBag<?> getAttributeValues(List<?> values, Class<?> valueType) {
    if ((values == null) || (values.isEmpty())) {
      if (valueType == null) {
        return Bags.emptyAttributeBag(StandardDatatypes.STRING, null);
      } else if (valueType == String.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.STRING, null);
      } else if (valueType == Boolean.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.BOOLEAN, null);
      } else if (valueType == Integer.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.INTEGER, null);
      } else if (valueType == Long.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.INTEGER, null);
      } else if (valueType == Float.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DOUBLE, null);
      } else if (valueType == Double.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DOUBLE, null);
      } else if (valueType == XMLGregorianCalendar.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DATETIME, null);
      } else if (valueType == Date.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DATE, null);
      } else if (valueType == LocalDate.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DATE, null);
      } else if (valueType == LocalDateTime.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DATETIME, null);
      } else if (valueType == ZonedDateTime.class) {
        return Bags.emptyAttributeBag(StandardDatatypes.DATETIME, null);
      } else {
        return Bags.emptyAttributeBag(StandardDatatypes.STRING, null);
      }
    } else {
      if (valueType == String.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.STRING,
            values.stream()
                .map(v -> (String) v)
                .map(StringValue::new)
                .collect(Collectors.toList()));
      } else if (valueType == Boolean.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.BOOLEAN,
            values.stream()
                .map(v -> (Boolean) v)
                .map(BooleanValue::valueOf)
                .collect(Collectors.toList()));
      } else if (valueType == Integer.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.INTEGER,
            values.stream()
                .map(v -> (Integer) v)
                .map(IntegerValue::valueOf)
                .collect(Collectors.toList()));
      } else if (valueType == Long.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.INTEGER,
            values.stream()
                .map(v -> (Long) v)
                .map(IntegerValue::valueOf)
                .collect(Collectors.toList()));
      } else if (valueType == Float.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DOUBLE,
            values.stream()
                .map(v -> ((Float) v).doubleValue())
                .map(DoubleValue::new)
                .collect(Collectors.toList()));
      } else if (valueType == Double.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DOUBLE,
            values.stream()
                .map(v -> (Double) v)
                .map(DoubleValue::new)
                .collect(Collectors.toList()));
      } else if (valueType == XMLGregorianCalendar.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DATETIME,
            values.stream()
                .map(v -> (XMLGregorianCalendar) v)
                .map(DateTimeValue::new)
                .collect(Collectors.toList()));
      } else if (valueType == Date.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DATE,
            values.stream()
                .map(
                    (Object dateObject) -> {
                      GregorianCalendar calendar = new GregorianCalendar();
                      calendar.setTime((Date) dateObject);

                      try {
                        return DateValue.getInstance(
                            DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
                      } catch (Throwable e) {
                        throw new RuntimeException(
                            "Failed to convert the Date value to a XMLGregorianCalendar value", e);
                      }
                    })
                .collect(Collectors.toList()));
      } else if (valueType == LocalDate.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DATE,
            values.stream()
                .map(
                    (Object localDateObject) -> {
                      try {
                        return DateValue.getInstance(
                            DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(((LocalDate) localDateObject).toString()));
                      } catch (Throwable e) {
                        throw new RuntimeException(
                            "Failed to convert the LocalDate value to a XMLGregorianCalendar value",
                            e);
                      }
                    })
                .collect(Collectors.toList()));
      } else if (valueType == LocalDateTime.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DATETIME,
            values.stream()
                .map(
                    (Object localDateTimeObject) -> {
                      try {
                        return new DateTimeValue(
                            DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(
                                    ((LocalDateTime) localDateTimeObject)
                                        .format(DateTimeFormatter.ISO_DATE_TIME)));
                      } catch (Throwable e) {
                        throw new RuntimeException(
                            "Failed to convert the LocalDateTime value to a XMLGregorianCalendar value",
                            e);
                      }
                    })
                .collect(Collectors.toList()));
      } else if (valueType == ZonedDateTime.class) {
        return Bags.newAttributeBag(
            StandardDatatypes.DATETIME,
            values.stream()
                .map(
                    (Object zonedDateTimeObject) -> {
                      GregorianCalendar gregorianCalendar =
                          GregorianCalendar.from(((ZonedDateTime) zonedDateTimeObject));

                      try {
                        return new DateTimeValue(
                            DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(gregorianCalendar));
                      } catch (Throwable e) {
                        throw new RuntimeException(
                            "Failed to convert the ZonedDateTime value to a XMLGregorianCalendar value",
                            e);
                      }
                    })
                .collect(Collectors.toList()));
      } else {
        return Bags.newAttributeBag(
            StandardDatatypes.STRING,
            values.stream()
                .map(Object::toString)
                .map(StringValue::new)
                .collect(Collectors.toList()));
      }
    }
  }

  /**
   * Retrieve the XACML attribute bag containing the specified value.
   *
   * @param value the value
   * @return the XACML attribute bag containing the specified value
   */
  public static AttributeBag<?> getAttributeValues(Object value) {
    return switch (value) {
      case null -> Bags.emptyAttributeBag(StandardDatatypes.STRING, null);
      case String stringValue -> Bags.singletonAttributeBag(
          StandardDatatypes.STRING, new StringValue(stringValue));
      case Boolean booleanValue -> Bags.singletonAttributeBag(
          StandardDatatypes.BOOLEAN, BooleanValue.valueOf(booleanValue));
      case Integer integerValue -> Bags.singletonAttributeBag(
          StandardDatatypes.INTEGER, IntegerValue.valueOf(integerValue));
      case Long longValue -> Bags.singletonAttributeBag(
          StandardDatatypes.INTEGER, IntegerValue.valueOf(longValue));
      case Float floatValue -> Bags.singletonAttributeBag(
          StandardDatatypes.DOUBLE, new DoubleValue(Double.valueOf(floatValue)));
      case Double doubleValue -> Bags.singletonAttributeBag(
          StandardDatatypes.DOUBLE, new DoubleValue(doubleValue));
      case XMLGregorianCalendar timeValue -> Bags.singletonAttributeBag(
          StandardDatatypes.DATETIME, new DateTimeValue(timeValue));
      case Date dateValue -> {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateValue);

        try {
          XMLGregorianCalendar xmlGregorianCalendar =
              DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

          yield Bags.singletonAttributeBag(
              StandardDatatypes.DATE, DateValue.getInstance(xmlGregorianCalendar));
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to convert the Date value to a XMLGregorianCalendar value", e);
        }
      }

      case LocalDate localDateValue -> {
        try {
          XMLGregorianCalendar xmlGregorianCalendar =
              DatatypeFactory.newInstance().newXMLGregorianCalendar(localDateValue.toString());

          yield Bags.singletonAttributeBag(
              StandardDatatypes.DATE, DateValue.getInstance(xmlGregorianCalendar));
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to convert the LocalDate value to a XMLGregorianCalendar value", e);
        }
      }

      case LocalDateTime localDateTime -> {
        try {
          XMLGregorianCalendar xmlGregorianCalendar =
              DatatypeFactory.newInstance()
                  .newXMLGregorianCalendar(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));

          yield Bags.singletonAttributeBag(
              StandardDatatypes.DATETIME, new DateTimeValue(xmlGregorianCalendar));
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to convert the LocalDateTime value to a XMLGregorianCalendar value", e);
        }
      }

      case ZonedDateTime zonedDateTime -> {
        try {
          GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
          XMLGregorianCalendar xmlGregorianCalendar =
              DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

          yield Bags.singletonAttributeBag(
              StandardDatatypes.DATETIME, new DateTimeValue(xmlGregorianCalendar));
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to convert the ZonedDateTime value to a XMLGregorianCalendar value", e);
        }
      }

      default -> Bags.singletonAttributeBag(
          StandardDatatypes.STRING, new StringValue(value.toString()));
    };
  }

  /**
   * Is the specified attribute value a valid value that can be added to a XACML attribute bag.
   *
   * @param value the value
   * @return <b>true</b> if the specified attribute value is a valid value that can be added to a
   *     XACML attribute bag or <b>false</b> otherwise
   */
  public static boolean isValidAttributeValue(Object value) {
    return switch (value) {
      case null -> true;
      case String ignored -> true;
      case Boolean ignored -> true;
      case Integer ignored -> true;
      case Long ignored -> true;
      case Float ignored -> true;
      case Double ignored -> true;
      case XMLGregorianCalendar ignored -> true;
      case Date ignored -> true;
      case LocalDate ignored -> true;
      case LocalDateTime ignored -> true;
      case ZonedDateTime ignored -> true;
      default -> false;
    };
  }

  /**
   * Returns the string representation of the list of policies.
   *
   * @param policies the policies
   * @return the string representation of the list of policies
   */
  public static String policiesToString(ImmutableList<PrimaryPolicyMetadata> policies) {
    StringBuilder buffer = new StringBuilder();

    for (PrimaryPolicyMetadata applicablePolicy : policies) {
      if (!buffer.isEmpty()) {
        buffer.append(",");
      }

      buffer.append(applicablePolicy.getId());
      buffer.append("#v");
      buffer.append(applicablePolicy.getVersion());
    }

    return buffer.toString();
  }
}
