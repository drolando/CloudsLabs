package fr.eurecom.dsg.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class OrderInversion extends Configured implements Tool {

	private final static String ASTERISK = "\0";

	public static class PartitionerTextPair extends Partitioner<TextPair, IntWritable> {
		@Override
		public int getPartition(TextPair key, IntWritable value,
				int numPartitions) {
			// TODO: implement getPartition such that pairs with the same first element
			// will go to the same reducer. You can use toUnsigned as utility.
			return toUnsigned(key.key1.toString().hashCode()) % numPartitions;
		}

		/**
		 * toUnsigned(10) = 10 toUnsigned(-1) = 2147483647
		 * 
		 * @param val
		 *            Value to convert
		 * @return the unsigned number with the same bits of val
		 * */
		public static int toUnsigned(int val) {
			return val & Integer.MAX_VALUE;
		}
	}

	public static class PairMapper extends
			Mapper<LongWritable, Text, TextPair, IntWritable> {

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws java.io.IOException, InterruptedException {

			int count;
            String line[] = value.toString().split("[^a-zA-Z]+");
            ArrayList<String> array = new ArrayList<String>();
            for (String word : line) {
                if (word.trim().length() == 0)
                    continue;
                word = word.toLowerCase();
                array.add(word.trim());
            }
            
            for (String k1 : array) {
                count = 0;		// Counter of the same word pairs, like [<w1, w2>, <w1, w3>] = 2
                for (String k2 : array) {
                    if (k1.compareTo(k2) != 0) {
                        count++;
                        TextPair p = new TextPair();
                        p.set(new Text(k1), new Text(k2));
                        context.write(p, new IntWritable(1));
                    }
                }
                TextPair p = new TextPair();
                p.set(new Text(k1), new Text(ASTERISK));
                context.write(p, new IntWritable(count));
            }
        }
	}

	public static class PairReducer extends
			Reducer<TextPair, IntWritable, TextPair, DoubleWritable> {
		
		double wordCount;

		@Override
		protected void reduce(TextPair key, // TODO: change Object to input key type
				Iterable<IntWritable> values, // TODO: change Object to input value type
				Context context) throws IOException, InterruptedException {

			Iterator<IntWritable> it = values.iterator();
			double count = 0;
			while (it.hasNext()) {
				count += it.next().get();
			}
			
			if(key.key2.toString().compareTo(ASTERISK) == 0){
				wordCount = count;
			}
			else {
                TextPair p = new TextPair();
                p.set(new Text(key.key1), new Text(key.key2));
                //context.write(p, new DoubleWritable(count/wordCount));
			}
            TextPair p = new TextPair();
            p.set(new Text(key.key1), new Text(key.key2));
            context.write(p, new DoubleWritable(count));
		}
	}

	private int numReducers;
	private Path inputPath;
	private Path outputDir;

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = this.getConf();

		Job job = new Job(conf); // TODO: define new job instead of null using
									// conf e setting a name

		// TODO: set job input format
		job.setInputFormatClass(TextInputFormat.class);
		// TODO: set map class and the map output key and value classes
		job.setMapperClass(PairMapper.class);
		job.setMapOutputKeyClass(TextPair.class);
		job.setMapOutputValueClass(IntWritable.class);
		// TODO: set reduce class and the reduce output key and value classes
		job.setReducerClass(PairReducer.class);
		job.setOutputKeyClass(TextPair.class);
		job.setOutputValueClass(DoubleWritable.class);
		// TODO: set job output format
		job.setOutputFormatClass(TextOutputFormat.class);
		// TODO: add the input file as job input (from HDFS) to the variable
		// inputFile
		TextInputFormat.setInputPaths(job, this.inputPath);
		// TODO: set the output path for the job results (to HDFS) to the
		// variable
		// outputPath
		TextOutputFormat.setOutputPath(job, this.outputDir);
		// TODO: set the number of reducers using variable numberReducers
		job.setNumReduceTasks(this.numReducers);
		// TODO: set the jar class
		job.setJarByClass(Pair.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	OrderInversion(String[] args) {
		if (args.length != 3) {
			System.out
					.println("Usage: OrderInversion <num_reducers> <input_file> <output_dir>");
			System.exit(0);
		}
		this.numReducers = Integer.parseInt(args[0]);
		this.inputPath = new Path(args[1]);
		this.outputDir = new Path(args[2]);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new OrderInversion(args),
				args);
		System.exit(res);
	}
}
