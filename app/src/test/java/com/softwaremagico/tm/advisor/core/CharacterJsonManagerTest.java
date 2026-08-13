package com.softwaremagico.tm.advisor.core;

import com.softwaremagico.tm.exceptions.InvalidJsonException;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CharacterJsonManagerTest {

    @Test
    public void fromJson_nullContent_throwsInvalidJsonException() {
        try {
            CharacterJsonManager.fromJson(null);
            fail("Expected InvalidJsonException");
        } catch (InvalidJsonException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void fromJson_blankContent_throwsInvalidJsonException() {
        try {
            CharacterJsonManager.fromJson("   ");
            fail("Expected InvalidJsonException");
        } catch (InvalidJsonException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void fromJson_nullLiteral_throwsInvalidJsonException() {
        try {
            CharacterJsonManager.fromJson("null");
            fail("Expected InvalidJsonException");
        } catch (InvalidJsonException e) {
            assertNotNull(e.getMessage());
        }
    }

}
