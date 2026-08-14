package com.softwaremagico.tm.qr;

import static org.junit.Assert.assertNotNull;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CharacterQrAppIntegrationTest {

    @Test
    public void writePng_withNewCharacterFromAppState_generatesImage() throws Exception {
        CharacterManager.getCharacters().clear();
        CharacterManager.addNewCharacter();

        final CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull(character);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(character, output);
    }

    @Test
    public void writePng_withRandomizedCharacterFromAppState_generatesImage()
            throws IOException, com.google.zxing.WriterException, InvalidRandomElementSelectedException,
            com.softwaremagico.tm.exceptions.InvalidXmlElementException {
        CharacterManager.getCharacters().clear();
        CharacterManager.addNewCharacter();
        CharacterManager.randomizeCharacter(java.util.Collections.emptySet(), 8);

        final CharacterPlayer character = CharacterManager.getSelectedCharacter();
        assertNotNull(character);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(character, output);
    }
}
