package com.credo.database.security;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String WRITER = "ROLE_WRITER";

    public static final String READER = "ROLE_READER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static String hasAuthorityOr(String... roles) {
        return Arrays.stream(roles).map(role -> "hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")").collect(Collectors.joining(" or "));
    }

    private AuthoritiesConstants() {}
}
