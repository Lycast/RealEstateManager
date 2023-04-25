package anthony.brenon.realestatemanager.ui.navigation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentDetailsBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import anthony.brenon.realestatemanager.utils.EstateStatus
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private var estate: Estate = Estate(0, "", "", "", "", "", "", "", "", "", 0.00, 0.00, "", "", "", "", BitmapFactory.decodeResource(resources, R.drawable.img_estate_test_1))

    private lateinit var adapter : RecyclerViewImage

    //TODO implement all data we needed to see in view
    //TODO Cette carte est dynamique : l'agent peut zoomer, dézoomer, se déplacer, et afficher le détail d'un bien en cliquant sur la punaise correspondante.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewModel.estateSelected.observe(requireActivity()) {
            estate = it
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateView(estate)
        initRVImage(estate.id)
        listenerClickView()
    }

    private fun populateView(estate: Estate ) {

        var price = ""
        val dec = DecimalFormat("#,###")
        if (estate.price.isNotEmpty()) {
            price = "${dec.format(estate.price.toInt())}$"
        }

        binding.apply {
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvType.text = estate.type
            detailsActivityTvPrice.text = price
            detailsActivityTvSurface.text = estate.surface
            detailsActivityTvRoom.text = estate.roomsNumber
            detailsActivityTvInteresting.text = estate.interestingPoint
            detailsActivityTvOnSaleDate.text = estate.onSaleDate
            detailsActivityTvSold.text = estate.dateOfSale
            detailsActivityTvAgent.text = estate.agentInChargeName

            val loc2 = "${estate.addressCode} ${estate.addressCity}"
            detailsActivityTvLoc1.text = estate.addressStreet
            detailsActivityTvLoc2.text = loc2
            detailsActivityTvLoc3.text = estate.addressCountry
        }
    }

    private fun initRVImage(estateId : Long) {

        adapter = RecyclerViewImage {
            binding.layoutDetails.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            binding.ivDetails.setImageBitmap(it)
        }

        binding.recyclerViewImage.adapter = adapter
        Log.i("MY_TAG","id estate recycler view details = $estateId")
        val images = mutableListOf<Bitmap>()
        viewModel.getPicturesByEstate(estateId).observe(requireActivity()) {
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    private fun listenerClickView() {
        binding.imgAddEstateBack?.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.imgImageClose.setOnClickListener {
            binding.layoutDetails.visibility = View.VISIBLE
            binding.layoutImage.visibility = View.GONE
        }

        binding.imgEstateEdit.setOnClickListener {
            if(viewModel.agentIsConnected()) {
                viewModel.selectThisEstateStatus(EstateStatus.UPDATE_EXISTING_ESTATE)
                Navigation.findNavController(binding.root).navigate(R.id.item_add_fragment)
            } else Snackbar.make(binding.root, "You need to be logged in for edit an estate", Snackbar.LENGTH_SHORT).show()
        }
    }
}