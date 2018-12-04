package elosoft.coinz.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;


import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Slide transition = new Slide();
        transition.setSlideEdge(Gravity.BOTTOM);
        getWindow().setExitTransition(new Slide());
        TextEmitter te = this.findViewById(R.id.tap_to_begin_emitter);
        te.emitText();

        final Intent transitonIntent = new Intent(this, LoadingActivity.class);
        final View tapToContinueBoundingBox = findViewById(R.id.tap_to_view_bounding_box);

        tapToContinueBoundingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(transitonIntent);
            }
        });
    }
}
