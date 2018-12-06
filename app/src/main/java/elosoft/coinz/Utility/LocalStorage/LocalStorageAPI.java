package elosoft.coinz.Utility.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;

public class LocalStorageAPI {

    public static void storeExchangeRate(Context appContext, ExchangeRate exchangeRate) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putFloat("EXCHANGE_RATE_QUID", (float)exchangeRate.ExchangeRateQUID);
        editor.putFloat("EXCHANGE_RATE_PENY", (float)exchangeRate.ExchangeRatePENY);
        editor.putFloat("EXCHANGE_RATE_DOLR", (float)exchangeRate.ExchangeRateDOLR);
        editor.putFloat("EXCHANGE_RATE_SHIL", (float)exchangeRate.ExchangeRateSHIL);
        asyncCommit(editor);
    }

    public static ExchangeRate readExchangeRate(Context appContext) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double exchangeRateQUID = sharedPref.getFloat("EXCHANGE_RATE_QUID", 0);
        double exchangeRatePENY = sharedPref.getFloat("EXCHANGE_RATE_PENY", 0);
        double exchangeRateDOLR = sharedPref.getFloat("EXCHANGE_RATE_DOLR", 0);
        double exchangeRateSHIL = sharedPref.getFloat("EXCHANGE_RATE_SHIL", 0);
        return new ExchangeRate(
                exchangeRateSHIL, exchangeRatePENY, exchangeRateDOLR, exchangeRateQUID
        );
    }

    public static void storeUserCoinzData(Context appContext, UserCoinzData userCoinzData) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        Editor editor = sharedPref.edit();
        editor.putFloat("USER_NUM_QUID", (float)userCoinzData.getNumQUID());
        editor.putFloat("USER_NUM_PENY", (float)userCoinzData.getNumPENY());
        editor.putFloat("USER_NUM_DOLR", (float)userCoinzData.getNumDOLR());
        editor.putFloat("USER_NUM_SHIL", (float)userCoinzData.getNumSHIL());
        asyncCommit(editor);
    }

    public static UserCoinzData readUserCoinzData(Context appContext, ExchangeRate exchangeRate) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double numQUID = sharedPref.getFloat("USER_NUM_QUID", 0);
        double numPENY = sharedPref.getFloat("USER_NUM_PENY", 0);
        double numDOLR = sharedPref.getFloat("USER_NUM_DOLR", 0);
        double numSHIL = sharedPref.getFloat("USER_NUM_SHIL", 0);
        return new UserCoinzData(numPENY,  numSHIL,  numDOLR, numQUID,  exchangeRate);
    }

    public static void updateUserCoinzData(Context appContext, ArrayList<Coin> coinzToAdd) {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(appContext);
        double numQUID = sharedPref.getFloat("USER_NUM_QUID", 0);
        double numPENY = sharedPref.getFloat("USER_NUM_PENY", 0);
        double numDOLR = sharedPref.getFloat("USER_NUM_DOLR", 0);
        double numSHIL = sharedPref.getFloat("USER_NUM_SHIL", 0);

        for (Coin c : coinzToAdd) {
            switch (c.type) {
                case DOLR: numDOLR += c.value; break;
                case QUID: numQUID += c.value; break;
                case SHIL: numSHIL += c.value; break;
                case PENY: numPENY += c.value; break;
                default: break;
            }
        }

        Editor editor = sharedPref.edit();
        editor.putFloat("USER_NUM_QUID", (float)numQUID);
        editor.putFloat("USER_NUM_PENY", (float)numPENY);
        editor.putFloat("USER_NUM_DOLR", (float)numDOLR);
        editor.putFloat("USER_NUM_SHIL", (float)numSHIL);

        asyncCommit(editor);
    }

    private static void asyncCommit(Editor editor) {
        Runnable runnable = () -> editor.commit();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
