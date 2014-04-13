package fr.eurecom.dsg.mapreduce;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count example of MapReduce job. Given a plain text in input, this job
 * counts how many occurrences of each word there are in that text and writes
 * the result on HDFS.
 * 
 */
public class WordCount extends Configured implements Tool {

  private int numReducers;
  private Path inputPath;
  private Path outputDir;

  @Override
  public int run(String[] args) throws Exception {

    Configuration conf = this.getConf();
    Job job = new Job(conf);

    // TODO: set job input format
    job.setInputFormatClass(TextInputFormat.class);
    // TODO: set map class and the map output key and value classes
    job.setMapperClass(WCMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(LongWritable.class);
    
    
    // TODO: set reduce class and the reduce output key and value classes
    job.setReducerClass(WCReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    
    // TODO: set job output format
    job.setOutputFormatClass(TextOutputFormat.class);
    // TODO: add the input file as job input (from HDFS) to the variable
    //       inputFile
    TextInputFormat.setInputPaths(job, this.inputPath);
    // TODO: set the output path for the job results (to HDFS) to the variable
    //       outputPath
    TextOutputFormat.setOutputPath(job, this.outputDir);
    // TODO: set the number of reducers using variable numberReducers
    job.setNumReduceTasks(this.numReducers);
    // TODO: set the jar class
    job.setJarByClass(WordCount.class);
    return job.waitForCompletion(true) ? 0 : 1; // this will execute the job
  }
  
  public WordCount (String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: WordCount <num_reducers> <input_path> <output_path>");
      System.exit(0);
    }
    this.numReducers = Integer.parseInt(args[0]);
    this.inputPath = new Path(args[1]);
    this.outputDir = new Path(args[2]);
  }
  
  public static void main(String args[]) throws Exception {
    int res = ToolRunner.run(new Configuration(), new WordCount(args), args);
    System.exit(res);
  }
}

class WCMapper extends Mapper<LongWritable, // TODO: change Object to input key type
                              Text, // TODO: change Object to input value type
                              Text, // TODO: change Object to output key type
                              LongWritable> { // TODO: change Object to output value type

  @Override
  protected void map(LongWritable key, // TODO: change Object to input key type
                     Text value, // TODO: change Object to input value type
			Context context) throws IOException, InterruptedException {

		String line[] = value.toString().split("[^a-zA-Z]+");

		for (String word : line) {
			if (word.trim().length() == 0)
				continue;
			word = word.toLowerCase();
			context.write(new Text(word), new LongWritable(1));
		}
		// TODO: implement the map method (use context.write to emit results)

	}

}

class WCReducer extends Reducer<Text, // TODO: change Object to input key type
								LongWritable, // .TODO: change Object to input value type
                                Text, // TODO: change Object to output key type
                                LongWritable> { // TODO: change Object to output value type

  @Override
  protected void reduce(Text key, // TODO: change Object to input key type
                        Iterable<LongWritable> values, // TODO: change Object to input value type
                        Context context) throws IOException, InterruptedException {
	  Iterator<LongWritable> it = values.iterator();
	  long count = 0;
	  while (it.hasNext()) {
		  count += it.next().get();
	  }
	  context.write(key, new LongWritable(count));
    // TODO: implement the reduce method (use context.write to emit results)
  }
}
