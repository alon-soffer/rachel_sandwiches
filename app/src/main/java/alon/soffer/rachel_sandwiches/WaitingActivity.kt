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
import com.google.firebase.firestore.ListenerRegistration

class WaitingActivity : AppCompatActivity() {
    private var snapshotListener : ListenerRegistration? = null

    private var nameText : TextView? = null
    private var pickleSlider : Slider? = null
    private var hummusSwitch : SwitchCompat? = null
    private var tahiniSwitch : SwitchCompat? = null
    private var commentText : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val app = applicationContext as RachelApplication
        val db = app.db
        var orderObject : SandwichOrder? = null

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
                    val order = res.toObject(SandwichOrder::class.java)
                    if (order != null) {
                        orderObject = order
                        nameText!!.text = order.name
                        pickleSlider!!.value = order.pickles.toFloat()
                        hummusSwitch!!.isChecked = order.hummus
                        tahiniSwitch!!.isChecked = order.tahini
                        commentText!!.setText(order.comment)
                    }
                }
                .addOnFailureListener { //TODO:
                }

        // cancel order
        cancelOrderButton.setOnClickListener { v: View? ->
            db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).delete()
                    .addOnSuccessListener {
                        val editor = app.sp.edit()
                        editor.remove(app.currentOrderId)
                        editor.apply()
                        app.currentOrderId = null

                        val newOrderIntent = Intent(this, NewOrderActivity::class.java)
                        startActivity(newOrderIntent)
                        this.finish()
                    }
                    .addOnFailureListener { //TODO:
                    }
        }

        // approve order edits
        editOrderButton.setOnClickListener { v: View? ->
            if (orderObject != null)
            {
                orderObject!!.pickles = pickleSlider!!.value.toInt()
                orderObject!!.hummus = hummusSwitch!!.isChecked
                orderObject!!.tahini = tahiniSwitch!!.isChecked
                orderObject!!.comment = commentText!!.text.toString()
                db.collection(RachelApplication.ORDERS_COLLECTION).document(app.currentOrderId).set(orderObject!!)
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

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snapshotListener?.remove()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("pickles", pickleSlider!!.value.toInt())
        outState.putBoolean("hummus", hummusSwitch!!.isChecked)
        outState.putBoolean("tahini", tahiniSwitch!!.isChecked)
        outState.putString("comments", commentText!!.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pickleSlider!!.value = savedInstanceState.getInt("pickles").toFloat()
        hummusSwitch!!.isChecked = savedInstanceState.getBoolean("hummus")
        tahiniSwitch !!.isChecked = savedInstanceState.getBoolean("tahini")
        commentText!!.setText(savedInstanceState.getString("comments"))
    }
}