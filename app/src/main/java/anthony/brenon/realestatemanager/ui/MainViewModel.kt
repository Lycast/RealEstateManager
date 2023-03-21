package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.utils.NavigationStates
import kotlinx.coroutines.launch

class MainViewModel(private val estateRepository: EstateRepository, private val agentRepository: AgentRepository) : ViewModel() {

    var estateSelected = MutableLiveData<Estate>()
    var agentSelected = MutableLiveData<Agent>()
    var navigationStates = MutableLiveData<NavigationStates>()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectThisAgent(agent: Agent) { agentSelected.value = agent }
    fun selectNavigationStates(navigationStates: NavigationStates) { this.navigationStates.value = navigationStates }

    // ROOM
    // - Repository is completely separated from the UI through the ViewModel.
    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(agent: Agent) = viewModelScope.launch {
        agentRepository.insert(agent)
    }
}