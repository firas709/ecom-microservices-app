package tn.firas.notificationservice.kafka;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {

}
