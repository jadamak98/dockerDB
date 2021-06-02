# etl

This program was created for 
1. Writing CSV file from Postgresql
2. Insert to Postgresql from CSV file

It supports running on Docker with docker-compose.

<b>Before starts</b>

Environment variable cntStr required before starts. It defines the connection string to Postgresql.

e.g. cntStr=jdbc:postgresql://10.151.128.191/company?user=postgres&password=fred&currentSchema=staff

<b> Writing CSV </b>

java -jar docker1-1.0.0.jar -w -cp "$CSV_PATH" -ss "$SQL_SELECT_STATEMENT"

e.g.  java -jar docker1-1.0.0.jar -w -cp "./staff.csv" -ss "select * from staff.employee" 

<b> Insert DB </b>

java -jar docker1-1.0.0.jar -i -cp "$CSV_PATH" -tn "$TABLE_NAME" -cn "COLUMN_NAME"

e.g. java -jar docker1-1.0.0.jar -i -cp "./staff.csv" -tn "employee" -cn "id, name, gender, department, email, salary"



    
