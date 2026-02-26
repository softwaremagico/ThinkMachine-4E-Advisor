package com.softwaremagico.tm.advisor.core.random;

public enum PreferenceGroup {
    CHARACTER_DESCRIPTION(PreferenceOption.AFFILIATION, PreferenceOption.AGE, PreferenceOption.ALIGNMENT,
            PreferenceOption.LEGAL_STATUS, PreferenceOption.ORIGIN),
    CHARACTER_CREATION(PreferenceOption.OPERATIONAL_ROLE, PreferenceOption.RANK_VALUE_ASSIGNATION),

    EQUIPMENT(PreferenceOption.ATTACK, PreferenceOption.DEFENSE, PreferenceOption.TECH, PreferenceOption.WEALTH),

    CYBERNETICS(),

    PSI(PreferenceOption.OCCULTISM);

    private final PreferenceOption[] options;


    PreferenceGroup(PreferenceOption... options) {
        this.options = options;
    }

    public PreferenceOption[] getOptions() {
        return options;
    }
}
