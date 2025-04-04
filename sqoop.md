# **Sqoop Commands with Explanation**  

Apache **Sqoop** (SQL to Hadoop) is a tool used for transferring data between relational databases and Hadoop. Below is a list of all essential **Sqoop commands** with explanations and examples.  

---

## **1. Sqoop Version**
```sh
sqoop version
```
- Displays the currently installed version of **Sqoop**.

---

## **2. Sqoop List Databases**
```sh
sqoop list-databases --connect jdbc:mysql://localhost:3306/ --username root --password root
```
- Lists all the databases available in the **MySQL server**.

---

## **3. Sqoop List Tables**
```sh
sqoop list-tables --connect jdbc:mysql://localhost:3306/dbname --username root --password root
```
- Lists all tables of a specific database (**dbname**) from the relational database.

---

## **4. Sqoop Import (RDBMS → HDFS)**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --target-dir /user/hadoop/employees \
--num-mappers 1
```
- Imports the entire **"employees"** table from **MySQL** into **HDFS** at `/user/hadoop/employees`.
- `--num-mappers 1`: Limits parallel processing to **1 mapper** (default is 4).

### **4.1 Import Only Specific Columns**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --columns "emp_id, emp_name, salary" \
--target-dir /user/hadoop/employees
```
- Imports only the specified columns (`emp_id, emp_name, salary`).

### **4.2 Import Using a Query**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--query "SELECT emp_id, emp_name FROM employees WHERE salary > 50000 AND \$CONDITIONS" \
--target-dir /user/hadoop/employees
```
- Imports data **filtered using SQL queries**.
- `\$CONDITIONS` is mandatory in the **WHERE clause** (used internally by Sqoop for splitting).

### **4.3 Import Data as a Sequence File**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --target-dir /user/hadoop/employees_seq \
--as-sequencefile
```
- Stores data in **Hadoop Sequence File format** (binary compressed).

### **4.4 Import Data as a Parquet File**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --target-dir /user/hadoop/employees_parquet \
--as-parquetfile
```
- Stores data in **Parquet format** (columnar storage, highly efficient for analytics).

### **4.5 Import Data in Avro Format**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --target-dir /user/hadoop/employees_avro \
--as-avrodatafile
```
- Imports data in **Avro format** (binary, schema-based).

---

## **5. Sqoop Incremental Import**
Used when we need to import only **new or updated** records.

### **5.1 Append Mode**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --incremental append \
--check-column emp_id --last-value 100 \
--target-dir /user/hadoop/employees_incremental
```
- Only imports **new records** where `emp_id > 100`.

### **5.2 Last Modified Mode**
```sh
sqoop import --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --incremental lastmodified \
--check-column last_updated \
--last-value "2024-04-01" \
--target-dir /user/hadoop/employees_incremental
```
- Imports rows modified **after April 1, 2024**.

---

## **6. Sqoop Import All Tables**
```sh
sqoop import-all-tables --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--warehouse-dir /user/hadoop/warehouse
```
- Imports **all tables** from `dbname` into HDFS.

---

## **7. Sqoop Export (HDFS → RDBMS)**
```sh
sqoop export --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees \
--export-dir /user/hadoop/employees
```
- Exports data from **HDFS** into the `employees` table in **MySQL**.

---

## **8. Sqoop Job (Save & Execute)**
### **8.1 Create a Job**
```sh
sqoop job --create myjob --import \
--connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees --target-dir /user/hadoop/employees
```
- Saves the **import command** as a reusable job named `myjob`.

### **8.2 Execute the Job**
```sh
sqoop job --exec myjob
```
- Runs the previously created **Sqoop job**.

### **8.3 List All Jobs**
```sh
sqoop job --list
```
- Displays all saved Sqoop jobs.

### **8.4 Delete a Job**
```sh
sqoop job --delete myjob
```
- Deletes the **Sqoop job** named `myjob`.

---

## **9. Sqoop Merge (Combine Incremental Imports)**
```sh
sqoop merge --new-data /user/hadoop/employees_incremental \
--onto /user/hadoop/employees_old \
--target-dir /user/hadoop/employees_merged \
--merge-key emp_id
```
- Merges **new data** (`employees_incremental`) with **old data** (`employees_old`).
- Overwrites records based on **emp_id**.

---

## **10. Sqoop Metastore**
### **10.1 Start Sqoop Metastore**
```sh
sqoop metastore
```
- Starts a **Sqoop metastore server** (stores Sqoop job metadata).

### **10.2 Connect to a Metastore**
```sh
sqoop --meta-connect jdbc:mysql://localhost:3306/sqoop_metastore --username root --password root
```
- Connects to a **remote metastore** for Sqoop jobs.

---

## **11. Sqoop Eval (Run SQL Queries)**
```sh
sqoop eval --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--query "SELECT COUNT(*) FROM employees"
```
- Runs **SQL queries** directly from Sqoop.

---

## **12. Sqoop Codegen (Generate Java Code)**
```sh
sqoop codegen --connect jdbc:mysql://localhost:3306/dbname \
--username root --password root \
--table employees
```
- Generates **Java class** for the `employees` table (useful for custom MapReduce programs).

---

## **13. Sqoop Help**
```sh
sqoop help
```
- Displays a list of all available Sqoop commands.

```sh
sqoop help import
```
- Displays help for the **import command**.

---

## **Conclusion**
These **Sqoop commands** cover all major functionalities, including **data import, export, incremental updates, jobs, merging, and metastore management**.
