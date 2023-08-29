package org.example.repository;

import java.util.Optional;
import org.example.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query(value = "SELECT s_c.id AS id, s_c.user_id, s_c.is_deleted FROM shopping_carts AS s_c JOIN cart_items AS c_i ON s_c.id=c_i.shopping_cart_id WHERE s_c.user_id=:id", nativeQuery = true)
    Optional<ShoppingCart> findShoppingCartByUserId(@Param("id") Long userId);
}
