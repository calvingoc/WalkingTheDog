package com.example.cgehredo.walkingthedog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by cgehredo on 3/2/2017.
 */

public class DogOnWalkAdapter extends RecyclerView.Adapter<DogOnWalkAdapter.DogOnWalkAdapterViewHolder>{
  // set up useful info
    private Long[] dogsList;
    private String[] dogsListNames;
    private String[] dogsOnWalk;

    private final DogOnWalkAdapterOnClickHandler mClickHandler;

    public interface DogOnWalkAdapterOnClickHandler{
        void onClick(Long petID);
    }

    public DogOnWalkAdapter(DogOnWalkAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class DogOnWalkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final CheckBox dogNameCheckBox;
        public final ImageView dogPicImageView;

        public DogOnWalkAdapterViewHolder(View view){
            super(view);
            dogNameCheckBox = (CheckBox) view.findViewById(R.id.dog_on_walk_checkbox);
            dogPicImageView = (ImageView) view.findViewById(R.id.dog_on_walk_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Long clickedDogID = dogsList[getAdapterPosition()];
            CheckBox dogNameCB = (CheckBox) view.findViewById(R.id.dog_on_walk_checkbox);
            dogNameCB.setChecked(!dogNameCB.isChecked());
            mClickHandler.onClick(clickedDogID);

        }

    }

    @Override
    public DogOnWalkAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdforListItem = R.layout.dogs_on_walk_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdforListItem, parent, false);
        return new DogOnWalkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DogOnWalkAdapterViewHolder holder, int position) {
        String dogName = dogsListNames[position];
        holder.dogNameCheckBox.setText(dogName);
        holder.dogNameCheckBox.setChecked(false);
        String id = Long.toString(dogsList[position]);
        for (String s : dogsOnWalk){
            if (s.equals(id)) holder.dogNameCheckBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        if (null == dogsList) return 0;
        return dogsList.length;
    }

    public void setDogsOnWalk(Long[] dogIDs, String[] dogNames, String[] dogIDsOnWalk){
        dogsList = dogIDs;
        dogsListNames = dogNames;
        dogsOnWalk = dogIDsOnWalk;
    }
}
