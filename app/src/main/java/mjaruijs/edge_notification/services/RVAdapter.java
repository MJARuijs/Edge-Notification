package mjaruijs.edge_notification.services;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.appName.setText(cards.get(position).getAppName());
        holder.appIcon.setImageResource(cards.get(position).getAppIcon());
        holder.appIcon.setBackgroundColor(cards.get(position).getNotificationColor());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView appName;
        ImageView appIcon;
        ImageView appNotificationColor;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appNotificationColor = (ImageView) itemView.findViewById(R.id.app_notification_color);
        }
    }
}
