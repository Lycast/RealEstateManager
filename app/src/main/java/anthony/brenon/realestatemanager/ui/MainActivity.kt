package anthony.brenon.realestatemanager.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.ui.navigation.DetailsFragment
import anthony.brenon.realestatemanager.ui.navigation.ListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    // drawer init
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val repository = EstateRepository()
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        initDrawer()
    }

    private fun displayListFrag(){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, ListFragment()).commitAllowingStateLoss()
    }

    private fun initDrawer() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            R.id.item_search -> {
                displayListFrag()
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }
}