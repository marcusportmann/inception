module digital.inception.test
{
  requires cglib.nodep;
  requires digital.inception.core;
  requires h2;
  requires java.sql;
  requires junit;
  requires slf4j.api;
  requires spring.beans;
  requires spring.boot;
  requires spring.boot.autoconfigure;
  requires spring.context;
  requires spring.core;
  requires spring.orm;
  requires spring.test;
  requires spring.tx;
  requires transactions.jdbc;

  exports digital.inception.test;

  opens digital.inception.test;
}