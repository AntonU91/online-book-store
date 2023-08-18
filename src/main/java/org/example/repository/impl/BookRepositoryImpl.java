package org.example.repository.impl;

import java.util.List;
import java.util.Optional;
import org.example.exception.DataProcessingException;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save a book : " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book b", Book.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find all books : ", e);
        }
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> query = session.createQuery("FROM Book b WHERE b.id=:id", Book.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            throw new DataProcessingException("Can't find  books with id: " + id, e);
        }

    }

}
