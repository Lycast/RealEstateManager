package anthony.brenon.realestatemanager

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils

class HomeActivity : AppCompatActivity() {

    private lateinit var textViewMain: TextView
    private lateinit var textViewQuantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // the link was wrong, I modified it
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)

        this.configureTextViewMain()
        this.configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        textViewMain.textSize = 15f
        textViewMain.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity.textSize = 20f
        // you can't put a int int a text view we need to format that
        textViewQuantity.text = quantity.toString()
    }
}