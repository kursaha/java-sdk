package com.kursaha.mailkeets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KursahaClientTest {
    @Test
    public void testAddMaxInteger() {
        Assertions.assertEquals(2147483646, Integer.sum(2147183646, 300000));
    }
}
