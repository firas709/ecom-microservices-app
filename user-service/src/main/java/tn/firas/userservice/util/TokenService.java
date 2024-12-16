package tn.firas.userservice.util;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.firas.userservice.email.EmailService;
import tn.firas.userservice.email.EmailTemplateName;
import tn.firas.userservice.entities.Token;
import tn.firas.userservice.entities.User;
import tn.firas.userservice.repositories.TokenRepository;
import tn.firas.userservice.repositories.UserRepository;


import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    private final MongoTemplate mongoTemplate;

    @Transactional
    public void updatePassword(String email, String password) {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("password", password);
        mongoTemplate.updateFirst(query, update, User.class);
    }


    public String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode();
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    public void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }


    public String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}

