package org.example.service;

import java.util.List;
import org.example.dto.OrderItemResponseDto;
import org.example.dto.OrderResponseDto;
import org.example.dto.ShippingAddressDto;
import org.example.dto.StatusDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    void createOrder(Authentication authentication, ShippingAddressDto shippingAddressDto);

    List<OrderResponseDto> getAllOrders(Authentication authentication, Pageable pageable);

    void changeStatus(Authentication authentication, StatusDto statusDto, Long id);

    List<OrderItemResponseDto> getOrderItems(Authentication authentication, Long orderId,
            Pageable pageable);

    OrderItemResponseDto getSpecificOrderItem(Authentication authentication, Long orderId,
            Long itemId);
}
