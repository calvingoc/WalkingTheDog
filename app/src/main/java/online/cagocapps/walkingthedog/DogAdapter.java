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
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogAdapterViewHolder> {
    //setting arrays of useful info
    private Long[] dogsList;
    private String[] dogsListNames;
    private Long[] mCurWalks;
    private double[] mCurTime;
    private Float[] mCurDist;
    private Long[] mGoalWalks;
    private double[] mGoalTime;
    private Float[] mGoalDist;
    private Bitmap[] mImages;


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
            dogPic = (ImageView) view.findViewById(R.id.dog_list_image);
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
        if (mImages[position]!= null) {
            holder.dogPic.setImageBitmap(mImages[position]);
        }
        if (mCurWalks != null){
            double math = mGoalTime[position] - mCurTime[position];
            if (math < 0) math = 0;
            holder.dogTimeView.setText(String.format("%.2f", math));
            long walkMath = mGoalWalks[position] - mCurWalks[position];
            if (walkMath < 0) walkMath = (long) 0;
            holder.dogWalksView.setText(Long.toString(walkMath));
            float distMath = mGoalDist[position] - mCurDist[position];
            if (distMath < 0) distMath = (float) 0;
            holder.dogDistView.setText(String.format("%.2f", distMath));
        }
    }

    @Override
    public int getItemCount() {
        if (null == dogsList) return 0;
        return dogsList.length;
    }

    //moves values into local variables.
    public  void setDogsList(Long[] dogsListID, String[] dogsListName,
                             Long[] curWalks, double[] curTime, Float[] curDist,
                             Long[] goalWalks, double[] goalTime, Float[] goalDist, Bitmap[] images)
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
