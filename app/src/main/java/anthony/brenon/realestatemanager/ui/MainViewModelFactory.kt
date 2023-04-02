package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository

class MainViewModelFactory(
    private val agentRepository : AgentRepository,
    private val estateRepository : EstateRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(agentRepository, estateRepository) as T
            }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}