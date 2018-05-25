package com.meresti.bookstore.bookservice;

import com.meresti.bookstore.bookservice.reactive.annotatio.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "com.meresti.bookstore.mongodb", name = "initialize", havingValue = "true")
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(final String... args) throws Exception {
        bookRepository.deleteAll().doOnSuccess(result -> bookRepository.saveAll(createSampleBooks()).subscribe(null, null, () -> log.info("Sample data saved to MongoDB.")));
    }

    private List<Book> createSampleBooks() {
        final Book designPatterns = Book.builder()
                .title("Design Patterns: Elements of Reusable Object-Oriented Software")
                .authors(Arrays.asList("John Vlissides", "Richard Helm", "Ralph Johnson", "Erich Gamma"))
                .isbn("9780201633610")
                .language("English")
                .publisher("Addison-Wesley Professional")
                .publicationDate(LocalDate.of(1994, Month.OCTOBER, 31))
                .build();

        final Book cleanCode = Book.builder()
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .authors(Collections.singletonList("Robert C. Martin"))
                .isbn("9780132350884")
                .language("English")
                .publisher("Prentice Hall")
                .publicationDate(LocalDate.of(2008, Month.AUGUST, 1))
                .build();

        final Book theCleanCoder = Book.builder()
                .title("The Clean Coder: A Code of Conduct for Professional Programmers")
                .authors(Collections.singletonList("Robert C. Martin"))
                .isbn("9780137081073")
                .language("English")
                .publisher("Prentice Hall")
                .publicationDate(LocalDate.of(2011, Month.MAY, 13))
                .build();

        final Book effectiveJava = Book.builder()
                .title("Effective Java - 2nd Edition")
                .authors(Collections.singletonList("Joshua J. Bloch"))
                .isbn("9780321356680")
                .language("English")
                .publisher("Addison-Wesley Professional")
                .publicationDate(LocalDate.of(2008, Month.MAY, 8))
                .build();

        return Arrays.asList(designPatterns, cleanCode, theCleanCoder, effectiveJava);
    }

}
