package mjaruijs.edge_notification.adapters;

import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.cards.SubCard;
import mjaruijs.edge_notification.data.cards.Sublist;

public class SublistAdapter extends RecyclerView.Adapter<SublistAdapter.SublistHolder> {

    private Sublist sublist;

    public SublistAdapter(Sublist sublist) {
        this.sublist = sublist;
    }

    @Override
    public SublistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcard_item, parent, false);
        return new SublistHolder(view);
    }

    @Override
    public void onBindViewHolder(SublistHolder holder, int position) {
        final SubCard card = sublist.get(position);

        final String tag = sublist.get(position).getItem();
        String deleteBtnTag = tag + "_Del_Btn";
        int[][] states = new int[][] { new int[0] };
        int[] colors = { card.getColor() };
        ColorStateList colorList = new ColorStateList(states, colors);

        holder.notificationColor.setBackgroundTintList(colorList);
        holder.notificationColor.setTag(tag);
        holder.item.setText(tag);
        holder.deleteButton.setTag(deleteBtnTag);
    }

    @Override
    public int getItemCount() {
        return sublist.size();
    }

    static class SublistHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        Button notificationColor;
        TextView item;
        Button deleteButton;

        SublistHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.sub_card_view);
            notificationColor = (Button) itemView.findViewById(R.id.sub_notification_color);
            item = (TextView) itemView.findViewById(R.id.sub_text);
            deleteButton = (Button) itemView.findViewById(R.id.sub_delete_button);
        }
    }

}
