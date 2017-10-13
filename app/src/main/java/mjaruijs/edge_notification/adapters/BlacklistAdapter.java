package mjaruijs.edge_notification.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.cards.CardList;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistHolder> {

    private CardList cards;

    public BlacklistAdapter(CardList cards) {
        this.cards = cards;
    }

    @Override
    public BlacklistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new BlacklistHolder(v);
    }

    @Override
    public void onBindViewHolder(final BlacklistHolder holder, int position) {
        Log.i(getClass().getSimpleName(), "ADDING BLACK");
        final String tag = cards.get(position).getAppName();
        String nameTag = tag + "_Name";
        String iconTag = tag + "_Icon";
        String deleteBtnTag = tag + "_Del_Btn";
        String deleteBackGrdTag = tag + "_Del_Backgrd";

        holder.appName.setText(cards.get(position).getAppName());
        holder.appIcon.setImageDrawable(cards.get(position).getAppIcon());
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
                holder.appName.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class BlacklistHolder extends RecyclerView.ViewHolder {

        CardView cv;
        Button deleteButton;
        ImageView deleteBackground;
        TextView appName;
        ImageView appIcon;

        BlacklistHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            deleteBackground = (ImageView) itemView.findViewById(R.id.delete_background);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
        }
    }
}
