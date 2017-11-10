package mjaruijs.edge_notification.data.cards;

public class BlackCard extends Card {

    private String item;

    BlackCard(String item) {
        super(item, null);
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
