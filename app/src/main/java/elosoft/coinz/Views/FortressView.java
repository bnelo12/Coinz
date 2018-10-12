package elosoft.coinz.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import elosoft.coinz.R;

public class FortressView extends RelativeLayout {
    public FortressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.fortress, this);
    }
}
