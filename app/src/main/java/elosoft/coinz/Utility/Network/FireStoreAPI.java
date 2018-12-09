package elosoft.coinz.Utility.Network;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import elosoft.coinz.Models.Coin;
import elosoft.coinz.Models.UserCoinzData;

import static elosoft.coinz.Utility.Serialize.SerializeCoin.seralizeCoinzForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeCoin.serializeCoinForFirestore;
import static elosoft.coinz.Utility.Serialize.SerializeUserData.serializeUserDataForFireStore;

public class FireStoreAPI {
    private static volatile FireStoreAPI instance;
    private static Object mutex = new Object();
    private FirebaseFirestore db;

    private FireStoreAPI() {
        db = FirebaseFirestore.getInstance();
    }

    public static class NoDocumentFound extends Exception {
        private String errorMessage;

        NoDocumentFound(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }
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
        db.collection("users")
                .document(userName)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void setUserData(String userName, UserCoinzData userCoinzData) {
        CollectionReference c = db.collection("users");
        c.document(userName).set(serializeUserDataForFireStore(userCoinzData));
    }

    public void setUserCollectableCoinz(String user, HashMap<String, Coin> coinz) {
        CollectionReference c = db.collection("collectable_coinz");
        c.document(user).set(seralizeCoinzForFirestore(coinz));
    }

    public void setUserCollectedCoinz(String user, HashMap<String, Coin> coinz) {
        CollectionReference c = db.collection("collected_coinz");
        c.document(user).set(seralizeCoinzForFirestore(coinz));
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

    public void removeUserCollectableCoinz(
            String user, Collection<Coin> coinz,
            OnCompleteListener<DocumentSnapshot> onCompleteListener) {
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
}
