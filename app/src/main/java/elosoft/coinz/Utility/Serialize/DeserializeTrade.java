package elosoft.coinz.Utility.Serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.Models.Trade;

import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromFireStore;
import static elosoft.coinz.Utility.Serialize.DeserializeExchangeRate.deserializeExchangeRateFromFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinzForFirestore;

public class DeserializeTrade {
    private static Trade deserializeTradeFromFirestore(Map<String, Object> serialTrade) {
        return new Trade(
                deserializeCoinzFromFireStore((HashMap<String, Object>) serialTrade.get("user_trade_coinz")),
                deserializeCoinzFromFireStore((HashMap<String, Object>) serialTrade.get("partner_trade_coinz")),
                (String)serialTrade.get("user"),
                (String)serialTrade.get("partner"),
                (String)serialTrade.get("status"),
                deserializeExchangeRateFromFirestore((Map<String, Object>)serialTrade.get("exchange_rate"))
        );
    }

    public static ArrayList<Trade> deserializeTradesFromFirestore(Map<String, Object> serialTrades) {
        ArrayList<Trade> trades = new ArrayList<>();
        for (Object serialTrade : serialTrades.values()) {
            trades.add(deserializeTradeFromFirestore((Map<String, Object>) serialTrade));
        }
        return trades;
    }
}
