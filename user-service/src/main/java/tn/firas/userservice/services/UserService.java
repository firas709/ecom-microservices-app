package tn.firas.userservice.services;



import tn.firas.userservice.dto.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);

}
