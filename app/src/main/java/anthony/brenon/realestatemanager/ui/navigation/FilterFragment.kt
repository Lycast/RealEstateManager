package anthony.brenon.realestatemanager.ui.navigation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentFilterBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FilterFragment : Fragment(),
    DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private var sortListEstate = listOf<Estate>()
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var datePickParam = ""
    private var saleDateStart = ""
    private var saleDateEnd = ""
    private var soldDateStart = ""
    private var soldDateEnd = ""

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

    private fun observerEstates() {
        viewModel.allEstates.observe(viewLifecycleOwner) {
            sortListEstate = it
        }
    }

    private fun setListenerView() {
        binding.apply {

            btnSaleStart.setOnClickListener { openDatePickerDial("saleDateStart") }
            btnSaleEnd.setOnClickListener { openDatePickerDial("saleDateEnd") }
            btnSoldStart.setOnClickListener { openDatePickerDial("soldDateStart") }
            btnSoldEnd.setOnClickListener { openDatePickerDial("soldDateEnd") }

            btnValidationFilter.setOnClickListener {
                if (etPriceMin.text.isNotEmpty()) filterByMinIntegerValue(etPriceMin.text.toString().toInt(), "price")
                if (etPriceMax.text.isNotEmpty()) filterByMaxIntegerValue(etPriceMax.text.toString().toInt(), "price")
                if (etSurfaceMin.text.isNotEmpty()) filterByMinIntegerValue(etSurfaceMin.text.toString().toInt(), "surface")
                if (etSurfaceMax.text.isNotEmpty()) filterByMaxIntegerValue(etSurfaceMax.text.toString().toInt(), "surface")
                if (etPictures.text.isNotEmpty()) filterByMinIntegerValue(etPictures.text.toString().toInt(), "picture")
                if (etInterestingPoint.text.isNotEmpty()) filterByContainsTextValue(etInterestingPoint.text.toString(), "interesting")
                if (etAddress.text.isNotEmpty()) filterByContainsTextValue(etAddress.text.toString(), "address")
                if (etType.text.isNotEmpty()) filterByContainsTextValue(etType.text.toString(),"type")

                if (saleDateStart.isNotEmpty() && saleDateEnd.isNotEmpty()) filterByDate(saleDateStart, saleDateEnd, "saleDate")
                if (soldDateStart.isNotEmpty() && soldDateEnd.isNotEmpty()) filterByDate(soldDateStart, soldDateEnd, "soldDate")

                viewModel.updateSortListEstate(sortListEstate)
                Navigation.findNavController(binding.root).popBackStack()
            }

            ivFilterFragBack.setOnClickListener {
                viewModel.updateSortListEstate(sortListEstate)
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
    }

    private fun filterByContainsTextValue(textValue: String, filteredAttribute: String) {
        val newSortListEstate = mutableListOf<Estate>()
        val listWordTextValue = Utils.convertStringToListOfWord(textValue)

        for (estate in sortListEstate) {
            val wordsToMatch = when (filteredAttribute) {
                "interesting" -> Utils.convertStringToListOfWord(estate.interestingPoint)
                "address" -> Utils.convertStringToListOfWord(estate.getAddressFormatFilter())
                "type" -> Utils.convertStringToListOfWord(estate.type)
                else -> emptyList()
            }

            if (wordsToMatch.any { word -> listWordTextValue.any { it.contains(word) } }) {
                newSortListEstate.add(estate)
            }
        }
        sortListEstate = newSortListEstate
    }

    private fun filterByMinIntegerValue(minValue: Int, filteredAttribute: String) {
        sortListEstate = when (filteredAttribute) {
            "price" -> sortListEstate.filter { it.price.toInt() >= minValue }
            "surface" -> sortListEstate.filter { it.surface.toInt() >= minValue }
            "picture" -> sortListEstate.filter { it.numberOfPicture >= minValue }
            else -> sortListEstate
        }
    }

    private fun filterByMaxIntegerValue(maxValue: Int, filteredAttribute: String) {
        sortListEstate = when (filteredAttribute) {
            "price" -> sortListEstate.filter { it.price.toInt() <= maxValue }
            "surface" -> sortListEstate.filter { it.surface.toInt() <= maxValue }
            else -> sortListEstate
        }
    }

    private fun filterByDate(startDate: String, endDate: String, filteredAttribute: String) {
        val newSortListEstate = mutableListOf<Estate>()
        calendar.time = dateFormat.parse(startDate) as Date
        val timeStart = calendar.timeInMillis
        calendar.time = dateFormat.parse(endDate) as Date
        val timeEnd = calendar.timeInMillis

        for (estate in sortListEstate) {
            when (filteredAttribute) {
                "soldDate" -> calendar.time = dateFormat.parse(estate.soldDate) as Date
                "saleDate" -> calendar.time = dateFormat.parse(estate.saleDate) as Date
            }

            if (calendar.timeInMillis in timeStart..timeEnd) {
                newSortListEstate.add(estate)
            }
        }

        sortListEstate = newSortListEstate
    }

    // picker date result
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val text = dateFormat.format(calendar.time)
        when (datePickParam) {
            "saleDateStart" -> {
                saleDateStart = text
                binding.btnSaleStart.text = text
            }
            "saleDateEnd" -> {
                saleDateEnd = text
                binding.btnSaleEnd.text = text
            }
            "soldDateStart" -> {
                soldDateStart = text
                binding.btnSoldStart.text = text
            }
            "soldDateEnd" -> {
                soldDateEnd = text
                binding.btnSoldEnd.text = text
            }
        }
    }

    private fun openDatePickerDial(datePick: String) {
        datePickParam = datePick
        DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}