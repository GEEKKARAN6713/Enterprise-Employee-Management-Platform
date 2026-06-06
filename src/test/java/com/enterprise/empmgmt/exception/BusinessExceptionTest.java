package com.enterprise.empmgmt.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void messageIsAvailable() {
        BusinessException ex = new BusinessException("something went wrong");
        assertEquals("something went wrong", ex.getMessage());
    }
}
