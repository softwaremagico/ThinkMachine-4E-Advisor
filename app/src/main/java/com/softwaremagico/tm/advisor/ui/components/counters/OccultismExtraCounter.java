package com.softwaremagico.tm.advisor.ui.components.counters;

import android.content.Context;
import android.util.AttributeSet;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class OccultismExtraCounter extends SegmentCounter {
    private final static int GEAR_COLOR = R.color.counterOccultism;
    private final static int TEXT_COLOR = R.color.counterOccultismText;

    private CharacterManager.PerkUpdatedListener listener;

    public OccultismExtraCounter(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setTag(R.string.counter_points);
    }

    public OccultismExtraCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCharacter(CharacterPlayer character) {
        CharacterManager.removePerkUpdatedListeners(listener);
        listener = CharacterManager.addPerkUpdatedListeners(characterPlayer -> {
            final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
            if (selectedCharacter != null) {
                setValue(selectedCharacter.getOccultismPointsSpent(),
                        selectedCharacter.getOccultismPointsAvailable(), true);
            }
        });
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter != null) {
            setValue(selectedCharacter.getOccultismPointsSpent(),
                    selectedCharacter.getOccultismPointsAvailable(), true);
        }
    }

    public int getGearColor() {
        return GEAR_COLOR;
    }

    public int getTextColor() {
        return TEXT_COLOR;
    }
}
