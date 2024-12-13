package com.softwaremagico.tm.advisor.ui.character.cybernetics;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CyberneticsViewModel extends ViewModel {

    public List<Cyberdevice> getAvailableCyberdevices(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return CyberdeviceFactory.getInstance().getElements().
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return CyberdeviceFactory.getInstance().getElements().
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
