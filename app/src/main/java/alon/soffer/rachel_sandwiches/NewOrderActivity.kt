package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import java.util.*

class NewOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_order)

        val app = applicationContext as RachelApplication
        val db = app.db

        /* find UI elements */
        val createNewOrderButton = findViewById<FloatingActionButton>(R.id.makeOrderButton)
        val nameText = findViewById<EditText>(R.id.nameField)
        val pickleSlider = findViewById<Slider>(R.id.pickleSlider)
        val hummusSwitch = findViewById<SwitchCompat>(R.id.hummusSwitch)
        val tahiniSwitch = findViewById<SwitchCompat>(R.id.tahiniSwitch)
        val commentText = findViewById<EditText>(R.id.comments)



        createNewOrderButton.setOnClickListener { v: View? ->
            val id = UUID.randomUUID().toString()
            val newOrder = SandwichOrder(
                    id=id,
                    name=nameText.text.toString(),
                    pickles=pickleSlider.value.toInt(),
                    hummus=hummusSwitch.isChecked,
                    tahini=tahiniSwitch.isChecked,
                    comment=commentText.text.toString())


            db.collection(RachelApplication.ORDERS_COLLECTION).document(id).set(newOrder)
                    .addOnSuccessListener { //TODO: open next screen and close this one.
                        val editor = app.sp.edit()
                        editor.putString(id, id)
                        editor.apply()
                        app.currentOrderId = id

                        val waitingIntent = Intent(app, WaitingActivity::class.java)
                        startActivity(waitingIntent)
                        this.finish()
                    }
                    .addOnFailureListener { //TODO: what to do on failure?
                    }
        }
    }
}