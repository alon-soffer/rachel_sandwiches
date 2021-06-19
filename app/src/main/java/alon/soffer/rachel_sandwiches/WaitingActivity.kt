package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.w3c.dom.Document

class WaitingActivity : AppCompatActivity() {
    private var snapshotListener : ListenerRegistration? = null

    private lateinit var nameText : TextView
    private lateinit var pickleSlider : Slider
    private lateinit var hummusSwitch : SwitchCompat
    private lateinit var tahiniSwitch : SwitchCompat
    private lateinit var commentText : EditText
    private lateinit var app: RachelApplication

    private lateinit var orderObject: SandwichOrder
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        app = applicationContext as RachelApplication
        db = app.db

        /* find UI elements */
        val cancelOrderButton = findViewById<FloatingActionButton>(R.id.cancelOrderButton)
        val editOrderButton = findViewById<FloatingActionButton>(R.id.editOrderButton)
        nameText = findViewById<TextView>(R.id.nameField)
        pickleSlider = findViewById<Slider>(R.id.pickleSlider)
        hummusSwitch = findViewById<SwitchCompat>(R.id.hummusSwitch)
        tahiniSwitch = findViewById<SwitchCompat>(R.id.tahiniSwitch)
        commentText = findViewById<EditText>(R.id.comments)

        // get order from DB and set UI accordingly
        db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).get()
                .addOnSuccessListener { res: DocumentSnapshot ->
                    onGetOrderFromDb(res)
                }
                .addOnFailureListener { //TODO:
                }

        // cancel order
        cancelOrderButton.setOnClickListener { v: View? ->
            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).delete()
                    .addOnSuccessListener {
                        onDeleteFromDb()
                    }
                    .addOnFailureListener { //TODO:
                    }
        }

        // approve order edits
        editOrderButton.setOnClickListener { v: View? ->
            if (orderObject != null)
            {
                onEditApprove()
            }
        }

        // set listener to see when status changes and move to in progress screen
        snapshotListener = db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                //TODO: what to do on error
                e.printStackTrace()
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val order = snapshot.toObject(SandwichOrder::class.java)
                onStatusChange(order)
            }

        }
    }

    private fun onEditApprove() {
        orderObject.pickles = pickleSlider.value.toInt()
        orderObject.hummus = hummusSwitch.isChecked
        orderObject.tahini = tahiniSwitch.isChecked
        orderObject.comment = commentText.text.toString()
        db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).set(orderObject)
    }

    fun onStatusChange(order: SandwichOrder?) {
        // if status changed, move to other activity
        if (order!!.status == RachelApplication.IN_PROGRESS) {
            val inProgressIntent = Intent(this, InProgressActivity::class.java)
            startActivity(inProgressIntent)
            this.finish()
        } else if (order.status == RachelApplication.READY) {   // TODO: probably redundant
            val inProgressIntent = Intent(this, InProgressActivity::class.java)
            startActivity(inProgressIntent)
            this.finish()
        }
    }

    private fun onGetOrderFromDb(res : DocumentSnapshot){
        val order = res.toObject(SandwichOrder::class.java)
        if (order != null) {
            orderObject = order
            nameText.text = order.name
            pickleSlider.value = order.pickles.toFloat()
            hummusSwitch.isChecked = order.hummus
            tahiniSwitch.isChecked = order.tahini
            commentText.setText(order.comment)
        }
    }

    private fun onDeleteFromDb() {
        val editor = app.sp.edit()
        editor.remove(app.currentOrderId)
        editor.apply()
        app.currentOrderId = null

        val newOrderIntent = Intent(this, NewOrderActivity::class.java)
        startActivity(newOrderIntent)
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        snapshotListener?.remove()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("pickles", pickleSlider.value.toInt())
        outState.putBoolean("hummus", hummusSwitch.isChecked)
        outState.putBoolean("tahini", tahiniSwitch.isChecked)
        outState.putString("comments", commentText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pickleSlider.value = savedInstanceState.getInt("pickles").toFloat()
        hummusSwitch.isChecked = savedInstanceState.getBoolean("hummus")
        tahiniSwitch .isChecked = savedInstanceState.getBoolean("tahini")
        commentText.setText(savedInstanceState.getString("comments"))
    }
}