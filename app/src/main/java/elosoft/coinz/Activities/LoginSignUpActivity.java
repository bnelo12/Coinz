package elosoft.coinz.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;

import elosoft.coinz.Components.EightBitRetroKeyBoard;
import elosoft.coinz.Models.ExchangeRate;
import elosoft.coinz.Models.UserCoinzData;
import elosoft.coinz.Utility.LocalStorage.LocalStorageAPI;
import elosoft.coinz.Utility.Network.EdAPI;
import elosoft.coinz.Utility.Serialize.DeserializeCoin;
import elosoft.coinz.Utility.Network.FireStoreAPI;
import elosoft.coinz.Components.TextEmitter;
import elosoft.coinz.R;
import elosoft.coinz.Utility.User.UserUtility;

import static elosoft.coinz.Utility.Serialize.DeserializeExchangeRate.deserializeExchangeRateFromGeoJSON;
import static elosoft.coinz.Utility.Serialize.DeserializeUserData.deserializeUserDataFromFireStore;
import static elosoft.coinz.Utility.UI.TradeUtility.slideKeyboardDown;
import static elosoft.coinz.Utility.UI.TradeUtility.slideKeyboardUp;

public class LoginSignUpActivity extends Activity {
    private TextEmitter emitter;
    private EightBitRetroKeyBoard keyboard;

    private int inputStage = 0;
    private HashMap<String, String> loginData = new HashMap<>();
    private HashMap<String, String> signUpData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initButtons();
        initKeyboard();
        initEmitter();
        loadCoinzFromFireStore();
    }

    private void loadCoinzFromFireStore() {
        emitter.appendText(" %n %n Welcome to the world of EDALOT!");
        // Attempt to load user data
        String currentUser = LocalStorageAPI.getLoggedInUserName(getApplicationContext());
        if (currentUser.equals("UNKNOWN_USER")) {
            emitter.appendText(" %n %n We can't seem to find you in our records. Please login or sign up.");
            emitter.onComplete = () -> {
                slideButtonLayoutUp();
                emitter.onComplete = null;
            };
        } else {
            emitter.appendText(" %n Logging in . . .");
            emitter.onComplete = () -> loginUser(currentUser);
        }
    }

    private void createUser() {
        String userName = signUpData.get("user");
        String password = signUpData.get("password");
        FireStoreAPI.getInstance().setUser(userName, password);
        emitter.appendText(" %n Connecting to Server . . .");
        EdAPI.getInstance().getCoinzGeoJSON(getApplicationContext(),
                geoJSON -> {
                    try {
                        UserUtility.createNewUser(getApplicationContext(), geoJSON, userName);
                        emitter.appendText(" %n %n Logging in . . .");
                        emitter.onComplete = () -> {
                            final Intent transitonIntent = new Intent(LoginSignUpActivity.this, CoinzNavigationActivity.class);
                            startActivity(transitonIntent);
                        };
                    }
                    catch (DeserializeCoin.CoinzGeoJSONParseError e) {
                        Log.e("Error while parsing GeoJSON", e.getErrorMessage());
                    }
                },
                error -> emitter.appendText(" %n CONNECTION ERROR: Trying again."));
    }

    private void initButtons() {
        Button signUpButton = findViewById(R.id.sign_up_button);
        Button loginButton = findViewById(R.id.login_button);

        signUpButton.setOnClickListener(v -> {
            slideButtonLayoutDown();
            inputStage = 200;
            emitQuery();
        });
        loginButton.setOnClickListener(v -> {
            slideButtonLayoutDown();
            inputStage = 100;
            emitQuery();
        });
    }

    private void initKeyboard() {
        keyboard = findViewById(R.id.keyboard);
        keyboard.addOnDoneKeyPressedListener(v -> {
            if (emitter.userInputMode) {
                String input = emitter.popUserInput();
                if (inputStage == 100) {
                    loginData.put("user", input);
                    inputStage = 101;
                    emitQuery();
                } else if (inputStage == 101) {
                    loginData.put("password", input);
                    inputStage = 102;
                    emitQuery();
                } else if (inputStage == 200) {
                    if (input.length() == 0) {
                        emitter.appendText("You must select a username. %n %n username: %i");
                        return;
                    }
                    FireStoreAPI.getInstance().getUser(input, task -> {
                        HashMap<String, Object> taskResult = (HashMap<String, Object>) FireStoreAPI
                                .getTaskResult(task);
                        if (taskResult == null) {
                            signUpData.put("user", input);
                            inputStage = 201;
                            emitQuery();
                        } else {
                            emitter.appendText("Username already taken. %n %n username: %i");
                        }
                    });
                } else if (inputStage == 201) {
                    if (input.length() <= 8) {
                        emitter.appendText("Passwords must be greater than 8 characters.");
                        emitQuery();
                    } else {
                        signUpData.put("password", input);
                        inputStage = 202;
                        emitQuery();
                    }
                } else if (inputStage == 202) {
                    if (!input.equals(signUpData.get("password"))) {
                        emitter.appendText("Passwords do not match. Please enter a password. %n %n password: %i");
                        inputStage = 201;
                    } else {
                        inputStage = 203;
                        emitQuery();
                    }
                }
            }
        });
    }

    private void emitQuery(){
        if (inputStage == 100) {
            emitter.appendText("%n username: %i");
        }
        else if (inputStage == 101) {
            emitter.appendText("password: %i");
        }
        else if (inputStage == 102) {
            emitter.appendText("Looking up user . . .");
            slideKeyboardDown(getApplicationContext(), keyboard);
            String user = loginData.get("user");
            String password = loginData.get("password");
            FireStoreAPI.getInstance().getUser(user, task -> {
                        HashMap<String, Object> taskResult = (HashMap<String, Object>) FireStoreAPI
                                .getTaskResult(task);
                        if (taskResult == null) {
                            emitter.appendText("User not found!");
                            inputStage = 100;
                            emitQuery();
                        } else if (!password.equals(taskResult.get("password"))) {
                            emitter.appendText("Invalid password!");
                            inputStage = 100;
                            emitQuery();
                        } else {
                            emitter.appendText(String.format("Logging in user: %s", user));
                            emitter.onComplete = () -> loginUser(user);
                        }
                    });
        } else if (inputStage == 200) {
            emitter.appendText(getString(R.string.sign_up_text_1));
        } else if (inputStage == 201) {
            emitter.appendText("And what will be thy password on thy quest. %n %n password: %i");
        } else if (inputStage == 202) {
            emitter.appendText("Very good %n Can you please confirm your password. %n %n password: %i");
        } else if (inputStage == 203) {
            emitter.appendText("Great. We'll finalize your account creation and you will be ready to start. %n %n Loading . . .");
            slideKeyboardDown(getApplicationContext(), keyboard);
            createUser();
        }
    }

    private void loginUser(String userName) {
        FireStoreAPI.getInstance().getUserData(userName, task -> {
            HashMap<String, Object> taskResult = (HashMap<String, Object>) FireStoreAPI
                    .getTaskResult(task);
            if (taskResult == null) {
                emitter.appendText("User data corrupted!");
                inputStage = 100;
                emitQuery();
            } else {
                EdAPI.getInstance().getCoinzGeoJSON(getApplicationContext(),
                        geoJSON -> {
                            try {
                                ExchangeRate exchangeRate = deserializeExchangeRateFromGeoJSON(geoJSON);
                                UserCoinzData userCoinzData = deserializeUserDataFromFireStore(taskResult);
                                LocalStorageAPI.storeUserCoinzData(getApplicationContext(), userCoinzData);
                                LocalStorageAPI.storeExchangeRate(getApplicationContext(), exchangeRate);
                                LocalStorageAPI.storeLoggedInUserName(getApplicationContext(), userName);

                                final Intent transitionIntent = new Intent(LoginSignUpActivity.this, CoinzNavigationActivity.class);
                                startActivity(transitionIntent);
                            }
                            catch (DeserializeCoin.CoinzGeoJSONParseError e) {
                                Log.e("Error while parsing GeoJSON", e.getErrorMessage());
                                emitter.appendText("ERROR: Please try again later");
                            }
                        },
                        error -> emitter.appendText(" %n CONNECTION ERROR: Trying again."));
            }
        });
    }

    private void initEmitter() {
        emitter = findViewById(R.id.emitter);
        emitter.addEightBitKeyBoard(keyboard);
        emitter.addOnWaitingForUserInput(() -> slideKeyboardUp(getApplicationContext(), keyboard));
        emitter.emitText();
    }


    private void slideButtonLayoutUp() {
        LinearLayout layout = findViewById(R.id.button_layout);
        layout.setVisibility(View.VISIBLE);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        layout.startAnimation(slide_up);
    }

    private void slideButtonLayoutDown() {
        LinearLayout layout = findViewById(R.id.button_layout);
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        layout.setVisibility(View.GONE);
        layout.startAnimation(slide_down);
    }

    @Override
    public void onBackPressed() {
        /* Do nothing */
    }
}
