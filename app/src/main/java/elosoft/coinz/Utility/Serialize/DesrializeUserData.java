package elosoft.coinz.Utility.Serialize;

import java.util.HashMap;

import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;

public class DesrializeUserData {
    public static UserCoinzData deserializeUserDataFromFireStore(
            HashMap<String, Object> userDataSerial, ExchangeRate exchangeRate) {
        return new UserCoinzData(
                (double)userDataSerial.get("num_PENY"),
                (double)userDataSerial.get("num_SHIL"),
                (double)userDataSerial.get("num_DOLR"),
                (double)userDataSerial.get("num_QUID"),
                exchangeRate,
                (double)userDataSerial.get("num_GOLD")
        );
    }
}
