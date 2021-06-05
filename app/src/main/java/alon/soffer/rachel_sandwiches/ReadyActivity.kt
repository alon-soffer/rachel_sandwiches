package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot

class ReadyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        val app = applicationContext as RachelApplication
        val db = app.db

        /* find UI elements */
        val gotItButton = findViewById<Button>(R.id.gotItButton)

        gotItButton.setOnClickListener { v: View? ->
            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).get()
                    .addOnSuccessListener { res: DocumentSnapshot ->
                        val order = res.toObject(SandwichOrder::class.java)
                        if (order != null) {
                            order.status = RachelApplication.DONE
                            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).set(order)
                            val editor = app.sp.edit()
                            editor.remove(app.currentOrderId)
                            editor.apply()
                            app.currentOrderId = null

                            val newOrderIntent = Intent(this, NewOrderActivity::class.java)
                            startActivity(newOrderIntent)
                            this.finish()
                        }
                    }
                    .addOnFailureListener { //TODO:
                    }
        }

    }
}