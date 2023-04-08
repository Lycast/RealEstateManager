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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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

    private lateinit var appBarConfiguration: AppBarConfiguration

    // drawer init
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // set field room for image
        setFieldRoom()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        addImageEstate()

        initDrawer()
    }

    //todo delete this inject after
    private fun addImageEstate() {
        //val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_2)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun initDrawer() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        // binding header
        val header: View = binding.headerNavigationView.getHeaderView(0)
        viewModel.agentSelected.observe(this) {
            Log.i("MY_LOG", "observe $it")
            val name: TextView = header.findViewById(R.id.name_header)
            val email: TextView = header.findViewById(R.id.email_header)
            name.text = it.nameAgent
            email.text = it.emailAgent
        }
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // listener drawer menu
        binding.headerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_login -> DialogAgentConnect().show(supportFragmentManager, "dialog_login")
                R.id.item_exit -> finishAndRemoveTask()
            }
            true
        }
    }

    // add menu toolbar
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
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_add_fragment)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setFieldRoom() {
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}