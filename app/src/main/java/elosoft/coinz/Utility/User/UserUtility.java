package elosoft.coinz.Utility.User;

import android.content.Context;

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

    public static void syncLocalUserDataWithFireStore(Context appContext) {
        ExchangeRate exchangeRate = LocalStorageAPI.readExchangeRate(appContext);
        UserCoinzData userCoinzData = LocalStorageAPI.readUserCoinzData(appContext, exchangeRate);
        String currentUser = LocalStorageAPI.getLoggedInUserName(appContext);
        FireStoreAPI.getInstance().setUserData(currentUser, userCoinzData);
    }
}
