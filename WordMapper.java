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
