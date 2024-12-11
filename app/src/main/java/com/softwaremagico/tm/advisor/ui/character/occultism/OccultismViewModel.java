package com.softwaremagico.tm.advisor.ui.character.occultism;

import androidx.lifecycle.ViewModel;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OccultismViewModel extends ViewModel {

    public List<OccultismPath> getAvailableOccultismPath(boolean nonOfficial) {
        try {
            if (nonOfficial) {
                return OccultismPathFactory.getInstance().getElements().
                        stream().filter(Objects::nonNull).collect(Collectors.toList());
            } else {
                return OccultismPathFactory.getInstance().getElements().
                        stream().filter(Objects::nonNull).filter(Element::isOfficial).collect(Collectors.toList());
            }
        } catch (InvalidXmlElementException | NullPointerException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return new ArrayList<>();
    }
}
