package anthony.brenon.realestatemanager.ui.map

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.callback.CallbackLocation
import anthony.brenon.realestatemanager.databinding.FragmentMapsBinding
import anthony.brenon.realestatemanager.ui.MainViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private var callbackLocation: CallbackLocation? = null
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        listenerClickView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackLocation = context as? CallbackLocation
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        setObservers()
    }

    private fun setObservers() {
        viewModel.locationLivedata.observe(viewLifecycleOwner) {
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