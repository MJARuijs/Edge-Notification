package mjaruijs.edge_notification.services;

import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.CardList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {

    private CardList cards;

    public RVAdapter(CardList cards){
        this.cards = cards;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        final String tag = cards.get(position).getAppName();
        String nameTag = tag + "_Name";
        String iconTag = tag + "_Icon";
        String deleteBtnTag = tag + "_Del_Btn";
        String deleteBackGrdTag = tag + "_Del_Backgrd";

        holder.appName.setText(cards.get(position).getAppName());
        holder.appIcon.setImageDrawable(cards.get(position).getAppIcon());
        holder.appNotificationColor.setTag(cards.get(position).getAppName());
        int[][] states = new int[][] { new int[0]};
        int[] colors = { cards.get(position).getNotificationColor() };
        ColorStateList colorList = new ColorStateList(states, colors);
        holder.appNotificationColor.setBackgroundTintList(colorList);
        holder.deleteButton.setTag(cards.get(position).getAppName());
        holder.appName.setTag(nameTag);
        holder.appIcon.setTag(iconTag);
        holder.deleteBackground.setTag(deleteBackGrdTag);
        holder.deleteButton.setTag(deleteBtnTag);

        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                cards.getByName(tag).setSelected(true);
                holder.deleteBackground.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.appNotificationColor.setVisibility(View.INVISIBLE);
                holder.appName.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        Button deleteButton;
        ImageView deleteBackground;
        TextView appName;
        ImageView appIcon;
        Button appNotificationColor;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            deleteBackground = (ImageView) itemView.findViewById(R.id.delete_background);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appNotificationColor = (Button) itemView.findViewById(R.id.app_notification_color);
        }
    }
}
