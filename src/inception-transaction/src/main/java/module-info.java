module digital.inception.transaction
{
  requires digital.inception.core;
  //requires java.transaction;
  requires spring.context;
  requires spring.tx;
  requires transactions.jta;
  requires transactions.jdbc;

  exports digital.inception.transaction;
}