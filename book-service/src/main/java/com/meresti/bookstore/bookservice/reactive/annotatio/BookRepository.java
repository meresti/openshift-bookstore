package com.meresti.bookstore.bookservice.reactive.annotatio;

import com.meresti.bookstore.bookservice.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, BigInteger> {
}
