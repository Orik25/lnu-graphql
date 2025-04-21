package ua.edu.lnu.pilavkaorest.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ua.edu.lnu.pilavkaorest.model.Book;
import ua.edu.lnu.pilavkaorest.model.BookInput;
import ua.edu.lnu.pilavkaorest.model.Publisher;
import ua.edu.lnu.pilavkaorest.model.PublishingDetails;
import ua.edu.lnu.pilavkaorest.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookGraphQLController {

    private static final String DATA_PATH = "/data/books.json";

    private final List<Book> books;

    public BookGraphQLController() {
       this.books = loadDataFromStorage();
    }

    @QueryMapping
    public List<Book> allBooks() {
        return books;
    }

    @QueryMapping
    public Book bookByTitle(@Argument("title") String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public List<Book> booksAfterYear(@Argument("year") int year) {
        return books.stream()
                .filter(book -> book.getYear() > year)
                .toList();
    }

    @QueryMapping
    public String authorName() {
        return "Orest";
    }

    @MutationMapping
    public Book addBook(@Argument("input") BookInput input) {
        var publishingDetailsInput = input.getPublisher().getPublishingDetails();
        var publishingDetails = new PublishingDetails(
                publishingDetailsInput.getFoundedYear(),
                publishingDetailsInput.getHeadquarters()
        );

        var publisherInput = input.getPublisher();
        var publisher = new Publisher(
                publisherInput.getName(),
                publisherInput.getCountry(),
                publishingDetails
        );

        var book = new Book(input.getTitle(), input.getAuthor(), input.getYear(), publisher);
        books.add(book);
        return book;
    }

    private List<Book> loadDataFromStorage() {
        return new ArrayList<>(JsonUtils.loadListFromJsonFile(DATA_PATH, Book[].class));
    }
}
