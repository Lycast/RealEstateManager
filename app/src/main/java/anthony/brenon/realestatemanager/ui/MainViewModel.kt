package anthony.brenon.realestatemanager.ui

import androidx.lifecycle.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.launch

class MainViewModel(private val agentRepository : AgentRepository, private val estateRepository : EstateRepository) : ViewModel() {

    var hasAllPermissions: Boolean = false
    var isNewEstate: Boolean = true
    var monetarySwitch: Boolean = false
    var estateSelected = MutableLiveData<Estate>()
    var agentSelected = MutableLiveData<Agent?>()
    var sortListEstate = MutableLiveData<List<Estate>>()

    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()
    val allEstates: LiveData<List<Estate>> = estateRepository.allEstates.asLiveData()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectThisAgent(agent: Agent?) { agentSelected.value = agent }
    fun updateSortListEstate(list: List<Estate>) { sortListEstate.value = list }

    fun agentIsConnected() : Boolean { return agentSelected.value != null }

    // AGENT ROOM
    fun insertAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.insertAgent(agent)
    }

    fun deleteAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.deleteAgent(agent)
    }

    // ESTATES ROOM
    fun insertEstate(estate: Estate) = viewModelScope.launch {
        estateRepository.insertEstate(estate)
    }
}