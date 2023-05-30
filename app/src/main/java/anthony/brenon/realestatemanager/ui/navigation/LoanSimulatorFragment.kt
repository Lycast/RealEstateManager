package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentLoanSimulatorBinding
import anthony.brenon.realestatemanager.ui.MainViewModel
import java.text.DecimalFormat
import kotlin.math.pow

class LoanSimulatorFragment : Fragment() {

    private var _binding: FragmentLoanSimulatorBinding? = null
    val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private var monetary: Boolean = false

    private var interestRate = 0.0
    private var loanTermYears = 0

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
        viewModel.monetarySwitch.observe(viewLifecycleOwner) { monetary = it }

        // Set the SeekBar listeners
        binding.seekbarInterestRate.setOnSeekBarChangeListener(createSeekBarListener())
        binding.seekbarLoanTerm.setOnSeekBarChangeListener(createSeekBarListener())

        binding.imgSimulatorBack.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
        binding.buttonCalculate.setOnClickListener { getAndDisplayLoan() }

        displaySeekbarValue()
    }

    fun calculateLoan(loanAmount: Int, interestRate: Double, loanTermYears: Int): Double {
        // Check if any of the input values is zero
        if (loanAmount == 0 || interestRate == 0.0 || loanTermYears == 0) {
            // If any input value is zero, return 0 as the loan amount
            return 0.0
        }

        // Calculate the loan amount by month
        val loanTermMonths = loanTermYears * 12
        val monthlyInterestRate = interestRate / 12 / 100.0
        return loanAmount * monthlyInterestRate / (1 - (1 + monthlyInterestRate).pow(-loanTermMonths))
    }

    private fun getSeekbarValue() {
        // Get and display the seekbar values
        loanTermYears = binding.seekbarLoanTerm.progress
        interestRate = (binding.seekbarInterestRate.progress / 10.0)
        displaySeekbarValue()
    }

    private fun displaySeekbarValue() {
        binding.textViewLoanTerm.text = loanTermYears.toString()
        binding.textViewInterestRate.text = interestRate.toString()
    }

    private fun displayLoanResult(monthlyPayment: Double, monetary: Boolean) {
        val dec = DecimalFormat("#,###.##")
        val resultText = if (monetary) "\u20AC ${dec.format(monthlyPayment)}" else "$ ${dec.format(monthlyPayment)}"
        binding.textViewResult.text = resultText
    }

    private fun getAndDisplayLoan() {
        val loanAmount = binding.editTextLoanAmount.text.toString().toInt()

        if (interestRate > 0 && loanTermYears > 0) {
            val monthlyPayment = calculateLoan(loanAmount, interestRate, loanTermYears)
            displayLoanResult(monthlyPayment, monetary)
        }
    }

    private fun createSeekBarListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getSeekbarValue()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }
}