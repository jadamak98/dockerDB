package org.example.docker1

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.*


class Database(val cntStr: String) {
    private var conn: Connection? =null

    fun connect() {
        conn = DriverManager.getConnection(cntStr)
        if (conn == null) {
            throw Exception("Cannot connect database!")
        }
        conn?.autoCommit = false
        println("connect to db successfully")
    }

    fun queryAndWrite(sqlStm: String, csvPath: String?){
        try {
            val result = conn?.prepareStatement(sqlStm)?.executeQuery()
            val writer = Files.newBufferedWriter(Paths.get(csvPath?:"./tmp/"))
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

    fun readAndInsert(csvPaths: String, tbl: String, columnName: String){
        try {
            val reader = Files.newBufferedReader(Paths.get(csvPaths))
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
            val count = columnName.split(", ").count()
            if(count == csvParser.headerMap.count()) {
                for (record in csvParser.records) {
                    var insertStm = "Insert into ${conn?.schema}.$tbl ($columnName) values ("
                    for (i in 0 until record.size() - 1) {
                        insertStm = insertStm + "\'" + record[i] + "\'" + ", "
                    }
                    insertStm = insertStm + record.last() + ");"
                    conn?.prepareStatement(insertStm)?.executeUpdate()
                }
                conn?.commit()
                println("insert to db successfully")
            } else {
                throw Exception("Db table format not matched with insert statement")
            }
        }catch(e: SQLException){
            println(e.printStackTrace())
        }catch (e: Exception){
            println(e.printStackTrace())
        }finally {
            close()
        }
    }

    fun close() {
        conn?.close()
    }
}
