package org.example.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderItemResponseDto;
import org.example.dto.OrderResponseDto;
import org.example.dto.ShippingAddressDto;
import org.example.dto.StatusDto;
import org.example.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public void placeOrder(Authentication authentication,
            @RequestBody @Valid ShippingAddressDto shippingAddressDto) {
        orderService.createOrder(authentication, shippingAddressDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderResponseDto> getAll(Authentication authentication, Pageable pageable) {
        return orderService.getAllOrders(authentication, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(Authentication authentication, @RequestBody @Valid StatusDto statusDto,
            @PathVariable Long id) {
        orderService.changeStatus(authentication, statusDto, id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsOfSpecificOrder(Authentication authentication,
            @PathVariable Long orderId, Pageable pageable) {
        return orderService.getOrderItems(authentication, orderId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getSpecificOrderItem(Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getSpecificOrderItem(authentication, orderId, itemId);
    }
}
