package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.example.model.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    private static final long CART_ITEM_ID = 1L;
    private static final long SHOPPING_CART_ID = 1L;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Get cart item by id and shopping cart id")
    @Sql(scripts = {
            "classpath:db/cartItem-repository-tests/"
            + "before/insert-books-to-books-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "before/insert-user-to-user-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "before/insert-role_user-to-role_user-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "before/insert-shoppingCart-to-shoppingCarts-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "before/insert-cart_item-to-cart_items-table.sql"
                    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/cartItem-repository-tests/"
            + "after/delete-cart_items-from-cart_items-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "after/delete-shopping_carts-from-shopping_carts-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "after/delete-roles_users-from-user_role-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "after/delete-users-from-users-table.sql",
            "classpath:db/cartItem-repository-tests/"
            + "after/delete-books-from-books-table.sql"
                     }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCartItem_ValidCartIdAndShoppingCart_Success() {
        CartItem cartItem =
                cartItemRepository.findByIdAndShoppingCartId(CART_ITEM_ID, SHOPPING_CART_ID)
                        .get();
        assertNotNull(cartItem);
    }
}
