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

package digital.inception.process;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.ResourceUtil;
import digital.inception.core.xml.XmlSchemaClasspathInputSource;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
 * The <code>ProcessService</code> class provides the Process Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ProcessService
  implements IProcessService
{
  /**
   * The Camunda Process Engine.
   */
  private ProcessEngine processEngine;

  /**
   * Constructs a new <code>ProcessService</code>.
   *
   * @param processEngine the Camunda Process Engine
   */
  public ProcessService(ProcessEngine processEngine)
  {
    this.processEngine = processEngine;
  }

  /**
   * Create the new process definition.
   *
   * @param processDefinitionData the BPMN XML data for the process definition(s)
   *
   * @return the process definition summaries for the BPMN processes defined by the BPMN XML data
   */
  @Override
  @Transactional
  public List<ProcessDefinitionSummary> createProcessDefinition(byte[] processDefinitionData)
    throws InvalidBPMNException, DuplicateProcessDefinitionException, ProcessServiceException
  {
    try
    {
      List<ProcessDefinitionSummary> processDefinitionSummaries = validateBPMN(
          processDefinitionData);

      for (ProcessDefinitionSummary processDefinitionSummary : processDefinitionSummaries)
      {
        if (processDefinitionExists(processDefinitionSummary.getId()))
        {
          throw new DuplicateProcessDefinitionException(processDefinitionSummary.getId());
        }
      }

      if (processDefinitionSummaries.size() != 1)
      {
        throw new InvalidBPMNException(
            "The BPMN 2.0 XML data does not contain a single process definition");
      }

      DeploymentBuilder processDeployment = processEngine.getRepositoryService().createDeployment();
      processDeployment.addInputStream(processDefinitionSummaries.get(0).getId() + ".bpmn",
          new ByteArrayInputStream(processDefinitionData));

      Deployment deployment = processDeployment.deploy();

      return processDefinitionSummaries;
    }
    catch (InvalidBPMNException | DuplicateProcessDefinitionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to create the process definition", e);
    }
  }

  /**
   * Returns the summaries for all the process definitions.
   *
   * @return the summaries for all the process definitions
   */
  @Override
  public List<ProcessDefinitionSummary> getProcessDefinitionSummaries()
    throws ProcessServiceException
  {
    try
    {
      ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService()
          .createProcessDefinitionQuery().latestVersion();

      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();

      for (ProcessDefinition processDefinition : processDefinitionQuery.list())
      {
        processDefinitionSummaries.add(new ProcessDefinitionSummary(processDefinition.getKey(),
            processDefinition.getName()));
      }

      return processDefinitionSummaries;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to retrieve the process definition summaries", e);
    }
  }

  /**
   * Check whether the process definition exists.
   *
   * @param processDefinitionId the ID used to uniquely identify the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   */
  @Override
  public boolean processDefinitionExists(String processDefinitionId)
    throws ProcessServiceException
  {
    try
    {
      ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService()
          .createProcessDefinitionQuery();
      processDefinitionQuery.processDefinitionKey(processDefinitionId).latestVersion();

      return processDefinitionQuery.count() > 0;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to check whether the process definition ("
          + processDefinitionId + ") exists", e);
    }
  }

  /**
   * Update the process definition(s).
   *
   * @param processDefinitionData the BPMN XML data for the process definition(s)
   *
   * @return the process definition summaries for the BPMN processes defined by the BPMN XML data
   */
  @Override
  @Transactional
  public List<ProcessDefinitionSummary> updateProcessDefinition(byte[] processDefinitionData)
    throws InvalidBPMNException, ProcessDefinitionNotFoundException, ProcessServiceException
  {
    try
    {
      List<ProcessDefinitionSummary> processDefinitionSummaries = validateBPMN(
          processDefinitionData);

      for (ProcessDefinitionSummary processDefinitionSummary : processDefinitionSummaries)
      {
        if (!processDefinitionExists(processDefinitionSummary.getId()))
        {
          throw new ProcessDefinitionNotFoundException(processDefinitionSummary.getId());
        }
      }

      if (processDefinitionSummaries.size() != 1)
      {
        throw new InvalidBPMNException(
            "The BPMN 2.0 XML data does not contain a single process definition");
      }

      DeploymentBuilder processDeployment = processEngine.getRepositoryService().createDeployment();
      processDeployment.addInputStream(processDefinitionSummaries.get(0).getId() + ".bpmn",
          new ByteArrayInputStream(processDefinitionData));

      Deployment deployment = processDeployment.deploy();

      return processDefinitionSummaries;
    }
    catch (InvalidBPMNException | ProcessDefinitionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to update the process definition", e);
    }
  }

  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
   *         successfully validated
   */
  public List<ProcessDefinitionSummary> validateBPMN(byte[] bpmnXml)
    throws InvalidBPMNException, ProcessServiceException
  {
    try
    {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      schemaFactory.setResourceResolver(
          (type, namespaceURI, publicId, systemId, baseURI) ->
          {
            switch (systemId)
            {
              case "BPMNDI.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/BPMNDI.xsd");
              }

              case "DC.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/DC.xsd");
              }

              case "DI.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/DI.xsd");
              }

              case "Semantic.xsd":
              {
                return new XmlSchemaClasspathInputSource(namespaceURI, publicId, systemId, baseURI,
                    "META-INF/Semantic.xsd");
              }
            }

            throw new RuntimeException("Failed to resolve the resource (" + systemId + ")");
          }
          );

      Schema schema = schemaFactory.newSchema(new StreamSource[] { new StreamSource(
          new ByteArrayInputStream(ResourceUtil.getClasspathResource("META-INF/BPMN20.xsd"))),
          new StreamSource(new ByteArrayInputStream(ResourceUtil.getClasspathResource(
              "META-INF/BPMN20.xsd"))) });

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
              throw new SAXException("Failed to process the BPMN XML data", exception);
            }

            @Override
            public void error(SAXParseException exception)
                throws SAXException
            {
              throw new SAXException("Failed to process the BPMN XML data", exception);
            }

            @Override
            public void fatalError(SAXParseException exception)
                throws SAXException
            {
              throw new SAXException("Failed to process the BPMN XML data", exception);
            }
          });

      Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(bpmnXml)));

      NodeList processElements = document.getDocumentElement().getElementsByTagNameNS(
          "http://www.omg.org/spec/BPMN/20100524/MODEL", "process");

      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();

      for (int i = 0; i < processElements.getLength(); i++)
      {
        Node node = processElements.item(i);

        if (node instanceof Element)
        {
          Element processElement = (Element) node;

          processDefinitionSummaries.add(new ProcessDefinitionSummary(processElement.getAttribute(
              "id"), processElement.getAttribute("name")));
        }
      }

      return processDefinitionSummaries;
    }
    catch (SAXException e)
    {
      throw new InvalidBPMNException(e);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to validate the BPMN XML", e);
    }
  }

  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the process definition summaries for the BPMN processes if the BPMN XML data was
   *         successfully validated
   */
  public List<ProcessDefinitionSummary> validateBPMN(String bpmnXml)
    throws InvalidBPMNException, ProcessServiceException
  {
    return validateBPMN(bpmnXml.getBytes(StandardCharsets.UTF_8));
  }
}
