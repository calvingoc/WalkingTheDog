package com.example.cgehredo.walkingthedog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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
        public final ImageView dogPicImageView;
        public final TextView dogIDholder;
        public final TextView dogName;
        public final View background;

        public DogOnWalkAdapterViewHolder(View view){
            super(view);
            dogPicImageView = (ImageView) view.findViewById(R.id.dog_on_walk_image);
            dogIDholder = (TextView) view.findViewById(R.id.petid_holder);
            dogName = (TextView) view.findViewById(R.id.dog_on_walk_rv_name) ;
            background = view.findViewById(R.id.dogs_on_walk_background);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("dogsonwalk", "Clicked");
            TextView dogIDTV = (TextView) view.findViewById(R.id.petid_holder);
            Long clickedDogID = Long.parseLong(dogIDTV.getText().toString());
            TextView dogName = (TextView) view.findViewById(R.id.dog_on_walk_rv_name);
            if (view.getBackground()== dogIDTV.getBackground()){
                view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
            }else view.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
            Log.d("onClick", "made it here " + Long.toString(clickedDogID));
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
        Long dogID = dogsList[position];
        holder.dogIDholder.setText(Long.toString(dogID));
        holder.dogName.setText(dogName);
        String id = Long.toString(dogsList[position]);
        for (String s : dogsOnWalk){
            if (s.equals(id)) holder.background.setBackgroundColor(R.color.colorPrimaryDark);
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
