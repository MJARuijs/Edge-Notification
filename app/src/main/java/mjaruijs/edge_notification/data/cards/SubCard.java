package mjaruijs.edge_notification.data.cards;

public class SubCard extends Card {

    private String item;
    private int color;

    SubCard(String item, int color) {
        super(item, null);
        this.item = item;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getItem() {
        return item;
    }

    public int getColor() {
        return color;
    }
}
