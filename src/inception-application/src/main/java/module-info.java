module digital.inception.application
{
  requires com.fasterxml.jackson.databind;
  requires digital.inception.core;
  requires digital.inception.json;
  requires digital.inception.transaction;
  requires httpclient;
  requires httpcore;
  requires java.sql;
  requires javax.servlet.api;
  requires slf4j.api;
  requires spring.beans;
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.context;
  requires spring.core;
  requires spring.orm;
  requires spring.tx;
  requires spring.web;
  requires transactions.jdbc;
  requires undertow.core;
  requires xnio.api;

  exports digital.inception.application;

  opens digital.inception.application;
}