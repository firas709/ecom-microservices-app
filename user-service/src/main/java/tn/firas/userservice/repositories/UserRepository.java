package tn.firas.userservice.repositories;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.firas.userservice.entities.User;

import java.util.Optional;
@Hidden
public interface UserRepository extends MongoRepository<User, String > {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);


}
