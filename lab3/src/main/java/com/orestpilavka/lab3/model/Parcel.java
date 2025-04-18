package com.orestpilavka.lab3.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parcel {
    private String id;
    private String trackingNumber;
    private String recipient;
    private String status;
    private String userId;
}
