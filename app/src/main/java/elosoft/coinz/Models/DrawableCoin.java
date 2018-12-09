package elosoft.coinz.Models;

public class DrawableCoin {
    private Coin coin;
    public boolean isSelected;

    public DrawableCoin(Coin coin, boolean isSelected) {
        this.coin = coin;
        this.isSelected = isSelected;
    }

    public Coin getCoin() {
        return coin;
    }

    public void toggleSelected() {
        this.isSelected = !isSelected;
    }
}
