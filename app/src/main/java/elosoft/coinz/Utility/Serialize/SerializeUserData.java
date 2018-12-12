package elosoft.coinz.Utility.Serialize;

import java.text.DateFormat;
import java.util.HashMap;

import elosoft.coinz.Models.UserCoinzData;

public class SerializeUserData {
    public static HashMap<String, Object> serializeUserDataForFireStore(
            UserCoinzData userCoinzData) {
        HashMap<String, Object> userDataSerial = new HashMap<>(4);
        userDataSerial.put("num_GOLD", userCoinzData.getNumGOLD());
        userDataSerial.put("num_Collected", userCoinzData.getCoinzCollectedToday());
        userDataSerial.put("num_Deposited", userCoinzData.getCoinzDepositedToday());
        String date = DateFormat.getInstance().format(userCoinzData.getDateLastUpdated());
        userDataSerial.put("dateLastUpdated", date);
        return userDataSerial;
    }
}
