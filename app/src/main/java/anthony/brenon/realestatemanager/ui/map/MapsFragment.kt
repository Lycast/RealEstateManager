package anthony.brenon.realestatemanager.ui.map

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.callback.CallbackLocation
import anthony.brenon.realestatemanager.databinding.FragmentMapsBinding
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var activity: MainActivity
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var callbackLocation: CallbackLocation? = null
    private lateinit var callback: OnMapReadyCallback
    private lateinit var googleMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
        callbackLocation = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = OnMapReadyCallback { googleMap = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        setObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        listenerClickView()
    }

    private fun setObservers() {
        viewModel.locationLivedata.observe(activity) {
            Log.i("TAG", "observer map position - long: ${it.latitude} / lat: ${it.longitude}")
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 11.0f))
        }
    }

    private fun listenerClickView() {
        binding.imgMapBack.setOnClickListener {
            callbackLocation?.stopLocationListener()
            Navigation.findNavController(binding.root).popBackStack()
        }
    }
}