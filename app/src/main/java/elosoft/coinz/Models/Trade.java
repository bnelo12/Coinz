package elosoft.coinz.Models;

import java.util.HashMap;

public class Trade {
    private final HashMap<String, Coin> userTradeCoinz;
    private final HashMap<String, Coin> partnerTradeCoinz;
    private final String user;
    private final String partner;

    public Trade(HashMap<String, Coin> userTradeCoinz, HashMap<String, Coin> partnerTradeCoinz,
          String user, String partner) {
        this.userTradeCoinz = userTradeCoinz;
        this.partnerTradeCoinz = partnerTradeCoinz;
        this.user = user;
        this.partner = partner;
    }

    public HashMap<String, Coin> getUserTradeCoinz() {
        return userTradeCoinz;
    }

    public HashMap<String, Coin> getPartnerTradeCoinz() {
        return partnerTradeCoinz;
    }

    public String getUser() {
        return user;
    }

    public String getPartner() {
        return partner;
    }
}
