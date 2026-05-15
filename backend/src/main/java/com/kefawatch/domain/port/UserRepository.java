package com.kefawatch.domain.port;

import com.kefawatch.domain.model.UserAccount;

import java.util.Optional;

public interface UserRepository {

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findById(long id);

    long insert(String username, String passwordHash);
}
