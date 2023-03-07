package anthony.brenon.realestatemanager.di

import anthony.brenon.realestatemanager.models.Agent

class AgentList() {

    fun getAgentList(): List<Agent> {
        return listOf(
            Agent(0,"Simon","Seller DEMO"),
            Agent(1,"Paul","Study DEMO")
        )
    }
}