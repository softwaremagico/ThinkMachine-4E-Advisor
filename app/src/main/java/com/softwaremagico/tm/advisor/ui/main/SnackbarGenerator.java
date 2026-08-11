package com.softwaremagico.tm.advisor.ui.main;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.log.AdvisorLog;

public final class SnackbarGenerator {

    private SnackbarGenerator() {

    }

    public static Snackbar getInfoMessage(View view, int messageResource) {
        if (view == null) {
            AdvisorLog.warning(SnackbarGenerator.class, "View is null in getInfoMessage");
            return null;
        }
        if (view.getContext() == null) {
            AdvisorLog.warning(SnackbarGenerator.class, "Context is null in getInfoMessage");
            return null;
        }
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorInfo));
        View snackbarView = snackbar.getView();
        if (snackbarView != null) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorInfoContainer));
        }
        return snackbar;
    }

    public static Snackbar getInfoMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getInfoMessage(view, messageResource);
        if (snackbar != null) {
            snackbar.setAction(actionResource, action);
        }
        return snackbar;
    }

    public static Snackbar getInfoMessage(View view, String message) {
        if (view == null || view.getContext() == null) {
            AdvisorLog.warning(SnackbarGenerator.class, "View or context is null in getInfoMessage(String)");
            return null;
        }
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorOnInfo));
        View snackbarView = snackbar.getView();
        if (snackbarView != null) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorOnInfoContainer));
        }
        return snackbar;
    }

    public static Snackbar getWarningMessage(View view, int messageResource) {
        if (view == null || view.getContext() == null) {
            AdvisorLog.warning(SnackbarGenerator.class, "View or context is null in getWarningMessage");
            return null;
        }
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorOnWarning));
        View snackbarView = snackbar.getView();
        if (snackbarView != null) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorOnWarningContainer));
        }
        return snackbar;
    }

    public static Snackbar getWarningMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getWarningMessage(view, messageResource);
        if (snackbar != null) {
            snackbar.setAction(actionResource, action);
        }
        return snackbar;
    }

    public static Snackbar getErrorMessage(View view, int messageResource) {
        if (view == null || view.getContext() == null) {
            AdvisorLog.warning(SnackbarGenerator.class, "View or context is null in getErrorMessage");
            return null;
        }
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.md_theme_onError));
        View snackbarView = snackbar.getView();
        if (snackbarView != null) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.md_theme_onErrorContainer));
        }
        return snackbar;
    }

    public static Snackbar getErrorMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getErrorMessage(view, messageResource);
        if (snackbar != null) {
            snackbar.setAction(actionResource, action);
        }
        return snackbar;
    }
}
