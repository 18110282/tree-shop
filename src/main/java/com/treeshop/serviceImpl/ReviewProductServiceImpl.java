package com.treeshop.serviceImpl;

import com.treeshop.dao.OrdersRepository;
import com.treeshop.dao.ReviewRepository;
import com.treeshop.entity.review.ReviewsEntity;
import com.treeshop.service.ReviewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReviewProductServiceImpl implements ReviewProductService {
    private final ReviewRepository reviewRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public ReviewProductServiceImpl(ReviewRepository reviewRepository, OrdersRepository ordersRepository) {
        this.reviewRepository = reviewRepository;
        this.ordersRepository = ordersRepository;
    }


    @Override
    public void addReviewForProduct(List<ReviewsEntity> reviewsEntityList, String orderId) {
        Timestamp timeCreated = Timestamp.valueOf(LocalDateTime.now());
        for (ReviewsEntity reviewEntity: reviewsEntityList) {
            reviewEntity.setCreated(timeCreated);
            reviewRepository.save(reviewEntity);
        }
        ordersRepository.updateReviewFlg(true, orderId);
    }
}
