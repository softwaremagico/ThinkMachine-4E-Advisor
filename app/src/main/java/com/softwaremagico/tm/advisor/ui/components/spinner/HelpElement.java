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
    private boolean contextualStylesEnabled = true;

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

    public void setContextualStylesEnabled(boolean contextualStylesEnabled) {
        this.contextualStylesEnabled = contextualStylesEnabled;
    }

    protected void openDescriptionWindow(E element) {
        if (element == null) return;
        final androidx.fragment.app.FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
        createDescriptionDialog(element).setContextualStylesEnabled(contextualStylesEnabled).show(fm, "");
    }

    protected ElementDescriptionDialog<?> createDescriptionDialog(E element) {
        if (element instanceof Shield) {
            return new ShieldDescriptionDialog((Shield) element);
        } else if (element instanceof Armor) {
            return new ArmorDescriptionDialog((Armor) element);
        } else if (element instanceof HandheldShield) {
            return new HandheldShieldDescriptionDialog((HandheldShield) element);
        } else if (element instanceof Weapon) {
            if (((Weapon) element).isRangedWeapon()) {
                return new RangeWeaponDescriptionDialog((Weapon) element);
            } else {
                return new MeleeWeaponDescriptionDialog((Weapon) element);
            }
        } else if (element instanceof Cyberdevice) {
            return new CyberdeviceDescriptionDialog((Cyberdevice) element);
        } else if (element instanceof OccultismPower) {
            return new OccultismPowerDescriptionDialog((OccultismPower) element);
        } else if (element instanceof OccultismPath) {
            return new OccultismPathDescriptionDialog((OccultismPath) element);
        } else if (element instanceof Specie) {
            return new SpecieDescriptionDialog((Specie) element);
        } else if (element instanceof Upbringing) {
            return new UpbringingDescriptionDialog((Upbringing) element);
        } else if (element instanceof Faction) {
            return new FactionDescriptionDialog((Faction) element);
        } else if (element instanceof Calling) {
            return new CallingDescriptionDialog((Calling) element);
        } else if (element instanceof Perk) {
            return new PerkDescriptionDialog((Perk) element);
        }
        return new ElementDescriptionDialog<>(element);
    }
}
