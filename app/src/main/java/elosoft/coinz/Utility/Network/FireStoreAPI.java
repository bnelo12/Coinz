package elosoft.coinz.Utility.Network;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

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

    public void getUserCoinz(String user, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("coinz")
                .document(user)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
