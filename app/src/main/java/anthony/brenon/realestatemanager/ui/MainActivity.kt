package anthony.brenon.realestatemanager.ui


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
import androidx.fragment.app.Fragment
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.RealEstateManagerApp
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.ui.dialog.DialogAgentConnect
import anthony.brenon.realestatemanager.ui.navigation.AddEstateFragment
import anthony.brenon.realestatemanager.ui.navigation.DetailsFragment
import anthony.brenon.realestatemanager.ui.navigation.ListFragment
import anthony.brenon.realestatemanager.utils.NavigationStates
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

        displayFragment(ListFragment())
        initDrawer()
        setNavigation()

        // set field room for image
        setFieldRoom()
    }

    private fun setNavigation() {

        viewModel.navigationStates.observe(this) {
            when (it!!) {
                NavigationStates.DISPLAY_DETAILS -> displayFragment(DetailsFragment())
                NavigationStates.CLOSE_DETAILS -> displayFragment(ListFragment())
                NavigationStates.CLOSE_CREATE -> displayFragment(ListFragment())
            }
        }

        // when tab land
        if ( binding.fragmentDetailsMain != null ) {
            viewModel.estateSelected.observe(this) { estate ->
                if ( estate != null) binding.fragmentDetailsMain!!.visibility = View.VISIBLE
            }
        }
    }

    private fun setFieldRoom() {
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, fragment)
            .commitAllowingStateLoss()
    }

    private fun initDrawer() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        // binding header
        val header: View = binding.designNavigationView.getHeaderView(0)
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
        binding.designNavigationView.setNavigationItemSelectedListener {
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
                displayFragment(AddEstateFragment())
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}