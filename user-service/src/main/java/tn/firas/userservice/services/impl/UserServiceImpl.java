package tn.firas.userservice.services.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.firas.userservice.dto.AddressMapper;
import tn.firas.userservice.dto.request.ChangePasswordRequest;
import tn.firas.userservice.entities.User;
import tn.firas.userservice.repositories.RoleRepository;
import tn.firas.userservice.repositories.UserRepository;
import tn.firas.userservice.services.UserService;
import tn.firas.userservice.util.TokenService;


import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    private final AddressMapper addressMapper;

    
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // save the new password
        userRepository.save(user);
    }


}
