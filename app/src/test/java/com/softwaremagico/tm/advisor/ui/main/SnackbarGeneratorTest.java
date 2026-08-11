package com.softwaremagico.tm.advisor.ui.main;

import com.google.android.material.snackbar.Snackbar;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class SnackbarGeneratorTest {

    @Test
    public void getInfoMessage_withNullView_returnsNull() {
        Snackbar result = SnackbarGenerator.getInfoMessage(null, android.R.string.ok);
        assertNull("Should return null for null view", result);
    }

    @Test
    public void getWarningMessage_withNullView_returnsNull() {
        Snackbar result = SnackbarGenerator.getWarningMessage(null, android.R.string.ok);
        assertNull("Should return null for null view", result);
    }

    @Test
    public void getErrorMessage_withNullView_returnsNull() {
        Snackbar result = SnackbarGenerator.getErrorMessage(null, android.R.string.ok);
        assertNull("Should return null for null view", result);
    }

    @Test
    public void getInfoMessage_withNullString_returnsNull() {
        Snackbar result = SnackbarGenerator.getInfoMessage(null, "test");
        assertNull("Should return null for null view", result);
    }
}
