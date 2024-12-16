package tn.firas.userservice.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.firas.userservice.entities.ForgetPasswordToken;
import tn.firas.userservice.entities.User;

import java.util.Optional;

@Hidden

public interface ForgetPasswordTokenRepository extends MongoRepository<ForgetPasswordToken,String> {
    @Query("{ 'user.id' : ?0 }")
    Optional<ForgetPasswordToken> findByUser(String userId);

    @Query("{ 'token': ?0, 'user.id': ?1 }")
    Optional<ForgetPasswordToken> findByTokenAndUser(String token, String userId);

}
