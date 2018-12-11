package elosoft.coinz.Utility.Serialize;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;

import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;

public class DeserializeUserData {
    public static UserCoinzData deserializeUserDataFromFireStore(
            HashMap<String, Object> userDataSerial) {
        try {
            return new UserCoinzData(
                    (double) userDataSerial.get("num_GOLD"),
                    DateFormat.getInstance().parse((String)userDataSerial.get("dateLastUpdated"))
            );
        } catch (ParseException e) {
            Log.e("DeserializeUserData", "[deserializeUserDataFromFireStore] Unable to parse date");
            return new UserCoinzData((double) userDataSerial.get("num_GOLD"), null);
        }
    }
}
