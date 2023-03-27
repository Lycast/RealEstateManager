package anthony.brenon.realestatemanager.ui


import android.database.CursorWindow
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.ui.dialog.DialogAgentConnect
import anthony.brenon.realestatemanager.ui.navigation.AddEstateFragment
import anthony.brenon.realestatemanager.ui.navigation.DetailsFragment
import anthony.brenon.realestatemanager.ui.navigation.ListFragment
import anthony.brenon.realestatemanager.utils.NavigationStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    // drawer init
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Room implementation

        // No need to cancel this scope as it'll be torn down with the process
        val applicationScope = CoroutineScope(SupervisorJob())

        val remRoomDatabase by lazy { REMRoomDatabase.getAgentDatabase(this, applicationScope) }
        val agentRepository by lazy { AgentRepository(remRoomDatabase.agentDao()) }
        val estateRepository by lazy { EstateRepository(remRoomDatabase.estateDao()) }

        // Room implementation
        val factory = MainViewModelFactory(estateRepository, agentRepository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        displayFragment(ListFragment())
        initDrawer()

        viewModel.navigationStates.observe(this) {
            when (it!!) {
                NavigationStates.DISPLAY_DETAILS -> displayFragment(DetailsFragment())
                NavigationStates.CLOSE_DETAILS -> displayFragment(ListFragment())
                NavigationStates.CLOSE_CREATE -> displayFragment(ListFragment())
            }
        }

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addEstate()
    }
    private fun addEstate() {
            // load bitmap image
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_2)
            val estate = Estate("House",41480000, 750,8,"description too long"
                ,imageBitmap
                ,"New York","idk",false,"sale date","date sale","maurice")
            viewModel.insert(estate)
    }

    private fun displayFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, fragment).commitAllowingStateLoss()
    }

    private fun initDrawer() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // binding header
        val header : View = binding.designNavigationView.getHeaderView(0)
        viewModel.agentSelected.observe(this) {
            Log.i("MY_LOG", "observe $it")
            val name : TextView = header.findViewById(R.id.name_header)
            val email : TextView = header.findViewById(R.id.email_header)
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
            when(it.itemId) {
                R.id.item_login -> DialogAgentConnect().show(supportFragmentManager,"dialog_login")
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) { return true }
        return when (item.itemId) {
            R.id.item_add -> {
                displayFragment(AddEstateFragment())
                return true
            }
            else -> {super.onOptionsItemSelected(item) }
        }
    }
}