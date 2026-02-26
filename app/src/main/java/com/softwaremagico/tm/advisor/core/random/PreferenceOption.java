package com.softwaremagico.tm.advisor.core.random;

import com.softwaremagico.tm.random.preferences.AffiliationPreference;
import com.softwaremagico.tm.random.preferences.AgePreference;
import com.softwaremagico.tm.random.preferences.AlignmentPreference;
import com.softwaremagico.tm.random.preferences.AttackPreferences;
import com.softwaremagico.tm.random.preferences.DefensePreference;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.LegalStatusPreference;
import com.softwaremagico.tm.random.preferences.OccultismPreference;
import com.softwaremagico.tm.random.preferences.OperationalRolePreference;
import com.softwaremagico.tm.random.preferences.OriginPreference;
import com.softwaremagico.tm.random.preferences.RankValueAssignationPreference;
import com.softwaremagico.tm.random.preferences.TechPreference;
import com.softwaremagico.tm.random.preferences.WealthPreference;

public enum PreferenceOption {

    AFFILIATION(null, AffiliationPreference.values()),
    AGE(AgePreference.ADULT, AgePreference.values()),
    ALIGNMENT(AlignmentPreference.NEUTRAL, AlignmentPreference.values()),
    ATTACK(null, AttackPreferences.values()),
    DEFENSE(null, DefensePreference.values()),
    LEGAL_STATUS(LegalStatusPreference.LEGAL, LegalStatusPreference.values()),
    OCCULTISM(null, OccultismPreference.values()),
    OPERATIONAL_ROLE(null, OperationalRolePreference.values()),
    ORIGIN(null, OriginPreference.values()),
    RANK_VALUE_ASSIGNATION(null, RankValueAssignationPreference.values()),
    TECH(null, TechPreference.values()),
    WEALTH(WealthPreference.MIDDLE_CLASS, WealthPreference.values());

    private final IRandomPreference[] randomPreferences;

    private final IRandomPreference defaultOption;

    PreferenceOption(IRandomPreference defaultOption, IRandomPreference... randomPreferences) {
        this.randomPreferences = randomPreferences;
        this.defaultOption = defaultOption;
    }

    public IRandomPreference[] getRandomPreferences() {
        return randomPreferences;
    }

    public IRandomPreference getDefaultOption() {
        return defaultOption;
    }

    public static PreferenceOption get(IRandomPreference randomPreference) {
        for (PreferenceOption option : PreferenceOption.values()) {
            for (IRandomPreference preference : option.getRandomPreferences()) {
                if (preference == randomPreference) {
                    return option;
                }
            }
        }
        return null;
    }
}