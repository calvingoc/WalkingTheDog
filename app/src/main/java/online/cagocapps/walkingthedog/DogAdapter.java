package online.cagocapps.walkingthedog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Calvin on 3/1/2017.
 * recycler view class for displaying dogs on walk and list of all dogs
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogAdapterViewHolder> {
    //setting arrays of useful info
    private Long[] dogsList;
    private String[] dogsListNames;
    private Long[] mCurWalks;
    private Long[] mCurTime;
    private Float[] mCurDist;
    private Long[] mGoalWalks;
    private Long[] mGoalTime;
    private Float[] mGoalDist;
    private Bitmap[] mImages;

    // set up click handler var
    private final DogAdapterOnClickHandler mClickHandler;
/*
* DogAdapterOnClickHandler
* link onClick to parent class
* */
    public interface DogAdapterOnClickHandler { // link onClick to parent class
        void onClick(Long petID);
    }
/*
* DogAdapter
* localize clickHandler
* */
    public DogAdapter(DogAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

/*
* DogAdapterViewHolder
* Class to populate recycler view items;
* */
    public class DogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // variables for each view
        public final TextView dogTextView;
        public final TextView dogWalksView;
        public final TextView dogDistView;
        public final TextView dogTimeView;
        public final ImageView dogPic;


        /*
        * DogAdapterViewHolder
        * links views to local variables
        * */
        public DogAdapterViewHolder(View view){
            super(view);

            dogTextView = (TextView) view.findViewById(R.id.dog_name_recyc_view);
            dogWalksView = (TextView) view.findViewById(R.id.dog_walks_rv);
            dogDistView = (TextView) view.findViewById(R.id.dog_dist_tv);
            dogTimeView = (TextView) view.findViewById(R.id.dog_time_rv);
            dogPic = (ImageView) view.findViewById(R.id.dog_list_image);
            view.setOnClickListener(this);
        }


        /*
        * onClick
        * pass variables specific to item clicked to click handler parent class
        * */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Long clickedDogID = dogsList[adapterPosition];
            mClickHandler.onClick(clickedDogID);
        }
    }
    /*
    * onCreateViewHolder
    * inflates items with dog_list_item
    * */
    @Override
    public DogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.dog_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new DogAdapterViewHolder(view);
    }

    /*
    * onBindViewHolder
    * populates views with dog specific info
    * */
    @Override
    public void onBindViewHolder(DogAdapterViewHolder holder, int position) {
        String dogName = dogsListNames[position];
        holder.dogTextView.setText(dogName);
        if (mImages[position]!= null){ //only populate image if dog has profile pic
            holder.dogPic.setImageBitmap(mImages[position]);
        if (mCurWalks != null){ //only attempt to show dog stats if mCurWalks array is not null
            //required because same adapter is used in track walks and view/edit dogs
            Long math = mGoalTime[position] - mCurTime[position];
            if (math < 0) math = (long) 0;
            holder.dogTimeView.setText(Long.toString(math));
            math = mGoalWalks[position] - mCurWalks[position];
            if (math < 0) math = (long) 0;
            holder.dogWalksView.setText(Long.toString(math));
            float distMath = mGoalDist[position] - mCurDist[position];
            if (distMath < 0) distMath = (float) 0;
            holder.dogDistView.setText(String.format("%.2f", distMath));
            }

        }
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
   * setDogsList
   * sets local variables with information needed to populate the recyclerview.
   * */
    public  void setDogsList(Long[] dogsListID, String[] dogsListName,
                             Long[] curWalks, Long[] curTime, Float[] curDist,
                             Long[] goalWalks, Long[] goalTime, Float[] goalDist, Bitmap[] images)
    {
        dogsList = dogsListID;
        dogsListNames = dogsListName;
        mCurWalks = curWalks;
        mCurTime = curTime;
        mCurDist = curDist;
        mGoalWalks = goalWalks;
        mGoalTime = goalTime;
        mGoalDist = goalDist;
        mImages = images;
    }

}
