package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val app = applicationContext as RachelApplication
        val db = app.db

        if (app.currentOrderId != null){ // if there is a current order
            // get it from DB
            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).get()
                    .addOnSuccessListener { res: DocumentSnapshot ->
                        val order = res.toObject(SandwichOrder::class.java)
                        onGotOrderFromDb(order)
                    }
                    .addOnFailureListener { //TODO:
                    }
        }
        else{   // there is no on going order, open the next order screen
            val activityIntent = Intent(this, NewOrderActivity::class.java)
            startActivity(activityIntent)
            this.finish()
        }
    }

    fun onGotOrderFromDb(order: SandwichOrder?) {
        if (order != null) {
            // if successfully read from db, check status and open the right activity
            val status = order.status;
            var activityIntent: Intent? = null

            when (status) {
                RachelApplication.WAITING -> {
                    // open waiting wactivity
                    activityIntent = Intent(this, WaitingActivity::class.java)
                }

                RachelApplication.IN_PROGRESS -> {
                    //open inprogress activity
                    activityIntent = Intent(this, InProgressActivity::class.java)
                }

                RachelApplication.READY -> {
                    //open ready activity
                    activityIntent = Intent(this, ReadyActivity::class.java)
                }
            }
            if (activityIntent != null) {
                startActivity(activityIntent)
                this.finish()
            }
        }
    }
}