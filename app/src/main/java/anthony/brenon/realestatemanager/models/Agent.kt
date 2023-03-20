package anthony.brenon.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
class Agent (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "agent_id")
    val id : Int,
    @ColumnInfo(name = "agent_name")
    val nameAgent: String,
    @ColumnInfo(name = "agent_email")
    val emailAgent: String,
) {
    override fun toString(): String {
        return nameAgent
    }
}