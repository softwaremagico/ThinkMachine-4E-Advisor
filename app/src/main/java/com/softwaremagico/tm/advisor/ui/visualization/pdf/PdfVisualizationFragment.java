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

package com.softwaremagico.tm.advisor.ui.visualization.pdf;

import android.content.Context;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwaremagico.tm.advisor.BuildConfig;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.CharacterExportUtils;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.advisor.ui.translation.TextVariablesManager;
import com.softwaremagico.tm.advisor.ui.visualization.VisualizationFragment;
import com.softwaremagico.tm.log.MachineLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PdfVisualizationFragment extends Fragment implements VisualizationFragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private static final int FILE_IDENTIFICATION = 42;
    protected CharacterPdfViewModel mViewModel;
    private File characterSheetAsPdf;
    private View root;
    private LinearLayout layout;
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private int latestRenderRequestId = 0;

    protected abstract View getFragmentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract byte[] generatePdf();

    protected abstract void generatePdfFile(String path);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = getFragmentView(inflater, container);
        mViewModel = new ViewModelProvider(this).get(CharacterPdfViewModel.class);
        layout = root.findViewById(R.id.content);

        final FloatingActionButton fab = root.findViewById(R.id.share);
        fab.setOnClickListener(view -> sharePdf());

        // Render when the view has a measured width to avoid empty/blank bitmaps.
        root.post(this::initData);
        return root;
    }

    protected void initData() {
        if (!isAdded() || layout == null) {
            return;
        }

        layout.removeAllViews();
        final int targetWidth = layout.getWidth() > 0 ? layout.getWidth() : root.getResources().getDisplayMetrics().widthPixels;
        final int requestId = ++latestRenderRequestId;

        backgroundExecutor.execute(() -> {
            try {
                byte[] pdfBytes = generatePdf();
                if (pdfBytes == null || pdfBytes.length == 0) {
                    AdvisorLog.warning(this.getClass(), "Empty PDF generated in memory. Falling back to temp file generation.");
                    pdfBytes = generatePdfFromFile();
                }
                if (pdfBytes.length == 0) {
                    AdvisorLog.warning(this.getClass(), "Unable to render PDF. Generated PDF is empty.");
                    return;
                }

                final List<Bitmap> images = pdfRender(pdfBytes, targetWidth);
                root.post(() -> {
                    if (!isAdded() || layout == null || requestId != latestRenderRequestId) {
                        return;
                    }
                    for (Bitmap image : images) {
                        if (image != null) {
                            final ImageView imageView = new ImageView(getContext());
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            imageView.setAdjustViewBounds(true);
                            imageView.setImageBitmap(image);
                            layout.addView(imageView);
                        }
                    }
                });
            } catch (IOException e) {
                AdvisorLog.errorMessage(this.getClass(), e);
            }
        });
    }


    private void sharePdf() {
        final Context context = getContext();
        if (context == null) {
            return;
        }

        final var selectedCharacter = CharacterManager.getSelectedCharacter();
        final String characterName = CharacterExportUtils.getSafeCharacterName(selectedCharacter);
        final File imagePath = new File(context.getCacheDir(), "pdf");
        if (!imagePath.exists() && !imagePath.mkdirs()) {
            AdvisorLog.warning(this.getClass(), "Unable to create PDF export folder '{}'.", imagePath);
            return;
        }

        characterSheetAsPdf = new File(imagePath, characterName.isEmpty() ? "pdf_sheet.pdf" : characterName + "_sheet.pdf");
        final Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", characterSheetAsPdf);

        if (contentUri != null) {
            backgroundExecutor.execute(() -> {
                generatePdfFile(characterSheetAsPdf.getAbsolutePath());
                root.post(() -> {
                    if (!isAdded() || getContext() == null || getActivity() == null) {
                        return;
                    }
                    final Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    shareIntent.setClipData(ClipData.newUri(getActivity().getContentResolver(), characterSheetAsPdf.getName(), contentUri));
                    shareIntent.setType(getActivity().getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + (!characterName.isEmpty() ?
                            ": " + characterName : ""));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, TextVariablesManager.replace(getString(R.string.share_body)));

                    final Intent chooser = Intent.createChooser(shareIntent, "Share File");
                    final List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (final ResolveInfo resolveInfo : resInfoList) {
                        final String packageName = resolveInfo.activityInfo.packageName;
                        context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    startActivity(chooser);
                });
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backgroundExecutor.shutdownNow();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_IDENTIFICATION && characterSheetAsPdf != null) {
            if (characterSheetAsPdf.delete()) {
                MachineLog.debug(this.getClass().getName(), "File deleted");
            }
        }
    }

    @Override
    public void updateData() {
        if (root != null) {
            root.post(this::initData);
        }
    }


    private List<Bitmap> pdfRender(byte[] byteArray, int width) throws IOException {
        // create a new renderer
        PdfRenderer renderer = new PdfRenderer(getFileDescriptor(byteArray));

        List<Bitmap> gallery = new ArrayList<>();

        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            gallery.add(pdfRender(renderer, i, width));
        }

        // close the renderer
        renderer.close();

        return gallery;
    }

    private byte[] generatePdfFromFile() throws IOException {
        final Context context = getContext();
        if (context == null) {
            return new byte[0];
        }
        final File tempPdf = File.createTempFile("temp_sheet_fallback", ".pdf", context.getCacheDir());
        generatePdfFile(tempPdf.getAbsolutePath());

        if (!tempPdf.exists() || tempPdf.length() == 0) {
            return new byte[0];
        }

        final byte[] content = Files.readAllBytes(tempPdf.toPath());
        if (!tempPdf.delete()) {
            MachineLog.debug(this.getClass().getName(), "Temp PDF fallback file not deleted: {}", tempPdf.getAbsolutePath());
        }
        return content;
    }

    private Bitmap pdfRender(PdfRenderer renderer, int pageNum, int width) {
        // let us just render all pages
        final int pageCount = renderer.getPageCount();
        final PdfRenderer.Page page = renderer.openPage(pageNum);

        // create bitmap at appropriate size
        final float ratio = (float) page.getHeight() / page.getWidth();
        final float newHeight = width * ratio;
        final Bitmap bitmap = Bitmap.createBitmap(width != 0 ? width : page.getWidth(), width != 0 ? (int) newHeight : page.getHeight(), Bitmap.Config.ARGB_8888);

        // render PDF page to bitmap
        //var rect = new Rect(0, 0, width, height);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

        // close the page
        page.close();

        //Compress image.
        return bitmap;
    }

    private ParcelFileDescriptor getFileDescriptor(byte[] byteArray) throws IOException {
        final Context context = getContext();
        if (context == null) {
            throw new IOException("Fragment context is null");
        }
        File file = File.createTempFile("temp_sheet", ".pdf", context.getCacheDir());
        try (FileOutputStream output = new FileOutputStream(file, true)) {
            output.write(byteArray);
        }
        file.deleteOnExit();

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}
