package com.terxiel.store.modules.order.services;

import com.terxiel.store.entities.Order;
import com.terxiel.store.entities.OrderStatus;
import com.terxiel.store.entities.User;
import com.terxiel.store.mappers.OrderMapper;
import com.terxiel.store.modules.auth.exceptions.AuthenticationNotFoundException;
import com.terxiel.store.modules.auth.services.AuthService;
import com.terxiel.store.modules.order.dtos.OrderDTO;
import com.terxiel.store.modules.order.exceptions.OrderNotFoundException;
import com.terxiel.store.modules.user.exceptions.UserNotFoundException;
import com.terxiel.store.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private User dummyUser1;
    private User dummyUser2;
    private Order dummyOrder;
    private OrderDTO.Order dummyOrderDto;

    @BeforeEach
    void setUp() {
        // Create dummy users
        dummyUser1 = User.builder()
                .id(1L)
                .name("Test User 1")
                .build();
        dummyUser2 = User.builder()
                .id(2L)
                .name("Test User 2")
                .build();

        // Create dummy Order
        dummyOrder = Order.builder()
                .id(1L)
                .customer(dummyUser1)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDate.now())
                .build();

        // Create dummy OrderDTO
        OrderDTO.OrderProduct dummyOrderProductDto = new OrderDTO.OrderProduct(
                1L,"Test Product", BigDecimal.valueOf(20000)
        );
        OrderDTO.OrderItem dummyOrderItemDto = new OrderDTO.OrderItem(
                dummyOrderProductDto,3,BigDecimal.valueOf(60000)
        );
        dummyOrderDto = new OrderDTO.Order(
                dummyOrder.getId(),
                dummyOrder.getStatus(),
                dummyOrder.getCreatedAt().atStartOfDay(),
                List.of(dummyOrderItemDto),
                dummyOrder.getTotalPrice()
        );
    }


    @Test
    @DisplayName("Should throw an AuthenticationNotFoundException error.")
    void shouldThrowAuthenticationNotFoundException()
    {
        // Arrange
        when(authService.getCurrentUser()).thenThrow(AuthenticationNotFoundException.class);

        // Act & Assert
        assertThrows(
                AuthenticationNotFoundException.class,
                () -> authService.getCurrentUser()
        );

        // Verify code stopped early and didn't getOrdersByCustomer or mapping
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }

    @Test
    @DisplayName("Should throw an UserNotFoundException error.")
    void shouldThrowUserNotFoundException()
    {
        // Arrange
        when(authService.getCurrentUser()).thenThrow(UserNotFoundException.class);

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> authService.getCurrentUser()
        );

        // Verify code stopped early and didn't getOrdersByCustomer or mapping
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }

    @Nested
    @DisplayName("Get orders test class")
    class GetOrdersTest {
        @Test
        @DisplayName("Should fetch orders of the authenticated current user.")
        void shouldFetchOrdersSuccessfully()
        {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(dummyUser1);
            when(orderRepository.getOrdersByCustomer(dummyUser1)).thenReturn(List.of(dummyOrder));
            when(orderMapper.toDto(dummyOrder)).thenReturn(dummyOrderDto);

            // Act
            List<OrderDTO.Order> result = orderService.getOrders();

            // Assert
            assertEquals(1, result.size());
            assertEquals(dummyOrderDto, result.getFirst());

            // Verify interactions
            verify(authService, times(1)).getCurrentUser();
            verify(orderRepository, times(1)).getOrdersByCustomer(dummyUser1);
            verify(orderMapper, times(1)).toDto(dummyOrder);
        }

        @Test
        @DisplayName("Should only return orders of authenticated user")
        void shouldOnlyReturnOwnedOrders()
        {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(dummyUser2);
            when(orderRepository.getOrdersByCustomer(dummyUser2)).thenReturn(List.of());

            // Act
            List<OrderDTO.Order> result = orderService.getOrders();

            // Assert
            assertEquals(0, result.size());

            // Verify interactions
            verify(authService, times(1)).getCurrentUser();
            verify(orderRepository, times(1)).getOrdersByCustomer(dummyUser2);
            verifyNoInteractions(orderMapper);
        }
    }

    @Nested
    @DisplayName("Get Order by ID test class")
    class GetOrderTest {
        @Test
        @DisplayName("Should fetch order of the authenticated current user.")
        void shouldFetchOrderSuccessfully()
        {
            // Arrange
            Long orderId = 1L;
            when(orderRepository.getOrderWithItems(orderId)).thenReturn(Optional.of(dummyOrder));
            when(authService.getCurrentUser()).thenReturn(dummyUser1);
            when(orderMapper.toDto(dummyOrder)).thenReturn(dummyOrderDto);

            // Act
            OrderDTO.Order result = orderService.getOrder(orderId);

            // Assert
            assertEquals(dummyOrderDto, result);

            // Verify interactions
            verify(authService, times(1)).getCurrentUser();
            verify(orderRepository, times(1)).getOrderWithItems(orderId);
            verify(orderMapper, times(1)).toDto(dummyOrder);
        }

        @Test
        @DisplayName("Should throw an OrderNotFoundException for missing order.")
        void shouldThrowOrderNotFoundException()
        {
            // Arrange
            Long orderId = 2L;
            when(orderRepository.getOrderWithItems(orderId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                    OrderNotFoundException.class,
                    () -> orderService.getOrder(orderId)
            );

            // Verify interactions
            verify(orderRepository, times(1)).getOrderWithItems(orderId);
            verifyNoInteractions(authService);
            verifyNoInteractions(orderMapper);
        }

        @Test
        @DisplayName("Should throw an AccessDeniedException for fetching unowned order.")
        void shouldThrowAccessDeniedException()
        {
            // Arrange
            Long orderId = 1L;
            when(orderRepository.getOrderWithItems(orderId)).thenReturn(Optional.of(dummyOrder));
            when(authService.getCurrentUser()).thenReturn(dummyUser2);

            // Act & Assert
            AccessDeniedException exception = assertThrows(
                    AccessDeniedException.class,
                    () -> orderService.getOrder(orderId)
            );

            // Verify interactions
            verify(orderRepository, times(1)).getOrderWithItems(orderId);
            verify(authService, times(1)).getCurrentUser();
            verifyNoInteractions(orderMapper);

            // Assert error message
            assertEquals("You don't have access to this order.",exception.getMessage());
        }
    }
}