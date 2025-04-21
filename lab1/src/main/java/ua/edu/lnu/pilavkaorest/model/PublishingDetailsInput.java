package ua.edu.lnu.pilavkaorest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublishingDetailsInput {
    private int foundedYear;
    private String headquarters;
}
