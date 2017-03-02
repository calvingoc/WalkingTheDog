package com.example.cgehredo.walkingthedog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Calvin on 3/1/2017.
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogAdapterViewHolder> {
    private Long[] dogsList;
    private String[] dogsListNames;

    private final DogAdapterOnClickHandler mClickHandler;

    public interface DogAdapterOnClickHandler {
        void onClick(Long petID);
    }

    public DogAdapter(DogAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class DogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView dogTextView;


        public DogAdapterViewHolder(View view){
            super(view);
            dogTextView = (TextView) view.findViewById(R.id.dog_name_recyc_view);
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
    }

    @Override
    public int getItemCount() {
        if (null == dogsList) return 0;
        return dogsList.length;
    }
    public  void setDogsList(Long[] dogsListID, String[] dogsListName)
    {
        dogsList = dogsListID;
        dogsListNames = dogsListName;
    }

}
