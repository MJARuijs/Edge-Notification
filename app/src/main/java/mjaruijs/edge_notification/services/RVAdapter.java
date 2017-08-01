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
import mjaruijs.edge_notification.color_picker.Colors;
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
        holder.appIcon.setImageDrawable(cards.get(position).getAppIcon());
        holder.appNotificationColor.setTag(cards.get(position).getAppName());
        int[][] states = new int[1][1];
        Colors.initializeColors();
        int[] colors = {0xFFFFFFF};
       // ColorStateList colorList = new ColorStateList(states, colors);
        ColorStateList colorList = holder.appNotificationColor.getBackgroundTintList();

        holder.appNotificationColor.setBackgroundTintList(colorList);
       // Drawable colorButton =

        //holder.appNotificationColor.setBackgroundColor(cards.get(position).getNotificationColor());
//        switch(cards.get(position).getNotificationColor()) {
//            case Color.RED:
//                holder.appNotificationColor.setBackgroundColor(cards.get(position).getNotificationColor());
//                break;
//            default:
//                holder.appNotificationColor.setBackgroundColor(cards.get(position).getNotificationColor());
//
//        }
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
        TextView appName;
        ImageView appIcon;
        Button appNotificationColor;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appNotificationColor = (Button) itemView.findViewById(R.id.app_notification_color);
        }
    }
}
