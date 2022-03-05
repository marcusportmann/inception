/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.core.wbxml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The <b>Element</b> class stores the data for a WBXML element content type in a WBXML document.
 *
 * <p>This content type represents a node in the WBXML document.
 *
 * @author Marcus Portmann
 */
public class Element implements Serializable, Content {

  private static final long serialVersionUID = 1000000;

  /** The attributes for the element. */
  private final List<Attribute> attributes = new ArrayList<>();

  /** The text content for the element. */
  private final List<Content> content = new ArrayList<>();

  /** The name of the element. */
  private String name = null;

  /** Hidden default constructor. */
  protected Element() {}

  /**
   * Constructs an <b>Element</b> with the specified name.
   *
   * @param name the name of the element
   */
  public Element(String name) {
    this.name = name;
  }

  /**
   * Constructs an <b>Element</b> with the specified name and content.
   *
   * @param name the name of the element
   * @param content the content for the element
   */
  public Element(String name, byte[] content) {
    this(name); // invoke above constructor
    this.addContent(content);
  }

  /**
   * Constructs an <b>Element</b> with the specified name and content.
   *
   * @param name the name of the element
   * @param content the content for the element
   */
  public Element(String name, String content) {
    this(name); // invoke above constructor
    this.addContent(content);
  }

  /**
   * Add the binary content to the element as an Opaque content type instance.
   *
   * @param data the binary content to add to the element
   */
  public void addContent(byte[] data) {
    content.add(new Opaque(data));
  }

  /**
   * Add the specified element as a child element to this element.
   *
   * @param element the child element
   */
  public void addContent(Element element) {
    content.add(element);
  }

  /**
   * Add the text content to the element as a CDATA content type instance.
   *
   * @param text the text content
   */
  public void addContent(String text) {
    content.add(new CDATA(text));
  }

  /**
   * Returns the attribute with the specified name.
   *
   * @param name the name of the attribute
   * @return an Optional containing the attribute with the specified name or an empty Optional if no
   *     matching attribute could be found
   */
  public Optional<Attribute> getAttribute(String name) {
    return attributes.stream()
        .filter(attribute -> Objects.equals(attribute.getName(), name))
        .findFirst();
  }

  /**
   * Returns the value for the attribute with the specified name.
   *
   * @param name the name of the attribute
   * @return an Optional containing the value for the attribute with the specified name or an empty
   *     Optional if no matching attribute could be found
   */
  public Optional<String> getAttributeValue(String name) {
    Optional<Attribute> attributeOptional = getAttribute(name);

    if (attributeOptional.isPresent()) {
      return Optional.of(attributeOptional.get().getValue());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the attributes for this element.
   *
   * @return the attributes for this element
   */
  public List<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * Returns the child element with the specified name.
   *
   * @param name the name of the child element
   * @return an Optional containing the child element with the specified name or an empty Optional
   *     if the child element could not be found
   */
  public Optional<Element> getChild(String name) {
    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        Element element = (Element) tmpContent;

        if (element.getName().equals(name)) {
          return Optional.of(element);
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Get the binary data content for the child element with the specified name.
   *
   * @param name the name of the child element
   * @return an Optional containing the binary data content for the child element or an empty
   *     Optional if a child element with the specified name could not be found
   */
  public Optional<byte[]> getChildOpaque(String name) {
    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        Element element = (Element) tmpContent;

        if (element.getName().equals(name)) {
          return Optional.of(element.getOpaque());
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Get the text content for the child element with the specified name.
   *
   * @param name the name of the child element
   * @return an Optional containing the text content for the child element or an empty Optional if a
   *     child element with the specified name could not be found
   */
  public Optional<String> getChildText(String name) {
    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        Element element = (Element) tmpContent;

        if (element.getName().equals(name)) {
          return Optional.of(element.getText());
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Returns the child elements for this element.
   *
   * @return the child elements for this element
   */
  public List<Element> getChildren() {
    List<Element> list = new ArrayList<>();

    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        list.add((Element) tmpContent);
      }
    }

    return Collections.unmodifiableList(list);
  }

  /**
   * Returns the child elements with the specified name for this element.
   *
   * @param name the name of the child elements to return
   * @return the child elements with the specified name
   */
  public List<Element> getChildren(String name) {
    return content.stream()
        .filter(tmpContent -> tmpContent instanceof Element)
        .map(tmpContent -> (Element) tmpContent)
        .filter(e -> Objects.equals(e.getName(), name))
        .collect(Collectors.toList());
  }

  /**
   * Returns the content items for this element.
   *
   * @return the content items for this element
   */
  public List<Content> getContent() {
    return content;
  }

  /**
   * Returns the name of the element.
   *
   * @return the name of the element
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the binary data content for the element.
   *
   * @return the binary data content for the element
   */
  public byte[] getOpaque() {
    BinaryBuffer buffer = new BinaryBuffer();

    for (Content tmpContent : content) {
      if (tmpContent instanceof Opaque) {
        Opaque opaque = (Opaque) tmpContent;

        buffer.append(opaque.getData());
      }
    }

    return buffer.getData();
  }

  /**
   * Returns the text content for the element.
   *
   * @return the text content for the element
   */
  public String getText() {
    StringBuilder buffer = new StringBuilder();

    for (Content tmpContent : content) {
      if (tmpContent instanceof CDATA) {
        CDATA cdata = (CDATA) tmpContent;

        buffer.append(cdata.getText());
      }
    }

    return buffer.toString();
  }

  /**
   * Returns whether the element has an attribute with the specified name.
   *
   * @param name the name of the attribute
   * @return <b>true</b> if the element has an attribute with the specified name or <b> false</b>
   *     otherwise
   */
  public boolean hasAttribute(String name) {
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if the element has attributes.
   *
   * @return true if the element has attributes or false otherwise
   */
  public boolean hasAttributes() {
    return (attributes.size() > 0);
  }

  /**
   * Returns <b>true</b> if the element has a child element with the specified name or <b> false</b>
   * otherwise.
   *
   * @param name the name of the child element
   * @return <b>true</b> if the element has a child element with the specified name or <b>false</b>
   *     otherwise
   */
  public boolean hasChild(String name) {
    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        if (((Element) tmpContent).getName().equals(name)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true if the element has child elements.
   *
   * @return true if the element has child elements or false otherwise
   */
  public boolean hasChildren() {
    for (Content tmpContent : content) {
      if (tmpContent instanceof Element) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if the element has content.
   *
   * @return true if the element has content or false otherwise
   */
  public boolean hasContent() {
    return (content.size() > 0);
  }

  /**
   * Print the content using the specified indent level.
   *
   * @param indent the indent level
   */
  public void print(int indent) {
    print(System.out, indent);
  }

  /**
   * Print the content to the specified <b>OutputStream</b> using the specified indent level.
   *
   * @param out the <b>OuputStream</b> to output the content to
   * @param indent the indent level
   */
  public void print(OutputStream out, int indent) {
    PrintStream pout = new PrintStream(out);

    pout.print("<" + name);

    if (hasAttributes()) {
      for (Attribute attribute : attributes) {
        pout.print(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
      }
    }

    if (hasContent()) {
      pout.println(">");

      for (Content tmpContent : content) {
        tmpContent.print(pout, indent + 1);
      }

      pout.print("</" + name + ">");
    } else {
      pout.print("/>");
    }

    // don't close pout - it will close out (the underlying outputstream)
    // see API - PrintStream.close().
  }

  /**
   * Remove the attribute with the specified name.
   *
   * @param name the name of the attribute to remove
   */
  public void removeAttribute(String name) {
    for (int i = 0; i < attributes.size(); i++) {
      if (attributes.get(i).getName().equals(name)) {
        attributes.remove(i);

        return;
      }
    }
  }

  /**
   * Set the value of the attribute with the specified name for the element.
   *
   * @param name the name of the attribute
   * @param value the value for the attribute
   */
  public void setAttribute(String name, String value) {
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        attribute.setValue(value);

        return;
      }
    }

    attributes.add(new Attribute(name, value));
  }

  /**
   * Set the binary data content for the element.
   *
   * @param data the binary data content for the element
   */
  public void setOpaque(byte[] data) {
    boolean hasRemoved = true;

    while (hasRemoved) {
      hasRemoved = false;

      for (int i = 0; i < content.size(); i++) {
        if (this.content.get(i) instanceof Opaque) {
          hasRemoved = true;
          this.content.remove(i);

          break;
        }
      }
    }

    content.add(new Opaque(data));
  }

  /**
   * Set the text content for the element.
   *
   * @param text the text content for the element
   */
  public void setText(String text) {
    boolean hasRemoved = true;

    while (hasRemoved) {
      hasRemoved = false;

      for (int i = 0; i < content.size(); i++) {
        if (this.content.get(i) instanceof CDATA) {
          hasRemoved = true;
          this.content.remove(i);

          break;
        }
      }
    }

    content.add(new CDATA(text));
  }

  /**
   * Return the string representation of the element.
   *
   * @return the string representation of the element
   */
  @Override
  public String toString() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      print(baos, 0);

      String result = baos.toString();

      baos.close();

      return result;
    } catch (Exception e) {
      return super.toString();
    }
  }

  /**
   * Set the name for the element.
   *
   * @param name the name for the element
   */
  protected void setName(String name) {
    this.name = name;
  }
}
