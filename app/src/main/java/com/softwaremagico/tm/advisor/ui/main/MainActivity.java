/*
 *  Copyright (C) 2024 Softwaremagico
 *
 *  This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero  <softwaremagico@gmail.com> Valencia (Spain).
 *
 *  This program is free software; you can redistribute it and/or modify it under  the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this Program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.softwaremagico.tm.advisor.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.softwaremagico.tm.advisor.BuildConfig;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.CharacterExportUtils;
import com.softwaremagico.tm.advisor.core.CharacterJsonManager;
import com.softwaremagico.tm.advisor.core.FileUtils;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;
import com.softwaremagico.tm.advisor.persistence.SettingsEntity;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;
import com.softwaremagico.tm.advisor.ui.about.AboutWindow;
import com.softwaremagico.tm.advisor.ui.about.SettingsWindow;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidJsonException;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.qr.CharacterQrCodec;
import com.softwaremagico.tm.qr.CharacterQrLogoPngWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_TMA_FILE = 0;
    private static final int CHARACTERS_SELECTOR_GROUP = 10;
    private static final int CHARACTERS_INDEX = 1000;
    private final java.util.concurrent.ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<ScanOptions> qrScannerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ThinkMachine4EAdvisor);
        Translator.setLanguage(Locale.getDefault().getLanguage());

        // Handle the splash screen transition.
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        launchQrScanner();
                    } else {
                        final View parentLayout = findViewById(android.R.id.content);
                        SnackbarGenerator.getErrorMessage(parentLayout, R.string.camera_permission_required).show();
                    }
                });
        qrScannerLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result == null || result.getContents() == null || result.getContents().isBlank()) {
                return;
            }
            importQrPayload(result.getContents());
        });

        setContentView(R.layout.activity_main);

        backgroundExecutor.execute(() -> {
            final SettingsEntity loadedSettings = SettingsHandler.loadSettingsEntity(getBaseContext());
            runOnUiThread(() -> {
                SettingsHandler.setSettingsEntity(loadedSettings);
                SettingsHandler.setModulesBySettings();
            });
        });

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        final View navHostFragment = findViewById(R.id.nav_host_fragment);
        final View container = findViewById(R.id.container);
        final int navViewTopPadding = navView.getPaddingTop();

        ViewCompat.setOnApplyWindowInsetsListener(container, (view, windowInsets) -> {
            final androidx.core.graphics.Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            navHostFragment.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            navView.setPadding(systemBars.left, navViewTopPadding, systemBars.right, systemBars.bottom);

            return windowInsets;
        });

        ViewCompat.requestApplyInsets(container);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_random, R.id.navigation_sheet, R.id.navigation_wiki).build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        final View parentLayout = findViewById(android.R.id.content);
        final int itemId = menuItem.getItemId();
        if (itemId == R.id.settings_load) {
            showDialog();
            return true;
        }
        if (itemId == R.id.settings_save) {
            saveCurrentCharacter(parentLayout);
            return true;
        }
        if (itemId == R.id.settings_new) {
            newCharacter();
            return true;
        }
        if (itemId == R.id.settings_global_settings) {
            globalSettings();
            return true;
        }
        if (itemId == R.id.settings_export_file) {
            try {
                exportJson(parentLayout);
            } catch (Exception e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                SnackbarGenerator.getErrorMessage(parentLayout, R.string.message_character_saved_error).show();
            }
            return true;
        }
        if (itemId == R.id.settings_import_file) {
            importJson();
            return true;
        }
        if (itemId == R.id.settings_export_qr) {
            try {
                exportQr(parentLayout);
            } catch (Exception e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                SnackbarGenerator.getErrorMessage(parentLayout, R.string.message_character_saved_error).show();
            }
            return true;
        }
        if (itemId == R.id.settings_import_qr) {
            importQr();
            return true;
        }
        if (itemId == R.id.settings_remove_character) {
            removeSelectedCharacter(parentLayout);
            return true;
        }
        if (itemId == R.id.settings_about) {
            new AboutWindow().show(getSupportFragmentManager(), "");
            return super.onOptionsItemSelected(menuItem);
        }

        // Select an existing character from dynamic options.
        if (itemId >= CHARACTERS_INDEX) {
            final List<CharacterPlayer> characters = CharacterManager.getCharacters();
            int index = itemId - CHARACTERS_INDEX;
            if (characters != null && index >= 0 && index < characters.size()) {
                CharacterManager.setSelectedCharacter(characters.get(index));
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final List<CharacterPlayer> existingCharacters = CharacterManager.getCharacters();
        menu.removeGroup(CHARACTERS_SELECTOR_GROUP);
        if (existingCharacters != null) {
            for (int i = 0; i < existingCharacters.size(); i++) {
                if (i >= existingCharacters.size()) {
                    break;
                }
                final CharacterPlayer character = existingCharacters.get(i);
                if (character == null) {
                    continue;
                }
                String name = character.getCompleteNameRepresentation();
                if (name == null || name.isEmpty()) {
                    name = "<<" + getString(R.string.character_name_empty) + ">>";
                }
                menu.add(CHARACTERS_SELECTOR_GROUP, CHARACTERS_INDEX + i, i, name);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void importJson() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, PICK_TMA_FILE);
    }

    private void importQr() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchQrScanner();
            return;
        }
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void launchQrScanner() {
        final ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt(getString(R.string.settings_import_qr));
        options.setBeepEnabled(false);
        options.setOrientationLocked(false);
        qrScannerLauncher.launch(options);
    }

    private void importQrPayload(String payload) {
        final View parentLayout = findViewById(android.R.id.content);
        backgroundExecutor.execute(() -> {
            try {
                final CharacterPlayer character = CharacterQrCodec.decode(payload);
                runOnUiThread(() -> CharacterManager.setSelectedCharacter(character));
            } catch (IOException | IllegalArgumentException e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                runOnUiThread(() -> SnackbarGenerator.getErrorMessage(parentLayout, R.string.invalid_qr_file).show());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        View parentLayout = findViewById(android.R.id.content);
        if (requestCode == PICK_TMA_FILE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                Uri uri = resultData.getData();
                if (uri != null) {
                    backgroundExecutor.execute(() -> {
                        try {
                            final CharacterPlayer character = CharacterJsonManager.fromJson(FileUtils.readFile(getBaseContext(), uri));
                            runOnUiThread(() -> CharacterManager.setSelectedCharacter(character));
                        } catch (InvalidJsonException e) {
                            AdvisorLog.errorMessage(this.getClass().getName(), e);
                            runOnUiThread(() -> SnackbarGenerator.getErrorMessage(parentLayout, R.string.invalid_json_file).show());
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundExecutor.shutdownNow();
    }


    private void exportJson(View view) throws IOException {
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null) {
            SnackbarGenerator.getErrorMessage(view, R.string.message_character_saved_error).show();
            return;
        }

        final String characterName = CharacterExportUtils.getSafeCharacterName(selectedCharacter);
        final File exportsPath = new File(view.getContext().getCacheDir(), "export");
        if (!exportsPath.exists() && !exportsPath.mkdirs()) {
            MachineLog.warning(this.getClass().getName(), "Unable to create export folder '{}'.", exportsPath);
        }

        final File characterExport = new File(exportsPath, characterName.isEmpty() ?
                "export_sheet." + FileUtils.CHARACTER_FILE_EXTENSION :
                characterName + "_sheet." + FileUtils.CHARACTER_FILE_EXTENSION);
        final Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", characterExport);

        if (contentUri != null) {
            String jsonContent = CharacterJsonManager.toJson(selectedCharacter);
            try (FileOutputStream stream = new FileOutputStream(characterExport)) {
                stream.write(jsonContent.getBytes());
            }

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType(this.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (!characterName.isEmpty() ?
                    ": " + characterName : ""));
            shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.export_body)));

            final Intent chooser = Intent.createChooser(shareIntent, "Share File");
            final List<ResolveInfo> resInfoList = view.getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (final ResolveInfo resolveInfo : resInfoList) {
                final String packageName = resolveInfo.activityInfo.packageName;
                view.getContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }
    }

    private BufferedImage loadAppLogo() {
        final Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (logoBitmap == null) {
            return null;
        }

        final int width = logoBitmap.getWidth();
        final int height = logoBitmap.getHeight();
        final int[] pixels = new int[width * height];
        logoBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        final BufferedImage logoImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        logoImage.setRGB(0, 0, width, height, pixels, 0, width);
        return logoImage;
    }

    private void exportQr(View view) throws IOException, WriterException {
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null) {
            SnackbarGenerator.getErrorMessage(view, R.string.message_character_saved_error).show();
            return;
        }

        final String characterName = CharacterExportUtils.getSafeCharacterName(selectedCharacter);
        final File exportsPath = new File(view.getContext().getCacheDir(), "export");
        if (!exportsPath.exists() && !exportsPath.mkdirs()) {
            MachineLog.warning(this.getClass().getName(), "Unable to create export folder '{}'.", exportsPath);
        }

        final File qrExport = new File(exportsPath, characterName.isEmpty() ?
                "export_sheet_qr.png" :
                characterName + "_sheet_qr.png");
        final Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", qrExport);

        if (contentUri != null) {
            try (FileOutputStream stream = new FileOutputStream(qrExport)) {
                CharacterQrLogoPngWriter.writePng(selectedCharacter, stream, loadAppLogo());
            }

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (!characterName.isEmpty() ?
                    ": " + characterName : ""));
            shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.export_qr_body)));

            final Intent chooser = Intent.createChooser(shareIntent, "Share File");
            final List<ResolveInfo> resInfoList = view.getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (final ResolveInfo resolveInfo : resInfoList) {
                final String packageName = resolveInfo.activityInfo.packageName;
                view.getContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        }
    }

    private void saveCurrentCharacter(View parentLayout) {
        final CharacterPlayer selectedCharacter = CharacterManager.getSelectedCharacter();
        if (selectedCharacter == null) {
            SnackbarGenerator.getErrorMessage(parentLayout, R.string.message_character_saved_error).show();
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CharacterHandler.getInstance().save(getApplicationContext(), selectedCharacter);
                SnackbarGenerator.getInfoMessage(parentLayout, R.string.message_character_saved_successfully).show();
            } catch (Exception e) {
                SnackbarGenerator.getErrorMessage(parentLayout, R.string.message_character_saved_error).show();
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        });
    }

    private void newCharacter() {
        CharacterManager.addNewCharacter();
    }

    private void removeSelectedCharacter(View parentLayout) {
        SnackbarGenerator.getWarningMessage(parentLayout, R.string.remove_character_warning,
                R.string.remove, action -> {
                    CharacterManager.removeSelectedCharacter();
                }).show();
    }

    private void showDialog() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
//        final LoadCharacter loadCharacter = new LoadCharacter();
//
//        final boolean isLargeLayout = true;
//
//        if (isLargeLayout) {
//            // The device is using a large layout, so show the fragment as a dialog
//            loadCharacter.show(fragmentManager, "dialog");
//        } else {
//            // The device is smaller, so show the fragment fullscreen
//            final FragmentTransaction transaction = fragmentManager.beginTransaction();
//            // For a little polish, specify a transition animation
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            // To make it fullscreen, use the 'content' root view as the container
//            // for the fragment, which is always the root view for the activity
//            transaction.add(android.R.id.content, loadCharacter)
//                    .addToBackStack(null).commit();
//        }
    }

    private void globalSettings() {
        new SettingsWindow().show(getSupportFragmentManager(), "");
    }
}
