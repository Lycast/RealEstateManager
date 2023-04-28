package anthony.brenon.realestatemanager.ui.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentSettingsBinding
import anthony.brenon.realestatemanager.ui.MainActivity


class SettingsFragment : Fragment() {

    private lateinit var activity: MainActivity
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    //TODO implement settings fragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSettingsFragBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}