package com.enterprise.empmgmt.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtPropertiesTest {

    @Test
    void gettersAndSetters() {
        JwtProperties props = new JwtProperties();
        props.setSecret("secret");
        props.setAccessTokenExpirationMs(3600000L);
        props.setRefreshTokenExpirationMs(86400000L);

        assertEquals("secret", props.getSecret());
        assertEquals(3600000L, props.getAccessTokenExpirationMs());
        assertEquals(86400000L, props.getRefreshTokenExpirationMs());
    }
}
