package com.courseproject.eshop.repository;

import com.courseproject.eshop.entity.CartEntity;
import com.courseproject.eshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Аида Есанян
 **/
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserId(Long id);

    Optional<CartEntity> findByUser(UserEntity user);
}
