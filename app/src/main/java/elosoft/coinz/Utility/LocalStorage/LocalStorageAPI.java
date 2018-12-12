package elosoft.coinz.Utility.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;

import static elosoft.coinz.Utility.User.UserUtility.syncLocalUserDataWithFireStore;

public class LocalStorageAPI {

    public static void storeLoggedInUserName(Context appContext, String userName) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putString("USER_NAME", userName);
        asyncCommit(editor, appContext);
    }

    public static String getLoggedInUserName(Context appContext) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        String userName = sharedPref.getString("USER_NAME", "UNKNOWN_USER");
        return userName;
    }


    public static void storeExchangeRate(Context appContext, ExchangeRate exchangeRate) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putFloat("EXCHANGE_RATE_QUID", (float)exchangeRate.ExchangeRateQUID);
        editor.putFloat("EXCHANGE_RATE_PENY", (float)exchangeRate.ExchangeRatePENY);
        editor.putFloat("EXCHANGE_RATE_DOLR", (float)exchangeRate.ExchangeRateDOLR);
        editor.putFloat("EXCHANGE_RATE_SHIL", (float)exchangeRate.ExchangeRateSHIL);
        asyncCommit(editor, appContext);
    }

    public static ExchangeRate readExchangeRate(Context appContext) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double exchangeRateQUID = (double)sharedPref.getFloat("EXCHANGE_RATE_QUID", 0);
        double exchangeRatePENY = (double)sharedPref.getFloat("EXCHANGE_RATE_PENY", 0);
        double exchangeRateDOLR = (double)sharedPref.getFloat("EXCHANGE_RATE_DOLR", 0);
        double exchangeRateSHIL = (double)sharedPref.getFloat("EXCHANGE_RATE_SHIL", 0);
        return new ExchangeRate(
                exchangeRateSHIL, exchangeRatePENY, exchangeRateDOLR, exchangeRateQUID
        );
    }

    public static void storeUserCoinzData(Context appContext, UserCoinzData userCoinzData) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        String date = DateFormat.getInstance().format(userCoinzData.getDateLastUpdated());
        editor.putString("USER_DATE_LAST_UPDATED", date);
        editor.putFloat("USER_NUM_GOLD", (float)userCoinzData.getNumGOLD());
        editor.putFloat("USER_NUM_DEPOSITED", (float)userCoinzData.getCoinzDepositedToday());
        editor.putFloat("USER_NUM_COLLECTED", (float)userCoinzData.getCoinzCollectedToday());
        asyncCommit(editor, appContext);
    }

    public static void updateLastUpdateDate(Context appContext, Date dateLastUpdated) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putString("USER_DATE_LAST_UPDATED", DateFormat.getInstance().format(dateLastUpdated));
        asyncCommit(editor, appContext);
    }

    public static void updateNumCoinzCollected(Context appContext, double coinzCollected) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putFloat("USER_NUM_COLLECTED",  (float)coinzCollected);
        asyncCommit(editor, appContext);
    }

    public static void updateNumGoldDeposited(Context appContext, double numGoldDeposited) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putFloat("USER_NUM_DEPOSITED",  (float)numGoldDeposited);
        asyncCommit(editor, appContext);
    }

    public static UserCoinzData readUserCoinzData(Context appContext) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double numGOLD = sharedPref.getFloat("USER_NUM_GOLD", 0.0f);
        double numCollected = sharedPref.getFloat("USER_NUM_COLLECTED", 0.0f);
        double numDeposited = sharedPref.getFloat("USER_NUM_DEPOSITED", 0.0f);
        try {
            Date dateLastUpdated = DateFormat.getInstance().parse(sharedPref.getString("USER_DATE_LAST_UPDATED", ""));
            return new UserCoinzData(numGOLD, dateLastUpdated, numCollected, numDeposited);
        } catch (ParseException e) {
            Log.e("LocalStorageAPI", "[readUserCoinzData] Unable to parse date");
            return null;
        }
    }

    public static void updateUserGOLD(Context appContext, double goldToAdd) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double numGOLD = sharedPref.getFloat("USER_NUM_GOLD", 0);

        numGOLD += goldToAdd;
        Editor editor = sharedPref.edit();
        editor.putFloat("USER_NUM_GOLD", (float)numGOLD);

        asyncCommit(editor, appContext);
    }

    private static void asyncCommit(Editor editor, Context appContext) {
        Runnable runnable = () -> {
            editor.commit();
            syncLocalUserDataWithFireStore(appContext);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
