package com.meresti.bookstore.bookservice.traditional;

import com.meresti.bookstore.bookservice.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("traditional/books")
public class BookTraditionalController {

    private final BookTraditionalRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.ok().body(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(@PathVariable BigInteger id) {
        final Optional<Book> book = bookRepository.findById(id);
        return book
                .map(b -> ResponseEntity.ok().body(b))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveOne(@Valid @RequestBody Book book) {
        final Book savedBook = bookRepository.save(book);

        final URI location = UriComponentsBuilder
                .fromPath("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateOne(@PathVariable BigInteger id, @Valid @RequestBody Book book) {
        final Optional<Book> bookToUpdate = bookRepository.findById(id);
        return bookToUpdate.
                map(b -> {
                    book.setId(id);
                    final Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok().body(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable BigInteger id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
