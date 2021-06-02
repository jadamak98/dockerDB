package org.example.docker1

import com.intellij.openapi.rd.defineNestedLifetime
import com.intellij.ui.colorpicker.HORIZONTAL_MARGIN_TO_PICKER_BORDER
import org.apache.commons.cli.*




const val WRITE_OPTION = "w"
const val INSERT_OPTION = "i"
const val SQL_STM_OPTION = "ss"
const val CSV_PATH_OPTION = "cp"
const val TABLE_NAME_OPTION = "tn"
const val COLUMN_NAME_OPTION = "cn"

fun main(args: Array<String>) {
    val cntStr = System.getenv("cntStr")
    val db = Database(cntStr)
    val formatter = HelpFormatter()
    val options = Options()
    try {
        db.connect()
        //cmd options
        options.addOption(Option(WRITE_OPTION, false, "writeCSV").apply { this.isRequired = false })
        options.addOption(Option(INSERT_OPTION, false, "insertDB").apply { this.isRequired = false })
        options.addOption(Option(SQL_STM_OPTION, true, "sql statement - required for WriteCSV function").apply { this.isRequired = false })
        options.addOption(Option(CSV_PATH_OPTION, true, "csv path - required for WriteCSV and InsertDB function").apply { this.isRequired = true })
        options.addOption(Option(TABLE_NAME_OPTION, true, "sql table name - required for InsertDB function").apply { this.isRequired = false })
        options.addOption(Option(COLUMN_NAME_OPTION, true, "column name - required for InsertDB function").apply { this.isRequired = false })

        val cmd = DefaultParser().parse(options, args)
        val csvPath = cmd.getOptionValue(CSV_PATH_OPTION) ?: throw Exception("csv path arg -$CSV_PATH_OPTION undefined")


        when {
            cmd.hasOption(WRITE_OPTION) -> {
                db.queryAndWrite(cmd.getOptionValue(SQL_STM_OPTION) ?: throw Exception("sql statement arg -$SQL_STM_OPTION undefined"), csvPath)
            }
            cmd.hasOption(INSERT_OPTION) -> {
                db.readAndInsert(csvPath,
                    cmd.getOptionValue(TABLE_NAME_OPTION) ?: throw Exception("table name arg -$TABLE_NAME_OPTION undefined"),
                    cmd.getOptionValue(COLUMN_NAME_OPTION) ?: throw Exception("column name arg -$COLUMN_NAME_OPTION undefined"))
            }
            else -> {
                throw Exception("action undefined")
            }
        }
    } catch (e: Exception) {
        println(e.printStackTrace())
        formatter.printHelp("etl", options)
    } finally {
        db.close()
    }
}





