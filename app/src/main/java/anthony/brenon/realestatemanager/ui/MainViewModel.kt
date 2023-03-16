package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.utils.NavigationStates
import kotlinx.coroutines.launch

class MainViewModel (private val estateRepository: EstateRepository, private val agentRepository: AgentRepository) : ViewModel() {

    //val allAgent: LiveData<List<Agent>> = agentRepository.allAgent.asLiveData()
    var estateSelected = MutableLiveData<Estate>()
    var detailsNavigationStates = MutableLiveData<NavigationStates>()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectDetailsStates(navigationStates: NavigationStates) { detailsNavigationStates.value = navigationStates }



    // ROOM
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(agent: Agent) = viewModelScope.launch {
        agentRepository.insert(agent)
    }
}