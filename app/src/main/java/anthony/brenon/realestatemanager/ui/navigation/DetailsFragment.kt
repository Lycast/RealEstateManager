package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.databinding.FragmentDetailsBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import anthony.brenon.realestatemanager.utils.NavigationStates

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var adapter : RecyclerViewImage
    private lateinit var images : List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.estateSelected.observe(requireActivity()) {
            populateView(it)
        }
        onClickFabClose()
    }

    private fun populateView(estate: Estate ) {
        binding.apply {
            //initRVImage(estate.photos)
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvSurface.text = "${estate.surface} sq m"
            detailsActivityTvRoomNumber.text = estate.roomsNumber.toString()
            detailsActivityTvLocation.text = estate.address
        }
    }

    private fun initRVImage(images : List<Int>) {
        adapter = RecyclerViewImage(images){
            //todo implement action on click
        }
        binding.recyclerViewImage.adapter = adapter
    }

    private fun onClickFabClose() {
        binding.imgDetailsClose?.setOnClickListener {
            viewModel.selectNavigationStates(NavigationStates.CLOSE_DETAILS)
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //todo _binding = null  --look for fix null pointer after that
    }
}