package anthony.brenon.realestatemanager.ui.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentFilterBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel
import java.text.Normalizer


class FilterFragment : Fragment() {
    /*
    L’agent immobilier peut effectuer une recherche multi-critères sur l’ensemble des biens immobiliers de la base.
    Par exemple :
    - Afficher les appartements d’une surface comprise entre 200 et 300m2, proches d’une école et des commerces,
    mis sur le marché depuis moins d’une semaine.
    - Afficher les maisons vendues au cours des trois derniers mois, dans le secteur de Long Island,
    avec au moins trois photos, pour un prix compris entre $1,500,000 et $2,000,000.
    */

    //TODO ajouter message si list vide + ajouter carte (tab layout?)

    //TODO implement filter fragment

    private lateinit var activity: MainActivity
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private var sortListEstate = listOf<Estate>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        observerEstates()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListenerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observerEstates() {
        viewModel.allEstates.observe(activity) {
            sortListEstate = it
        }
    }

    private fun setListenerView() {
        binding.apply {

            btnValidationFilter.setOnClickListener {
                if (etPriceMin.text.isNotEmpty()) filterByMinIntegerValue(etPriceMin.text.toString().toInt(), "price")
                if (etPriceMax.text.isNotEmpty()) filterByMaxIntegerValue(etPriceMax.text.toString().toInt(), "price")
                if (etSurfaceMin.text.isNotEmpty()) filterByMinIntegerValue(etSurfaceMin.text.toString().toInt(), "surface")
                if (etSurfaceMax.text.isNotEmpty()) filterByMaxIntegerValue(etSurfaceMax.text.toString().toInt(), "surface")
                if (etPictures.text.isNotEmpty()) filterByMinIntegerValue(etPictures.text.toString().toInt(), "picture")
                if (etInterestingPoint.text.isNotEmpty()) filterByContainsTextValue(etInterestingPoint.text.toString(), "interesting")
                if (etCity.text.isNotEmpty()) filterByContainsTextValue(etCity.text.toString(), "town")

                viewModel.updateSortListEstate(sortListEstate)
                Navigation.findNavController(binding.root).popBackStack()
            }

            ivFilterFragBack.setOnClickListener {
                viewModel.updateSortListEstate(sortListEstate)
                Navigation.findNavController(root).popBackStack()
            }

            etDateOfSale
            etSoldDate
        }
    }

    private fun filterByContainsTextValue(textValue: String, filteredAttribute: String) {
        val textValueNormalize = Normalizer.normalize(textValue, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "")
        val newSortListEstate = mutableListOf<Estate>()
        when ( filteredAttribute ) {
            "interesting" -> for (estate in sortListEstate) {
                val normalizeCity = Normalizer.normalize(estate.interestingPoint, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "").replace(" ", "")
                if (normalizeCity.contains(textValueNormalize, true)) newSortListEstate.add(estate)
            }
            "town" -> for (estate in sortListEstate) {
                val normalizeCity = Normalizer.normalize(estate.addressCity, Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(), "").replace(" ", "")
                if (normalizeCity.contains(textValueNormalize, true)) newSortListEstate.add(estate)
            }
        }
        sortListEstate = newSortListEstate
    }

    private fun filterByMinIntegerValue(minValue: Int, filteredAttribute: String) {
        val newSortListEstate = mutableListOf<Estate>()
        when ( filteredAttribute ) {
            "price" -> for (estate in sortListEstate) { if (estate.price.toInt() >= minValue) newSortListEstate.add(estate) }
            "surface" -> for (estate in sortListEstate) { if (estate.surface.toInt() >= minValue) newSortListEstate.add(estate) }
            "picture" -> for (estate in sortListEstate) { if (estate.numberOfPicture >= minValue) newSortListEstate.add(estate) }
        }
        sortListEstate = newSortListEstate
    }

    private fun filterByMaxIntegerValue(maxValue: Int, filteredAttribute: String) {
        val newSortListEstate = mutableListOf<Estate>()
        when ( filteredAttribute ) {
            "price" -> for (estate in sortListEstate) { if (estate.price.toInt() <= maxValue) newSortListEstate.add(estate) }
            "surface" -> for (estate in sortListEstate) { if (estate.surface.toInt() <= maxValue) newSortListEstate.add(estate) }
        }
        sortListEstate = newSortListEstate
    }
}