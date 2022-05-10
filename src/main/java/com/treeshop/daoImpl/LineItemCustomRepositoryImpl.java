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
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public SessionFactory getSessionFactory() {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
    @Override
    public List<ProductsEntity> findTopSellProduct(){
        SessionFactory sessionFactory = this.getSessionFactory();
        Session session = sessionFactory.openSession();
        String hql = "select l.productsEntity from LineItemEntity l group by l.lineItemIdKey.productId order by sum(l.quantity) desc";
        return session.createQuery(hql, ProductsEntity.class).setMaxResults(8).list();
    }

}

