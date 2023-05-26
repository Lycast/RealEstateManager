package anthony.brenon.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.utils.ContentValuesConverter.fromContentValues
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class REMContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "anthony.brenon.realestatemanager.provider"
        const val TABLE_ESTATE = "table_estate"
        val URI_ESTATE: Uri = Uri.parse("content://$AUTHORITY/$TABLE_ESTATE")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        context?.let {
            val cursor = REMRoomDatabase.getDatabase(it).estateDao().getEstatesWithCursor()
            cursor.setNotificationUri(it.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_ESTATE"
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values?.let {
            val estate = fromContentValues(values)
            context?.let { context ->
                GlobalScope.launch {
                    REMRoomDatabase.getDatabase(context).estateDao().insert(estate)
                }
                context.contentResolver.notifyChange(uri, null)
                return Uri.parse("$URI_ESTATE/${estate.id}")
            }
        }
        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // We can't remove an estate
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // We don't need update an estate we replace when we insert estate
        return 0
    }
}