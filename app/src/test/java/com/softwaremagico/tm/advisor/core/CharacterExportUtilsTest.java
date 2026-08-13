package com.softwaremagico.tm.advisor.core;

import static org.junit.Assert.assertEquals;

import com.softwaremagico.tm.character.CharacterPlayer;

import org.junit.Test;

public class CharacterExportUtilsTest {

    @Test
    public void getSafeCharacterName_whenCharacterIsNull_returnsEmptyString() {
        assertEquals("", CharacterExportUtils.getSafeCharacterName(null));
    }

    @Test
    public void getSafeCharacterName_replacesInvalidFileCharacters() {
        final CharacterPlayer character = new CharacterPlayer() {
            @Override
            public String getCompleteNameRepresentation() {
                return "Juan / Test: 01";
            }
        };

        assertEquals("Juan _ Test_ 01", CharacterExportUtils.getSafeCharacterName(character));
    }

    @Test
    public void buildExportFileName_whenNameIsBlank_usesFallback() {
        final CharacterPlayer character = new CharacterPlayer();

        assertEquals("export_sheet_qr.png", CharacterExportUtils.buildExportFileName(character, "_sheet_qr.png"));
    }
}
