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

package com.softwaremagico.tm.advisor.ui.load;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.core.CharacterJsonManager;
import com.softwaremagico.tm.advisor.core.DateUtils;
import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.advisor.persistence.CharacterEntity;
import com.softwaremagico.tm.advisor.persistence.CharacterHandler;
import com.softwaremagico.tm.advisor.ui.animations.ExpandAndCollapseViewUtil;
import com.softwaremagico.tm.advisor.ui.main.SnackbarGenerator;
import com.softwaremagico.tm.advisor.ui.session.CharacterManager;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidJsonException;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterRecyclerViewAdapter extends RecyclerView
        .Adapter<CharacterRecyclerViewAdapter.CharacterEntityViewHolder> {

    private final List<CharacterEntity> dataSet;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private final Map<CharacterEntity, String> charactersDescriptions;
    private ClosePopUpListener closePopUpListener;

    public interface ClosePopUpListener {
        void dismiss();
    }

    public CharacterRecyclerViewAdapter(List<CharacterEntity> data) {
        this.dataSet = data;
        charactersDescriptions = new HashMap<>();
    }

    /**
     * Represent each character entity with a character card.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CharacterEntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterEntityViewHolder(this, LayoutInflater.from(parent.getContext()).inflate(R.layout.character_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CharacterEntityViewHolder holder, int position) {
        holder.cardView.setSelected(selectedPosition == position);
        holder.update(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public CharacterEntity getSelectedItem() {
        return selectedPosition == RecyclerView.NO_POSITION ? null : dataSet.get(selectedPosition);
    }

    public void addClosePopUpListener(ClosePopUpListener listener) {
        closePopUpListener = listener;
    }

    class CharacterEntityViewHolder extends RecyclerView.ViewHolder {
        private static final int DURATION = 250;

        private final ImageView imageViewExpand;
        private final ViewGroup detailLayout;
        private CharacterEntity characterEntity;
        private final View cardView;
        private final Toolbar characterTitle;
        private final TextView characterPlayer;
        private TextView completeDescription;
        private final TextView sortDescription;
        private final RecyclerView.Adapter adapter;

        @SuppressLint("NonConstantResourceId")
        public CharacterEntityViewHolder(RecyclerView.Adapter adapter, View cardView) {
            super(cardView);
            this.cardView = cardView;
            this.adapter = adapter;
            completeDescription = cardView.findViewById(R.id.character_description_skills);
            sortDescription = cardView.findViewById(R.id.short_description);
            characterTitle = cardView.findViewById(R.id.character_title);
            characterPlayer = cardView.findViewById(R.id.character_player);
            detailLayout = cardView.findViewById(R.id.details_layout);
            imageViewExpand = cardView.findViewById(R.id.image_view_expand);
            imageViewExpand.setImageResource(R.drawable.ic_more);

            imageViewExpand.setOnClickListener(this::toggleDetails);

            characterTitle.inflateMenu(R.menu.character_selector_menu);
            characterTitle.setOnMenuItemClickListener(item -> {
                final int itemId = item.getItemId();
                if (itemId == R.id.character_load) {
                    if (characterEntity.getCharacterPlayer() != null) {
                        CharacterManager.setSelectedCharacter(characterEntity.getCharacterPlayer());
                        if (closePopUpListener != null) {
                            closePopUpListener.dismiss();
                        }
                    }
                } else if (itemId == R.id.character_copy) {
                    duplicate(characterEntity);
                } else if (itemId == R.id.character_delete) {
                    delete(characterEntity);
                }
                return true;
            });
        }

        private void delete(final CharacterEntity characterEntity) {
            try {
                CharacterHandler.getInstance().delete(cardView.getContext(), characterEntity);
                final int index = dataSet.indexOf(characterEntity);
                SnackbarGenerator.getInfoMessage(cardView, R.string.character_deleted_correctly, R.string.undo, v -> {
                    dataSet.add(index, characterEntity);
                    adapter.notifyDataSetChanged();
                }).show();
                dataSet.remove(characterEntity);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                SnackbarGenerator.getErrorMessage(cardView, R.string.error_deleting_character).show();
            }
        }

        private void duplicate(CharacterEntity characterEntity) {
            try {
                if (characterEntity.getCharacterPlayer() == null) {
                    return;
                }
                //The engine does not provide a "duplicate()" method for CharacterPlayer, so the
                //character is cloned through a JSON round-trip (same mechanism used to persist it).
                final String json = CharacterJsonManager.toJson(characterEntity.getCharacterPlayer());
                final CharacterPlayer duplicatedCharacterPlayer = CharacterJsonManager.fromJson(json);
                final CharacterEntity duplicatedCharacterEntity = new CharacterEntity(duplicatedCharacterPlayer);
                CharacterHandler.getInstance().save(cardView.getContext(), duplicatedCharacterEntity);
                SnackbarGenerator.getInfoMessage(cardView, R.string.message_duplication_ok).show();
                dataSet.add(0, duplicatedCharacterEntity);
                adapter.notifyDataSetChanged();
            } catch (InvalidJsonException | RuntimeException e) {
                AdvisorLog.errorMessage(this.getClass().getName(), e);
                SnackbarGenerator.getErrorMessage(cardView, R.string.error_duplicating_character).show();
            }
        }

        public void update(CharacterEntity characterEntity) {
            this.characterEntity = characterEntity;
            final CharacterPlayer characterPlayerData = characterEntity.getCharacterPlayer();
            if (characterPlayerData == null) {
                characterTitle.setTitle("");
                characterTitle.setSubtitle("");
                sortDescription.setText("");
                characterPlayer.setVisibility(View.GONE);
                return;
            }
            characterTitle.setTitle(characterPlayerData.getCompleteNameRepresentation());
            characterTitle.setSubtitle(DateUtils.formatTimestamp(characterEntity.getUpdateTime()));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sortDescription.setText(Html.fromHtml(createStatusText(characterEntity), Html.FROM_HTML_MODE_LEGACY));
            } else {
                sortDescription.setText(Html.fromHtml(createStatusText(characterEntity)));
            }
            final ImageView factionImageView = cardView.findViewById(R.id.image_view_faction);
            factionImageView.setMaxWidth(175);
            factionImageView.setMaxHeight(175);
            factionImageView.setImageResource(FactionLogoSelection.getLogo(cardView.getContext(), getFaction(characterPlayerData)));
            if (characterEntity.getPlayer() == null || characterEntity.getPlayer().isEmpty()) {
                characterPlayer.setVisibility(View.GONE);
            } else {
                characterPlayer.setText(characterEntity.getPlayer());
                characterPlayer.setVisibility(View.VISIBLE);
            }
        }

        private Faction getFaction(CharacterPlayer characterPlayerData) {
            try {
                if (characterPlayerData == null || characterPlayerData.getFaction() == null) {
                    return null;
                }
                return characterPlayerData.getFaction().get();
            } catch (Exception e) {
                return null;
            }
        }

        private String createStatusText(CharacterEntity characterEntity) {
            final CharacterPlayer characterPlayerData = characterEntity.getCharacterPlayer();
            if (characterPlayerData == null) {
                return "";
            }
            final StringBuilder stringBuilder = new StringBuilder(100);
            final Faction faction = getFaction(characterPlayerData);
            if (faction != null) {
                stringBuilder.append(faction.getNameRepresentation());
            }
            if (characterPlayerData.getSpecie() != null) {
                try {
                    final Specie specie = SpecieFactory.getInstance().getElement(characterPlayerData.getSpecie());
                    if (specie != null) {
                        stringBuilder.append(" (").append(specie.getNameRepresentation()).append(")");
                    }
                } catch (Exception e) {
                    //Specie not available.
                }
            }
            stringBuilder.append("<br>");
            //Threat
            final ThreatLevelHandler threatLevelHandler = new ThreatLevelHandler(ThreatLevel.getThreatLevel(characterPlayerData));
            stringBuilder.append(itemView.getContext().getString(R.string.character_threat)).append(" <font color=\"").append(threatLevelHandler.getColor(cardView.getContext())).append("\"><b>")
                    .append(threatLevelHandler.getThreatLevel()).append("</b>");
            return stringBuilder.toString();
        }


        public void cardClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
        }

        public void toggleDetails(View view) {
            completeDescription = cardView.findViewById(R.id.character_description_skills);
            if (detailLayout.getVisibility() == View.GONE) {
                completeDescription.setText(getExtendedDescription(characterEntity));
                ExpandAndCollapseViewUtil.expand(detailLayout, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_more);
                rotate(-180.0f);
            } else {
                ExpandAndCollapseViewUtil.collapse(detailLayout, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_less);
                rotate(180.0f);
            }
        }

        private String getExtendedDescription(CharacterEntity characterEntity) {
            if (characterEntity.getCharacterPlayer() == null) {
                return "";
            }
            if (charactersDescriptions.get(characterEntity) == null) {
                final CharacterSheet characterSheet = new CharacterSheet(characterEntity.getCharacterPlayer());
                charactersDescriptions.put(characterEntity, characterSheet.toString());
            }
            return charactersDescriptions.get(characterEntity);
        }

        private void rotate(float angle) {
            final Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            imageViewExpand.startAnimation(animation);
        }
    }

}
