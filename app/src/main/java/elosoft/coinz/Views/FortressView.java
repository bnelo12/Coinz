package elosoft.coinz.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class FortressView extends ConstraintLayout {
    public FortressView(Context context) {
        super(context);
        inflate(context, R.layout.fortress, this);
        TextEmitter welcomeMessage = findViewById(R.id.fortress_welcome_message);
        welcomeMessage.emitText();
    }
}
