package org.example.repository;

import java.util.Optional;
import org.example.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart s_c LEFT JOIN FETCH s_c.cartItems WHERE s_c.user.id =:userId")
    Optional<ShoppingCart> findShoppingCartByUserId(@Param("userId") Long userId);
}
