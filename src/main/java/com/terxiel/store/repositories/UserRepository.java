package com.terxiel.store.repositories;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @EntityGraph(attributePaths = {"tags","addresses"})
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses")
    List<User> findAllWithAddresses();

    @EntityGraph(attributePaths = "profile")
    @Query("SELECT u.id, u.email FROM User u WHERE u.profile.loyaltyPoints > :loyaltyPoints ORDER BY u.email")
    List<UserSummary> findUsersByLoyaltyPoints(@Param("loyaltyPoints") int loyaltyPoints);

    @EntityGraph(attributePaths = "profile")
    @Query("SELECT u FROM User u")
    List<User> findAllUsers(Sort sort);

    boolean existsByEmail(String email);
}
