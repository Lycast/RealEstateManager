package anthony.brenon.realestatemanager.ui

import android.content.Context
import android.location.Location
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
    var sortListEstate = MutableLiveData<List<Estate>>()
    var locationLivedata = MutableLiveData<Location>()

    val allAgents: LiveData<List<Agent>> = agentRepository.allAgents.asLiveData()
    val allEstates: LiveData<List<Estate>> = estateRepository.allEstates.asLiveData()

    fun selectThisEstate(estate: Estate) { estateSelected.value = estate }
    fun selectThisAgent(agent: Agent) { agentSelected.value = agent }
    fun selectThisEstateStatus(status: EstateStatus) { estateStatus.value = status }
    fun selectThisMonetaryUnit(unity: MonetaryUnit) { monetaryUnit.value = unity }
    fun updateSortListEstate(list: List<Estate>) { sortListEstate.value = list }
    fun updateLocation(location: Location) { locationLivedata.value = location }

    fun agentIsConnected() : Boolean { return agentSelected.value != null }

    // AGENT ROOM
    fun insertAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.insertAgent(agent)
    }

    fun deleteAgent(agent: Agent) = viewModelScope.launch {
        agentRepository.deleteAgent(agent)
    }

    // ESTATES ROOM
    fun insertEstate(context: Context,estate: Estate) = liveData {
        try {
            val response = estateRepository.insertEstate(context, estate)
            emit(response)
        } catch (e: Exception) { e.printStackTrace() }
    }

    // PICTURES ROOM
    fun insertPicture(picture: Picture) = viewModelScope.launch {
        estateRepository.insertPicture(picture)
    }

    fun getPicturesByEstate(estateId: Long) : LiveData<List<Picture>> { return estateRepository.getPicturesByEstate(estateId).asLiveData() }
}