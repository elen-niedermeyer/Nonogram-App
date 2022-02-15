package niedermeyer.nonogram.persistence.puzzle

import android.content.Context
import niedermeyer.nonogram.gui.activity.GameActivity
import niedermeyer.nonogram.logics.CountValue
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
class NonogramCountPersistence(val context: Context) {

    /**
     * File names
     */
    private val _rowCountsFileName = "counts_row"
    private val _columnCountsFileName = "counts_column"

    fun saveGroupCount(groupCount: ArrayList<ArrayList<CountValue>>, isRowCount: Boolean) {
        // make file object
        val fileName = if (isRowCount) _rowCountsFileName else _columnCountsFileName
        val groupCountFile = File(context.filesDir, fileName)
        // look if the file exists
        if (!groupCountFile.exists()) {
            // create a new file if it doesn't exist
            try {
                groupCountFile.createNewFile()
            } catch (e: Exception) {
                Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
            }
        }
        writeGroupCountObject(groupCount, fileName)
    }

    fun loadRowCount(): ArrayList<ArrayList<CountValue>>? {
        val countsFile = File(context.filesDir, _rowCountsFileName)
        return if (countsFile.exists()) {
            readGroupCountObject(_rowCountsFileName)
        } else {
            null
        }
    }

    fun loadColumnCount(): ArrayList<ArrayList<CountValue>>? {
        val countsFile = File(context.filesDir, _columnCountsFileName)
        return if (countsFile.exists()) {
            readGroupCountObject(_columnCountsFileName)
        } else {
            null
        }
    }

    /**
     * Writes the given object to the given file.
     *
     * @param pObject   the data that should be saved
     * @param pFileName the name of the file in which the data should be wrote
     */
    private fun writeGroupCountObject(pObject: ArrayList<ArrayList<CountValue>>, pFileName: String) {
        var out: ObjectOutputStream? = null
        try {
            // write the object
            out = ObjectOutputStream(context.openFileOutput(pFileName, Context.MODE_PRIVATE))
            out.writeObject(pObject)
        } catch (e: Exception) {
            Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
        } finally {
            // close the stream
            try {
                out!!.close()
            } catch (e: Exception) {
                Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
            }
        }
    }

    /**
     * Reads a group count from the given file.
     *
     * @param pFileName the name of the file which contains the data
     * @return the read data
     */
    private fun readGroupCountObject(pFileName: String): ArrayList<ArrayList<CountValue>>? {
        var readObject: ArrayList<ArrayList<CountValue>>? = null
        var `in`: ObjectInputStream? = null
        try {
            // read the object
            `in` = ObjectInputStream(context.openFileInput(pFileName))
            readObject = `in`.readObject() as ArrayList<ArrayList<CountValue>>
        } catch (e: Exception) {
            Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
        } finally {
            // close the stream
            try {
                `in`!!.close()
            } catch (e: Exception) {
                Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
            }
        }
        return readObject
    }
}