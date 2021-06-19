package alon.soffer.rachel_sandwiches

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import java.util.*

class NewOrderActivity : AppCompatActivity() {
    private var nameText : EditText? = null
    private var pickleSlider : Slider? = null
    private var hummusSwitch : SwitchCompat? = null
    private var tahiniSwitch : SwitchCompat? = null
    private var commentText : EditText? = null
    private lateinit var app : RachelApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_order)

        app = applicationContext as RachelApplication
        val db = app.db

        /* find UI elements */
        val createNewOrderButton = findViewById<FloatingActionButton>(R.id.makeOrderButton)
        nameText = findViewById<EditText>(R.id.nameField)
        pickleSlider = findViewById<Slider>(R.id.pickleSlider)
        hummusSwitch = findViewById<SwitchCompat>(R.id.hummusSwitch)
        tahiniSwitch = findViewById<SwitchCompat>(R.id.tahiniSwitch)
        commentText = findViewById<EditText>(R.id.comments)

        // set previous user name
        if (app.userName != ""){
            nameText!!.setText(app.userName)
        }

        // update user name if it changes
        nameText!!.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val name = nameText!!.text.toString()
                app.userName = name
                val editor = app.sp.edit()
                editor.putString(RachelApplication.USER_NAME_KEY, name)
                editor.apply()
            }
        })

        // when button is pressed, create new order object, try to add to DB, if
        // successful go to next screen
        createNewOrderButton.setOnClickListener { v: View? ->
            val id = UUID.randomUUID().toString()
            val newOrder = SandwichOrder(
                    id=id,
                    name=nameText!!.text.toString(),
                    pickles=pickleSlider!!.value.toInt(),
                    hummus=hummusSwitch!!.isChecked,
                    tahini=tahiniSwitch!!.isChecked,
                    comment=commentText!!.text.toString())

            // if adding to DB is successful, go to next screen and kill this one
            db.collection(RachelApplication.ORDERS_COLLECTION).document(id).set(newOrder)
                    .addOnSuccessListener {
                        onDbSuccess(id)
                    }
                    .addOnFailureListener { //TODO: what to do on failure?
                    }
        }
    }

    fun onDbSuccess(id: String){
        val editor = app.sp.edit()
        editor.putString(id, id)    // put the current order in sp
        editor.apply()
        app.currentOrderId = id     // save current orderId also in application

        val waitingIntent = Intent(app, WaitingActivity::class.java)
        startActivity(waitingIntent)
        this.finish()
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString("name", nameText!!.text.toString())
        outState.putInt("pickles", pickleSlider!!.value.toInt())
        outState.putBoolean("hummus", hummusSwitch!!.isChecked)
        outState.putBoolean("tahini", tahiniSwitch!!.isChecked)
        outState.putString("comments", commentText!!.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nameText!!.setText(savedInstanceState.getString("name"))
        pickleSlider!!.value = savedInstanceState.getInt("pickles").toFloat()
        hummusSwitch!!.isChecked = savedInstanceState.getBoolean("hummus")
        tahiniSwitch !!.isChecked = savedInstanceState.getBoolean("tahini")
        commentText!!.setText(savedInstanceState.getString("comments"))
    }
}