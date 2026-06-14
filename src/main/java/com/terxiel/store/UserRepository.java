package com.terxiel.store;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}
