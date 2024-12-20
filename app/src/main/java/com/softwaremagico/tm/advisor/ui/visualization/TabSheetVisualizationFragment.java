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

package com.softwaremagico.tm.advisor.ui.visualization;

import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.softwaremagico.tm.advisor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabSheetVisualizationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // https://stackoverflow.com/questions/68342930/setretaininstance-is-deprecated-what-is-alternative
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.character_visualization_fragment, container, false);
        final VisualizationSectionsPagerAdapter characterSheetsPagerAdapter = new VisualizationSectionsPagerAdapter(getActivity());
        final ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(characterSheetsPagerAdapter);

        //Avoid refreshing of fragments. We will update them manually.
        viewPager.setOffscreenPageLimit(characterSheetsPagerAdapter.getItemCount());

        final TabLayout tabs = view.findViewById(R.id.tabs);
        for (int titleIndex : VisualizationSectionsPagerAdapter.TAB_TITLES) {
            tabs.addTab(tabs.newTab().setText(titleIndex));
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabs.selectTab(tabs.getTabAt(position));
                characterSheetsPagerAdapter.refreshFragment(position);
            }
        });

        return view;
    }

}
