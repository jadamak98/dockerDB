package org.example.docker1


import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.*
import kotlin.system.exitProcess

class Database(val cntStr: String, val sqlStm: String, val csvPath: String = "/tmp", val fileName: String?) {
    var conn: Connection
    init {
        try {
            conn = DriverManager.getConnection(cntStr)
        } catch (e: SQLException) {
            println(e.printStackTrace())
            exitProcess(1)
        }
        println("connect to db successfully")
    }

    fun queryAndWrite(){
        try {
            val result = conn.prepareStatement(sqlStm).executeQuery()
            val writer = Files.newBufferedWriter(Paths.get("${csvPath}${fileName ?: conn.schema}.csv"))
            val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(result))
            csvPrinter.printRecords(result)
            csvPrinter.flush()
            csvPrinter.close()
            println("write csv successfully")
        }catch(e: SQLException){
            println(e.printStackTrace())
        }catch (e: Exception){
            println(e.printStackTrace())
        } finally {
            close()
        }
    }

    private fun close() {
        conn.close()
    }
}
