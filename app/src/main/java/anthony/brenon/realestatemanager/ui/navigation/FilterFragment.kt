package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    /*
    L’agent immobilier peut effectuer une recherche multi-critères sur l’ensemble des biens immobiliers de la base.

    Par exemple :

    - Afficher les appartements d’une surface comprise entre 200 et 300m2, proches d’une école et des commerces,
    mis sur le marché depuis moins d’une semaine.

    - Afficher les maisons vendues au cours des trois derniers mois, dans le secteur de Long Island,
    avec au moins trois photos, pour un prix compris entre $1,500,000 et $2,000,000.
    */

    //TODO implement filter fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivFilterFragBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
    }
}