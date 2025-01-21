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

package digital.inception.server.resource.xacmlpdp;

import digital.inception.core.util.ISO8601Util;
import digital.inception.web.RequestBodyObjectContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.datatype.DatatypeFactory;
import ognl.Ognl;
import ognl.OgnlException;
import org.ow2.authzforce.core.pdp.api.IndeterminateEvaluationException;
import org.ow2.authzforce.core.pdp.api.expression.Expression;
import org.ow2.authzforce.core.pdp.api.func.BaseFirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.FirstOrderFunctionCall;
import org.ow2.authzforce.core.pdp.api.func.SingleParameterTypedFirstOrderFunction;
import org.ow2.authzforce.core.pdp.api.value.BooleanValue;
import org.ow2.authzforce.core.pdp.api.value.Datatype;
import org.ow2.authzforce.core.pdp.api.value.DateTimeValue;
import org.ow2.authzforce.core.pdp.api.value.DateValue;
import org.ow2.authzforce.core.pdp.api.value.DoubleValue;
import org.ow2.authzforce.core.pdp.api.value.IntegerValue;
import org.ow2.authzforce.core.pdp.api.value.LongInteger;
import org.ow2.authzforce.core.pdp.api.value.SimpleValue;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;
import org.ow2.authzforce.xacml.identifiers.XacmlStatusCode;

/**
 * The <b>XacmlGetRequestBodyObjectAttributeValueFunctions</b> class provides the custom AuthzForce
 * functions that allow the retrieval of attribute values from the request body object populated
 * from the JSON request body of a RESTful API invocation. The request body object populated from
 * the JSON request body is stored in a thread-local variable, and these functions use OGNL
 * (Object-Graph Navigation Language) to dynamically access nested attributes.
 *
 * <p>The class includes functionality for caching parsed OGNL expressions to optimize performance
 * for frequently used attribute paths.
 *
 * @author Marcus Portmann
 */
public class XacmlGetRequestBodyObjectAttributeValueFunctions {

  /** The prefix for the IDs of the custom functions provided by this class. */
  public static final String FUNCTION_ID_PREFIX =
      "urn:inception:xacml:function:get-request-body-object-";

  /** The suffix for the IDs of the custom functions provided by this class. */
  public static final String FUNCTION_ID_SUFFIX = "-attribute-value";

  /**
   * A thread-safe cache for storing parsed OGNL expressions to avoid repeated parsing of the same
   * expression.
   */
  private static final Map<String, Object> parsedOgnlExpressionCache = new ConcurrentHashMap<>();

  /**
   * Private constructor to prevent instantiation.
   */
  private XacmlGetRequestBodyObjectAttributeValueFunctions() {}

  /**
   * Returns the parsed OGNL expression, caching the result for future reuse.
   *
   * <p>If the expression is already cached, the cached version is returned. Otherwise, the
   * expression is parsed, stored in the cache, and returned.
   *
   * @param ognlExpression the OGNL expression to be parsed
   * @return the parsed OGNL expression
   * @throws RuntimeException if the expression cannot be parsed by the OGNL library
   */
  private static Object getParsedOgnlExpression(String ognlExpression) {
    return parsedOgnlExpressionCache.computeIfAbsent(
        ognlExpression,
        key -> {
          try {
            return Ognl.parseExpression(key);
          } catch (OgnlException e) {
            throw new RuntimeException("Failed to parse the OGNL expression (" + key + ")", e);
          }
        });
  }

  /**
   * Abstract base class for the custom AuthzForce functions to retrieve attribute values from the
   * request body object populated from the JSON request body of a RESTful API invocation.
   *
   * <p>Each subclass represents a specific function that returns a specific type of value (e.g.,
   * string).
   *
   * @param <RETURN_DATA_TYPE> the return data type of the function
   */
  private abstract static class XacmlGetRequestBodyAttributeValueFunction<
          ATTRIBUTE_VALUE_TYPE, RETURN_DATA_TYPE extends SimpleValue<?>>
      extends SingleParameterTypedFirstOrderFunction<RETURN_DATA_TYPE, StringValue> {

    /** The required type of the attribute value retrieved from the Java request body object. */
    private final Class<ATTRIBUTE_VALUE_TYPE> attributeValueType;

    /**
     * Constructs a new <b>XacmlGetRequestBodyAttributeValueFunction</b>.
     *
     * @param functionId the function ID
     * @param attributeValueType the required type of the attribute value retrieved from the Java
     *     request body object
     * @param returnDataType the data type of the function's return value
     */
    public XacmlGetRequestBodyAttributeValueFunction(
        final String functionId,
        final Class<ATTRIBUTE_VALUE_TYPE> attributeValueType,
        final Datatype<RETURN_DATA_TYPE> returnDataType) {
      super(functionId, returnDataType, false, List.of(StandardDatatypes.STRING));
      this.attributeValueType = attributeValueType;
    }

    @Override
    public FirstOrderFunctionCall<RETURN_DATA_TYPE> newCall(
        final List<Expression<?>> argExpressions, final Datatype<?>... remainingArgTypes) {

      return new BaseFirstOrderFunctionCall.EagerSinglePrimitiveTypeEval<>(
          functionSignature, argExpressions, remainingArgTypes) {

        /**
         * Evaluates the function with the given arguments.
         *
         * @param argStack the stack of argument values
         * @return the result of the function evaluation
         * @throws IndeterminateEvaluationException if the function cannot be evaluated
         */
        @Override
        protected RETURN_DATA_TYPE evaluate(Deque<StringValue> argStack)
            throws IndeterminateEvaluationException {
          if (argStack.size() != 1) {
            throw new IndeterminateEvaluationException(
                "A single OGNL expression argument that is provided as an attribute value with the string data type (http://www.w3.org/2001/XMLSchema#string) is required for the function ("
                    + getId()
                    + ")",
                XacmlStatusCode.SYNTAX_ERROR.value());
          }

          Object requestBodyObject = RequestBodyObjectContext.getRequestBodyObject();

          if (requestBodyObject == null) {
            throw new IndeterminateEvaluationException(
                "Failed to retrieve the request body object in the function (" + getId() + ")",
                XacmlStatusCode.PROCESSING_ERROR.value());
          }

          String ognlExpression = argStack.getFirst().getUnderlyingValue();

          Object parsedOgnlExpression;
          try {
            parsedOgnlExpression = getParsedOgnlExpression(ognlExpression);
          } catch (Throwable e) {
            throw new IndeterminateEvaluationException(
                "Failed to parse the invalid OGNL expression argument ("
                    + ognlExpression
                    + ") that was provided as an attribute value with the string data type ("
                    + StandardDatatypes.STRING.getId()
                    + ") to the function ("
                    + getId()
                    + ")",
                XacmlStatusCode.PROCESSING_ERROR.value(),
                e);
          }

          Object attributeValue;
          try {
            attributeValue = Ognl.getValue(parsedOgnlExpression, requestBodyObject);
          } catch (Throwable e) {
            throw new IndeterminateEvaluationException(
                "Failed to retrieve the attribute value using the OGNL expression argument ("
                    + ognlExpression
                    + ") from the request body object in the function ("
                    + getId()
                    + ")",
                XacmlStatusCode.PROCESSING_ERROR.value(),
                e);
          }

          if (attributeValue == null) {
            throw new IndeterminateEvaluationException(
                "A null attribute value was retrieved using the OGNL expression argument ("
                    + ognlExpression
                    + ") from the request body object in the function ("
                    + getId()
                    + ")",
                XacmlStatusCode.PROCESSING_ERROR.value());
          }

          // Convert a LocalDateTime value into a OffsetDateTime value
          if ((attributeValue instanceof LocalDateTime localDateTime)
              && (attributeValueType == OffsetDateTime.class)) {
            attributeValue = localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
          }

          if (!attributeValueType.isInstance(attributeValue)) {
            throw new IndeterminateEvaluationException(
                "The attribute value ("
                    + attributeValue
                    + ") with type ("
                    + attributeValue.getClass().getName()
                    + ") retrieved using the OGNL expression argument ("
                    + ognlExpression
                    + ") from the request body object does not have the required type ("
                    + attributeValueType.getName()
                    + ") in the function ("
                    + getId()
                    + ")",
                XacmlStatusCode.PROCESSING_ERROR.value());
          } else {
            try {
              return convertAttributeValueToReturnValue(attributeValueType.cast(attributeValue));
            } catch (Throwable e) {
              throw new IndeterminateEvaluationException(
                  "Failed to convert the attribute value ("
                      + attributeValue
                      + ") retrieved using the OGNL expression argument ("
                      + ognlExpression
                      + ") from the request body object to the return value type ("
                      + getReturnType().getId()
                      + ") in the function ("
                      + getId()
                      + ")",
                  XacmlStatusCode.PROCESSING_ERROR.value(),
                  e);
            }
          }
        }
      };
    }

    /**
     * Converts the non-null attribute value retrieved from the request body object to the return
     * value for the derived function.
     *
     * @param attributeValue the non-null attribute value retrieved from the Java request body
     *     object
     * @return the non-null attribute value retrieved from the request body object to the return
     *     value for the derived function
     */
    protected abstract RETURN_DATA_TYPE convertAttributeValueToReturnValue(
        ATTRIBUTE_VALUE_TYPE attributeValue);
  }

  /**
   * The <b>XacmlGetRequestBodyBooleanAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a boolean attribute from the request body object populated from the JSON
   * request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyBooleanAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<Boolean, BooleanValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "boolean" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyBooleanAttributeValueFunction</b>. */
    public XacmlGetRequestBodyBooleanAttributeValueFunction() {
      super(ID, Boolean.class, StandardDatatypes.BOOLEAN);
    }

    @Override
    protected BooleanValue convertAttributeValueToReturnValue(Boolean attributeValue) {
      try {
        return new BooleanValue(attributeValue);
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }

  /**
   * The <b>XacmlGetRequestBodyDateAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a date (LocalDate) attribute from the request body object populated from
   * the JSON request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyDateAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<LocalDate, DateValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "date" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyDateAttributeValueFunction</b>. */
    public XacmlGetRequestBodyDateAttributeValueFunction() {
      super(ID, LocalDate.class, StandardDatatypes.DATE);
    }

    @Override
    protected DateValue convertAttributeValueToReturnValue(LocalDate attributeValue) {
      try {
        return new DateValue(ISO8601Util.fromLocalDate(attributeValue));
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }

  /**
   * The <b>XacmlGetRequestBodyDateTimeAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a date time (OffsetDateTime) attribute from the request body object
   * populated from the JSON request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyDateTimeAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<OffsetDateTime, DateTimeValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "date-time" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyDateTimeAttributeValueFunction</b>. */
    public XacmlGetRequestBodyDateTimeAttributeValueFunction() {
      super(ID, OffsetDateTime.class, StandardDatatypes.DATETIME);
    }

    @Override
    protected DateTimeValue convertAttributeValueToReturnValue(OffsetDateTime attributeValue) {
      try {
        GregorianCalendar gregorianCalendar =
            GregorianCalendar.from(attributeValue.toZonedDateTime());

        return new DateTimeValue(
            DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }

  /**
   * The <b>XacmlGetRequestBodyDoubleAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a double attribute from the request body object populated from the JSON
   * request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyDoubleAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<Double, DoubleValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "double" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyDoubleAttributeValueFunction</b>. */
    public XacmlGetRequestBodyDoubleAttributeValueFunction() {
      super(ID, Double.class, StandardDatatypes.DOUBLE);
    }

    @Override
    protected DoubleValue convertAttributeValueToReturnValue(Double attributeValue) {
      try {
        return new DoubleValue(attributeValue);
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }

  /**
   * The <b>XacmlGetRequestBodyIntegerAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a integer attribute from the request body object populated from the JSON
   * request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyIntegerAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<Integer, IntegerValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "integer" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyIntegerAttributeValueFunction</b>. */
    public XacmlGetRequestBodyIntegerAttributeValueFunction() {
      super(ID, Integer.class, StandardDatatypes.INTEGER);
    }

    @Override
    protected IntegerValue convertAttributeValueToReturnValue(Integer attributeValue) {
      try {
        return new IntegerValue(new LongInteger(attributeValue));
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }

  /**
   * The <b>XacmlGetRequestBodyStringAttributeValueFunction</b> class provides a custom AuthzForce
   * function to retrieve a string attribute from the request body object populated from the JSON
   * request body of a RESTful API invocation.
   */
  public static class XacmlGetRequestBodyStringAttributeValueFunction
      extends XacmlGetRequestBodyAttributeValueFunction<String, StringValue> {

    /** The function ID. */
    public static final String ID = FUNCTION_ID_PREFIX + "string" + FUNCTION_ID_SUFFIX;

    /** Constructs a new <b>XacmlGetRequestBodyStringAttributeValueFunction</b>. */
    public XacmlGetRequestBodyStringAttributeValueFunction() {
      super(ID, String.class, StandardDatatypes.STRING);
    }

    @Override
    protected StringValue convertAttributeValueToReturnValue(String attributeValue) {
      try {
        return new StringValue(attributeValue);
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to convert the attribute value ("
                + attributeValue
                + ") with type ("
                + attributeValue.getClass().getName()
                + ") to a return value with type ("
                + getReturnType()
                + ")",
            e);
      }
    }
  }
}
