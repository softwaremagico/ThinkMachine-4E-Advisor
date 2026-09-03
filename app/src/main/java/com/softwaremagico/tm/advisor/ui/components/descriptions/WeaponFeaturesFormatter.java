package com.softwaremagico.tm.advisor.ui.components.descriptions;

import java.util.Arrays;
import java.util.stream.Collectors;

final class WeaponFeaturesFormatter {

    private WeaponFeaturesFormatter() {
        // Utility class.
    }

    static String format(String features) {
        if (features == null) {
            return "";
        }
        final String cleanedFeatures = features.trim().replaceFirst("^[,;\\s]+", "");
        if (cleanedFeatures.isEmpty() || cleanedFeatures.contains(",") || cleanedFeatures.contains(";")) {
            return cleanedFeatures;
        }

        // Some data sources collapse multiple features as CamelCase (e.g. SlamHard).
        if (cleanedFeatures.matches(".*[a-z][A-Z].*")) {
            return Arrays.stream(cleanedFeatures.split("(?<=[a-z])(?=[A-Z])"))
                    .map(String::trim)
                    .filter(token -> !token.isEmpty())
                    .collect(Collectors.joining(", "));
        }
        return cleanedFeatures;
    }
}

