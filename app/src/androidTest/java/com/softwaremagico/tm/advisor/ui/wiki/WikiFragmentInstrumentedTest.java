package com.softwaremagico.tm.advisor.ui.wiki;

import android.widget.SpinnerAdapter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.fragment.app.testing.FragmentScenario;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.ElementSpinner;
import com.softwaremagico.tm.advisor.ui.components.spinner.SearchableSpinner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WikiFragmentInstrumentedTest {

    @Test
    public void wikiTabShouldOpenAndRenderContent() {
        try (FragmentScenario<WikiFragment> scenario = FragmentScenario.launchInContainer(WikiFragment.class, null,
                R.style.Theme_ThinkMachine4EAdvisor)) {
            scenario.onFragment(fragment -> {
                assertNotNull(fragment.getView());
                assertNotNull(fragment.requireView().findViewById(R.id.wiki_specie));
                assertNotNull(fragment.requireView().findViewById(R.id.wiki_faction));
                assertNotNull(fragment.requireView().findViewById(R.id.wiki_occultism_power));
            });
        }
    }

    @Test
    public void wikiSelectorsShouldContainItems() {
        try (FragmentScenario<WikiFragment> scenario = FragmentScenario.launchInContainer(WikiFragment.class, null,
                R.style.Theme_ThinkMachine4EAdvisor)) {
            scenario.onFragment(fragment -> {
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_specie));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_upbringing));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_faction));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_calling));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_planet));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_melee_weapon));
                assertSpinnerHasItems(fragment.requireView().findViewById(R.id.wiki_occultism_power));
            });
        }
    }

    private void assertSpinnerHasItems(ElementSpinner<?> elementSpinner) {
        assertNotNull(elementSpinner);

        final SearchableSpinner<?> searchableSpinner = elementSpinner.findViewById(R.id.spinner);
        assertNotNull(searchableSpinner);

        final SpinnerAdapter adapter = searchableSpinner.getAdapter();
        assertNotNull(adapter);
        assertTrue(adapter.getCount() > 0);
    }
}




