package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ListenerRegistration

class InProgressActivity : AppCompatActivity() {
    private var snapshotListener : ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_progress)
        val app = applicationContext as RachelApplication
        val db = app.db

        // set listener to see when status changes and move to in progress screen
        snapshotListener = db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId)
                .addSnapshotListener { snapshot, e ->
            if (e != null) {
                //TODO: what to do on error
                e.printStackTrace()
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val order = snapshot.toObject(SandwichOrder::class.java)
                // if status changed, move to other activity
                if (order!!.status == RachelApplication.READY) {
                    val readyIntent = Intent(this, ReadyActivity::class.java)
                    startActivity(readyIntent)
                    this.finish()
                }
            }

        }
    }
}