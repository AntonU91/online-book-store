package org.example.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.config.MapperConfig;
import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.mapper.impl.OrderItemMapperImpl;
import org.example.model.Order;
import org.example.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapperImpl.class)
public interface OrderMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "orderItems", source = "cartItems")
    OrderRequestDto toDto(ShoppingCart shoppingCart);

    Order toEntity(OrderRequestDto orderRequestDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "total", source = "total")
    OrderResponseDto toResponseDto(Order order);

    @AfterMapping
    default void enrichOrderDto(@MappingTarget OrderRequestDto dto) {
        dto.setOrderDate(LocalDateTime.now());
        dto.setStatus(Order.Status.PINNED);
        setTotalSumOfOrder(dto);
    }

    private void setTotalSumOfOrder(@MappingTarget OrderRequestDto dto) {
        BigDecimal totalSum = dto.getOrderItems().stream()
                                      .map(orderItemDto -> orderItemDto.getPrice().multiply(
                                              BigDecimal.valueOf(orderItemDto.getQuantity())))
                                      .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotal(totalSum);
    }
}
