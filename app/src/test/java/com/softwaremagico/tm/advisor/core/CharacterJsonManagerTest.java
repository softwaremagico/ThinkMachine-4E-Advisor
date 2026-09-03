package com.softwaremagico.tm.advisor.core;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidJsonException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
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

    @Test
    public void toJson_fromJson_roundTrip_returnsEquivalentCharacter() throws InvalidJsonException {
        final CharacterPlayer original = new CharacterPlayer();
        original.getInfo().setPlayer("RoundTripPlayer");

        final String json = CharacterJsonManager.toJson(original);
        final CharacterPlayer restored = CharacterJsonManager.fromJson(json);

        assertNotNull(restored);
        assertNotSame(original, restored);
        assertEquals("RoundTripPlayer", restored.getInfo().getPlayer());
    }

    @Test
    public void toJson_fromJson_roundTrip_preservesLevel() throws InvalidJsonException {
        final CharacterPlayer original = new CharacterPlayer();

        final String json = CharacterJsonManager.toJson(original);
        final CharacterPlayer restored = CharacterJsonManager.fromJson(json);

        assertEquals(original.getLevel(), restored.getLevel());
    }
}
