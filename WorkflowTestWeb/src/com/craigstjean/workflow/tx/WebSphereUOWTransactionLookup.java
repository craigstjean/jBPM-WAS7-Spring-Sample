package com.craigstjean.workflow.tx;

import java.lang.reflect.Method;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;

import org.hibernate.HibernateException;
import org.hibernate.service.jta.platform.internal.AbstractJtaPlatform;

/**
* @author rafaelri Support for proprietary interfaces for registering
*         synchronizations in WebSphere 6.1+
*/
public class WebSphereUOWTransactionLookup extends AbstractJtaPlatform {

   private static final long serialVersionUID = 6514686920271789210L;
   private static final String UOW_MANAGER_JNDI = "java:comp/websphere/UOWManager";
   private static final String UOW_SYNCHRONIZATION_MANAGER_JNDI = "java:comp/websphere/UOWSynchronizationRegistry";
   public static final String UT_NAME = "java:comp/UserTransaction";

   @Override
   protected TransactionManager locateTransactionManager() {
      return new TransactionManagerAdapter();
   }

   @Override
   protected UserTransaction locateUserTransaction() {
      return (UserTransaction) jndiService().locate(UT_NAME);
   }

   public class TransactionManagerAdapter implements TransactionManager {

      private final Class<?> uowManagerClass;
      private final Class<?> uowSynchronizationRegistryClass;
      private Object uowManager;
      private Object uowSynchronizationRegistry;
      private final Method registerSynchronizationMethod;
      private final Method setRollbackOnlyMethod;

      private final Method getLocalIdMethod;

      @SuppressWarnings({ "unchecked", "rawtypes" })
      private TransactionManagerAdapter() {
         try {
            uowManagerClass = Class.forName("com.ibm.ws.uow.UOWManager");
            setRollbackOnlyMethod = uowManagerClass.getMethod(
                  "setRollbackOnly", new Class[] {});

            uowSynchronizationRegistryClass = Class
                  .forName("com.ibm.websphere.uow.UOWSynchronizationRegistry");

            registerSynchronizationMethod = uowSynchronizationRegistryClass
                  .getMethod("registerInterposedSynchronization",
                        new Class[] { Synchronization.class });
            Class extendedJTATransactionClass = Class
                  .forName("com.ibm.websphere.jtaextensions.ExtendedJTATransaction");
            getLocalIdMethod = extendedJTATransactionClass.getMethod(
                  "getLocalId", new Class[0]);

         } catch (ClassNotFoundException cnfe) {
            throw new HibernateException(cnfe);
         } catch (NoSuchMethodException nsme) {
            throw new HibernateException(nsme);
         }
      }

      /**
       * Lazily loaded because UOW_MANAGER_JNDI isn't always available on
       * Hibernate startup (when
       * HibernatePersistence.createContainerEntityManagerFactory is called).
       */
      private Object getUowManager() {
         if (uowManager == null) {
            uowManager = jndiService().locate(UOW_MANAGER_JNDI);
         }
         return uowManager;
      }

      private Object getUowSynchronizationRegistry() {
         if (uowSynchronizationRegistry == null) {
            uowSynchronizationRegistry = jndiService().locate(UOW_SYNCHRONIZATION_MANAGER_JNDI);
         }
         return uowSynchronizationRegistry;
      }

      public void begin() throws NotSupportedException, SystemException {
         throw new UnsupportedOperationException();
      }

      public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
         throw new UnsupportedOperationException();
      }

      @Override
      public int getStatus() throws SystemException {
         return getTransaction() == null ? Status.STATUS_NO_TRANSACTION
               : getTransaction().getStatus();
      }

      public Transaction getTransaction() throws SystemException {
         return new TransactionAdapter();
      }

      public void resume(Transaction txn) throws InvalidTransactionException,
            IllegalStateException, SystemException {
         throw new UnsupportedOperationException();
      }

      public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
         throw new UnsupportedOperationException();
      }

      public void setRollbackOnly() throws IllegalStateException,
            SystemException {
         try {
            setRollbackOnlyMethod.invoke(getUowManager(), new Object[] {});
         } catch (Exception e) {
            throw new HibernateException(e);
         }
      }

      public void setTransactionTimeout(int i) throws SystemException {
         throw new UnsupportedOperationException();
      }

      public Transaction suspend() throws SystemException {
         throw new UnsupportedOperationException();
      }

      public class TransactionAdapter implements Transaction {

         private final Object extendedJTATransaction;

         private TransactionAdapter() {
            extendedJTATransaction = jndiService().locate(
                  "java:comp/websphere/ExtendedJTATransaction");
         }

         public void registerSynchronization(
               final Synchronization synchronization)
               throws RollbackException, IllegalStateException,
               SystemException {

            try {
               registerSynchronizationMethod.invoke(
                     getUowSynchronizationRegistry(),
                     new Object[] { synchronization });
            } catch (Exception e) {
               throw new HibernateException(e);
            }

         }

         public int hashCode() {
            return getLocalId().hashCode();
         }

         public boolean equals(Object other) {
            if (!(other instanceof TransactionAdapter))
               return false;
            TransactionAdapter that = (TransactionAdapter) other;
            return getLocalId().equals(that.getLocalId());
         }

         private Object getLocalId() {
            try {
               return getLocalIdMethod
                     .invoke(extendedJTATransaction, new Object[0]);
            } catch (Exception e) {
               throw new HibernateException(e);
            }
         }

         public void commit() throws RollbackException,
               HeuristicMixedException, HeuristicRollbackException,
               SecurityException, IllegalStateException, SystemException {
            throw new UnsupportedOperationException();
         }

         public boolean delistResource(XAResource resource, int i)
               throws IllegalStateException, SystemException {
            throw new UnsupportedOperationException();
         }

         public boolean enlistResource(XAResource resource)
               throws RollbackException, IllegalStateException,
               SystemException {
            throw new UnsupportedOperationException();
         }

         public int getStatus() throws SystemException {
            return new Integer(0).equals(getLocalId()) ? Status.STATUS_NO_TRANSACTION
                  : Status.STATUS_ACTIVE;
         }

         public void rollback() throws IllegalStateException,
               SystemException {
            throw new UnsupportedOperationException();
         }

         public void setRollbackOnly() throws IllegalStateException,
               SystemException {
            try {
               setRollbackOnlyMethod.invoke(getUowManager(), new Object[] {});
            } catch (Exception e) {
               throw new HibernateException(e);
            }
         }
      }

   }

   /**
    * {@inheritDoc}
    */
   public Object getTransactionIdentifier(Transaction transaction) {
      // WebSphere, however, is not a sane JEE/JTA container...
      return new Integer(transaction.hashCode());
   }
}