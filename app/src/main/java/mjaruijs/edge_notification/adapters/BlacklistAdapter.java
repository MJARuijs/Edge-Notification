package mjaruijs.edge_notification.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.cards.Blacklist;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistHolder> {

    private Blacklist cards;

    public BlacklistAdapter(Blacklist cards) {
        this.cards = cards;
    }

    @Override
    public BlacklistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blacklist_item, parent, false);
        return new BlacklistHolder(v);
    }

    @Override
    public void onBindViewHolder(final BlacklistHolder holder, int position) {
        final String tag = cards.get(position).getAppName();
        String deleteBtnTag = tag + "_Del_Btn";

        holder.itemName.setText(cards.get(position).getItem());
        holder.deleteButton.setTag(deleteBtnTag);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class BlacklistHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView itemName;
        Button deleteButton;

        BlacklistHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            itemName = itemView.findViewById(R.id.blacklist_text);
            deleteButton = itemView.findViewById(R.id.blacklist_delete_button);
        }
    }
}
