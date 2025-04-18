package com.orestpilavka.lab3.service;

import com.orestpilavka.lab3.model.Parcel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
public class ParcelService {

    private final Sinks.Many<Parcel> sink;

    public ParcelService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Parcel createParcel(String trackingNumber, String recipient, String status, String userId) {
        Parcel parcel = new Parcel(UUID.randomUUID().toString(), trackingNumber, recipient, status, userId);
        if ("ARRIVED".equals(status)) {
            sink.tryEmitNext(parcel);
        }
        return parcel;
    }

    public Flux<Parcel> getParcelArrivalsForUser(String userId) {
        return sink.asFlux()
                .filter(parcel -> parcel.getUserId().equals(userId));
    }
}
