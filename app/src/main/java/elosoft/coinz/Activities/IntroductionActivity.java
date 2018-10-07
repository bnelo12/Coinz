package elosoft.coinz.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import elosoft.coinz.Components.EightBitKeyBoard;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class IntroductionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        TextEmitter te = this.findViewById(R.id.sign_up_emitter);
        EightBitKeyBoard ebkb = this.findViewById(R.id.keyboard);
        te.addEightBitKeyBoard(ebkb);
        te.emitText();
        te.onComplete = new Runnable() {
            @Override
            public void run() {
                final Intent transitonIntent = new Intent(IntroductionActivity.this, IntroductionActivity.class);
                final ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(IntroductionActivity.this);
                startActivity(transitonIntent, activityOptions.toBundle());
            }
        };
    }

    @Override
    public void onBackPressed() {
        /* Do nothing */
    }
}