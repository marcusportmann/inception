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

package digital.inception.bmi;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ResourceUtil;
import digital.inception.core.xml.XmlSchemaClasspathInputSource;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.CaseDefinitionQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * The <code>CaseService</code> class provides the Case Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused", "WeakerAccess" })
public class CaseService
  implements ICaseService
{
  /**
   * The Camunda Process Engine.
   */
  private ProcessEngine processEngine;

  /**
   * Constructs a new <code>CaseService</code>.
   *
   * @param processEngine the Camunda Process Engine
   */
  public CaseService(ProcessEngine processEngine)
  {
    this.processEngine = processEngine;
  }

  /**
   * Check whether the case definition exists.
   *
   * @param caseDefinitionId the ID used to uniquely identify the case definition
   *
   * @return <code>true</code> if the case definition exists or <code>false</code> otherwise
   */
  @Override
  public boolean caseDefinitionExists(String caseDefinitionId)
    throws CaseServiceException
  {
    try
    {
      CaseDefinitionQuery caseDefinitionQuery = processEngine.getRepositoryService()
          .createCaseDefinitionQuery();
      caseDefinitionQuery.caseDefinitionKey(caseDefinitionId).latestVersion();

      return caseDefinitionQuery.count() > 0;
    }
    catch (Throwable e)
    {
      throw new CaseServiceException("Failed to check whether the case definition ("
          + caseDefinitionId + ") exists", e);
    }
  }

  /**
   * Create the new case definition.
   *
   * @param caseDefinitionData the CMMN XML data for the case definition(s)
   *
   * @return the case definition summaries for the CMMN casees defined by the CMMN XML data
   */
  @Override
  @Transactional
  public List<CaseDefinitionSummary> createCaseDefinition(byte[] caseDefinitionData)
    throws InvalidCMMNException, DuplicateCaseDefinitionException, CaseServiceException
  {
    try
    {
      List<CaseDefinitionSummary> caseDefinitionSummaries = validateCMMN(caseDefinitionData);

      for (CaseDefinitionSummary caseDefinitionSummary : caseDefinitionSummaries)
      {
        if (caseDefinitionExists(caseDefinitionSummary.getId()))
        {
          throw new DuplicateCaseDefinitionException(caseDefinitionSummary.getId());
        }
      }

      if (caseDefinitionSummaries.size() != 1)
      {
        throw new InvalidCMMNException(
            "The CMMN 1.1 XML data does not contain a single case definition");
      }

      DeploymentBuilder caseDeployment = processEngine.getRepositoryService().createDeployment();
      caseDeployment.addInputStream(caseDefinitionSummaries.get(0).getId() + ".cmmn",
          new ByteArrayInputStream(caseDefinitionData));

      Deployment deployment = caseDeployment.deploy();

      return caseDefinitionSummaries;
    }
    catch (InvalidCMMNException | DuplicateCaseDefinitionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CaseServiceException("Failed to create the case definition", e);
    }
  }

  /**
   * Returns the summaries for all the case definitions.
   *
   * @return the summaries for all the case definitions
   */
  @Override
  public List<CaseDefinitionSummary> getCaseDefinitionSummaries()
    throws CaseServiceException
  {
    try
    {
      CaseDefinitionQuery caseDefinitionQuery = processEngine.getRepositoryService()
          .createCaseDefinitionQuery().latestVersion();

      List<CaseDefinitionSummary> caseDefinitionSummaries = new ArrayList<>();

      for (CaseDefinition caseDefinition : caseDefinitionQuery.list())
      {
        caseDefinitionSummaries.add(new CaseDefinitionSummary(caseDefinition.getKey(),
            caseDefinition.getName()));
      }

      return caseDefinitionSummaries;
    }
    catch (Throwable e)
    {
      throw new CaseServiceException("Failed to retrieve the case definition summaries", e);
    }
  }

  /**
   * Update the case definition(s).
   *
   * @param caseDefinitionData the CMMN XML data for the case definition(s)
   *
   * @return the case definition summaries for the CMMN casees defined by the CMMN XML data
   */
  @Override
  @Transactional
  public List<CaseDefinitionSummary> updateCaseDefinition(byte[] caseDefinitionData)
    throws InvalidCMMNException, CaseDefinitionNotFoundException, CaseServiceException
  {
    try
    {
      List<CaseDefinitionSummary> caseDefinitionSummaries = validateCMMN(caseDefinitionData);

      for (CaseDefinitionSummary caseDefinitionSummary : caseDefinitionSummaries)
      {
        if (!caseDefinitionExists(caseDefinitionSummary.getId()))
        {
          throw new CaseDefinitionNotFoundException(caseDefinitionSummary.getId());
        }
      }

      if (caseDefinitionSummaries.size() != 1)
      {
        throw new InvalidCMMNException(
            "The CMMN 1.1 XML data does not contain a single case definition");
      }

      DeploymentBuilder caseDeployment = processEngine.getRepositoryService().createDeployment();
      caseDeployment.addInputStream(caseDefinitionSummaries.get(0).getId() + ".cmmn",
          new ByteArrayInputStream(caseDefinitionData));

      Deployment deployment = caseDeployment.deploy();

      return caseDefinitionSummaries;
    }
    catch (InvalidCMMNException | CaseDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CaseServiceException("Failed to update the case definition", e);
    }
  }

  /**
   * Validate the CMMN XML data.
   *
   * @param cmmnXml the CMMN XML data
   *
   * @return the case definition summaries for the CMMN casees if the CMMN XML data was
   *         successfully validated
   */
  public List<CaseDefinitionSummary> validateCMMN(byte[] cmmnXml)
    throws InvalidCMMNException, CaseServiceException
  {
    try
    {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      schemaFactory.setResourceResolver(
          (type, namespaceURI, publicId, systemId, baseURI) ->
          {
            switch (systemId)
            {
              case "CMMN11CaseModel.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                  "META-INF/cmmn/CMMN11CaseModel.xsd");
              }

              case "CMMNDI11.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/cmmn/CMMNDI11.xsd");
              }

              case "DC.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/cmmn/DC.xsd");
              }

              case "DI.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/cmmn/DI.xsd");
              }
            }

            throw new RuntimeException("Failed to resolve the resource (" + systemId + ")");
          }
          );

      Schema schema = schemaFactory.newSchema(new StreamSource[] { new StreamSource(
          new ByteArrayInputStream(ResourceUtil.getClasspathResource("META-INF/cmmn/CMMN11.xsd"))),
          new StreamSource(new ByteArrayInputStream(ResourceUtil.getClasspathResource(
              "META-INF/cmmn/CMMN11.xsd"))) });

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setSchema(schema);

      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      documentBuilder.setErrorHandler(new ErrorHandler()
          {
            @Override
            public void warning(SAXParseException exception)
                throws SAXException
            {
              throw new SAXException("Failed to process the CMMN XML data", exception);
            }

            @Override
            public void error(SAXParseException exception)
                throws SAXException
            {
              throw new SAXException("Failed to process the CMMN XML data", exception);
            }

            @Override
            public void fatalError(SAXParseException exception)
                throws SAXException
            {
              throw new SAXException("Failed to process the CMMN XML data", exception);
            }
          });

      Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(cmmnXml)));

      NodeList caseElements = document.getDocumentElement().getElementsByTagNameNS(
          "http://www.omg.org/spec/CMMN/20151109/MODEL", "case");

      List<CaseDefinitionSummary> caseDefinitionSummaries = new ArrayList<>();

      for (int i = 0; i < caseElements.getLength(); i++)
      {
        Node node = caseElements.item(i);

        if (node instanceof Element)
        {
          Element caseElement = (Element) node;

          caseDefinitionSummaries.add(new CaseDefinitionSummary(caseElement.getAttribute("id"),
              caseElement.getAttribute("name")));
        }
      }

      return caseDefinitionSummaries;
    }
    catch (SAXException e)
    {
      throw new InvalidCMMNException(e);
    }
    catch (Throwable e)
    {
      throw new CaseServiceException("Failed to validate the CMMN XML", e);
    }
  }

  /**
   * Validate the CMMN XML data.
   *
   * @param cmmnXml the CMMN XML data
   *
   * @return the case definition summaries for the CMMN casees if the CMMN XML data was
   *         successfully validated
   */
  public List<CaseDefinitionSummary> validateCMMN(String cmmnXml)
    throws InvalidCMMNException, CaseServiceException
  {
    return validateCMMN(cmmnXml.getBytes(StandardCharsets.UTF_8));
  }
}
