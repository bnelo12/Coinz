package elosoft.coinz.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;


import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;

import static elosoft.coinz.Utility.UI.MapBoxUtility.checkMapPermissions;
import static elosoft.coinz.Utility.UI.MapBoxUtility.requestMapPermissions;

public class MainActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextEmitter te = this.findViewById(R.id.tap_to_begin_emitter);
        te.emitText();

        final Intent transitionIntent = new Intent(this, LoginSignUpActivity.class);
        final View tapToContinueBoundingBox = findViewById(R.id.tap_to_view_bounding_box);

        tapToContinueBoundingBox.setOnClickListener(v -> {
            if (checkMapPermissions(getApplicationContext())) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1
                );
            } else {
                startActivity(transitionIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }
}
