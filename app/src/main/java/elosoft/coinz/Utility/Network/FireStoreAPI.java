package elosoft.coinz.Utility.Network;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.Trade;
import elosoft.coinz.Models.UserCoinzData;

import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinzForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeTrade.serializeTradeForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeUserData.serializeUserDataForFireStore;

public class FireStoreAPI {
    private static volatile FireStoreAPI instance;
    private static final Object mutex = new Object();
    private FirebaseFirestore db;

    private FireStoreAPI() {
        db = FirebaseFirestore.getInstance();
    }

    public static FireStoreAPI getInstance() {
        FireStoreAPI result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) instance = result = new FireStoreAPI();
            }
        }
        return result;
    }

    public static Map<String, Object> getTaskResult(Task<DocumentSnapshot> task) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
            return document.getData();
        } else {
            return null;
        }
    }

    public void getUserCollectableCoinz(
            String user, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("collectable_coinz")
                .document(user)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getUserData(
            String userName, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("users_data")
                .document(userName)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getUser(
            String userName, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("users")
                .document(userName)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void setUser(String userName, String password) {
        CollectionReference c = db.collection("users");
        HashMap<String, String> user = new HashMap<>(1);
        user.put("password", password);
        c.document(userName).set(user);
    }

    public void setUserData(String userName, UserCoinzData userCoinzData) {
        CollectionReference c = db.collection("users_data");
        c.document(userName).set(serializeUserDataForFireStore(userCoinzData));
    }

    public void setUserCollectableCoinz(String user, HashMap<String, Coin> coinz) {
        CollectionReference c = db.collection("collectable_coinz");
        c.document(user).set(serializeCoinzForFirestore(coinz));
    }

    public void addTrade(Trade trade) {
        DocumentReference docRefPending = db.collection("pending_trades").document(trade.getPartner());
        DocumentReference docRefSent = db.collection("sent_trades").document(trade.getUser());

        String tradeId = trade.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put(tradeId, serializeTradeForFirestore(trade));
        docRefPending.update(updates);
        docRefSent.update(updates);
    }

    public void removePendingTrade(Trade trade) {
        DocumentReference docRefPending = db.collection("pending_trades").document(trade.getPartner());

        String tradeId = trade.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put(tradeId, FieldValue.delete());
        docRefPending.update(updates);
    }

    public void removeSentTrade(Trade trade) {
        DocumentReference docRefSent = db.collection("sent_trades").document(trade.getUser());

        String tradeId = trade.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put(tradeId, FieldValue.delete());
        docRefSent.update(updates);
    }

    public void getPendingUserTrades(String user, OnCompleteListener onCompleteListener) {
        db.collection("sent_trades")
                .document(user)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getReceivedUserTrades(String user, OnCompleteListener onCompleteListener) {
        db.collection("pending_trades")
                .document(user)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void initTrades(String user) {
        DocumentReference docRefPending = db.collection("pending_trades").document(user);
        DocumentReference docRefSent = db.collection("sent_trades").document(user);
        docRefPending.set(new HashMap<>());
        docRefSent.set(new HashMap<>());
    }

    public void setUserCollectedCoinz(String user, HashMap<String, Coin> coinz) {
        CollectionReference c = db.collection("collected_coinz");
        c.document(user).set(serializeCoinzForFirestore(coinz));
    }

    public void getUserCollectedCoinz(
            String user, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("collected_coinz")
                .document(user)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void addUserCollectedCoinz(
            String user, Collection<Coin> coinz) {
        DocumentReference docRef = db.collection("collected_coinz").document(user);
        Map<String,Object> updates = new HashMap<>();
        for(Coin coin : coinz) {
            updates.put(coin.id, serializeCoinForFirestore(coin));
        }
        docRef.update(updates);
    }

    public void removeUserCollectibleCoinz(String user, Collection<Coin> coinz) {
        DocumentReference docRef = db.collection("collectable_coinz").document(user);
        Map<String,Object> updates = new HashMap<>();
        for(Coin coin : coinz) {
            updates.put(coin.id, FieldValue.delete());
        }
        docRef.update(updates);
    }

    public void removeUserDepositedCoinz(
            String user, Collection<Coin> coinz) {
        DocumentReference docRef = db.collection("collected_coinz").document(user);
        Map<String,Object> updates = new HashMap<>();
        for(Coin coin : coinz) {
            updates.put(coin.id, FieldValue.delete());
        }
        docRef.update(updates);
    }

    public void getHighScores(OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("high_scores")
                .document("scores")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void setHighScore(int position, String user, double num) {
        DocumentReference docRefSent = db.collection("high_scores").document("scores");
        HashMap<String, Object> score = new HashMap<>();
        score.put("user", user);
        score.put("score", Double.toString(num));
        Map<String, Object> updates = new HashMap<>();
        updates.put(Integer.toString(position), score);
        docRefSent.update(updates);
    }

}
