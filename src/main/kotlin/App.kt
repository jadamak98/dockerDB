package org.example.docker1


val cntStr = System.getenv("cntstr")
val csvPath = System.getenv("csvpath")
val sqlStm = System.getenv("sqlStm")
val fileName = System.getenv("fileName")

fun main() {
    val db = Database(cntStr, sqlStm, csvPath, fileName )
    db.queryAndWrite()
}