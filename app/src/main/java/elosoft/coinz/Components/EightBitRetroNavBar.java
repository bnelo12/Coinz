package elosoft.coinz.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import elosoft.coinz.R;

public class EightBitRetroNavBar extends LinearLayout {

    private View currentView;

    public EightBitRetroNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.eight_bit_navbar, this);
    }

}
