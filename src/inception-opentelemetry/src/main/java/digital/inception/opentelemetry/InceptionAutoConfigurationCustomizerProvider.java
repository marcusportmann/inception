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

package digital.inception.opentelemetry;

import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/** The {@code InceptionAutoConfigurationCustomizerProvider} class. */
public class InceptionAutoConfigurationCustomizerProvider
    implements AutoConfigurationCustomizerProvider {

  private static final String API_PATTERN = "/api.*";

  private static final Logger log =
      Logger.getLogger(InceptionAutoConfigurationCustomizerProvider.class.getName());

  /** Constructs a new {@code InceptionAutoConfigurationCustomizerProvider}. */
  public InceptionAutoConfigurationCustomizerProvider() {}

  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {
    String applicationName = getEnvironmentVariable("APPLICATION_NAME", "unknown");
    String kubernetesNamespace = getEnvironmentVariable("KUBERNETES_NAMESPACE_NAME", "unknown");
    String otelExporterOtlpEndpoint = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    String otelTracingApiRuleEnabled =
        getEnvironmentVariable("OTEL_TRACING_API_RULE_ENABLED", "true");
    String otelTracingInternalSpanRulePackages =
        System.getenv("OTEL_TRACING_INTERNAL_SPAN_RULE_PACKAGES");
    String otelTracingInceptionFrameworkRuleEnabled =
        getEnvironmentVariable("OTEL_TRACING_INCEPTION_FRAMEWORK_RULE_ENABLED", "false");
    String otelTracingRuleLoggingEnabled =
        getEnvironmentVariable("OTEL_TRACING_RULE_LOGGING_ENABLED", "false");

    log.info(
        "Configuring the OpenTelemetry SDK (service.name: "
            + applicationName
            + ", k8s.namespace.name: "
            + kubernetesNamespace
            + ")");

    int otelTracingSpansPerSecond = 1;

    try {
      otelTracingSpansPerSecond =
          Integer.parseInt(getEnvironmentVariable("OTEL_TRACING_SPANS_PER_SECOND", "1"));
    } catch (NumberFormatException ex) {
      log.warning(
          "Failed to parse the OTEL_TRACING_SPANS_PER_SECOND ("
              + getEnvironmentVariable("OTEL_TRACING_SPANS_PER_SECOND", "1")
              + ") environment variable, using the default value of 1");
    }

    final int finalOtelTracingSpansPerSecond = otelTracingSpansPerSecond;

    Map<String, String> properties = new HashMap<>();
    properties.put("otel.logs.exporter", "none");
    properties.put("otel.metrics.exporter", "none");

    if (otelExporterOtlpEndpoint != null && !otelExporterOtlpEndpoint.isEmpty()) {
      properties.put("otel.traces.exporter", "otlp");
    } else {
      properties.put("otel.traces.exporter", "console");
    }

    autoConfiguration.addPropertiesSupplier(() -> properties);

    autoConfiguration.addResourceCustomizer(
        (resource, configProperties) ->
            resource.toBuilder()
                .put("service.name", applicationName)
                .put("k8s.namespace.name", kubernetesNamespace)
                .build());

    autoConfiguration.addPropagatorCustomizer(
        (textMapPropagator, configProperties) ->
            TextMapPropagator.composite(
                W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance()));

    autoConfiguration.addSpanExporterCustomizer(
        (spanExporter, configProperties) -> {
          if (spanExporter instanceof OtlpHttpSpanExporter otlpHttpSpanExporter) {
            String endpoint = otelExporterOtlpEndpoint + "/v1/traces";

            log.info("Customizing the OtlpHttpSpanExporter using the endpoint: " + endpoint);

            return otlpHttpSpanExporter.toBuilder()
                .setEndpoint(endpoint)
                .setCompression("gzip")
                .build();
          } else if (spanExporter instanceof LoggingSpanExporter loggingSpanExporter) {
            // Do nothing
          }

          return spanExporter;
        });

    autoConfiguration.addTracerProviderCustomizer(
        (sdkTracerProviderBuilder, configProperties) -> {
          RateLimitedRuleBasedSamplerBuilder rateLimitedRuleBasedSamplerBuilder =
              new RateLimitedRuleBasedSamplerBuilder(
                  "true".equalsIgnoreCase(otelTracingRuleLoggingEnabled),
                  finalOtelTracingSpansPerSecond,
                  Sampler.alwaysOff());

          if ("true".equals(otelTracingApiRuleEnabled)) {
            log.info("API server span tracing enabled (" + API_PATTERN + ")");

            rateLimitedRuleBasedSamplerBuilder.recordAndSample(
                SpanKind.SERVER, AttributeKey.stringKey("url.path"), API_PATTERN);
          } else {
            log.info("API server span tracing DISABLED");
          }

          if ("true".equalsIgnoreCase(otelTracingInceptionFrameworkRuleEnabled)) {
            log.info("Inception Framework internal span tracing enabled");

            rateLimitedRuleBasedSamplerBuilder.recordAndSample(
                SpanKind.INTERNAL, AttributeKey.stringKey("code.namespace"), "digital.inception.*");
          }

          if (otelTracingInternalSpanRulePackages != null
              && !otelTracingInternalSpanRulePackages.isEmpty()) {
            String[] packageNames = otelTracingInternalSpanRulePackages.split(",");

            String[] trimmedPackageNames =
                Arrays.stream(packageNames)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);

            for (String packageName : trimmedPackageNames) {
              if (!packageName.isEmpty()) {
                log.info("Enabling tracing for internal spans for the package: " + packageName);

                rateLimitedRuleBasedSamplerBuilder.recordAndSample(
                    SpanKind.INTERNAL,
                    AttributeKey.stringKey("code.namespace"),
                    packageName + ".*");
              }
            }
          }

          RateLimitedRuleBasedSampler rateLimitedRuleBasedSampler =
              rateLimitedRuleBasedSamplerBuilder.build();

          log.info(rateLimitedRuleBasedSampler.getDescription());

          Sampler sampler =
              Sampler.parentBasedBuilder(rateLimitedRuleBasedSampler)
                  .setRemoteParentSampled(Sampler.alwaysOn())
                  .setRemoteParentNotSampled(Sampler.alwaysOff())
                  .setLocalParentSampled(Sampler.alwaysOn())
                  .setLocalParentNotSampled(Sampler.alwaysOff())
                  .build();

          sdkTracerProviderBuilder.setSampler(sampler);
          return sdkTracerProviderBuilder;
        });
  }

  private String getEnvironmentVariable(String name, String defaultValue) {
    String environmentVariable = System.getenv(name);

    if (environmentVariable != null && !defaultValue.isEmpty()) {
      return environmentVariable;
    } else {
      return defaultValue;
    }
  }
}
