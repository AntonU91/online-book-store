# Online Book Store API


## Introduction

The Online Book Store API is a comprehensive solution for book management, category management, order placement, and shopping cart functionality. This API is designed to provide a seamless experience for both book enthusiasts and administrators. Whether you're looking to purchase books, manage your shopping cart, or administer your bookstore, this API has you covered.

## Technologies and Tools Used

The Online Book Store API leverages a variety of technologies and tools, including:

- **Spring Boot**: A powerful framework for building Java-based applications.
- **Spring Security**: Ensures secure user authentication and authorization.
- **Spring Data JPA**: Simplifies database operations through the JPA specification.
- **Swagger**: Provides a user-friendly interface for exploring and testing API endpoints.
- **OpenAPI**: Utilized for documenting the API specifications.

## API Functionalities

### Authentication Manager API

#### User Login

- **Endpoint:** `/api/auth/login` (POST)
- **Description:** Allows users to log in and obtain an authentication token.
- **Request Body:** UserLoginRequestDto
- **Response:** UserLoginResponseDto

#### User Registration

- **Endpoint:** `/api/auth/register` (POST)
- **Description:** Allows users to register for the platform.
- **Request Body:** UserRegistrationRequestDto
- **Response:** UserResponseDto

### Book Manager API

#### Create a New Book

- **Endpoint:** `/api/books` (POST)
- **Description:** Enables the creation of new book entries.
- **Request Body:** CreateBookRequestDto
- **Response:** BookDto

#### Get All Books

- **Endpoint:** `/api/books` (GET)
- **Description:** Retrieves a list of all available books with optional pagination.
- **Parameters:** `page` (optional), `size` (optional)
- **Response:** List of BookDto

#### Get Book by ID

- **Endpoint:** `/api/books/{id}` (GET)
- **Description:** Retrieves details of a specific book by its ID.
- **Parameters:** `id` (path)
- **Response:** BookDto

#### Update a Book by ID

- **Endpoint:** `/api/books/{id}` (PUT)
- **Description:** Allows updating the details of a specific book.
- **Parameters:** `id` (path)
- **Request Body:** CreateBookRequestDto

#### Delete a Book by ID

- **Endpoint:** `/api/books/{id}` (DELETE)
- **Description:** Deletes a specific book by its ID.
- **Parameters:** `id` (path)

#### Get Books by Category ID

- **Endpoint:** `/api/categories/{id}/books` (GET)
- **Description:** Retrieves a list of books within a specific category by its ID.
- **Parameters:** `id` (path)
- **Response:** List of BookDtoWithoutCategoryIds

### Category Manager API

#### Create a New Category

- **Endpoint:** `/api/categories` (POST)
- **Description:** Allows the creation of new book categories.
- **Request Body:** CategoryDto
- **Response:** CategoryDto

#### Get All Categories

- **Endpoint:** `/api/categories` (GET)
- **Description:** Retrieves a list of all available book categories with optional pagination.
- **Parameters:** `page` (optional), `size` (optional)
- **Response:** List of CategoryDto

#### Get Category by ID

- **Endpoint:** `/api/categories/{id}` (GET)
- **Description:** Retrieves details of a specific book category by its ID.
- **Parameters:** `id` (path)
- **Response:** CategoryDto

#### Update a Category

- **Endpoint:** `/api/categories/{id}` (POST)
- **Description:** Allows updating the details of a specific book category.
- **Parameters:** `id` (path)
- **Request Body:** CategoryDto

#### Delete a Category by ID

- **Endpoint:** `/api/categories/{id}` (DELETE)
- **Description:** Deletes a specific book category by its ID.
- **Parameters:** `id` (path)

#### Get Books by Category ID

- **Endpoint:** `/api/categories/{id}/books` (GET)
- **Description:** Retrieves a list of books within a specific category by its ID.
- **Parameters:** `id` (path)
- **Response:** List of BookDtoWithoutCategoryIds

### Order Manager API

#### Place a New Order

- **Endpoint:** `/api/orders` (POST)
- **Description:** Allows users to place a new order.
- **Request Body:** ShippingAddressDto
- **Response:** OrderResponseDto
- **Security:** Bearer Authentication (JWT)

#### Get All Orders

- **Endpoint:** `/api/orders` (GET)
- **Description:** Retrieves a list of all orders.
- **Response:** List of OrderResponseDto
- **Security:** Bearer Authentication (JWT)

#### Change the Status of an Order

- **Endpoint:** `/api/orders/{id}` (PATCH)
- **Description:** Allows changing the status of a specific order.
- **Parameters:** `id` (path)
- **Request Body:** StatusDto
- **Security:** Bearer Authentication (JWT)

#### Get Order Items for a Specific Order

- **Endpoint:** `/api/orders/{orderId}/items` (GET)
- **Description:** Retrieves a list of order items for a specific order.
- **Parameters:** `orderId` (path)
- **Response:** List of OrderItemResponseDto
- **Security:** Bearer Authentication (JWT)

#### Get a Specific Order Item

- **Endpoint:** `/api/orders/{orderId}/items/{itemId}` (GET)
- **Description:** Retrieves details of a specific order item.
- **Parameters:** `orderId` (path), `itemId` (path)
- **Response:** OrderItemResponseDto
- **Security:** Bearer Authentication (JWT)

### Shopping Cart API

#### Add a Book to the Shopping Cart

- **Endpoint:** `/cart` (POST)
- **Description:** Allows adding a book to the shopping cart.
- **Request Body:** CartItemRequestDto
- **Security:** Bearer Authentication (JWT)

#### Get All Items in the Shopping Cart

- **Endpoint:** `/cart` (GET)
- **Description:** Retrieves a list of all items in the shopping cart.
- **Response:** ShoppingCartDto
- **Security:** Bearer Authentication (JWT)

#### Update the Quantity of a Cart Item

- **Endpoint:** `/cart/cart-items/{cartItemId}` (PUT)
- **Description:** Allows updating the quantity of a cart item.
- **Parameters:** `cartItemId` (path)
- **Request Body:** CartItemQuantityRequestDto
- **Security:** Bearer Authentication (JWT)

#### Delete a Cart Item by ID

- **Endpoint:** `/cart/cart-items/{cartItemId}` (DELETE)
- **Description:** Deletes a specific cart item by its ID.
- **Parameters:** `cartItemId` (path)
- **Security:** Bearer Authentication (JWT)

## Setup and Usage

To set up and use the Online Book Store API, follow these steps:

1. Clone the repository to your local machine.
2. Configure the database settings in the application properties.
3. Build and run the application using your preferred Java IDE or build tool.
4. Access the API documentation at `http://localhost:8080/swagger-ui.html` to learn about available endpoints and request/response structures.

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. To access protected endpoints, include the generated JWT token in the `Authorization` header of your requests.

## Conclusion

The Online Book Store API is a robust solution for building and managing an online bookstore. It offers a wide range of functionalities for both users and administrators, making it an excellent choice for any book-related business. Whether you're selling books or just looking for your next read, this API has you covered.
