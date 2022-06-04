package com.treeshop.daoImpl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class GetSessionFactory {
    public SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
