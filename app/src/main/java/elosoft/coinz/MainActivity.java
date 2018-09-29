package elosoft.coinz;

import android.app.Activity;
import android.os.Bundle;

import elosoft.coinz.Components.TextEmitter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextEmitter te = this.findViewById(R.id.tap_to_begin_emitter);
        te.emitText();
    }
}
