package ua.edu.lnu.pilavkaorest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.graphql.data.method.annotation.Argument;

@Data
@AllArgsConstructor
public class BookInput {
    private String title;
    private String author;
    private int year;
    private PublisherInput publisher;
}
