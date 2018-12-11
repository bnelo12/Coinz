package elosoft.coinz.Models;

import java.util.HashMap;

public class Trade {
    private final HashMap<String, Coin> userTradeCoinz;
    private final HashMap<String, Coin> partnerTradeCoinz;
    private final String user;
    private final String partner;
    private final ExchangeRate exchangeRate;

    private final String id;

    private String status;

    public Trade(HashMap<String, Coin> userTradeCoinz, HashMap<String, Coin> partnerTradeCoinz,
          String user, String partner, String status, ExchangeRate exchangeRate, String id) {
        this.userTradeCoinz = userTradeCoinz;
        this.partnerTradeCoinz = partnerTradeCoinz;
        this.user = user;
        this.partner = partner;
        this.status = status;
        this.exchangeRate = exchangeRate;
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public String getId() {
        return id;
    }
}
