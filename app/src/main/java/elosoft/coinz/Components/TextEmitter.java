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
    private int currentLine = 0;
    private boolean viewInitialized = false;
    private String displayText;
    private ArrayList<String> textBlocks;
    public ArrayList<String> userInputList = new ArrayList<>();
    private String currentUserInput = "";
    private Bitmap pointerBM;
    private Paint textPaint = new Paint();
    private int viewWidth = 0;
    private int maxLines = 0;
    public boolean showCursor = true;
    public boolean paused = false;
    public boolean completed = false;
    public boolean userInputMode = false;

    private Handler mainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable updateCursorPosition = new Runnable() {
        public void run() {
            if (!viewInitialized) {
                mainLoopHandler.post(this);
                return;
            }
            invalidate();
            cursorPosition += 1;
            if (cursorPosition >= textBlocks.get(currentLine).length()) {
                if (textBlocks.size() - 1 == currentLine) {
                    completed = true;
                    pause();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                    return;
                }
                if (textBlocks.get(currentLine + 1).equals("%p")) {
                    pause();
                    textBlocks.remove(currentLine + 1);
                    return;
                }
                if (textBlocks.get(currentLine + 1).equals("%i")) {
                    getUserInput();
                    textBlocks.remove(currentLine + 1);
                    return;
                }
                cursorPosition = 0;
                if (currentLine + 1 == maxLines) {
                    textBlocks.remove(0);
                } else {
                    currentLine += 1;
                }
            }
            mainLoopHandler.postDelayed(this, textRate);
        }
    };

    public Runnable onComplete = null;
    private  Runnable onWaitingForUserInput = null;

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
                viewWidth = right - left;
                maxLines = (bottom-top-20)/60;
                if (viewWidth <= 0) return;
                viewInitialized = true;
                textBlocks = preProcessText(displayText, currentLine);
                TextEmitter.this.removeOnLayoutChangeListener(this);
            }
         });
    }

    private ArrayList<String> preProcessText(String text, int currentLine) {
        if (textBlocks == null) {
            textBlocks = new ArrayList<>();
        }
        textBlocks.add("");
        String[] splitString = text.split("\\s+");
        int runningLineLengthCount = 0;
        for (String aSplitString : splitString) {
            if (aSplitString.equals("%n")) {
                runningLineLengthCount = 0;
                currentLine += 1;
                textBlocks.add("");
                continue;
            }
            if (aSplitString.equals("%p")) {
                runningLineLengthCount = 0;
                currentLine += 2;
                textBlocks.add("%p");
                textBlocks.add("");
                continue;
            }
            if (aSplitString.equals("%i")) {
                runningLineLengthCount = 0;
                currentLine += 2;
                textBlocks.add("%i");
                textBlocks.add("");
                continue;
            }
            int wordLength = (int) textPaint.measureText(aSplitString + " ");
            if (runningLineLengthCount + wordLength > viewWidth - 96) {
                runningLineLengthCount = 0;
                currentLine += 1;
                textBlocks.add("");
            }
            runningLineLengthCount += wordLength;
            textBlocks.set(currentLine, textBlocks.get(currentLine).concat(aSplitString + " "));
        }
        return textBlocks;
    }

    public void emitText() {
        updateCursorPosition.run();
    }

    public void appendText(String text) {
        completed = false;
        if (textBlocks == null) {
            displayText += text;
        } else {
            textBlocks = preProcessText(text, textBlocks.size());
            this.continueEmit();
        }
    }

    public void continueEmit() {
        if (paused && !completed) {
            paused = false;
            updateCursorPosition.run();
        }
    }

    public void getUserInput() {
        userInputMode = true;
        paused = true;
        if (onWaitingForUserInput != null) {
            onWaitingForUserInput.run();
        }
    }

    public void pause() {
        paused = true;
    }

    public void addString(String input) {
        if (userInputMode) {
            currentUserInput = currentUserInput.concat(input);
            invalidate();
        }
    }

    public void backSpace() {
        if (currentUserInput.length() > 0) {
            currentUserInput = currentUserInput.substring(0, currentUserInput.length() - 1);
            invalidate();
        }
    }

    public void addOnWaitingForUserInput(Runnable onWaitingForUserInput) {
        this.onWaitingForUserInput = onWaitingForUserInput;
    }

    public void addEightBitKeyBoard(EightBitRetroKeyBoard keyboard) {
        keyboard.addTextEmitter(this);
    }

    public String popUserInput() {
        userInputMode = false;
        textBlocks.set(currentLine, textBlocks.get(currentLine).concat(this.currentUserInput));
        String returnInput = currentUserInput;
        currentUserInput = "";
        cursorPosition += returnInput.length() - 1;
        return returnInput;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int line = 0; line < currentLine; line++) {
            canvas.drawText(textBlocks.get(line), 100, 60+60*line, textPaint);
        }
        if (textBlocks.get(currentLine).length() > 0) {
            if (userInputMode) {
                canvas.drawText(textBlocks.get(currentLine).concat(this.currentUserInput),100, 60 + 60 * currentLine, textPaint);
            } else {
                canvas.drawText(textBlocks.get(currentLine).substring(0, cursorPosition), 100, 60 + 60 * currentLine, textPaint);
            }
        }
        if (showCursor) {
            canvas.drawBitmap(pointerBM, 32, 20 + 60 * currentLine, textPaint);
        }
    }
}
