package com.treeshop.daoImpl;

import com.treeshop.dao.LineItemCustomRepository;
import com.treeshop.entity.ProductsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class LineItemCustomRepositoryImpl implements LineItemCustomRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final GetSessionFactory sessionFactory;

    @Autowired
    public LineItemCustomRepositoryImpl(EntityManagerFactory entityManagerFactory, GetSessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ProductsEntity> findTopSellProduct(){
        SessionFactory sessionFactory = this.sessionFactory.getSessionFactory(entityManagerFactory);
        Session session = sessionFactory.openSession();
        String hql = "select l.productsEntity from LineItemEntity l where l.productsEntity.enabled = true group by l.lineItemIdKey.productId order by sum(l.quantity) desc";
        return session.createQuery(hql, ProductsEntity.class).setMaxResults(8).list();
    }

}

