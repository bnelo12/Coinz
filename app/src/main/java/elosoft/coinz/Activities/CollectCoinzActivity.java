package elosoft.coinz.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class CollectCoinzActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_coinz);
        TextEmitter te = findViewById(R.id.collect_coinz_emitter);
        te.emitText();
        int numberOfCoinzCollected = getIntent().getIntExtra("NUMBER_OF_COINZ", 0);
        String textToAdd = "My lord, we continue to increase our riches . . . %n You have just collected " + numberOfCoinzCollected + " coinz! %n %n Tap to return . . .";
        te.appendText(textToAdd);
        te.onComplete = new Runnable() {
            @Override
            public void run() {
                te.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        };
    }
}
