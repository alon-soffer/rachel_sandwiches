package alon.soffer.rachel_sandwiches;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Set;


public class RachelApplication extends Application {
    public static final String WAITING = "waiting";
    public static final String IN_PROGRESS = "in progress";
    public static final String READY = "ready";
    public static final String DONE = "done";
    public static final String ORDERS_COLLECTION = "orders";
    public static final String USER_NAME_KEY = "user_name";

    private FirebaseFirestore db;
    private String currentOrderId = null;
    private SharedPreferences sp = null;
    private String userName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // initiate from sp
        sp = this.getSharedPreferences("rachel_sp", MODE_PRIVATE);
        Set<String> spKeys = sp.getAll().keySet();
        for (String spKey : spKeys){
            if (spKey.equals(USER_NAME_KEY)){
                userName = sp.getString(spKey, null);
            }
            else{ // spKey is a order id
                currentOrderId = sp.getString(spKey, null);
            }
        }
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
