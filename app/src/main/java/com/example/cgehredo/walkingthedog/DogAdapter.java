package com.example.cgehredo.walkingthedog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Calvin on 3/1/2017.
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogAdapterViewHolder> {
    //setting arrays of useful info
    private Long[] dogsList;
    private String[] dogsListNames;
    private Long[] mCurWalks;
    private Long[] mCurTime;
    private Long[] mCurDist;
    private Long[] mGoalWalks;
    private Long[] mGoalTime;
    private Long[] mGoalDist;

    private final DogAdapterOnClickHandler mClickHandler;

    public interface DogAdapterOnClickHandler {
        void onClick(Long petID);
    }

    public DogAdapter(DogAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class DogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView dogTextView;
        public final TextView dogWalksView;
        public final TextView dogDistView;
        public final TextView dogTimeView;
        public final ImageView dogPic;


        public DogAdapterViewHolder(View view){
            super(view);
            dogTextView = (TextView) view.findViewById(R.id.dog_name_recyc_view);
            dogWalksView = (TextView) view.findViewById(R.id.dog_walks_rv);
            dogDistView = (TextView) view.findViewById(R.id.dog_dist_tv);
            dogTimeView = (TextView) view.findViewById(R.id.dog_time_rv);
            dogPic = (ImageView) view.findViewById(R.id.dogPic);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Long clickedDogID = dogsList[adapterPosition];
            mClickHandler.onClick(clickedDogID);
        }
    }

    @Override
    public DogAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.dog_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImediately);
        return new DogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DogAdapterViewHolder holder, int position) {
        String dogName = dogsListNames[position];
        holder.dogTextView.setText(dogName);
        if (mCurWalks != null){
            Long math = mGoalTime[position] - mCurTime[position];
            if (math < 0) math = (long) 0;
            holder.dogTimeView.setText(Long.toString(math));
            math = mGoalWalks[position] - mCurWalks[position];
            if (math < 0) math = (long) 0;
            holder.dogWalksView.setText(Long.toString(math));
            math = mGoalDist[position] - mCurDist[position];
            if (math < 0) math = (long) 0;
            holder.dogDistView.setText(Long.toString(math));

        }
    }

    @Override
    public int getItemCount() {
        if (null == dogsList) return 0;
        return dogsList.length;
    }

    //moves values into local variables.
    public  void setDogsList(Long[] dogsListID, String[] dogsListName,
                             Long[] curWalks, Long[] curTime, Long[] curDist,
                             Long[] goalWalks, Long[] goalTime, Long[] goalDist)
    {
        dogsList = dogsListID;
        dogsListNames = dogsListName;
        mCurWalks = curWalks;
        mCurTime = curTime;
        mCurDist = curDist;
        mGoalWalks = goalWalks;
        mGoalTime = goalTime;
        mGoalDist = goalDist;
    }

}
