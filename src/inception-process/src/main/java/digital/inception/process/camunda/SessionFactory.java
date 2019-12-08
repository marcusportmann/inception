//package digital.inception.process.camunda;
//
//import digital.inception.process.camunda.PersistenceSession;
//import org.camunda.bpm.engine.impl.interceptor.Session;
//
//public class SessionFactory
//  implements org.camunda.bpm.engine.impl.interceptor.SessionFactory
//{
//  @Override
//  public Class<?> getSessionType()
//  {
//    return org.camunda.bpm.engine.impl.db.PersistenceSession.class;
//  }
//
//  @Override
//  public Session openSession()
//  {
//    return new PersistenceSession();
//  }
//}
