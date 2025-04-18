package com.orestpilavka.lab3.controller;

import com.orestpilavka.lab3.model.Parcel;
import com.orestpilavka.lab3.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ParcelMutationResolver {

    private final ParcelService parcelService;

    @QueryMapping
    public String getParcel() {
        return "Test!";
    }

    @MutationMapping
    public Parcel createParcel(@Argument String trackingNumber,@Argument String recipient,@Argument String status, @Argument String userId) {
        return parcelService.createParcel(trackingNumber, recipient, status, userId);
    }
}
