package anthony.brenon.realestatemanager.ui


import android.annotation.SuppressLint
import android.database.CursorWindow
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.RealEstateManagerApp
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.ui.dialog.DialogAgentConnect
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as RealEstateManagerApp).agentRepository, (application as RealEstateManagerApp).estateRepository)
    }

    // drawer init
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initDrawer()
        addImageEstate()
    }

    //todo delete this inject after
    private fun addImageEstate() {
        //val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_2)
    }

    private fun initDrawer() {

        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        val header: View = binding.headerNavigationView.getHeaderView(0)
        viewModel.agentSelected.observe(this) {
            Log.i("MY_LOG", "observe $it")
            val name: TextView = header.findViewById(R.id.name_header)
            val email: TextView = header.findViewById(R.id.email_header)
            name.text = it.nameAgent
            email.text = it.emailAgent
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.headerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_login -> DialogAgentConnect().show(supportFragmentManager, "dialog_login")
                R.id.item_exit -> finishAndRemoveTask()
            }
            true
        }
    }

    // add custom menu toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // toolbar listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.item_add -> {
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment_list)).navigate(R.id.item_add_fragment)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}