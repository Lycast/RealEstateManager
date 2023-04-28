package anthony.brenon.realestatemanager.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.RealEstateManagerApp
import anthony.brenon.realestatemanager.callback.CallbackLocation
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.utils.EstateStatus
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), CallbackLocation {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as RealEstateManagerApp).agentRepository, (application as RealEstateManagerApp).estateRepository)
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    // Declare the LocationManager and LocationListener
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    //TODO implement content provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        initializeLocationManger()
        if (!hasPermissionLocation()) requestLocationPermissions()

        setContentView(view)
        viewModel.allEstates.observe(this) { viewModel.updateSortListEstate(it) }
        initDrawer()
        requestLocationPermissions()
    }

    private fun initDrawer() {
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        val header: View = binding.headerNavigationView.getHeaderView(0)
        viewModel.agentSelected.observe(this) {
            val name: TextView = header.findViewById(R.id.name_header)
            val email: TextView = header.findViewById(R.id.email_header)
            name.text = it.nameAgent
            email.text = it.emailAgent
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerMenuListener()
    }

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
            R.id.item_search -> {
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_filter_fragment)
                return true
            }

            R.id.item_map -> {
                startLocationListener()
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_map_fragment)
                return true
            }

            R.id.item_add -> {
                if (viewModel.agentIsConnected()) {
                    viewModel.selectThisEstateStatus(EstateStatus.ADD_NEWS_ESTATE)
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_add_fragment)
                } else Snackbar.make(binding.root, "You need to be logged in for create an estate", Snackbar.LENGTH_SHORT).show()
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun drawerMenuListener() {
        binding.headerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_settings -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_settings_fragment)
                    drawerLayout.close()
                }
                R.id.item_login -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_connect_fragment)
                    drawerLayout.close()
                }
                R.id.item_exit -> finishAndRemoveTask()
            }
            true
        }
    }

    // Initialize the LocationManager and LocationListener
    private fun initializeLocationManger() {
        Log.i("TAG", "initializeLocationManger()")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            // This method will be called when the GPS location changes
            viewModel.updateLocation(location)
        }
    }

    private fun requestLocationPermissions() {
        // Request location updates from the LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission is granted
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun hasPermissionLocation() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    @SuppressLint("MissingPermission")
    private fun startLocationListener() {
        Log.i("TAG", "startLocationListener()")
        if (hasPermissionLocation()) locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
    }

    override fun stopLocationListener() {
        // Callback when map fragment is closed for remove update location
        Log.i("TAG", "stopLocationListener()")
        locationManager.removeUpdates(locationListener)
    }
}