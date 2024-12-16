package tn.firas.userservice.dto;

import org.springframework.stereotype.Service;
import tn.firas.userservice.dto.request.AddressRequest;
import tn.firas.userservice.entities.Address;


@Service
public class AddressMapper {

        public Address toAddress(AddressRequest request) {
            return Address.builder()
                    .street(request.street())
                    .zipCode(request.zipCode())
                    .houseNumber(request.houseNumber())
                    .build();
        }


}
