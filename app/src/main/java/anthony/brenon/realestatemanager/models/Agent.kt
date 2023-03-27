package anthony.brenon.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
class Agent (

    @ColumnInfo(name = "agent_name")
    val nameAgent: String,
    @ColumnInfo(name = "agent_email")
    val emailAgent: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    override fun toString(): String {
        return nameAgent
    }
}