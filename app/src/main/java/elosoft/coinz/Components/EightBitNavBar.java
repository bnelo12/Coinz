package elosoft.coinz.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import elosoft.coinz.R;

public class EightBitNavBar extends LinearLayout {
    public EightBitNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.eight_bit_navbar, this);
    }
}
