package org.example.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.example.model.Order;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private Integer total;
    private Order.Status status;
}
