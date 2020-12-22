/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.core.xml;

// ~--- non-JDK imports --------------------------------------------------------

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>XmlUtil</code> class provides utility methods for working with XML documents.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class XmlUtil {

  /**
   * The <code>DatatypeFactory</code> instance used to convert java.xml.datatype object that map XML
   * to/from Java objects.
   */
  private static final DatatypeFactory datatypeFactory;

  static {
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException("Failed to create a new DatatypeFactory instance", e);
    }
  }

  /**
   * Converts a <code>javax.xml.datatype.XMLGregorianCalendar</code> instance to a <code>
   * java.util.Date</code> instance.
   *
   * @param calendar the <code>javax.xml.datatype.XMLGregorianCalendar</code> instance to convert
   * @return the converted <code>java.util.Date</code> instance
   */
  public static java.util.Date asDate(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    } else {
      return calendar.toGregorianCalendar().getTime();
    }
  }

  /**
   * Converts a <code>java.util.Date</code> instance to a <code>
   *  javax.xml.datatype.XMLGregorianCalendar</code> instance.
   *
   * @param date the <code>java.util.Date</code> instance to convert
   * @return the converted <code>javax.xml.datatype.XMLGregorianCalendar</code> instance
   */
  public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
    if (date == null) {
      return null;
    } else {
      GregorianCalendar calendar = new GregorianCalendar();

      calendar.setTimeInMillis(date.getTime());

      return datatypeFactory.newXMLGregorianCalendar(calendar);
    }
  }

  /**
   * Returns the child element with the specified name for the specified element.
   *
   * @param element the parent element
   * @param name the name of the child element to return
   * @return the child element or <code>null</code> if a child element with the specified name could
   *     not be found
   */
  public static Element getChildElement(Element element, String name) {
    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if (node instanceof Element) {
        Element childElement = (Element) node;

        if (childElement.getNodeName().equals(name)) {
          return childElement;
        }
      }
    }

    return null;
  }

  /**
   * Returns the boolean value of the text content for the child element with the specified name for
   * the specified element.
   *
   * @param element the parent element
   * @param name the name of the child element to return
   * @return the boolean value of the text content for the child element or <code>null</code> if a
   *     child element with the specified name could not be found
   */
  public static Boolean getChildElementBoolean(Element element, String name) {
    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if (node instanceof Element) {
        Element childElement = (Element) node;

        if (childElement.getNodeName().equals(name)) {
          try {
            return Boolean.parseBoolean(childElement.getTextContent());
          } catch (Throwable e) {
            throw new RuntimeException(
                "Failed to parse the invalid boolean value ("
                    + childElement.getTextContent()
                    + ")");
          }
        }
      }
    }

    return null;
  }

  /**
   * Returns the text content for the child element with the specified name for the specified
   * element.
   *
   * @param element the parent element
   * @param name the name of the child element to return
   * @return the text content for the child element or <code>null</code> if a child element with the
   *     specified name could not be found
   */
  public static String getChildElementText(Element element, String name) {
    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if (node instanceof Element) {
        Element childElement = (Element) node;

        if (childElement.getNodeName().equals(name)) {
          return childElement.getTextContent();
        }
      }
    }

    return null;
  }

  /**
   * Returns the child elements with the specified name for the specified element.
   *
   * @param element the parent element
   * @param name the name of the child elements to return
   * @return the child elements with the specified name for the specified element
   */
  public static List<Element> getChildElements(Element element, String name) {
    List<Element> childElements = new ArrayList<>();

    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if (node instanceof Element) {
        Element childElement = (Element) node;

        if (childElement.getNodeName().equals(name)) {
          childElements.add(childElement);
        }
      }
    }

    return childElements;
  }

  /**
   * Returns the <code>javax.xml.namespace.QName</code> instance for the specified QName.
   *
   * @param document the <code>org.w3c.dom.Document</code> instance used to determine the namespace
   *     URI for the QName if the namespace is referenced using a prefix as part of the <code>name
   *                 </code> parameter
   * @param qname the QName
   * @return the <code>javax.xml.namespace.QName</code> instance for the specified QName
   */
  public static QName getQName(Document document, String qname) {
    qname = StringUtils.hasText(qname) ? qname.trim() : "";

    String[] nameParts = qname.split(":");

    if (nameParts.length == 1) {
      if (StringUtils.hasText(document.getNamespaceURI())) {
        return new QName(document.getNamespaceURI(), nameParts[0]);
      } else {
        return new QName(XMLConstants.NULL_NS_URI, nameParts[0], XMLConstants.DEFAULT_NS_PREFIX);
      }
    } else if (nameParts.length == 2) {
      String namespaceURI =
          nameParts[0].equals(XMLConstants.DEFAULT_NS_PREFIX)
              ? document.lookupNamespaceURI(null)
              : document.lookupNamespaceURI(nameParts[0]);

      return new QName(namespaceURI, nameParts[1], nameParts[0]);
    } else {
      throw new RuntimeException("Failed to parse the QName (" + qname + ")");
    }
  }

  /**
   * Returns the <code>javax.xml.namespace.QName</code> instance for the specified QName.
   *
   * @param element the <code>org.w3c.dom.Element</code> instance used to determine the namespace
   *     URI for the QName if the namespace is referenced using a prefix as part of the <code>name
   *                </code> parameter
   * @param qname the QName
   * @return the <code>javax.xml.namespace.QName</code> instance for the specified QName
   */
  public static QName getQName(Element element, String qname) {
    return getQName(element.getOwnerDocument(), qname);
  }

  /**
   * Returns <code>true</code> if the specified element has a child with the specified name or
   * <code>false</code> otherwise.
   *
   * @param element the parent element
   * @param name the name of the child element
   * @return <code>true</code> if the specified element has a child with the specified name or
   *     <code>false</code> otherwise
   */
  public static boolean hasChildElement(Element element, String name) {
    NodeList nodeList = element.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if (node instanceof Element) {
        Element childElement = (Element) node;

        if (childElement.getNodeName().equals(name)) {
          return true;
        }
      }
    }

    return false;
  }
}
