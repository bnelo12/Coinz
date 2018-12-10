package elosoft.coinz.Utility.UI;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.HashMap;

import elosoft.coinz.Components.EightBitRetroKeyBoard;
import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.DrawableCoin;
import elosoft.coinz.R;

public class TradeUtility {

    private static boolean hasSelectedCoin(ArrayList<DrawableCoin> coinz) {
        for (DrawableCoin coin : coinz) {
            if (coin.isSelected) return true;
        }
        return false;
    }

    public static HashMap<String, Coin> getSelectedCoin(ArrayList<DrawableCoin> coinz) {
        HashMap<String, Coin> selectedCoinz = new HashMap<>();
        for (DrawableCoin coin : coinz) {
            if (coin.isSelected) selectedCoinz.put(coin.getCoin().id, coin.getCoin());
        }
        return selectedCoinz;
    }

    public static boolean tradePossible(ArrayList<DrawableCoin> userDrawableCoinz,
                                         ArrayList<DrawableCoin> partnerDrawableCoinz) {
        if (userDrawableCoinz != null && partnerDrawableCoinz != null) {
            return hasSelectedCoin(userDrawableCoinz) && hasSelectedCoin(partnerDrawableCoinz);
        }
        return false;
    }

    public static void slideKeyboardUp(Context appContext, EightBitRetroKeyBoard keyboard) {
        if (keyboard.getVisibility() != View.VISIBLE) {
            keyboard.setVisibility(View.VISIBLE);
            Animation slide_up = AnimationUtils.loadAnimation(appContext, R.anim.slide_up);
            keyboard.startAnimation(slide_up);
        }
    }

    public static void slideKeyboardDown(Context appContext, EightBitRetroKeyBoard keyboard) {
        if (keyboard.getVisibility() != View.GONE) {
            keyboard.setVisibility(View.GONE);
            Animation slide_down = AnimationUtils.loadAnimation(appContext, R.anim.slide_down);
            keyboard.startAnimation(slide_down);
        }
    }
}
