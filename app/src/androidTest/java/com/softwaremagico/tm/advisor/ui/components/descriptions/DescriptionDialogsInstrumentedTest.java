package com.softwaremagico.tm.advisor.ui.components.descriptions;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DescriptionDialogsInstrumentedTest {
    private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

    @Before
    public void setUp() {
        ApplicationProvider.getApplicationContext();
        CharacterManager.getSelectedCharacter();
    }

    @Test
    public void callingDescriptionDialogShouldCreate() {
        final Calling calling = firstElement(CallingFactory.getInstance().getElements());
        assertDialogShows(new CallingDescriptionDialog(calling));
    }

    @Test
    public void upbringingDescriptionDialogShouldCreate() {
        final Upbringing upbringing = firstElement(UpbringingFactory.getInstance().getElements());
        assertDialogShows(new UpbringingDescriptionDialog(upbringing));
    }

    @Test
    public void factionDescriptionDialogShouldCreate() {
        final Faction faction = firstElement(FactionFactory.getInstance().getElements());
        assertDialogShows(new FactionDescriptionDialog(faction));
    }

    @Test
    public void specieDescriptionDialogShouldCreate() {
        final Specie specie = firstElement(SpecieFactory.getInstance().getElements());
        assertDialogShows(new SpecieDescriptionDialog(specie));
    }

    @Test
    public void perkDescriptionDialogShouldCreate() {
        final Perk perk = firstElement(PerkFactory.getInstance().getElements());
        assertDialogShows(new PerkDescriptionDialog(perk));
    }

    @Test
    public void occultismPathDescriptionDialogShouldCreate() {
        final OccultismPath path = firstElement(OccultismPathFactory.getInstance().getElements());
        assertDialogShows(new OccultismPathDescriptionDialog(path));
    }

    @Test
    public void handheldShieldDescriptionDialogShouldCreate() {
        final HandheldShield shield = firstElement(HandheldShieldFactory.getInstance().getElements());
        assertDialogShows(new HandheldShieldDescriptionDialog(shield));
    }

    @Test
    public void cyberdeviceDescriptionDialogShouldCreate() {
        final Cyberdevice cyberdevice = firstElement(CyberdeviceFactory.getInstance().getElements());
        assertDialogShows(new CyberdeviceDescriptionDialog(cyberdevice));
    }

    @Test
    public void armorDescriptionDialogShouldCreate() {
        final Armor armor = firstElement(ArmorFactory.getInstance().getElements());
        assertDialogShows(new ArmorDescriptionDialog(armor));
    }

    @Test
    public void shieldDescriptionDialogShouldCreate() {
        final Shield shield = firstElement(ShieldFactory.getInstance().getElements());
        assertDialogShows(new ShieldDescriptionDialog(shield));
    }

    @Test
    public void meleeWeaponDescriptionDialogShouldCreate() {
        final Weapon meleeWeapon = firstElement(WeaponFactory.getInstance().getElements(), Weapon::isMeleeWeapon, "melee weapon");
        assertDialogShows(new MeleeWeaponDescriptionDialog(meleeWeapon));
    }

    @Test
    public void rangeWeaponDescriptionDialogShouldCreate() {
        final Weapon rangedWeapon = firstElement(WeaponFactory.getInstance().getElements(),
                weapon -> weapon.isRangedWeapon()
                        && weapon.getAgora() == null
                        && (weapon.getAgoraGroups() == null || weapon.getAgoraGroups().isEmpty()),
                "ranged weapon without agora data");
        assertDialogShows(new RangeWeaponDescriptionDialog(rangedWeapon));
    }

    @Test
    public void occultismPowerDescriptionDialogShouldCreate() {
        final OccultismPath pathWithPowers = firstElement(OccultismPathFactory.getInstance().getElements(),
                path -> path.getOccultismPowers() != null && !path.getOccultismPowers().isEmpty(),
                "occultism path with powers");
        final OccultismPower power = firstElement(pathWithPowers.getOccultismPowers().values().stream().toList());
        assertDialogShows(new OccultismPowerDescriptionDialog(power));
    }

    private void assertDialogShows(DialogFragment dialogFragment) {
        try (FragmentScenario<TestDialogHostFragment> scenario = FragmentScenario.launchInContainer(
                TestDialogHostFragment.class, null, R.style.Theme_ThinkMachine4EAdvisor)) {
            scenario.onFragment(host -> dialogFragment.show(host.getParentFragmentManager(), dialogFragment.getClass().getSimpleName()));

            instrumentation.waitForIdleSync();
            SystemClock.sleep(300);

            scenario.onFragment(host -> {
                final DialogFragment shownDialog = (DialogFragment) host.getParentFragmentManager()
                        .findFragmentByTag(dialogFragment.getClass().getSimpleName());
                assertNotNull(shownDialog);
                assertNotNull(shownDialog.getDialog());
                assertTrue(shownDialog.getDialog().isShowing());

                final View dialogView = shownDialog.getDialog().findViewById(R.id.content);
                assertNotNull(dialogView);
                assertTrue(dialogView instanceof WebView);

                final WebView webView = (WebView) dialogView;
                final String html = Objects.toString(webView.getUrl(), "") + Objects.toString(webView.getTitle(), "");
                assertNotNull(webView);
                assertNotNull(html);
                shownDialog.dismissAllowingStateLoss();
            });
        }
    }

    private <T> T firstElement(List<T> elements) {
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        final T element = elements.get(0);
        assertNotNull(element);
        return element;
    }

    private <T> T firstElement(List<T> elements, Predicate<T> predicate, String label) {
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        for (T element : elements) {
            if (element != null && predicate.test(element)) {
                return element;
            }
        }
        throw new AssertionError("No " + label + " found in test data");
    }
}


