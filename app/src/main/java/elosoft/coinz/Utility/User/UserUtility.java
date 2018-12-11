package elosoft.coinz.Utility.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.EdAPI;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.Utility.Serialize.DeserializeCoin;

import static elosoft.coinz.Utility.Serialize.DeserializeExchangeRate.deserializeExchangeRateFromGeoJSON;
import static elosoft.coinz.Utility.Serialize.DeserializeCoin.deserializeCoinzFromGeoJSON;

public class UserUtility {
    public static void createNewUser(Context appContext, JSONObject geoJSON, String userName)
            throws DeserializeCoin.CoinzGeoJSONParseError {
        LocalStorageAPI.storeLoggedInUserName(appContext, userName);
        FireStoreAPI fs = FireStoreAPI.getInstance();
        HashMap<String, Coin> collectibleCoinz = deserializeCoinzFromGeoJSON(geoJSON);
        HashMap<String, Coin> collectedCoins = new HashMap<>();
        UserCoinzData userCoinzData = new UserCoinzData(0, new Date());
        ExchangeRate coinExchangeRates = deserializeExchangeRateFromGeoJSON(geoJSON);
        LocalStorageAPI.storeUserCoinzData(appContext, userCoinzData);
        LocalStorageAPI.storeExchangeRate(appContext, coinExchangeRates);
        fs.setUserCollectableCoinz(userName, collectibleCoinz);
        fs.setUserCollectedCoinz(userName, collectedCoins);
        fs.setUserData(userName, userCoinzData);
        fs.initTrades(userName);
    }

    public static void removeUserCoinz(Context appContext, ArrayList<Coin> userCoinz) {
        String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
        FireStoreAPI.getInstance().removeUserDepositedCoinz(currentUser, userCoinz);
    }

    public static void updateHighScores(UserCoinzData userCoinzData, String user) {
        double goldAmount = userCoinzData.getNumGOLD();
        FireStoreAPI.getInstance().getHighScores(task -> {
            HashMap<String, Object> scores = (HashMap<String, Object>)FireStoreAPI.getTaskResult(task);
            for (int i = 1; i <= scores.size(); i++) {
                HashMap<String, Object> score = (HashMap<String, Object>)scores.get(String.format("%d", i));
                Double num = Double.parseDouble((String)score.get("score"));
                String otherUser = (String)score.get("user");
                if (goldAmount >= num) {
                    FireStoreAPI.getInstance().setHighScore(i, user, goldAmount);
                    for (int j = i; j > 1; j--) {
                        score = (HashMap<String, Object>)scores.get(Integer.toString(j));
                        otherUser = (String)score.get("user");
                        num = Double.parseDouble((String)score.get("score"));
                        FireStoreAPI.getInstance().setHighScore(j-1, otherUser, num);
                    }
                    return;
                }
            }
        });
    }

    public static void updateToTodaysCoinz(Context appContext, UserCoinzData userCoinzData,
                                           String userName){
        EdAPI.getInstance().getCoinzGeoJSON(appContext, geoJSON -> {
            try {
                HashMap<String, Coin> collectibleCoinz = deserializeCoinzFromGeoJSON(geoJSON);
                ExchangeRate exchangeRate = deserializeExchangeRateFromGeoJSON(geoJSON);
                LocalStorageAPI.storeExchangeRate(appContext, exchangeRate);
                LocalStorageAPI.storeUserCoinzData(appContext, userCoinzData);
                FireStoreAPI.getInstance().setUserCollectableCoinz(userName, collectibleCoinz);
            } catch (DeserializeCoin.CoinzGeoJSONParseError coinzGeoJSONParseError) {
                Log.e("UserUtility", "Unable to parse coinz");
            }

        }, error -> {

        });

    }

    public static void syncLocalUserDataWithFireStore(Context appContext) {
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext);
        String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
        updateHighScores(userCoinzData, currentUser);
        FireStoreAPI.getInstance().setUserData(currentUser, userCoinzData);
    }
}
