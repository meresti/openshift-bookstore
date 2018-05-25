package com.meresti.bookstore.bookservice;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Book {

    @Id
    private BigInteger id;

    @NotNull
    private String title;

    @NotNull
    @Size(min = 1)
    private List<String> authors;

    @NotNull
    @Size(max = 13)
    private String isbn;

    private String publisher;

    private LocalDate publicationDate;

    @Size(max = 20)
    private String language;

}
