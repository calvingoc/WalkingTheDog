package online.cagocapps.walkingthedog;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Calvin on 6/1/2017.
 */

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementAdapterViewHolder> {
    private String[] achievementsArray;
    private double[] thresholdArray;
    private double[] progressArray;
    private double[] completedArray;
    private double[] seenArray;
    private double[] typeArray;

    public class AchievementAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView achTitle;
        public final TextView achDesc;
        public final TextView achProg;
        public final ImageView star;
        public final ImageView badge;
        public final ConstraintLayout achItem;

        public AchievementAdapterViewHolder(View view){
            super(view);
            achTitle = (TextView) view.findViewById(R.id.achievement_tv_title);
            achDesc = (TextView) view.findViewById(R.id.achievements_tv_description);
            achProg = (TextView) view.findViewById(R.id.achievements_tv_progress);
            star = (ImageView) view.findViewById(R.id.achievements_iv_star);
            badge = (ImageView) view.findViewById(R.id.achievement_iv_badge);
            achItem = (ConstraintLayout) view.findViewById(R.id.achievement_item);
        }
    }

    @Override
    public AchievementAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.achievement_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new AchievementAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AchievementAdapterViewHolder holder, int position) {
        String[] description = achievementsArray[position].split(";");
        holder.achTitle.setText(description[0]);
        holder.achDesc.setText(description[1]);
        holder.achProg.setWidth(50);
        String progress = String.valueOf((progressArray[position]/thresholdArray[position]) *100) + "%";
        if (completedArray[position] == 0){
            holder.star.setVisibility(View.GONE);
            holder.achProg.setVisibility(View.VISIBLE);
            holder.achProg.setText(progress);
            holder.achTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.achItem.setBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
            if (typeArray[position] > 6){
                if (completedArray[position] == 1) progress = progress + " - Earned 1 time.";
                else if (completedArray[position] > 1) progress = progress + " - Earned " + completedArray[position] + " times.";
                holder.star.setVisibility(View.GONE);
                holder.achProg.setVisibility(View.VISIBLE);
                holder.achProg.setText(progress);
                holder.achProg.setWidth(175);
            }
            else if (typeArray[position] > 3){
                progress = "Earned 0 times.";
                holder.star.setVisibility(View.GONE);
                holder.achProg.setVisibility(View.VISIBLE);
                holder.achProg.setText(progress);
                holder.achProg.setWidth(175);
            }
        }
        else {
            holder.star.setVisibility(View.VISIBLE);
            holder.achProg.setVisibility(View.GONE);
            if (typeArray[position] > 6){
                if (completedArray[position] == 1) progress = progress + " - Earned 1 time.";
                else if (completedArray[position] > 1) progress = progress + " - Earned " + completedArray[position] + " times.";
                holder.star.setVisibility(View.GONE);
                holder.achProg.setVisibility(View.VISIBLE);
                holder.achProg.setText(progress);
                holder.achProg.setWidth(175);
            }
            else if (typeArray[position] > 3){
                if (completedArray[position] == 1) progress = "Earned 1 time.";
                else if (completedArray[position] > 1) progress = "Earned " + completedArray[position] + " times.";
                else progress = "Earned 0 times.";
                holder.star.setVisibility(View.GONE);
                holder.achProg.setVisibility(View.VISIBLE);
                holder.achProg.setText(progress);
                holder.achProg.setWidth(175);
            }
            if (seenArray[position] == 0) holder.achTitle.setTextColor(holder.itemView.getResources().getColor(R.color.colorAccent));
            else holder.achTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.achItem.setBackgroundColor(holder.itemView.getResources().getColor(R.color.powderBlue));
        }
    }

    @Override
    public int getItemCount() {
        return achievementsArray.length;
    }

    public void setAchList(String[] achievements, double[] threshold, double[] progress, double[] completed,
                           double[] seen, double[] type){
        achievementsArray = achievements;
        thresholdArray = threshold;
        progressArray = progress;
        completedArray = completed;
        seenArray = seen;
        typeArray = type;
    }
}
