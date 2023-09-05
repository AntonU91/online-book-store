package org.example.dto;

import java.math.BigDecimal;
import lombok.Data;
import org.example.model.Book;
import org.example.model.Order;

@Data
public class OrderItemDto {
    private Order order;
    private Book book;
    private Integer quantity;
    private BigDecimal price;
}
