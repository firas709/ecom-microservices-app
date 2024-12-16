package tn.firas.userservice.dto.request;

public record ChangePasswordResetRequest(
       String newPassword,
       String confirmationPassword
) {
}
