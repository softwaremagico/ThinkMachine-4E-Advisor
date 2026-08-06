package com.softwaremagico.tm.advisor.ui.components.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.ElementComponent;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ArmorDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.CallingDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.CyberdeviceDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ElementDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.FactionDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.HandheldShieldDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.MeleeWeaponDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.OccultismPathDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.OccultismPowerDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.PerkDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.RangeWeaponDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.ShieldDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.SpecieDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.components.descriptions.UpbringingDescriptionDialog;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.upbringing.Upbringing;

public abstract class HelpElement<E extends Element> extends ElementComponent<E> {
    private ImageView helpButton;

    public HelpElement(Context context) {
        this(context, null);
    }

    public HelpElement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initComponents(AttributeSet attrs) {
        final TextView tagText = findViewById(R.id.translated_tag);
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.TranslatedEditText, 0, 0);
        final String tag = attributes.getString(R.styleable.TranslatedEditText_translation);
        if (tag != null) {
            tagText.setText(resolveTagText(tag) + " ");
        }
        tagText.setTextAppearance(R.style.CharacterInfo);
        attributes.recycle();

        helpButton = findViewById(R.id.button_help);
        if (helpButton != null) {
            helpButton.setOnClickListener(v -> openDescriptionWindow(getSelection()));
        }
    }

    protected ImageView getHelpButton() {
        return helpButton;
    }

    private String resolveTagText(String tag) {
        try {
            return ThinkMachineTranslator.getTranslatedText(tag);
        } catch (Exception ignored) {
            // Fallback for wiki labels backed by Android strings instead of TextFactory keys.
            final int stringId = getContext().getResources().getIdentifier(tag, "string", getContext().getPackageName());
            if (stringId != 0) {
                return getContext().getString(stringId);
            }
            return tag;
        }
    }

    public abstract E getSelection();

    protected void openDescriptionWindow(E element) {
        if (element == null) return;
        final androidx.fragment.app.FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();

        if (element instanceof Shield) {
            new ShieldDescriptionDialog((Shield) element).show(fm, "");
        } else if (element instanceof Armor) {
            new ArmorDescriptionDialog((Armor) element).show(fm, "");
        } else if (element instanceof HandheldShield) {
            new HandheldShieldDescriptionDialog((HandheldShield) element).show(fm, "");
        } else if (element instanceof Weapon) {
            if (((Weapon) element).isRangedWeapon()) {
                new RangeWeaponDescriptionDialog((Weapon) element).show(fm, "");
            } else {
                new MeleeWeaponDescriptionDialog((Weapon) element).show(fm, "");
            }
        } else if (element instanceof Cyberdevice) {
            new CyberdeviceDescriptionDialog((Cyberdevice) element).show(fm, "");
        } else if (element instanceof OccultismPower) {
            new OccultismPowerDescriptionDialog((OccultismPower) element).show(fm, "");
        } else if (element instanceof OccultismPath) {
            new OccultismPathDescriptionDialog((OccultismPath) element).show(fm, "");
        } else if (element instanceof Specie) {
            new SpecieDescriptionDialog((Specie) element).show(fm, "");
        } else if (element instanceof Upbringing) {
            new UpbringingDescriptionDialog((Upbringing) element).show(fm, "");
        } else if (element instanceof Faction) {
            new FactionDescriptionDialog((Faction) element).show(fm, "");
        } else if (element instanceof Calling) {
            new CallingDescriptionDialog((Calling) element).show(fm, "");
        } else if (element instanceof Perk) {
            new PerkDescriptionDialog((Perk) element).show(fm, "");
        } else {
            new ElementDescriptionDialog<>(element).show(fm, "");
        }
    }
}
