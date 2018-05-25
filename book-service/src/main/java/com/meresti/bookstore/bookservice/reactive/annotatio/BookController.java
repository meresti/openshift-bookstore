package com.meresti.bookstore.bookservice.reactive.annotatio;

import com.meresti.bookstore.bookservice.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigInteger;
import java.net.URI;

@RestController
@RequestMapping("reactive-annotation/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Flux<Book> getAll() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Book>> getOne(@PathVariable BigInteger id) {
        return bookRepository.findById(id)
                .map(savedBook -> ResponseEntity.ok().body(savedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Book>> saveOne(@Valid @RequestBody Book book) {
        return bookRepository.save(book)
                .map(savedBook -> {
                    final URI location = UriComponentsBuilder
                            .fromPath("/{id}")
                            .buildAndExpand(savedBook.getId()).toUri();
                    return ResponseEntity.created(location).build();
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Book>> updateOne(@PathVariable BigInteger id, @Valid @RequestBody Book book) {
        return bookRepository.findById(id)
                .flatMap(existingBook -> {
                    book.setId(id);
                    return bookRepository.save(book);
                })
                .map(updatedBook -> ResponseEntity.ok().body(updatedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteOne(@PathVariable BigInteger id) {
        return bookRepository.deleteById(id)
                .map(d -> ResponseEntity.ok().build());
    }

}
