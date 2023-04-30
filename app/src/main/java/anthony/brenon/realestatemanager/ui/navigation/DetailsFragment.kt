package anthony.brenon.realestatemanager.ui.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentDetailsBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.DecimalFormat

class DetailsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private var estate: Estate? = null
    private lateinit var adapter : RecyclerViewImage
    private lateinit var googleMap: GoogleMap

    private val estateObserver = Observer<Estate> {
        binding.layoutDetails.isVisible = true
        estate = it
        populateView(it)
        initRVImage(it.id)
        googleMap.addMarker(MarkerOptions().position(LatLng(it.lat, it.lng)).title(it.addressStreet))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lng), 11.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewImage.adapter = null
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_details) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun populateView(estate: Estate ) {
        with(binding) {
            val dec = DecimalFormat("#,###")
            val newPrice = if (estate.price.isNotEmpty()) "${dec.format(estate.price.toInt())}$" else ""
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvType.text = estate.type
            detailsActivityTvPrice.text = newPrice
            detailsActivityTvSurface.text = estate.surface
            detailsActivityTvRoom.text = estate.roomsNumber
            detailsActivityTvInteresting.text = estate.interestingPoint
            detailsActivityTvOnSaleDate.text = estate.onSaleDate
            detailsActivityTvSold.text = estate.dateOfSale
            detailsActivityTvAgent.text = estate.agentInChargeName
            detailsActivityTvLoc1.text = estate.addressStreet
            val textLoc2 = "${estate.addressCode} ${estate.addressCity}"
            detailsActivityTvLoc2.text = textLoc2
            detailsActivityTvLoc3.text = estate.addressCountry
        }
    }

    private fun initRVImage(estateId : Long) {
        adapter = RecyclerViewImage { image ->
            binding.layoutDetails.isVisible = false
            binding.layoutImage.isVisible = true
            binding.ivDetails.setImageBitmap(image)
        }
        binding.recyclerViewImage.adapter = adapter

        viewModel.getPicturesByEstate(estateId).observe(viewLifecycleOwner) { pictures ->
            val images = pictures.map { it.picture }
            adapter.setData(images)
        }
    }

    private fun setListeners() {
        binding.imgAddEstateBack?.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.imgImageClose.setOnClickListener {
            binding.layoutDetails.isVisible = true
            binding.layoutImage.isVisible = false
        }

        binding.imgEstateEdit.setOnClickListener {
            if (viewModel.agentIsConnected()) {
                viewModel.isNewEstate = false
                Navigation.findNavController(binding.root).navigate(R.id.item_add_fragment)
            } else {
                Toast.makeText(requireContext(), getString(R.string.you_need_to_be_logged_in_for_edit_an_estate), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        viewModel.estateSelected.observe(viewLifecycleOwner, estateObserver)
    }
}