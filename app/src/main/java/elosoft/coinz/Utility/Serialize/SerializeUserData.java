package elosoft.coinz.Utility.Serialize;

import java.util.HashMap;

import elosoft.coinz.Models.UserCoinzData;

public class SerializeUserData {
    public static HashMap<String, Object> serializeUserDataForFireStore(
            UserCoinzData userCoinzData) {
            HashMap<String, Object> userDataSerial = new HashMap<>(4);
            userDataSerial.put("num_PENY", userCoinzData.getNumPENY());
            userDataSerial.put("num_SHIL", userCoinzData.getNumSHIL());
            userDataSerial.put("num_DOLR", userCoinzData.getNumDOLR());
            userDataSerial.put("num_QUID", userCoinzData.getNumQUID());
            return userDataSerial;
    }
}
