package tn.firas.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.firas.userservice.dto.request.ChangePasswordRequest;
import tn.firas.userservice.services.UserService;


import java.security.Principal;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "User Services (Change Password )")
public class UserController {
    private final UserService userService;

    @PatchMapping("/updatePassword")
    @Operation(
            description = "updatePassword endpoint",
            summary = "This is to update password endpoint"
    )
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
