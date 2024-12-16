package tn.firas.userservice.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.mongodb.repository.MongoRepository;
import tn.firas.userservice.entities.Role;

import java.util.Optional;

@Hidden

public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(String role);
}
