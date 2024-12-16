package tn.firas.userservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.firas.userservice.config.security.JwtService;
import tn.firas.userservice.dto.AddressMapper;
import tn.firas.userservice.entities.Role;
import tn.firas.userservice.entities.Token;
import tn.firas.userservice.entities.User;
import tn.firas.userservice.exception.EmailAlreadyExistsException;
import tn.firas.userservice.exception.IncompleteProfileException;
import tn.firas.userservice.repositories.RoleRepository;
import tn.firas.userservice.repositories.TokenRepository;
import tn.firas.userservice.repositories.UserRepository;
import tn.firas.userservice.util.TokenService;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    private final MongoTemplate mongoTemplate;

    private final AddressMapper addressMapper;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public static final String CUSTOMERROLE = "CUSTOMER";


    @Transactional
    public void registerCustomer(RegistrationRequest request) throws MessagingException {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already registered");
        }
        var userRole = roleRepository.findByName(CUSTOMERROLE)
                .orElseThrow(() -> new IllegalStateException("ROLE STAGIAIRE was not initiated"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        tokenService.sendValidationEmail(user);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()+"not found"));
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }



        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var userConnected = ((User) auth.getPrincipal());
        claims.put("fullName", userConnected.getFullName());
        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        var refreshToken = jwtService.generateRefreshToken(userConnected);



        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @PostConstruct
    public void createRoles(){

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").build());
        }
        if (roleRepository.findByName(CUSTOMERROLE).isEmpty()) {
            roleRepository.save(Role.builder().name(CUSTOMERROLE).build());
        }

    }

    @PostConstruct
    public void createSuperAdmin(){

        var userRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        String email = "firas@mail.com";
        if (!userRepository.existsByEmail(email)) {

            var user = User.builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email(email)
                    .password(passwordEncoder.encode("Admin123@"))
                    .accountLocked(false)
                    .enabled(true)
                    .roles(List.of(userRole))
                    .build();
            userRepository.save(user);
        }

    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (savedToken.getValidatedAt() != null) {
            throw new RuntimeException("Account is already activated");
        }
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            tokenService.sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }






}
