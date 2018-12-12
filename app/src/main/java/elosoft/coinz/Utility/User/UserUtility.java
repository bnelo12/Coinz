package elosoft.coinz.Utility.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
        UserCoinzData userCoinzData = new UserCoinzData(0, new Date(), 0, 0);
        ExchangeRate coinExchangeRates = deserializeExchangeRateFromGeoJSON(geoJSON);
        LocalStorageAPI.storeUserCoinzData(appContext, userCoinzData);
        LocalStorageAPI.storeExchangeRate(appContext, coinExchangeRates);
        fs.setUserCollectableCoinz(userName, collectibleCoinz);
        fs.setUserCollectedCoinz(userName, collectedCoins);
        fs.setUserData(userName, userCoinzData);
        fs.initTrades(userName);
    }

    private static class Score {
        public String user;
        public Double score;
        Score(String user, Double score) {
            this.user = user;
            this.score = score;
        }
    }

    public static void updateHighScores(UserCoinzData userCoinzData, String user) {
        FireStoreAPI.getInstance().getHighScores(task -> {
            HashMap<String, Object> scores = (HashMap<String, Object>) FireStoreAPI.getTaskResult(task);
            ArrayList<Score> userTable = new ArrayList<>();

            boolean userContained = false;

            for (String key : scores.keySet()) {
                HashMap<String, String> score = (HashMap<String, String>) scores.get(key);
                String parseUser = score.get("user");
                double parseNum = Double.parseDouble(score.get("score"));
                if (parseUser.equals(user) && userCoinzData.getNumGOLD() < parseNum) {
                    return;
                } else if (parseUser.equals(user)) {
                    parseNum = userCoinzData.getNumGOLD();
                    userTable.add(new Score(parseUser, parseNum));
                    userContained = true;
                } else {
                    userTable.add(new Score(parseUser, parseNum));
                }
            }

            if (!userContained) {
                userTable.add(new Score(user, userCoinzData.getNumGOLD()));
            }

            userTable.sort(Comparator.comparingDouble(o -> -o.score));
            for (int i = 0; i < 3; i++) {
                FireStoreAPI.getInstance().setHighScore(i+1,
                        userTable.get(i).user, userTable.get(i).score);
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
                userCoinzData.setCoinzCollectedToday(0);
                userCoinzData.setCoinzDepositedToday(0);
                userCoinzData.setDateLastUpdated(new Date());
                LocalStorageAPI.storeUserCoinzData(appContext, userCoinzData);
            } catch (DeserializeCoin.CoinzGeoJSONParseError coinzGeoJSONParseError) {
                Log.e("UserUtility", "Unable to parse coinz");
            }

        }, error -> {

        });

    }

    public static void syncLocalUserDataWithFireStore(Context appContext) {
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext);
        if (userCoinzData != null) {
            String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
            updateHighScores(userCoinzData, currentUser);
            FireStoreAPI.getInstance().setUserData(currentUser, userCoinzData);
        }
    }
}
