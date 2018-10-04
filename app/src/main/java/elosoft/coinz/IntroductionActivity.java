package elosoft.coinz;

import android.app.Activity;
import android.os.Bundle;

import elosoft.coinz.Components.EightBitKeyBoard;
import elosoft.coinz.Components.TextEmitter;

public class IntroductionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        TextEmitter te = this.findViewById(R.id.sign_up_emitter);
        EightBitKeyBoard ebkb = this.findViewById(R.id.keyboard);
        te.addEightBitKeyBoard(ebkb);
        te.emitText();
    }
}