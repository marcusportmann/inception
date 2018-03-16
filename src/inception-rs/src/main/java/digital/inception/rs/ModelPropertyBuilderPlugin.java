//package digital.inception.rs;
//
//import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
//import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
//import com.google.common.base.Optional;
//import io.swagger.annotations.ApiModelProperty;
//import springfox.documentation.builders.ModelPropertyBuilder;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
//
//@Component
//public class ModelPropertyBuilderPlugin implements springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
//{
//
//  @Override
//  public boolean supports(final DocumentationType arg0) {
//    return true;
//  }
//
//  @Override
//  public void apply(final ModelPropertyContext context) {
//    final ModelPropertyBuilder builder = context.getBuilder();
//
//    final Optional<BeanPropertyDefinition> beanPropDef = context.getBeanPropertyDefinition();
//    final BeanPropertyDefinition beanDef = beanPropDef.get();
//    final AnnotatedMethod method = beanDef.getGetter();
//    if (method == null) {
//      return;
//    }
//
//    final ApiModelProperty apiModelProperty = method.getAnnotation(ApiModelProperty.class);
//    if (apiModelProperty == null) {
//      return;
//    }
//
//    builder.allowEmptyValue(false);
//  }
//}
