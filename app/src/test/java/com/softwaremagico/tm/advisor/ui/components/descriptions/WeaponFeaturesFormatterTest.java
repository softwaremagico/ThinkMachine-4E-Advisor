package com.softwaremagico.tm.advisor.ui.components.descriptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WeaponFeaturesFormatterTest {

    @Test
    public void formatShouldSplitCamelCaseFeatures() {
        assertEquals("Slam, Hard", WeaponFeaturesFormatter.format("SlamHard"));
    }

    @Test
    public void formatShouldRemoveLeadingSeparatorsBeforeSplit() {
        assertEquals("Slam, Hard", WeaponFeaturesFormatter.format(", SlamHard"));
    }

    @Test
    public void formatShouldKeepAlreadySeparatedFeatures() {
        assertEquals("Slam, Hard", WeaponFeaturesFormatter.format("Slam, Hard"));
    }
}

