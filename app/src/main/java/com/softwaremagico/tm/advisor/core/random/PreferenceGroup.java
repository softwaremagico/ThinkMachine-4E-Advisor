package com.softwaremagico.tm.advisor.core.random;

public enum PreferenceGroup {
    CHARACTER_DESCRIPTION(PreferenceOption.AFFILIATION,
            PreferenceOption.AGE, PreferenceOption.ALIGNMENT, PreferenceOption.LEGAL_STATUS,
            PreferenceOption.ORIGIN),
    CHARACTER_CREATION(PreferenceOption.OPERATIONAL_ROLE, PreferenceOption.POWER_LEVEL,
            PreferenceOption.RANK_VALUE_ASSIGNATION),

    THE_OCCULT(PreferenceOption.OCCULTISM),

    EQUIPMENT(PreferenceOption.ATTACK, PreferenceOption.DEFENSE, PreferenceOption.TECH, PreferenceOption.WEALTH);

    private final PreferenceOption[] options;


    PreferenceGroup(PreferenceOption... options) {
        this.options = options;
    }

    public PreferenceOption[] getOptions() {
        return options;
    }
}
