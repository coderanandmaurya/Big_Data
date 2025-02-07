**Hadoop MapReduce Java code** for **Python** using the **Hadoop Streaming API**

### **Steps to Run Python MapReduce on Hadoop:**
1. Convert **Java Mapper & Reducer** to **Python scripts**.  
2. Use **Hadoop Streaming** to execute the Python scripts.  
3. Run the job on **Hadoop Cluster or Local Mode**.

---

## **1️⃣ Python Mapper (word_mapper.py)**
Equivalent of Java **WordMapper**:
```python
#!/usr/bin/env python3
import sys
import re

# Read input line by line
for line in sys.stdin:
    line = line.strip().lower()  # Normalize text (remove spaces, convert to lowercase)
    words = re.split(r'\W+', line)  # Split on non-word characters

    for word in words:
        if word:  # Ignore empty words
            print(f"{word}\t1")  # Output format: word \t count(1)
```
📌 **Explanation:**  
✅ Reads lines from input, converts to lowercase.  
✅ Splits words using regex (`\W+`).  
✅ Outputs **word and count (1)** in `Tab-separated format`.  

---

## **2️⃣ Python Reducer (sum_reducer.py)**
Equivalent of Java **SumReducer**:
```python
#!/usr/bin/env python3
import sys

current_word = None
current_count = 0

# Read sorted key-value pairs from stdin
for line in sys.stdin:
    word, count = line.strip().split("\t")  # Read input from Mapper
    count = int(count)

    if current_word == word:
        current_count += count  # Sum word occurrences
    else:
        if current_word:  # Print previous word count
            print(f"{current_word}\t{current_count}")
        current_word = word
        current_count = count

# Print the last word count
if current_word:
    print(f"{current_word}\t{current_count}")
```
📌 **Explanation:**  
✅ Reads key-value pairs (`word → count`).  
✅ Aggregates counts per word.  
✅ Outputs final **word-count pairs** in `Tab-separated format`.  

---

## **3️⃣ Running Python MapReduce on Hadoop**
### **Command to Run on Hadoop:**
```sh
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-*.jar \
    -input /input_dir \
    -output /output_dir \
    -mapper word_mapper.py \
    -reducer sum_reducer.py \
    -file word_mapper.py \
    -file sum_reducer.py
```
📌 **Explanation:**  
✅ `-mapper word_mapper.py` → Uses Python script as Mapper.  
✅ `-reducer sum_reducer.py` → Uses Python script as Reducer.  
✅ `-file` → Ensures the scripts are available on the cluster.  

---

## **4️⃣ Verifying Output**
After execution, check the output:
```sh
hdfs dfs -cat /output_dir/part-00000
```
This will show word counts in the format:
```
word1    10
word2    5
word3    20
```

### **Conclusion:**
✅ **Yes, you can write and run Python MapReduce on Hadoop** using **Hadoop Streaming**.  
✅ **Performance Consideration:** Java MapReduce is faster, but Python is easier to develop and maintain.  
✅ **Works on Hadoop Cluster & Local Mode**.  

Would you like further guidance on **setting up Hadoop** or **running the job**? 🚀
