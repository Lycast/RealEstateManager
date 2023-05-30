package anthony.brenon.realestatemanager

import anthony.brenon.realestatemanager.ui.navigation.LoanSimulatorFragment
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UnitTestLoanSimulator {

    @Test
    fun calculateLoan_withValidInputs_shouldReturnCorrectResult() {
        // Given
        val loanAmount = 10000
        val interestRate = 5.0
        val loanTermYears = 5
        // When
        val result = LoanSimulatorFragment().calculateLoan(loanAmount, interestRate, loanTermYears)
        // Then
        val expected = 188.71 // The expected result for the given inputs
        val epsilon = 0.01 // A small value to account for floating-point precision
        assertEquals(expected, result, epsilon)
    }

    @Test
    fun calculateLoan_withZeroLoanAmount_shouldReturnZero() {
        // Given
        val loanAmount = 0
        val interestRate = 5.0
        val loanTermYears = 5
        // When
        val result = LoanSimulatorFragment().calculateLoan(loanAmount, interestRate, loanTermYears)
        // Then
        val expected = 0.0
        assertEquals(expected, result)
    }

    @Test
    fun calculateLoan_withZeroInterestRate_shouldReturnZero() {
        // Given
        val loanAmount = 10000
        val interestRate = 0.0
        val loanTermYears = 5
        // When
        val result = LoanSimulatorFragment().calculateLoan(loanAmount, interestRate, loanTermYears)
        // Then
        val expected = 0.0
        assertEquals(expected, result)
    }

    @Test
    fun calculateLoan_withZeroLoanTermYears_shouldReturnZero() {
        // Given
        val loanAmount = 10000
        val interestRate = 5.0
        val loanTermYears = 0
        // When
        val result = LoanSimulatorFragment().calculateLoan(loanAmount, interestRate, loanTermYears)
        // Then
        val expected = 0.0
        assertEquals(expected, result)
    }
}