package ua.edu.lnu.pilavkaorest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    private String name;
    private String country;
    private PublishingDetails publishingDetails;
}
