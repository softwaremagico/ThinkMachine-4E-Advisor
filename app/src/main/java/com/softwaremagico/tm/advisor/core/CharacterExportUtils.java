package com.softwaremagico.tm.advisor.core;

import com.softwaremagico.tm.character.CharacterPlayer;

public final class CharacterExportUtils {
    private CharacterExportUtils() {
    }

    public static String getSafeCharacterName(CharacterPlayer character) {
        if (character == null) {
            return "";
        }

        final String name = character.getCompleteNameRepresentation();
        if (name == null) {
            return "";
        }

        final String normalized = name.trim();
        if (normalized.isEmpty()) {
            return "";
        }

        return normalized.replaceAll("[\\\\/:*?\"<>|\\r\\n]+", "_");
    }

    public static String buildExportFileName(CharacterPlayer character, String suffix) {
        final String safeName = getSafeCharacterName(character);
        final String baseName = safeName.isEmpty() ? "export" : safeName;
        return baseName + suffix;
    }
}
