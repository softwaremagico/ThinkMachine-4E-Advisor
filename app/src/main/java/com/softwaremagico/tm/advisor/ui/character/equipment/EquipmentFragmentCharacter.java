/*
 *  Copyright (C) 2020 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.character.equipment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.components.CharacterCustomFragment;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.IncrementalElementsLayout;
import com.softwaremagico.tm.advisor.ui.components.counters.FirebirdsCounter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ArmorAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ElementAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.ShieldAdapter;
import com.softwaremagico.tm.advisor.ui.components.spinner.adapters.WeaponAdapter;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.ThinkMachineTranslator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.exceptions.InvalidArmorException;
import com.softwaremagico.tm.exceptions.InvalidShieldException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EquipmentFragmentCharacter extends CharacterCustomFragment {
    private EquipmentViewModel mViewModel;
    private IncrementalElementsLayout<Weapon> meleeWeaponsLayout;
    private IncrementalElementsLayout<Weapon> rangeWeaponsLayout;
    private IncrementalElementsLayout<Armor> ArmorsLayout;
    private IncrementalElementsLayout<Shield> shieldsLayout;
    private FirebirdsCounter firebirdsCounter;
    private View root;


    public static EquipmentFragmentCharacter newInstance(int index) {
        final EquipmentFragmentCharacter fragment = new EquipmentFragmentCharacter();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void populateElements(View root, CharacterPlayer character) {
        if (getContext() != null) {
            if (meleeWeaponsLayout != null) {
                meleeWeaponsLayout.setElements(CharacterManager.getSelectedCharacter().getPurchasedRangedWeapons());
            }
            if (rangeWeaponsLayout != null) {
                rangeWeaponsLayout.setElements(CharacterManager.getSelectedCharacter().getPurchasedMeleeWeapons());
            }
            if (ArmorsLayout != null) {
                ArmorsLayout.setElement(CharacterManager.getSelectedCharacter().getPurchasedArmor());
            }
            if (shieldsLayout != null) {
                shieldsLayout.setElement(CharacterManager.getSelectedCharacter().getPurchasedShield());
            }
            firebirdsCounter.setCharacter(character);
        }
    }

    @Override
    protected void initData() {
        final LinearLayout rootLayout = root.findViewById(R.id.root_container);

        addSection(ThinkMachineTranslator.getTranslatedText("rangedWeapons"), rootLayout);

        rangeWeaponsLayout = new RangedWeaponsLayout(getContext(), true);
        rootLayout.addView(rangeWeaponsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("meleeWeapons"), rootLayout);

        meleeWeaponsLayout = new MeleeWeaponsLayout(getContext(), true);
        rootLayout.addView(meleeWeaponsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("armor"), rootLayout);
        ArmorsLayout = new ArmorLayout(getContext(), true);
        rootLayout.addView(ArmorsLayout);
        addSpace(rootLayout);

        addSection(ThinkMachineTranslator.getTranslatedText("shield"), rootLayout);
        shieldsLayout = new ShieldLayout(getContext(), true);
        rootLayout.addView(shieldsLayout);

        populateElements(root, CharacterManager.getSelectedCharacter());
    }

    @Override
    protected void updateSettings(CharacterPlayer characterPlayer) {
        if (getContext() != null) {
            rangeWeaponsLayout.updateElementAdapter(!characterPlayer.getSettings().isOnlyOfficialAllowed());
            meleeWeaponsLayout.updateElementAdapter(!characterPlayer.getSettings().isOnlyOfficialAllowed());
            ArmorsLayout.updateElementAdapter(!characterPlayer.getSettings().isOnlyOfficialAllowed());
            shieldsLayout.updateElementAdapter(!characterPlayer.getSettings().isOnlyOfficialAllowed());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.character_equipment_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
        firebirdsCounter = root.findViewById(R.id.firebirds_counter);

        CharacterManager.addSelectedCharacterListener(characterPlayer -> populateElements(root, characterPlayer));

        return root;
    }


    class ShieldLayout extends IncrementalElementsLayout<Shield> {
        private static final int MAX_ITEMS = 1;

        public ShieldLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed, MAX_ITEMS);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setShield(getElementSpinners());
                    checkTechLevel(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setShield(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<Shield> createElementAdapter(boolean nonOfficial) {
            return new ShieldAdapter(getActivity(), mViewModel.getAvailableShields(nonOfficial), isNullAllowed());
        }

        private void setShield(List<ElementSpinner<Shield>> spinners) {
            final List<Shield> shields = new ArrayList<>();
            for (final ElementSpinner<Shield> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    shields.add(spinner.getSelection());
                }
            }
            if (shields.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setPurchasedShield(null);
                } catch (InvalidShieldException | UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setPurchasedShield(shields.get(0));
                } catch (InvalidShieldException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_invalid_shield_armor_combination).show();
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_unofficial_element_not_allowed).show();
                }
            }
        }
    }

    private <T extends Equipment> void checkTechLevel(List<ElementSpinner<T>> spinners) {
        //Check if has the capability
    }


    class ArmorLayout extends IncrementalElementsLayout<Armor> {
        private static final int MAX_ITEMS = 1;

        public ArmorLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed, MAX_ITEMS);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setArmor(getElementSpinners());
                    checkTechLevel(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    setArmor(getElementSpinners());
                    checkTechLevel(getElementSpinners());
                }
            });
        }

        @Override
        protected ElementAdapter<Armor> createElementAdapter(boolean nonOfficial) {
            return new ArmorAdapter(getActivity(), mViewModel.getAvailableArmors(nonOfficial), isNullAllowed());
        }

        private void setArmor(List<ElementSpinner<Armor>> spinners) {
            final List<Armor> Armors = new ArrayList<>();
            for (final ElementSpinner<Armor> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    Armors.add(spinner.getSelection());
                }
            }
            if (Armors.isEmpty()) {
                try {
                    CharacterManager.getSelectedCharacter().setPurchasedArmor(null);
                } catch (InvalidArmorException | UnofficialElementNotAllowedException e) {
                    AdvisorLog.errorMessage(this.getClass().getName(), e);
                }
            } else {
                try {
                    CharacterManager.getSelectedCharacter().setPurchasedArmor(Armors.get(0));
                } catch (InvalidArmorException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_invalid_shield_armor_combination).show();
                } catch (UnofficialElementNotAllowedException e) {
                    SnackbarGenerator.getErrorMessage(this, R.string.message_unofficial_element_not_allowed).show();
                }
            }
        }
    }

    abstract class WeaponsLayout extends IncrementalElementsLayout<Weapon> {

        protected WeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);

            setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (removeDuplicates()) {
                        SnackbarGenerator.getInfoMessage(root, R.string.message_duplicated_item_removed).show();
                    }
                    setWeapons(getElementSpinners());
                    checkTechLevel(getElementSpinners());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    removeDuplicates();
                    setWeapons(getElementSpinners());
                }
            });
        }

        @Override
        protected abstract ElementAdapter<Weapon> createElementAdapter(boolean nonOfficial);

        private void setWeapons(List<ElementSpinner<Weapon>> spinners) {
            final List<Weapon> weapons = new ArrayList<>();
            for (final ElementSpinner<Weapon> spinner : spinners) {
                if (spinner.getSelection() != null) {
                    weapons.add(spinner.getSelection());
                }
            }
            assignWeapons(weapons);
        }

        protected abstract void assignWeapons(List<Weapon> weapons);
    }

    class MeleeWeaponsLayout extends WeaponsLayout {
        public MeleeWeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);
        }

        @Override
        protected ElementAdapter<Weapon> createElementAdapter(boolean nonOfficial) {
            return new WeaponAdapter(getActivity(), mViewModel.getAvailableMeleeWeapons(nonOfficial), isNullAllowed());
        }

        @Override
        protected void assignWeapons(List<Weapon> weapons) {
            try {
                CharacterManager.getSelectedCharacter().setPurchasedMeleeWeapons(weapons.stream()
                        .filter(weapon -> !Objects.equals(weapon.getId(), null)).collect(Collectors.toList()));
            } catch (UnofficialElementNotAllowedException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_unofficial_element_not_allowed).show();
            }
        }
    }

    class RangedWeaponsLayout extends WeaponsLayout {
        public RangedWeaponsLayout(Context context, boolean nullsAllowed) {
            super(context, nullsAllowed);
        }

        @Override
        protected ElementAdapter<Weapon> createElementAdapter(boolean nonOfficial) {
            return new WeaponAdapter(getActivity(), mViewModel.getAvailableRangedWeapons(nonOfficial), isNullAllowed());
        }

        @Override
        protected void assignWeapons(List<Weapon> weapons) {
            try {
                CharacterManager.getSelectedCharacter().setPurchasedRangedWeapons(weapons.stream()
                        .filter(weapon -> !Objects.equals(weapon.getId(), null)).collect(Collectors.toList()));
            } catch (UnofficialElementNotAllowedException e) {
                SnackbarGenerator.getErrorMessage(this, R.string.message_unofficial_element_not_allowed).show();
            }
        }
    }

}