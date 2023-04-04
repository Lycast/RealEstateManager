package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentAddEstateBinding
import anthony.brenon.realestatemanager.ui.MainViewModel

class AddEstateFragment : Fragment() {

    private var _binding: FragmentAddEstateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEstateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonListener()
    }

    private fun buttonListener() {
        binding.btnAddEstateCancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
    }
}