package elosoft.coinz.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class TradingFloorView extends ConstraintLayout {
    public TradingFloorView(Context context) {
        super(context);
        inflate(context, R.layout.trading_floor, this);
        TextEmitter welcomeMessage = findViewById(R.id.trading_floor_welcome_message);
        welcomeMessage.emitText();
    }
}
