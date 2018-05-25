package com.meresti.bookstore.bookservice.traditional;

import com.meresti.bookstore.bookservice.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BookTraditionalRepository extends MongoRepository<Book, BigInteger> {
}
