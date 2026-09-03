package com.softwaremagico.tm.qr;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CharacterQrCodecTest {

    @Test
    public void encodeDecode_shouldRoundTripCharacterData() throws Exception {
        CharacterPlayer player = new CharacterPlayer();
        player.getInfo().setPlayer("Alice");
        player.getInfo().setGender(Gender.FEMALE);
        player.getInfo().setAge(30);
        player.getInfo().setPlanet("Valhalla");
        player.getInfo().setHair("Black");
        player.getInfo().setEyes("Blue");
        player.getInfo().setComplexion("Fair");
        player.getInfo().setHeight("180 cm");
        player.getInfo().setWeight("75 kg");

        String payload = CharacterQrCodec.encode(player);
        CharacterPlayer decoded = CharacterQrCodec.decode(payload);

        assertNotNull(decoded);
        assertEquals("Alice", decoded.getInfo().getPlayer());
        assertEquals(Gender.FEMALE, decoded.getInfo().getGender());
        assertEquals(Integer.valueOf(30), decoded.getInfo().getAge());
        assertEquals("Valhalla", decoded.getInfo().getPlanet());
    }

    @Test
    public void pngWriterAndReader_shouldRoundTripCharacterData() throws Exception {
        CharacterPlayer player = new CharacterPlayer();
        player.getInfo().setPlayer("Bob");
        player.getInfo().setGender(Gender.MALE);
        player.getInfo().setAge(27);
        player.getInfo().setPlanet("Nexus");

        Path tempFile = Files.createTempFile("tm-qr-roundtrip", ".png");
        try {
            CharacterQrPngWriter.writePng(player, tempFile);
            CharacterPlayer decoded = CharacterQrPngReader.readPng(tempFile);

            assertNotNull(decoded);
            assertEquals("Bob", decoded.getInfo().getPlayer());
            assertEquals(Gender.MALE, decoded.getInfo().getGender());
            assertEquals(Integer.valueOf(27), decoded.getInfo().getAge());
            assertEquals("Nexus", decoded.getInfo().getPlanet());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void decodeInvalidPayload_shouldThrow() {
        assertThrows(Exception.class, () -> CharacterQrCodec.decode("not-a-valid-qr-payload"));
    }
}
