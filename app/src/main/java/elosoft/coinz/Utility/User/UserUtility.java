package elosoft.coinz.Utility.User;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.Utility.Serialize.DeserializeCoin;

import static elosoft.coinz.Utility.Serialize.DeserializeExchangeRate.deserializeExchangeRateFromGeoJSON;
import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromGeoJSON;

public class UserUtility {
    public static void createNewUser(Context appContext, JSONObject geoJSON, String userName)
            throws DeserializeCoin.CoinzGeoJSONParseError {
        FireStoreAPI fs = FireStoreAPI.getInstance();
        HashMap<String, Coin> collectibleCoinz = deserializeCoinzFromGeoJSON(geoJSON);
        HashMap<String, Coin> collectedCoins = new HashMap<>();
        ExchangeRate coinExchangeRates = deserializeExchangeRateFromGeoJSON(geoJSON);
        UserCoinzData userCoinzData = new UserCoinzData(collectedCoins, coinExchangeRates, 0);
        LocalStorageAPI.storeExchangeRate(appContext, coinExchangeRates);
        LocalStorageAPI.storeUserCoinzData(appContext, userCoinzData);
        fs.setUserCollectableCoinz(userName, collectibleCoinz);
        fs.setUserCollectedCoinz(userName, collectedCoins);
        fs.setUserData(userName, userCoinzData);
        fs.initTrades(userName);
        LocalStorageAPI.storeLoggedInUserName(appContext, userName);
    }

    public static void removeUserCoinz(Context appContext, ArrayList<Coin> userCoinz) {
        String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
        LocalStorageAPI.removeUserCoinzData(appContext, userCoinz);
        FireStoreAPI.getInstance().removeUserDepositedCoinz(currentUser, userCoinz);
    }

    public static void updateHighScores(UserCoinzData userCoinzData, String user) {
        double goldAmount = userCoinzData.getNumGOLD();
        FireStoreAPI.getInstance().getHighScores(task -> {
            HashMap<String, Object> scores = (HashMap<String, Object>)FireStoreAPI.getTaskResult(task);
            for (int i = 1; i <= scores.size(); i++) {
                HashMap<String, Object> score = (HashMap<String, Object>)scores.get(String.format("%d", i));
                final Double num = Double.parseDouble((String)score.get("score"));
                if (goldAmount > num) {
                    FireStoreAPI.getInstance().setHighScore(i, user, goldAmount);
                    return;
                }
            }
        });
    }

    public static void syncLocalUserDataWithFireStore(Context appContext) {
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(appContext);
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext, exchangeRate);
        String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
        updateHighScores(userCoinzData, currentUser);
        FireStoreAPI.getInstance().setUserData(currentUser, userCoinzData);
    }
}
