### **Explanation of Java Word Count Program (Hadoop MapReduce)**  

This **MapReduce** program counts the occurrences of each word in a given input text file. It consists of three main components:  
1. **Mapper (`WordMapper.java`)** – Extracts words and assigns them a count of `1`.  
2. **Reducer (`SumReducer.java`)** – Aggregates word counts.  
3. **Driver (`WordCount.java`)** – Configures and runs the job on **Hadoop**.  

---

## **1️⃣ Mapper Class (WordMapper.java)**  
This class **processes input line by line**, extracts words, and assigns an initial count of **1** to each word.  

### **Code:**
```java
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    
    private final Text wordText = new Text();  // Reuse Text object
    private static final IntWritable ONE = new IntWritable(1); // Constant for efficiency
    
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().toLowerCase().trim(); // Normalize text
        
        for (String word : line.split("\\W+")) { // Split on non-word characters
            if (!word.isEmpty()) { // Ensure non-empty words
                wordText.set(word); // Reuse object
                context.write(wordText, ONE);
            }
        }
    }
}
```
### **Working of Mapper:**
- Each line of input is received by the `map()` function.  
- **`LongWritable key`** → Byte offset of the line in the file (ignored in this case).  
- **`Text value`** → The actual line of text.  
- Converts the line to **lowercase** for uniformity.  
- **Splits** the line into words using `\\W+` (splitting on non-word characters).  
- **Outputs (Key, Value) pairs** → `(word, 1)`.  

📌 **Example:**  
**Input Line:**  
```
Hadoop is great. Hadoop is powerful!
```
**Mapper Output:**  
```
hadoop    1  
is        1  
great     1  
hadoop    1  
is        1  
powerful  1  
```
Each word is emitted with an initial count of `1`.  

---

## **2️⃣ Reducer Class (SumReducer.java)**  
This class **aggregates the counts for each word** received from multiple mapper outputs.  

### **Code:**
```java
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    private IntWritable result = new IntWritable(); // Reuse object to reduce GC overhead
    
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)  
            throws IOException, InterruptedException {
        
        int sum = 0; // Clearer variable name
        for (IntWritable value : values) {
            sum += value.get();
        }
        
        result.set(sum);
        context.write(key, result);
    }
}
```
### **Working of Reducer:**
- The `reduce()` method receives a **word (key)** and **a list of counts (values)** from the mapper.  
- It **sums up** all counts for each word.  
- **Writes the final (word, count) pair to output**.  

📌 **Example:**  
**Reducer Input (Grouped by Key):**  
```
hadoop    (1, 1)  
is        (1, 1)  
great     (1)  
powerful  (1)  
```
**Reducer Output:**  
```
hadoop    2  
is        2  
great     1  
powerful  1  
```
Here, "hadoop" appeared **twice**, so the count is `2`.  

---

## **3️⃣ Driver Class (WordCount.java)**
This is the **main class** that sets up and runs the **MapReduce Job**.  

### **Code:**
```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <input path> <output path>");
            System.exit(-1);
        }

        // Create Configuration and Job instance
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Word Count");

        job.setJarByClass(WordCount.class);

        // Set Input and Output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Set Mapper and Reducer classes
        job.setMapperClass(WordMapper.class);
        job.setReducerClass(SumReducer.class);

        // Set Output Key-Value Classes
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // Run job and exit based on success
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```
### **Working of Driver:**
- **Takes two command-line arguments** (input file & output directory).  
- **Creates a Hadoop job** and sets its configuration.  
- **Defines Mapper & Reducer classes**.  
- **Defines input/output types** for both Mapper & Reducer.  
- **Executes the job** and waits for completion.  

📌 **Example Command to Run on Hadoop:**
```sh
hadoop jar WordCount.jar WordCount /input /output
```

---

## **📌 Summary of Execution Flow**
1️⃣ **Input File (HDFS)**
```
hadoop is great
hadoop is powerful
```
2️⃣ **Mapper Output**
```
hadoop    1
is        1
great     1
hadoop    1
is        1
powerful  1
```
3️⃣ **Shuffling & Sorting (Hadoop Internal Process)**
```
great     (1)
hadoop    (1, 1)
is        (1, 1)
powerful  (1)
```
4️⃣ **Reducer Output (Final)**
```
great     1
hadoop    2
is        2
powerful  1
```

---

## **📌 Key Takeaways**
✅ **Mapper** extracts words and emits `(word, 1)`.  
✅ **Hadoop Framework** **groups** all occurrences of the same word.  
✅ **Reducer** sums up counts for each word and produces final results.  
✅ The **Driver** class sets up and runs the **MapReduce Job**.  

This is how **Hadoop MapReduce** processes **big data** in a distributed way! 🚀  
