package alon.soffer.rachel_sandwiches;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
/*
TODO: make app open where it left from (if we were in progress then open in in progress...).
      rotation, the whole on restore and stuff.
      UI.
 */
public class RachelApplication extends Application {
    public static final String WAITING = "waiting";
    public static final String IN_PROGRESS = "in progress";
    public static final String READY = "ready";
    public static final String DONE = "done";
    public static final String ORDERS_COLLECTION = "orders";

    private FirebaseFirestore db;
    private String currentOrderId = null;
    private SharedPreferences sp = null;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        sp = this.getSharedPreferences("rachel_sp", MODE_PRIVATE);
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }
    public void setCurrentOrderId(String id){
        currentOrderId = id;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public SharedPreferences getSp() {
        return sp;
    }
}
