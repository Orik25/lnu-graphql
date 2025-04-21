package ua.edu.lnu.pilavkaorest.client;

import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;
import ua.edu.lnu.pilavkaorest.model.Book;

import java.util.List;

public class ApiClient {

    private static final String GRAPHQL_URL = "http://localhost:8080/graphql";

    public static void main(String[] args) {
        List<Book> allBooks = getAllBooks();
        System.out.println(allBooks);

        Book bookByTitle = findByTitle("The Catcher in the Rye");
        System.out.println(bookByTitle);

        List<Book> booksAfterYear = booksAfterYear(1952);
        System.out.println(booksAfterYear);
        System.out.println();
    }

    private static List<Book> getAllBooks() {
        BookQueryBuilder queryBuilder = new BookQueryBuilder();

        String query = queryBuilder
                .title()
                .author()
                .year()
                .publisher()
                    .name()
                    .country()
                    .publishingDetails()
                        .foundedYear()
                        .headquarters()
                    .endPublishingDetails()
                    .endPublisher()
                .build();

        return makeCall(query, "allBooks", List.class);
    }

    private static List<Book> booksAfterYear(int year) {
        String query = new BooksAfterYearQueryBuilder(year)
                .title()
                .author()
                .year()
                .build();

        return makeCall(query, "booksAfterYear", List.class);
    }

    private static Book findByTitle(String title) {
        BookByTitleQueryBuilder queryBuilder = new BookByTitleQueryBuilder(title);

        String query = queryBuilder
                .title()
                .author()
                .year()
                .publisher()
                    .name()
                    .country()
                    .publishingDetails()
                        .foundedYear()
                        .headquarters()
                    .endPublishingDetails()
                    .endPublisher()
                .build();

        return makeCall(query, "bookByTitle", Book.class);
    }


    private static  <T> T makeCall(String query, String fieldName, Class<T> clazz) {
        RestClient restClient = RestClient.create(GRAPHQL_URL);
        HttpSyncGraphQlClient graphQLClient = HttpSyncGraphQlClient.create(restClient);

        ClientGraphQlResponse response = graphQLClient.document(query).executeSync();

        return response.field(fieldName).toEntity(clazz);
    }
}
