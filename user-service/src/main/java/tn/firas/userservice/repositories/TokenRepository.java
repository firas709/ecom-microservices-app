package tn.firas.userservice.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.firas.userservice.entities.Token;

import java.util.Optional;
@Hidden

public interface TokenRepository extends MongoRepository<Token,String> {
    Optional<Token> findByToken(String token);
}
