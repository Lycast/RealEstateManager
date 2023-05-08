package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentLoanSimulatorBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class LoanSimulatorFragment : Fragment() {

    private var _binding: FragmentLoanSimulatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanSimulatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgSimulatorBack.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
        binding.buttonCalculate.setOnClickListener { calculateLoan() }
    }

    private fun calculateLoan() {
        // Get the input values
        val loanAmount = binding.editTextLoanAmount.text.toString().toDoubleOrNull() ?: return
        val interestRate = binding.editTextInterestRate.text.toString().toDoubleOrNull() ?: return
        val loanTermYears = binding.editTextLoanTerm.text.toString().toIntOrNull() ?: return

        // Calculate the loan
        val loanTermMonths = loanTermYears * 12
        val monthlyInterestRate = interestRate / 12 / 100
        val monthlyPayment =
            loanAmount * monthlyInterestRate / (1 - (1 + monthlyInterestRate).pow(-loanTermMonths))

        // Display the result
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        binding.textViewResult.text = formatter.format(monthlyPayment)
    }
}