package anthony.brenon.realestatemanager.ui

import android.content.Context
import androidx.lifecycle.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.utils.EstateStatus
import anthony.brenon.realestatemanager.utils.MonetaryUnit
import kotlinx.coroutines.launch

class MainViewModel(private val agentRepository : AgentRepository, private val estateRepository : EstateRepository) : ViewModel() {

    var estateSelected = MutableLiveData<Estate>()
    var agentSelected = MutableLiveData<Agent>()
    var estateStatus = MutableLiveData<EstateStatus>()
    var monetaryUnit = MutableLiveData<MonetaryUnit>()


    fun selectThisAgent(agent: Agent) { agentSelected.value = agent }

    fun agentIsConnected() : Boolean { return agentSelected.value != null }

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectThisEstateStatus(status: EstateStatus) { estateStatus.value = status }
    fun selectThisMonetaryUnit(unity: MonetaryUnit) { monetaryUnit.value = unity }


    // AGENT ROOM
    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()

    fun insertAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.insertAgent(agent)
    }

    fun deleteAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.deleteAgent(agent)
    }

    // ESTATES ROOM
    val allEstates: LiveData<List<Estate>> = estateRepository.allEstates.asLiveData()

    fun insertEstate(context: Context,estate: Estate) = liveData {
        try {
            val response = estateRepository.insertEstate(context, estate)
            emit(response)
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun updateEstate(estate: Estate) = viewModelScope.launch {
        estateRepository.updateEstate(estate)
    }

    // PICTURES ROOM
    fun insertPicture(picture: Picture) = viewModelScope.launch {
        estateRepository.insertPicture(picture)
    }

    fun deletePicture(picture: Picture) = viewModelScope.launch {
        estateRepository.deletePicture(picture)
    }

    fun getPicturesByEstate(estateId: Long) : LiveData<List<Picture>> { return estateRepository.getPicturesByEstate(estateId).asLiveData() }
}