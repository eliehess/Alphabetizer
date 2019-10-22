public class Card implements Comparable<Card> {
    final String name;
    final int quantity;

    Card(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return quantity + " " + name;
    }

    @Override
    public int compareTo(Card otherCard) {
        return name.compareTo(otherCard.name);
    }
}
