///*
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package digital.inception.flowable.service;
//
//import digital.inception.core.service.InvalidArgumentException;
//import digital.inception.core.service.ServiceUnavailableException;
//import digital.inception.core.util.ResourceUtil;
//import digital.inception.core.xml.XmlSchemaClasspathInputSource;
//import digital.inception.flowable.model.InvalidBPMNException;
//import digital.inception.flowable.model.ProcessDefinitionSummary;
//import java.io.ByteArrayInputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import javax.xml.XMLConstants;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.Schema;
//import javax.xml.validation.SchemaFactory;
//// import org.camunda.bpm.engine.ProcessEngine;
//// import org.camunda.bpm.engine.persistence.Deployment;
//// import org.camunda.bpm.engine.persistence.DeploymentBuilder;
//// import org.camunda.bpm.engine.persistence.ProcessDefinition;
//// import org.camunda.bpm.engine.persistence.ProcessDefinitionQuery;
//// import org.camunda.bpm.engine.runtime.ProcessInstance;
//import org.flowable.engine.ProcessEngine;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.ErrorHandler;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
//
///**
// * The <b>ProcessService</b> class provides the Process Service implementation.
// *
// * @author Marcus Portmann
// */
//@Service
//@SuppressWarnings({"unused", "WeakerAccess"})
//public class ProcessService implements IProcessService {
//
//  /** The Flowable Process Engine. */
//  private final ProcessEngine processEngine;
//
//  /**
//   * Constructs a new <b>ProcessService</b>.
//   *
//   * @param processEngine the Camunda Process Engine
//   */
//  public ProcessService(ProcessEngine processEngine) {
//    this.processEngine = processEngine;
//  }
//
//  //  @Override
//  //  @Transactional
//  //  public List<ProcessDefinitionSummary> createProcessDefinition(byte[] processDefinitionData)
//  //      throws InvalidArgumentException, InvalidBPMNException,
//  // DuplicateProcessDefinitionException,
//  //          ServiceUnavailableException {
//  //    if ((processDefinitionData == null) || (processDefinitionData.length == 0)) {
//  //      throw new InvalidArgumentException("processDefinitionData");
//  //    }
//  //
//  //    try {
//  //      List<ProcessDefinitionSummary> processDefinitionSummaries =
//  //          validateBPMN(processDefinitionData);
//  //
//  //      for (ProcessDefinitionSummary processDefinitionSummary : processDefinitionSummaries) {
//  //        if (processDefinitionExists(processDefinitionSummary.getId())) {
//  //          throw new DuplicateProcessDefinitionException(processDefinitionSummary.getId());
//  //        }
//  //      }
//  //
//  //      if (processDefinitionSummaries.size() != 1) {
//  //        throw new InvalidBPMNException(
//  //            "The BPMN 2.0 XML data does not contain a single process definition");
//  //      }
//  //
//  //      DeploymentBuilder deploymentBuilder =
//  // processEngine.getRepositoryService().createDeployment();
//  //      deploymentBuilder.addInputStream(
//  //          processDefinitionSummaries.get(0).getId() + ".bpmn",
//  //          new ByteArrayInputStream(processDefinitionData));
//  //
//  //      Deployment deployment = deploymentBuilder.deploy();
//  //
//  //      return processDefinitionSummaries;
//  //    } catch (InvalidBPMNException | DuplicateProcessDefinitionException e) {
//  //      throw e;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException("Failed to create the process definition", e);
//  //    }
//  //  }
//  //
//  //  @Override
//  //  @Transactional
//  //  public void deleteProcessDefinition(String processDefinitionId)
//  //      throws InvalidArgumentException, ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException {
//  //    if (!StringUtils.hasText(processDefinitionId)) {
//  //      throw new InvalidArgumentException("processDefinitionId");
//  //    }
//  //
//  //    try {
//  //      ProcessDefinitionQuery processDefinitionQuery =
//  //          processEngine.getRepositoryService().createProcessDefinitionQuery();
//  //      processDefinitionQuery.processDefinitionKey(processDefinitionId);
//  //
//  //      if (processDefinitionQuery.count() == 0) {
//  //        throw new ProcessDefinitionNotFoundException(processDefinitionId);
//  //      }
//  //
//  //      List<ProcessDefinition> processsDefinitions = processDefinitionQuery.list();
//  //
//  //      for (ProcessDefinition processDefinition : processsDefinitions) {
//  //
//  // processEngine.getRepositoryService().deleteDeployment(processDefinition.getDeploymentId());
//  //      }
//  //    } catch (ProcessDefinitionNotFoundException e) {
//  //      throw e;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException("Failed to delete the process definition", e);
//  //    }
//  //  }
//  //
//  //  @Override
//  //  public List<ProcessDefinitionSummary> getProcessDefinitionSummaries()
//  //      throws ServiceUnavailableException {
//  //    try {
//  //      ProcessDefinitionQuery processDefinitionQuery =
//  //          processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion();
//  //
//  //      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();
//  //
//  //      for (ProcessDefinition processDefinition : processDefinitionQuery.list()) {
//  //        processDefinitionSummaries.add(
//  //            new ProcessDefinitionSummary(
//  //                processDefinition.getKey(),
//  //                processDefinition.getName(),
//  //                processDefinition.getVersion()));
//  //      }
//  //
//  //      return processDefinitionSummaries;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException(
//  //          "Failed to retrieve the process definition summaries", e);
//  //    }
//  //  }
//  //
//  //  @Override
//  //  public boolean processDefinitionExists(String processDefinitionId)
//  //      throws InvalidArgumentException, ServiceUnavailableException {
//  //    if (!StringUtils.hasText(processDefinitionId)) {
//  //      throw new InvalidArgumentException("processDefinitionId");
//  //    }
//  //
//  //    try {
//  //      ProcessDefinitionQuery processDefinitionQuery =
//  //          processEngine.getRepositoryService().createProcessDefinitionQuery();
//  //      processDefinitionQuery.processDefinitionKey(processDefinitionId).latestVersion();
//  //
//  //      return processDefinitionQuery.count() > 0;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException(
//  //          "Failed to check whether the process definition (" + processDefinitionId + ") exists",
//  // e);
//  //    }
//  //  }
//  //
//  //  @Override
//  //  @Transactional
//  //  public void startProcessInstance(String processDefinitionId, Map<String, Object> parameters)
//  //      throws InvalidArgumentException, ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException {
//  //    if (!StringUtils.hasText(processDefinitionId)) {
//  //      throw new InvalidArgumentException("processDefinitionId");
//  //    }
//  //
//  //    if (parameters == null) {
//  //      throw new InvalidArgumentException("parameters");
//  //    }
//  //
//  //    try {
//  //      if (!processDefinitionExists(processDefinitionId)) {
//  //        throw new ProcessDefinitionNotFoundException(processDefinitionId);
//  //      }
//  //
//  //      ProcessInstance processInstance =
//  //          processEngine
//  //              .getRuntimeService()
//  //              .startProcessInstanceByKey(processDefinitionId, parameters);
//  //    } catch (ProcessDefinitionNotFoundException e) {
//  //      throw e;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException(
//  //          "Failed to start the process instance (" + processDefinitionId + ")", e);
//  //    }
//  //  }
//  //
//  //  @Override
//  //  @Transactional
//  //  public List<ProcessDefinitionSummary> updateProcessDefinition(byte[] processDefinitionData)
//  //      throws InvalidArgumentException, InvalidBPMNException, ProcessDefinitionNotFoundException,
//  //          ServiceUnavailableException {
//  //    if ((processDefinitionData == null) || (processDefinitionData.length == 0)) {
//  //      throw new InvalidArgumentException("processDefinitionData");
//  //    }
//  //
//  //    try {
//  //      List<ProcessDefinitionSummary> processDefinitionSummaries =
//  //          validateBPMN(processDefinitionData);
//  //
//  //      for (ProcessDefinitionSummary processDefinitionSummary : processDefinitionSummaries) {
//  //        if (!processDefinitionExists(processDefinitionSummary.getId())) {
//  //          throw new ProcessDefinitionNotFoundException(processDefinitionSummary.getId());
//  //        }
//  //      }
//  //
//  //      if (processDefinitionSummaries.size() != 1) {
//  //        throw new InvalidBPMNException(
//  //            "The BPMN 2.0 XML data does not contain a single process definition");
//  //      }
//  //
//  //      DeploymentBuilder deploymentBuilder =
//  // processEngine.getRepositoryService().createDeployment();
//  //      deploymentBuilder.addInputStream(
//  //          processDefinitionSummaries.get(0).getId() + ".bpmn",
//  //          new ByteArrayInputStream(processDefinitionData));
//  //
//  //      Deployment deployment = deploymentBuilder.deploy();
//  //
//  //      return processDefinitionSummaries;
//  //    } catch (InvalidBPMNException | ProcessDefinitionNotFoundException e) {
//  //      throw e;
//  //    } catch (Throwable e) {
//  //      throw new ServiceUnavailableException("Failed to update the process definition", e);
//  //    }
//  //  }
//
//  @Override
//  public List<ProcessDefinitionSummary> validateBPMN(String bpmnXml)
//      throws InvalidArgumentException, InvalidBPMNException, ServiceUnavailableException {
//    if (!StringUtils.hasText(bpmnXml)) {
//      throw new InvalidArgumentException("bpmnXml");
//    }
//
//    return validateBPMN(bpmnXml.getBytes(StandardCharsets.UTF_8));
//  }
//
//  @Override
//  public List<ProcessDefinitionSummary> validateBPMN(byte[] bpmnXml)
//      throws InvalidArgumentException, InvalidBPMNException, ServiceUnavailableException {
//    if ((bpmnXml == null) || (bpmnXml.length == 0)) {
//      throw new InvalidArgumentException("bpmnXml");
//    }
//
//    try {
//      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//      schemaFactory.setResourceResolver(
//          (type, namespaceURI, publicId, systemId, baseURI) -> {
//            switch (systemId) {
//              case "BPMNDI.xsd":
//                {
//                  return new XmlSchemaClasspathInputSource(
//                      namespaceURI, publicId, systemId, baseURI, "META-INF/bpmn/BPMNDI.xsd");
//                }
//
//              case "DC.xsd":
//                {
//                  return new XmlSchemaClasspathInputSource(
//                      namespaceURI, publicId, systemId, baseURI, "META-INF/bpmn/DC.xsd");
//                }
//
//              case "DI.xsd":
//                {
//                  return new XmlSchemaClasspathInputSource(
//                      namespaceURI, publicId, systemId, baseURI, "META-INF/bpmn/DI.xsd");
//                }
//
//              case "Semantic.xsd":
//                {
//                  return new XmlSchemaClasspathInputSource(
//                      namespaceURI, publicId, systemId, baseURI, "META-INF/bpmn/Semantic.xsd");
//                }
//            }
//
//            throw new RuntimeException("Failed to resolve the resource (" + systemId + ")");
//          });
//
//      Schema schema =
//          schemaFactory.newSchema(
//              new StreamSource[] {
//                new StreamSource(
//                    new ByteArrayInputStream(
//                        ResourceUtil.getClasspathResource("META-INF/bpmn/BPMN20.xsd")))
//              });
//
//      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//      documentBuilderFactory.setNamespaceAware(true);
//      documentBuilderFactory.setSchema(schema);
//
//      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//      documentBuilder.setErrorHandler(
//          new ErrorHandler() {
//            @Override
//            public void error(SAXParseException exception) throws SAXException {
//              throw new SAXException("Failed to process the BPMN XML data", exception);
//            }
//
//            @Override
//            public void fatalError(SAXParseException exception) throws SAXException {
//              throw new SAXException("Failed to process the BPMN XML data", exception);
//            }
//
//            @Override
//            public void warning(SAXParseException exception) throws SAXException {
//              throw new SAXException("Failed to process the BPMN XML data", exception);
//            }
//          });
//
//      Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(bpmnXml)));
//
//      NodeList processElements =
//          document
//              .getDocumentElement()
//              .getElementsByTagNameNS("http://www.omg.org/spec/BPMN/20100524/MODEL", "process");
//
//      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();
//
//      for (int i = 0; i < processElements.getLength(); i++) {
//        Node node = processElements.item(i);
//
//        if (node instanceof Element) {
//          Element processElement = (Element) node;
//
//          //          String versionTag =
//          //              processElement.getAttributeNS("http://camunda.org/schema/1.0/bpmn",
//          // "versionTag");
//
//          processDefinitionSummaries.add(
//              new ProcessDefinitionSummary(
//                  processElement.getAttribute("id"), processElement.getAttribute("name"), 0));
//        }
//      }
//
//      return processDefinitionSummaries;
//    } catch (SAXException e) {
//      throw new InvalidBPMNException(e);
//    } catch (Throwable e) {
//      throw new ServiceUnavailableException("Failed to validate the BPMN XML data", e);
//    }
//  }
//}
