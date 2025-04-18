package com.orestpilavka.lab3.controller;

import com.orestpilavka.lab3.model.Parcel;
import com.orestpilavka.lab3.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class ParcelSubscriptionResolver {

    private final ParcelService parcelService;

    @SubscriptionMapping
    public Flux<Parcel> trackUserParcels(@Argument String userId) {
        return parcelService.getParcelArrivalsForUser(userId);
    }
}
