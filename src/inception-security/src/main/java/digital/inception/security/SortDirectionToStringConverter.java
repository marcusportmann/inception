//package digital.inception.security;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
///**
// * The <code>SortDirectionToStringConverter</code> class implements the Spring converter that
// * converts a <code>SortDirection</code> type into an <code>String</code> type.
// *
// * @author Marcus Portmann
// */
//@SuppressWarnings("unused")
//@Component
//@WritingConverter
//public class SortDirectionToStringConverter
//  implements Converter<SortDirection, String>
//{
//  /**
//   * Constructs a new <code>SortDirectionToStringConverter</code>.
//   */
//  public SortDirectionToStringConverter() {}
//
//  @Override
//  public String convert(SortDirection source)
//  {
//    if (source == null)
//    {
//      return null;
//    }
//
//    return source.getCode();
//  }
//}
