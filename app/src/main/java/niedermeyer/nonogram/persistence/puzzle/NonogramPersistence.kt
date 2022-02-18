package niedermeyer.nonogram.persistence.puzzle

import android.content.Context
import niedermeyer.nonogram.gui.activity.GameActivity
import niedermeyer.nonogram.logics.Nonogram
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.logging.Level
import java.util.logging.Logger

class NonogramPersistence(val context: Context) {

    private val fileName = "nonogram"

    fun savePuzzle(nonogram: Nonogram) {
        val groupCountFile = File(context.filesDir, fileName)
        if (!groupCountFile.exists()) {
            // create a new file if it doesn't exist
            try {
                groupCountFile.createNewFile()
            } catch (e: Exception) {
                Logger.getLogger(GameActivity::class.java.name).log(Level.WARNING, null, e)
            }
        }
        writeNonogramObject(nonogram)
    }

    fun loadPuzzle(): Nonogram? {
        val countsFile = File(context.filesDir, fileName)
        return if (countsFile.exists()) {
            readNonogramObject()
        } else {
            null
        }
    }

    private fun writeNonogramObject(puzzle: Nonogram) {
        var out: ObjectOutputStream? = null
        try {
            // write the object
            out = ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE))
            out.writeObject(puzzle)
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

    private fun readNonogramObject(): Nonogram? {
        var readObject: Nonogram? = null
        var `in`: ObjectInputStream? = null
        try {
            // read the object
            `in` = ObjectInputStream(context.openFileInput(fileName))
            readObject = `in`.readObject() as Nonogram
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