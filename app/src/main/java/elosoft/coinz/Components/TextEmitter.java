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
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import elosoft.coinz.R;

public class TextEmitter extends View {
    private int textRate;
    private int cursorPosition = 0;
    private int currentLine = 0;
    private boolean viewInitialized = false;
    private String displayText;
    private ArrayList<String> textBlocks;
    private Bitmap pointerBM;
    private Paint textPaint = new Paint();

    private Handler mainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable updateCursorPosition = new Runnable() {
        public void run() {
            if (!viewInitialized) {
                mainLoopHandler.post(this);
                return;
            }
            invalidate();
            cursorPosition += 1;
            if (cursorPosition == textBlocks.get(currentLine).length()) {
                if (textBlocks.size() - 1 == currentLine) return;
                cursorPosition = 0;
                currentLine += 1;
            }
            mainLoopHandler.postDelayed(this, textRate);
        }
    };

    public TextEmitter(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray  styledAttr = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.TextEmitter, 0, 0);
        displayText = styledAttr.getString(R.styleable.TextEmitter_displayText);
        textRate = styledAttr.getInt(R.styleable.TextEmitter_displayRate, 100);

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

         this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int viewWidth = right - left;
                if (viewWidth <= 0) return;
                viewInitialized = true;
                textBlocks = preProcessText(displayText, viewWidth);
            }
        });
    }

    private ArrayList<String> preProcessText(String text, int viewWidth) {
        ArrayList<String> textBlocks = new ArrayList<String>();
        textBlocks.add("");
        String[] splitString = text.split("\\s+");
        int runningLineLengthCount = 0;
        int currentLine = 0;
        for (int i = 0; i < splitString.length; i++) {
            int wordLength = (int) textPaint.measureText(splitString[i] + " ");
            if (runningLineLengthCount + wordLength > viewWidth - 96) {
                runningLineLengthCount = 0;
                currentLine += 1;
                textBlocks.add("");
            }
            runningLineLengthCount += wordLength;
            textBlocks.set(currentLine, textBlocks.get(currentLine).concat(splitString[i] + " "));
        }
        return textBlocks;
    }

    public void emitText() {
        updateCursorPosition.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int line = 0; line < currentLine; line++) {
            canvas.drawText(textBlocks.get(line), 100, 60+60*line, textPaint);
        }
        canvas.drawText(textBlocks.get(currentLine).substring(0, cursorPosition), 100, 60+60*currentLine, textPaint);
        canvas.drawBitmap(pointerBM,32,20+60*currentLine, textPaint);
    }
}
