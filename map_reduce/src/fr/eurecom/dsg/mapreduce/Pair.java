package fr.eurecom.dsg.mapreduce;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Pair extends Configured implements Tool {

  public static class PairMapper 
   extends Mapper<LongWritable, // TODO: change Object to input key type
                  Text, // TODO: change Object to input value type
                  TextPair, // TODO: change Object to output key type
                  LongWritable> { // TODO: change Object to output value type
	  
	  // TODO: implement mapper
	  @Override
	  protected void map(LongWritable key, // TODO: change Object to input key type
	                     Text value, // TODO: change Object to input value type
	                     Context context) throws IOException, InterruptedException {
		  String[] words = value.toString().split(" ");
		  for(int i=0; i<words.length; i++) {
			  for (int j=0; j<words.length; j++) {
				  if (i!=j && words[i].compareTo(words[j]) != 0) {
					  TextPair p = new TextPair();
					  p.set(new Text(words[i]), new Text(words[j]));
					  context.write(p, new LongWritable(1));
				  }
			  }
		  }		  
	  }
  }

  public static class PairReducer
    extends Reducer<TextPair, // TODO: change Object to input key type
                    LongWritable, // TODO: change Object to input value type
                    Text, // TODO: change Object to output key type
                    LongWritable> { // TODO: change Object to output value type
    // TODO: implement reducer
	  @Override
	  protected void reduce(TextPair key, // TODO: change Object to input key type
	                        Iterable<LongWritable> values, // TODO: change Object to input value type
	                        Context context) throws IOException, InterruptedException {
		  Iterator<LongWritable> it = values.iterator();
		  long count = 0;
		  while (it.hasNext()) {
			  count += it.next().get();
		  }
		  context.write(new Text(key.toString()), new LongWritable(count));
	    // TODO: implement the reduce method (use context.write to emit results)
	  }
  }

  private int numReducers;
  private Path inputPath;
  private Path outputDir;

  public Pair(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: Pair <num_reducers> <input_path> <output_path>");
      System.exit(0);
    }
    this.numReducers = Integer.parseInt(args[0]);
    this.inputPath = new Path(args[1]);
    this.outputDir = new Path(args[2]);
  }
  

  @Override
  public int run(String[] args) throws Exception {

    Configuration conf = this.getConf();
    Job job = new Job(conf);  // TODO: define new job instead of null using conf e setting a name
    
    
    // TODO: set job input format
  
    job.setInputFormatClass(TextInputFormat.class);
    // TODO: set map class and the map output key and value classes
    job.setMapperClass(PairMapper.class);
    job.setMapOutputKeyClass(TextPair.class);
    job.setMapOutputValueClass(LongWritable.class);
    // TODO: set reduce class and the reduce output key and value classes
    job.setReducerClass(PairReducer.class);
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
    job.setJarByClass(Pair.class);
    
    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new Pair(args), args);
    System.exit(res);
  }
}
