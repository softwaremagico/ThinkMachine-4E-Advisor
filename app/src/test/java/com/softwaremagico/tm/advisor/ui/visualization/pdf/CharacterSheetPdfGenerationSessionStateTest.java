package com.softwaremagico.tm.advisor.ui.visualization.pdf;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterSheetPdfGenerationSessionStateTest {

    @Test
    public void generate_currentSelectedCharacterSession_returnsNonEmptyPdf() throws Exception {
        CharacterManager.getCharacters().clear();
        CharacterManager.addNewCharacter();

        final CharacterSheet characterSheet = new CharacterSheet(CharacterManager.getSelectedCharacter());
        final byte[] pdfContent = characterSheet.generate();

        assertNotNull("PDF content should not be null", pdfContent);
        assertTrue("PDF content should not be empty", pdfContent.length > 0);
    }
}

