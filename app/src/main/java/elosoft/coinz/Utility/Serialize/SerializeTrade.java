package elosoft.coinz.Utility.Serialize;

import java.io.ObjectStreamClass;
import java.util.HashMap;

import elosoft.coinz.Models.Trade;

import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinzForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeExchangeRate.serializeExchangeRateForFirestore;

public class SerializeTrade {
    public static HashMap<String, Object> serializeTradeForFirestore(Trade trade) {
        HashMap<String, Object> tradeSerial = new HashMap<>();
        tradeSerial.put("user", trade.getUser());
        tradeSerial.put("partner", trade.getPartner());
        tradeSerial.put("user_trade_coinz", serializeCoinzForFirestore(trade.getUserTradeCoinz()));
        tradeSerial.put("partner_trade_coinz", serializeCoinzForFirestore(trade.getPartnerTradeCoinz()));
        tradeSerial.put("status", trade.getStatus());
        tradeSerial.put("exchange_rate", serializeExchangeRateForFirestore(trade.getExchangeRate()));
        tradeSerial.put("id", trade.getId());
        return tradeSerial;
    }
}
