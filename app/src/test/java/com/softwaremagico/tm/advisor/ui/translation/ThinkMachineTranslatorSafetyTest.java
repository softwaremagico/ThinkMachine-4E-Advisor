package com.softwaremagico.tm.advisor.ui.translation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThinkMachineTranslatorSafetyTest {

    @Test
    public void getTranslatedText_missingTag_shouldReturnTagInsteadOfCrashing() {
        assertEquals("missing_tag_123", ThinkMachineTranslator.getTranslatedText("missing_tag_123"));
    }

    @Test
    public void getDescriptionText_missingTag_shouldReturnEmptyString() {
        assertEquals("", ThinkMachineTranslator.getDescriptionText("missing_tag_123"));
    }
}
