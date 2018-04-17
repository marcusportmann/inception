module digital.inception.core
{
  requires java.naming;
  requires java.sql;
  requires java.xml;
  requires java.xml.bind;

  requires slf4j.api;
  requires spring.beans;
  requires spring.context;
  requires spring.core;
  requires spring.tx;

  exports digital.inception.core.configuration;
  exports digital.inception.core.converters;
  exports digital.inception.core.persistence;
  exports digital.inception.core.service;
  exports digital.inception.core.support;
  exports digital.inception.core.util;
  exports digital.inception.core.wbxml;
  exports digital.inception.core.xml;

  opens digital.inception.core;
  opens digital.inception.core.converters;
  opens digital.inception.core.persistence;
}