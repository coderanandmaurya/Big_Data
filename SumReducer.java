import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    private IntWritable result = new IntWritable(); // Reuse object to reduce GC overhead
    
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)  
            throws IOEx
