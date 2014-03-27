package fr.eurecom.dsg.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Stripes extends Configured implements Tool {

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
		job.setMapperClass(StripesMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(StringToIntMapWritable.class);

		// TODO: set reduce class and the reduce output key and value classes
		job.setReducerClass(StripesReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

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
		job.setJarByClass(Stripes.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public Stripes(String[] args) {
		if (args.length != 3) {
			System.out
					.println("Usage: Stripes <num_reducers> <input_path> <output_path>");
			System.exit(0);
		}
		this.numReducers = Integer.parseInt(args[0]);
		this.inputPath = new Path(args[1]);
		this.outputDir = new Path(args[2]);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new Stripes(args), args);
		System.exit(res);
	}
}

class StripesMapper extends Mapper<LongWritable, // TODO: change Object to input key
											// type
		Text, // TODO: change Object to input value type
		Text, // TODO: change Object to output key type
		StringToIntMapWritable> { // TODO: change Object to output value type

	@Override
	public void map(LongWritable key, // TODO: change Object to input key type
			Text text, // TODO: change Object to input value type
			Context context) throws java.io.IOException, InterruptedException {
		
		HashMap<String, StringToIntMapWritable> list = new HashMap<String, StringToIntMapWritable>();
		
		
		// TODO: implement map method
		String line[] = text.toString().split("[^a-zA-Z]+");
		ArrayList<String> array = new ArrayList<String>();
		for (String word : line) {
			if (word.trim().length() == 0)
				continue;
			word = word.toLowerCase();
			array.add(word);
	    }
		
		
		//array: w1 w2 w3 w2 w1
		StringToIntMapWritable sti;
		
		
		for (String k1 : array) {
			sti = list.get(k1);
			if (sti == null) {
				sti = new StringToIntMapWritable();
			}
			
			for (String k2 : array) {
				if (k1.compareTo(k2) != 0) {
					//context.write(new Text("damn " + k1 + "|" + k2 + "|" + k1.length() + "|" + k2.length()), new StringToIntMapWritable());
					Integer old = sti.get(k2);
					if (old == null)
						old = new Integer(0);
					sti.put(k2, old+1);
				}
			}
			list.put(k1, sti);
		}
		
		for (String k : list.keySet()) {
			System.out.println(k + "\t" + list.get(k).toString());
			context.write(new Text(k), list.get(k));
		}
	}
}

class StripesReducer extends Reducer<Text, // TODO: change Object to input key type
		StringToIntMapWritable, // TODO: change Object to input value type
		Text, // TODO: change Object to output key type
		Text> { // TODO: change Object to output value type
	@Override
	public void reduce(Text key, // TODO: change Object to input key type
			Iterable<StringToIntMapWritable> values, // TODO: change Object to input value type
			Context context) throws IOException, InterruptedException {
		
		
		Iterator<StringToIntMapWritable> it = values.iterator();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		while(it.hasNext()) {
			StringToIntMapWritable t = it.next();
			for (String k : t.keySet()) {
				Integer old = map.get(k);
				if (old == null)
					old = new Integer(0);
				
				old += t.get(k);
				map.put(k, old);
			}
		}
		
		String out = ": {";
		for (String k : map.keySet()) {
			out += k + ":" + map.get(k).toString() + ", ";
		}
		out += "}";
		
				
		context.write(key, new Text(out));
		

		// TODO: implement the reduce method
	}
}
