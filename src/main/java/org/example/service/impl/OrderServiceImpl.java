package org.example.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.OrderItemResponseDto;
import org.example.dto.OrderRequestDto;
import org.example.dto.OrderResponseDto;
import org.example.dto.ShippingAddressDto;
import org.example.dto.StatusDto;
import org.example.exception.DataProcessingException;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.OrderItemMapper;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.ShoppingCart;
import org.example.model.User;
import org.example.repository.OrderItemRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ShoppingCartRepository;
import org.example.repository.UserRepository;
import org.example.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public void createOrder(Authentication authentication, ShippingAddressDto shippingAddressDto) {
        String userEmail = authentication.getName();
        User user = getUser(userEmail);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                                            .orElseThrow(() -> new EntityNotFoundException(
                                                    "Can not find shopping cart with id: "
                                                    + user.getId()));
        OrderRequestDto dto = orderMapper.toDto(shoppingCart);
        dto.setShippingAddress(shippingAddressDto.getShippingAddress());
        Order entity = orderMapper.toEntity(dto);
        orderRepository.save(entity);
        Set<OrderItem> orderItems = entity.getOrderItems().stream()
                                            .peek(orderItem -> orderItem.setOrder(entity))
                                            .peek(orderItemRepository::save)
                                            .collect(Collectors.toSet());
        entity.setOrderItems(orderItems);
        orderRepository.save(entity);
        shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = getUser(userEmail);
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orders.stream()
                       .map(orderMapper::toResponseDto)
                       .toList();
    }

    @Override
    public void changeStatus(Authentication authentication, StatusDto statusDto,
            Long id) {
        String userEmail = authentication.getName();
        User user = getUser(userEmail);
        orderRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find order with user specified user id:"
                        + user.getId() + " and order id: "
                        + id));
        try {
            Order.Status newStatus = Order.Status.valueOf(statusDto.getStatus());
            orderRepository.updateStatusById(id, newStatus);
        } catch (IllegalArgumentException e) {
            throw new DataProcessingException("Invalid status name:" + statusDto.getStatus(), e);
        }

    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Authentication authentication, Long orderId) {
        String userEmail = authentication.getName();
        User user = getUser(userEmail);
        Order order = orderRepository.findByUserIdAndId(user.getId(), orderId)
                              .orElseThrow(() -> new EntityNotFoundException(
                                      "Can not find order with user specified user id:"
                                      + user.getId() + " and order id: "
                                      + orderId));
        return order.getOrderItems().stream()
                       .map(orderItemMapper::toResponseDto)
                       .toList();
    }

    @Override
    public OrderItemResponseDto getSpecificOrderItem(Authentication authentication, Long orderId,
            Long itemId) {
        List<OrderItemResponseDto> orderItems = getOrderItems(authentication, orderId);
        return orderItems.stream()
                       .filter(orderItemResponseDto -> orderItemResponseDto.getId().equals(itemId))
                       .findFirst()
                       .orElseThrow(
                               () -> new EntityNotFoundException(
                                       "Can not find specified order item"));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can not find user by email" + email));
    }
}
