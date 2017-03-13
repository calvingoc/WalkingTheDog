package online.cagocapps.walkingthedog;

import android.content.Context;
import android.graphics.Bitmap;
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
 * recycler view class for selecting dogs on walk
 */

public class DogOnWalkAdapter extends RecyclerView.Adapter<DogOnWalkAdapter.DogOnWalkAdapterViewHolder>{
  // set up useful info
    private Long[] dogsList;
    private String[] dogsListNames;
    private String[] dogsOnWalk;
    private Bitmap[] mImages;

    // set up click handler var
    private final DogOnWalkAdapterOnClickHandler mClickHandler;

    /*
    * DogAOnWalkdapterOnClickHandler
    * link onClick to parent class
    * */
    public interface DogOnWalkAdapterOnClickHandler{
        void onClick(Long petID);
    }

    /*
    * DogOnWalkAdapter
    * localize clickHandler
    * */
    public DogOnWalkAdapter(DogOnWalkAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    /*
    * DogOnWalkAdapterViewHolder
    * Class to populate recycler view items;
    * */
    public class DogOnWalkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // variables for each view
        public final ImageView dogPicImageView;
        public final TextView dogIDholder;
        public final TextView dogName;
        public final View background;

        /*
        * DogOnWalkAdapterViewHolder
        * links views to local variables
        * */
        public DogOnWalkAdapterViewHolder(View view){
            super(view);
            dogPicImageView = (ImageView) view.findViewById(R.id.dog_on_walk_image);
            dogIDholder = (TextView) view.findViewById(R.id.petid_holder);
            dogName = (TextView) view.findViewById(R.id.dog_on_walk_rv_name) ;
            background = view.findViewById(R.id.dogs_on_walk_background);
            view.setOnClickListener(this);
        }

        /*
        * onClick
        * pass variables specific to item clicked to click handler parent class
        * Also changes color to reflect if the dog is on a walk
        * */
        @Override
        public void onClick(View view) {
            TextView dogIDTV = (TextView) view.findViewById(R.id.petid_holder);
            Long clickedDogID = Long.parseLong(dogIDTV.getText().toString());
            if (dogIDTV.isEnabled()){
                view.setBackgroundColor(Color.parseColor("#FF4081"));
                dogIDTV.setEnabled(false);
            }else{
                view.setBackgroundColor(Color.parseColor("#303F9F"));
                dogIDTV.setEnabled(true);
            }
            mClickHandler.onClick(clickedDogID);

        }

    }

    /*
    * onCreateViewHolder
    * inflates items with dog_list_item
    * */
    @Override
    public DogOnWalkAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdforListItem = R.layout.dogs_on_walk_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdforListItem, parent, false);
        return new DogOnWalkAdapterViewHolder(view);
    }

    /*
    * onBindViewHolder
    * populates views with dog specific info
    * */
    @Override
    public void onBindViewHolder(DogOnWalkAdapterViewHolder holder, int position) {
        String dogName = dogsListNames[position];
        Long dogID = dogsList[position];
        holder.dogIDholder.setText(Long.toString(dogID));
        holder.dogIDholder.setEnabled(false);
        holder.dogName.setText(dogName);
        String id = Long.toString(dogsList[position]);
        for (String s : dogsOnWalk){
            if (s.equals(id)) {
                holder.background.setBackgroundColor(Color.parseColor("#303F9F"));
                holder.dogIDholder.setEnabled(true);
            }
        }
        if (mImages[position] != null) holder.dogPicImageView.setImageBitmap(mImages[position]);
    }

    /*
    * getItemCount
    * gets item count... duh
    * */
    @Override
    public int getItemCount() {
        if (null == dogsList) return 0;
        return dogsList.length;
    }

    /*
   * setDogsonWalk
   * sets local variables with information needed to populate the recyclerview.
   * */
    public void setDogsOnWalk(Long[] dogIDs, String[] dogNames, String[] dogIDsOnWalk, Bitmap[] images){
        mImages = images;
        dogsList = dogIDs;
        dogsListNames = dogNames;
        dogsOnWalk = dogIDsOnWalk;
    }
}
