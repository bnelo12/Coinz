package elosoft.coinz.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class BankView extends ConstraintLayout {
    public BankView(Context context) {
        super(context);
        inflate(context, R.layout.bank, this);
        TextEmitter welcomeMessage = findViewById(R.id.bank_welcome_message);
        welcomeMessage.emitText();
    }
}
