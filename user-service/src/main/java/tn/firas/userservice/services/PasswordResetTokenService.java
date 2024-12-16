package tn.firas.userservice.services;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import tn.firas.userservice.dto.request.ChangePasswordResetRequest;
import tn.firas.userservice.dto.response.Response;

public interface PasswordResetTokenService {
    ResponseEntity<Response> verifyEmail(String email) throws MessagingException;

    ResponseEntity<Response> verifyOtp(String otp, String email) throws MessagingException;

    ResponseEntity<Response> changePasswordHandler(
            ChangePasswordResetRequest changePasswordResetRequest,
            String email
    );

}
