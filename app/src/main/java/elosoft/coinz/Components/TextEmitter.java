package elosoft.coinz.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import elosoft.coinz.R;

public class TextEmitter extends View {
    private int textRate;
    private int cursorPosition = 0;
    private int line = 0;
    private String displayText;
    private ArrayList<ArrayList<String>> textBlocks;
    private Bitmap pointerBM;
    private Paint textPaint = new Paint();

    private Handler mainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable updateCursorPosition = new Runnable() {
        public void run() {
            cursorPosition += 1;
            invalidate();
            if (cursorPosition != displayText.length()) {
                mainLoopHandler.postDelayed(this, 50);
            }
        }
    };

    public TextEmitter(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray  styledAttr = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.TextEmitter, 0, 0);
        displayText = styledAttr.getString(R.styleable.TextEmitter_displayText);

        // Get correct font
        Typeface tf = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "font/eight_bit_body.otf");

        // Load Bitmap of pointer
        pointerBM = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
                R.drawable.pointer_icon);

        // Style text Paint
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(tf);
        textPaint.setTextSize(50);
    }

    private ArrayList<ArrayList<String>> preProcessText(String text, int viewWidth) {
        ArrayList<ArrayList<String>> textBlocks = new ArrayList<ArrayList<String>>();
        String[] splitString = text.split("\\s+");
        int runningLineCount = 0;
        return textBlocks;
    }

    public void emitText() {
        updateCursorPosition.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(pointerBM,32,20,textPaint);
        canvas.drawText(displayText.substring(0, this.cursorPosition), 90, 60, textPaint);
    }
}
