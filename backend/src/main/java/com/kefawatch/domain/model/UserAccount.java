package com.kefawatch.domain.model;

public record UserAccount(long id, String username, String email, String firstName, String lastName, String passwordHash) {
}
