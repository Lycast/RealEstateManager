package anthony.brenon.realestatemanager.repository

import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.database.dao.AgentDAO
import kotlinx.coroutines.flow.Flow

class AgentRepository(private val agentDAO: AgentDAO) {

    val allAgents: Flow<List<Agent>> = agentDAO.getAllAgents()

    @WorkerThread
    suspend fun insertAgent(agent: Agent) {
        agentDAO.insert(agent)
    }

    @WorkerThread
    suspend fun deleteAgent(agent: Agent) {
        agentDAO.delete(agent)
    }
}