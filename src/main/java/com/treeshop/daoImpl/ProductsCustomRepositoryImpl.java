package com.treeshop.daoImpl;

import com.treeshop.dao.ProductsCustomRepository;
import com.treeshop.entity.ProductsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class ProductsCustomRepositoryImpl implements ProductsCustomRepository {
    private final EntityManagerFactory entityManagerFactory;
    private final GetSessionFactory sessionFactory;

    @Autowired
    public ProductsCustomRepositoryImpl(EntityManagerFactory entityManagerFactory, GetSessionFactory sessionFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ProductsEntity> findRandomProductInSameCategory(String categoryId) {
        SessionFactory sessionFactory = this.sessionFactory.getSessionFactory(entityManagerFactory);
        Session session = sessionFactory.openSession();
        String hql1 = "select p from ProductsEntity p where p.categoryId = :categoryId and p.enabled = true";
        List<ProductsEntity> productsEntityList =  session.createQuery(hql1, ProductsEntity.class).setParameter("categoryId", categoryId).list();
        Random rand = new Random();
        List<ProductsEntity> productsEntityListRandom = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if(productsEntityList.size() == 0){
                break;
            }
            int randomIndex = rand.nextInt(productsEntityList.size());
            ProductsEntity randomElement = productsEntityList.get(randomIndex);
            productsEntityList.remove(randomIndex);
            productsEntityListRandom.add(randomElement);
        }
        return productsEntityListRandom;
    }

    @Override
    public List<ProductsEntity> searchByCondition(Integer max, Integer min, String weight, String height) {
        SessionFactory sessionFactory = this.sessionFactory.getSessionFactory(entityManagerFactory);
        Session session = sessionFactory.openSession();
        String hql = "select p from ProductsEntity p where p.enabled = true ";
        if (max != null) {
            hql += " and p.price <= :maxP";
        }
        if (min != null) {
            hql += " and p.price >= :minP";
        }
        if (weight != null) {
            switch (weight) {
                case "<2000":
                    hql += " and p.weight < 2000";
                    break;
                case "2000-10000":
                    hql += " and p.weight >= 2000";
                    hql += " and p.weight <= 10000";
                    break;
                case ">10000":
                    hql += " and p.weight > 10000";
                    break;
            }
            ;
        }
        if (height != null) {
            switch (height) {
                case "<50":
                    hql += " and p.height < 50";
                    break;
                case "50-150":
                    hql += " and p.height >= 50";
                    hql += " and p.height <= 150";
                    break;
                case ">150":
                    hql += " and p.height > 150";
                    break;
            }
            ;
        }
        Query<ProductsEntity> query = session.createQuery(hql, ProductsEntity.class);
        if (max != null) {
            query.setParameter("maxP", max);
        }
        if (min != null) {
            query.setParameter("minP", min);
        }
        return query.list();
    }
}
