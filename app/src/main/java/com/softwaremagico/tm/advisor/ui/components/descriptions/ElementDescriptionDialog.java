package com.softwaremagico.tm.advisor.ui.components.descriptions;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;

import java.util.stream.Collectors;

public class ElementDescriptionDialog<T extends Element> extends DialogFragment {
    protected static final String TABLE_STYLE = "border:1px solid;border-color:"+ R.color.md_theme_onPrimaryContainer +";margin-left:auto;margin-right:auto;font-size:70%";
    protected static final int TABLE_PADDING = 2;
    private final T element;

    public ElementDescriptionDialog(T element) {
        super();
        this.element = element;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();

        Rect size = window.getWindowManager().getCurrentWindowMetrics().getBounds();

        int width = size.width();

        window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.description_window, container, false);
        WebView content = root.findViewById(R.id.content);
        Button closeButton = root.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());
        updateContent(content, element);
        return root;
    }

    private void updateContent(WebView content, T element) {
        content.loadData(setContent(element), "text/html", "UTF-8");
    }

    private String getHeader(T element) {
        return "<h2>" + adaptText(element.getNameRepresentation()) + "</h2>";
    }

    private String getBody(T element) {
        return "<p>" + adaptText(element.getDescription().getTranslatedText()) + "</p>";
    }

    private String getRestrictions(T element) {
        StringBuilder restrictions = new StringBuilder();
        if (element.getRestrictions().getRestrictedToSpecies() != null && !element.getRestrictions().getRestrictedToSpecies().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_species)).append("</b> ")
                    .append(SpecieFactory.getInstance().getElements(element.getRestrictions().getRestrictedToSpecies())
                            .stream().map(Specie::getNameRepresentation)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictions().getRestrictedToFactions() != null && !element.getRestrictions().getRestrictedToFactions().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_factions)).append("</b> ")
                    .append(FactionFactory.getInstance().getElements(element.getRestrictions().getRestrictedToFactions())
                            .stream().map(Faction::getNameRepresentation)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictions().getRestrictedToCallings() != null && !element.getRestrictions().getRestrictedToCallings().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_callings)).append("</b> ")
                    .append(CallingFactory.getInstance().getElements(element.getRestrictions().getRestrictedToCallings())
                            .stream().map(Calling::getNameRepresentation)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictions().getRestrictedToCapabilities() != null && !element.getRestrictions().getRestrictedToCapabilities().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_capabilities)).append("</b> ")
                    .append(CapabilityFactory.getInstance().getElements(element.getRestrictions().getRestrictedToCapabilities())
                            .stream().map(Capability::getNameRepresentation)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictions().getRestrictedPerks() != null && !element.getRestrictions().getRestrictedPerks().isEmpty()) {
            restrictions.append("<p><b>").append(getString(R.string.restricted_perks)).append("</b> ")
                    .append(PerkFactory.getInstance().getElements(element.getRestrictions().getRestrictedPerks())
                            .stream().map(Perk::getNameRepresentation)
                            .collect(Collectors.joining(", "))).append("</p>");
        }
        if (element.getRestrictions().isRestricted()) {
            restrictions.append("<p><b><font color=\"").append(getColor(R.color.restricted)).append("\">").append(getString(R.string.restricted))
                    .append("</font></b></p>");
        }
        return restrictions.toString();
    }

    private String setContent(T element) {
        return "<html><body style='text-align:justify;font-size:14px;"
                + "color:" + getColor(R.color.md_theme_onPrimaryContainer) + ";"
                + "background-color:" + getColor(R.color.md_theme_background) + "'>" +
                getHeader(element) +
                getBody(element) +
                getDetails(element) +
                getRestrictions(element) +
                "</body></html>";
    }

    protected String getDetails(T element) {
        return "";
    }

    protected String getColor(int color) {
        int labelColor = ContextCompat.getColor(getContext(), color);
        //Removing Alpha value
        return String.format("%X", labelColor).substring(2);
    }

    protected String adaptText(String text) {
        return text.replaceAll("\\\\n", "<br><br>").trim().replaceAll(" +", " ");
    }
}
