package com.pridesys.ticketing.auth;

public record UserRecord(long id, String email, String passwordHash, UserRole role,
                         String name, String mobile, String designation, String office,
                         boolean active) {
}
