package com.softwaremagico.tm.advisor.ui.character.cybernetics;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CyberneticsViewModel extends ViewModel {

    public List<Cyberdevice> getAvailableCyberdevices(CharacterPlayer characterPlayer) {
        try {
            return characterPlayer.getCyberdevices().stream()
                    .sorted(Comparator.comparing(Cyberdevice::getNameRepresentation))
                    .collect(Collectors.toList());
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
