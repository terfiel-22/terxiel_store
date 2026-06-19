package com.terxiel.store.repositories;

import com.terxiel.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {

    @EntityGraph(attributePaths = {"tags","addresses"})
    Optional<User> findByEmail(String email);
}
