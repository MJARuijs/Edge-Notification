package mjaruijs.edge_notification.adapters;

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
import mjaruijs.edge_notification.data.cards.AppCard;
import mjaruijs.edge_notification.data.cards.AppCardList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private AppCardList cards;

    public CardAdapter(AppCardList cards){
        this.cards = cards;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
        final AppCard card = cards.get(position);
        final String tag = cards.get(position).getAppName();

        String nameTag = tag + "_Name";
        String iconTag = tag + "_Icon";
        String deleteBtnTag = tag + "_Del_Btn";
        String deleteBackGrdTag = tag + "_Del_Backgrd";

        holder.appName.setText(card.getAppName());
        holder.appIcon.setImageDrawable(card.getAppIcon());
        holder.appNotificationColor.setTag(card.getAppName());

        int[][] states = new int[][] { new int[0] };
        int[] colors = { card.getColor() };
        ColorStateList colorList = new ColorStateList(states, colors);
        holder.appNotificationColor.setBackgroundTintList(colorList);

        holder.deleteButton.setTag(card.getAppName());
        holder.appName.setTag(nameTag);
        holder.appIcon.setTag(iconTag);
        holder.deleteBackground.setTag(deleteBackGrdTag);
        holder.deleteButton.setTag(deleteBtnTag);
        holder.deleteButton.setBackgroundResource(R.drawable.delete_icon);
        holder.subCardsButton.setTag(card.getAppName());
        holder.blacklistButton.setTag(card.getAppName());

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
        Button subCardsButton;
        Button blacklistButton;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteBackground = itemView.findViewById(R.id.delete_background);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            appNotificationColor = itemView.findViewById(R.id.app_notification_color);
            subCardsButton = itemView.findViewById(R.id.subCards_button);
            blacklistButton = itemView.findViewById(R.id.blacklist_button);
        }
    }
}
