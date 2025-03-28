package org.repin.model;

import java.util.UUID;

public interface AppUser {
    UUID getId();
    String getEmail();
    String getPassword();
    String getRole();
}