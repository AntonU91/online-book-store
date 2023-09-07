package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.example.model.Order;
import org.example.model.User;

@Data
public class OrderRequestDto {
    private User user;
    private Order.Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
