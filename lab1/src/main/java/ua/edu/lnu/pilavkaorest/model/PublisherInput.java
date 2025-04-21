package ua.edu.lnu.pilavkaorest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublisherInput {
    private String name;
    private String country;
    private PublishingDetailsInput publishingDetails;
}
