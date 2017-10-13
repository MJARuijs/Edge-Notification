package mjaruijs.edge_notification.data.cards;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blacklist extends CardList {

    private static List<BlackCard> blacklist;
    private static final String TAG = "Blacklist";

    public Blacklist() {
        blacklist = new ArrayList<>();
    }

    public void addCard(BlackCard card) {
        super.addCard(card);
        blacklist.add(card);
    }

    public void clear() {
        super.clear();
        blacklist.clear();
    }

    public BlackCard get(int i) {
        return blacklist.get(i);
    }

    public static Blacklist readFromXML(Scanner sc) {
        Blacklist list = new Blacklist();
        String line;
        String blacklistName = "";
//        sc.next();

        do {
            line = sc.nextLine();
            if (line.contains("<item>")) {
                blacklistName = getValue(line);
            } else if (line.contains("</black-card>")) {
                blacklist.add(new BlackCard(blacklistName));
            }

        } while(!line.contains("</blacklist>"));

        Log.i("BLACKLIST PARSER: ", list.toString());
        return list;
    }

    @Override
    public String toString() {
        String fileContent = "";

        if (blacklist.size() > 0) {

            for (BlackCard blackCard : blacklist) {
                fileContent += "\n\t\t\t\t<black-card>"
                    + "\n\t\t\t\t<item>" + blackCard.getItem() + "</item>"
                    + "\n\t\t\t</black-card>";
            }
        }

        return fileContent;
    }

//    @Override
//    public boolean areAllItemsEnabled() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
//
//    @Override
//    public void registerDataSetObserver(DataSetObserver observer) {
//
//    }
//
//    @Override
//    public void unregisterDataSetObserver(DataSetObserver observer) {
//
//    }
//
//    @Override
//    public int getCount() {
//        return blacklist.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return blacklist.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        return null;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 0;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 1;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
}
