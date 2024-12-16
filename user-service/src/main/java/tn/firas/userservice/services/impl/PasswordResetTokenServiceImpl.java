package tn.firas.userservice.services.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.firas.userservice.dto.request.ChangePasswordResetRequest;
import tn.firas.userservice.dto.response.Response;
import tn.firas.userservice.email.EmailService;
import tn.firas.userservice.email.EmailTemplateName;
import tn.firas.userservice.entities.ForgetPasswordToken;
import tn.firas.userservice.entities.User;
import tn.firas.userservice.repositories.ForgetPasswordTokenRepository;
import tn.firas.userservice.repositories.UserRepository;
import tn.firas.userservice.services.PasswordResetTokenService;
import tn.firas.userservice.util.TokenService;


import java.time.Instant;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final UserRepository userRepository;
    private final ForgetPasswordTokenRepository forgetPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    public static final String INVALID_EMAIL_MESSAGE = "Please provide a valid email";

    //send mail for email verification
    public ResponseEntity<Response> verifyEmail(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_EMAIL_MESSAGE));

        Optional<ForgetPasswordToken> fp =forgetPasswordRepository.findByUser(user.getId());

        fp.ifPresent(forgetPasswordToken -> forgetPasswordRepository.deleteById(forgetPasswordToken.getId()));

        //time to formulate the mail body
        String generatedToken = tokenService.generateActivationCode();
        var token = ForgetPasswordToken.builder()
                .token(generatedToken)
                .expirationTime(new Date(System.currentTimeMillis() + 24 * 60 * 1000))
                .user(user)
                .build();

        forgetPasswordRepository.save(token);

        //Send Mail
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.RESET_PASSWORD,
                activationUrl,
                generatedToken,
                "OTP for Forgot Password request"
        );

        return ResponseEntity.ok(Response.builder()
                        .message("\"Password reset OTP sent! You'll receive an email if you are registered on our system.\"")
                .build());

    }
    @Transactional
    public ResponseEntity<Response> verifyOtp(String token, String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_EMAIL_MESSAGE));

        ForgetPasswordToken fp =forgetPasswordRepository.findByTokenAndUser(token,user.getId())
                .orElseThrow(()-> new RuntimeException("Invalid OTP for email "+email ));

        //Check if the expiration time of OTP is not expired
        if (fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgetPasswordRepository.deleteById(fp.getId());
            verifyEmail(email);
            throw new RuntimeException("Forget Password token has expired. A new token has been send to the same email address");
        }
        fp.setVerified(true);

        forgetPasswordRepository.save(fp);
        return ResponseEntity.ok(Response.builder()
                .message("OTP verified ")
                .build());

    }


    //Now User Can change the password

    public ResponseEntity<Response> changePasswordHandler(
            ChangePasswordResetRequest changePassword,
            String email
    ){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_EMAIL_MESSAGE));

        ForgetPasswordToken fp =forgetPasswordRepository.findByUser(user.getId())
                .orElseThrow(()-> new RuntimeException("Invalid OTP for email "+email ));
        boolean areEqual = (changePassword.newPassword()).equals(changePassword.confirmationPassword());
        if (!areEqual){
            return new ResponseEntity<>(Response.builder()
                    .message("Please enter the password again!")
                    .build(),HttpStatus.EXPECTATION_FAILED);
        }
        if (fp.isVerified()){
            //We need to encode password
            String encodedPassword = passwordEncoder.encode(changePassword.newPassword());

            tokenService.updatePassword(email,encodedPassword);
//            userRepository.updatePassword(email,encodedPassword);

            forgetPasswordRepository.deleteById(fp.getId());
            return ResponseEntity.ok(Response.builder()
                    .message("Password has been succesfully changed!")
                    .build());

        }

        return ResponseEntity.ok(Response.builder()
                .message("Otp User Not Verified!")
                .build());

    }



}
