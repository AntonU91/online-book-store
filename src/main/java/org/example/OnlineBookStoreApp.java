package org.example;

import java.math.BigDecimal;
import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.example")
public class OnlineBookStoreApp {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApp.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Shild");
            book.setIsbn("234456");
            book.setPrice(BigDecimal.TEN);
            book.setTitle("Java 8");
            bookService.save(book);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
