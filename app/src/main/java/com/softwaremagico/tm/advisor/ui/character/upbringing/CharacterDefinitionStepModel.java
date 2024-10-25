package com.softwaremagico.tm.advisor.ui.character.upbringing;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;

public class CharacterDefinitionStepModel extends ViewModel {

    public CharacterPlayer getCharacterPlayer(){
        return CharacterManager.getSelectedCharacter();
    }
}
