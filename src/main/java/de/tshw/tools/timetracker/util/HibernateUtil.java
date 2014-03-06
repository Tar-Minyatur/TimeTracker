package de.tshw.tools.timetracker.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.Stoppable;

public class HibernateUtil {

    ServiceRegistry serviceRegistry = null;
    SessionFactory sessionFactory = null;

    public SessionFactory getSessionFactory() {
        if ((this.sessionFactory == null) || (this.serviceRegistry == null)) {
            Configuration config = new Configuration();
            config.configure();
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySettings(config.getProperties());
            this.serviceRegistry = registryBuilder.build();
            this.sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
        return this.sessionFactory;
    }

    public void closeSession () {
        final SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
        ConnectionProvider connectionProvider = sessionFactoryImplementor.getConnectionProvider();
        if (Stoppable.class.isInstance(connectionProvider)) {
            ((Stoppable) connectionProvider).stop();
        }
    }
}