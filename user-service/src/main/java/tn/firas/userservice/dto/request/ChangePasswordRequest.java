package tn.firas.userservice.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {}
