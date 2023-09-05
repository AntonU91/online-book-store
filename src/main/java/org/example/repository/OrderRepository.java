package org.example.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.example.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    List<Order> findAllByUserId(Long userId);

    @Query("UPDATE Order  o SET o.status=:status WHERE o.id=:id")
    @Modifying
    @Transactional
    void updateStatusById(@Param("id") Long id, @Param("status") Order.Status status);

    Optional<Order> findByUserIdAndId(Long userId, Long id);
}
