package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ReadyActivity : AppCompatActivity() {
    private lateinit var app: RachelApplication
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        app = applicationContext as RachelApplication
        db = app.db

        /* find UI elements */
        val gotItButton = findViewById<Button>(R.id.gotItButton)

        gotItButton.setOnClickListener { v: View? ->
            // when the got it button is pressed, we want to set the current order to null, remove it from sp, and go to new order activity
            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).get()
                    .addOnSuccessListener { res: DocumentSnapshot ->
                        val order = res.toObject(SandwichOrder::class.java)
                        if (order != null) {
                            order.status = RachelApplication.DONE
                            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).set(order)
                        }
                        onStatusChangedToDone(order)
                    }
                    .addOnFailureListener { //TODO:
                    }
        }

    }

    public fun onStatusChangedToDone(order: SandwichOrder?) {

        if (order != null) {
            val editor = app.sp.edit()
            editor.remove(app.currentOrderId)
            editor.apply()
            app.currentOrderId = null

            val newOrderIntent = Intent(this, NewOrderActivity::class.java)
            startActivity(newOrderIntent)
            this.finish()
        }
    }
}