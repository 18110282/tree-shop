package com.treeshop.service;

import com.treeshop.entity.review.ReviewsEntity;

import java.util.List;

public interface ReviewProductService {
    void addReviewForProduct(List<ReviewsEntity> reviewsEntityList, String orderId);
}
