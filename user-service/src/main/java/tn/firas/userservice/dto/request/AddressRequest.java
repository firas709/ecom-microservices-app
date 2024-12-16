package tn.firas.userservice.dto.request;

public record AddressRequest(
      String street,
      String houseNumber,
      String zipCode
) { }
