package com.softwaremagico.tm.advisor.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public final class BookContentUiRefresher {
    private BookContentUiRefresher() {
    }

    public static void refresh(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        for (Fragment fragment : fragmentManager.getFragments()) {
            refresh(fragment);
        }
    }

    private static void refresh(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (fragment instanceof BookContentRefreshable) {
            ((BookContentRefreshable) fragment).refreshBookContent();
        }
        refresh(fragment.getChildFragmentManager());
    }
}

