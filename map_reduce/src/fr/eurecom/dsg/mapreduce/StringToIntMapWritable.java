package fr.eurecom.dsg.mapreduce;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
/*
 * Very simple (and scholastic) implementation of a Writable associative array for String to Int 
 *
 **/
public class StringToIntMapWritable implements Writable {

	// TODO: add an internal field that is the real associative array
	private HashMap<String, Integer> map;
	
	public StringToIntMapWritable() {
		this.map = new HashMap<String, Integer>();
	}
	
	public Integer get(String key) {
		return map.get(key);
	}
	
	public void put(String key, int val) {
		map.put(key, val);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		IntWritable l = new IntWritable();
		
		map.clear();
		
		l.readFields(in);
		
		for (int i=0; i<l.get(); i++) {
			Text key = new Text();
			key.readFields(in);
			IntWritable v = new IntWritable();
			v.readFields(in);
			this.put(key.toString(), v.get());
		}
		// TODO: implement serialization
	}
	
	public Set<String> keySet() {
		return this.map.keySet();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		IntWritable l = new IntWritable(map.size());
		l.write(out);
		
		for (String k : map.keySet()) {
			Text key = new Text(k);
			key.write(out);
			IntWritable v = new IntWritable(map.get(k));
			v.write(out);
		}
		// TODO: implement deserialization
	}
}
