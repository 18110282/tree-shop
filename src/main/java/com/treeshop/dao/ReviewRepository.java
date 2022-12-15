package com.treeshop.dao;

import com.treeshop.entity.review.ReviewsEntity;
import com.treeshop.entity.review.ReviewsIdKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<ReviewsEntity, ReviewsIdKey> {

}
