package cn.ac.iscas.counters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.ac.iscas.util.LongToTime;

public class TaskCounters {
	
	public static ArrayList<String> generateArrayList(String[] params, boolean isMap) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(LongToTime.longToHHMMSS(Long.parseLong(params[0]) * 1000));
		Map<String, Long> map = new HashMap<String, Long>();
		for(int i = 1; i < params.length; i++) {
			String kv[] = params[i].split(":");
			assert(kv.length == 2);
			map.put(kv[0], Long.parseLong(kv[1]));
		}
		if(isMap) {
			list.add(get(map, "FILE_BYTES_READ", 1024 * 1024));
			list.add(get(map, "HDFS_BYTES_READ", 1024 * 1024));
			list.add(get(map, "FILE_BYTES_WRITTEN", 1024 * 1024));
			
			list.add(get(map, "Map input records", 1000 * 1000));
			list.add(get(map, "Map output records", 1000 * 1000));
			list.add(get(map, "Combine input records", 1000 * 1000));
			list.add(get(map, "Combine output records", 1000 * 1000));
			list.add(get(map, "Spilled Records", 1000 * 1000));
			
			list.add(get(map, "Map input bytes", 1024 * 1024));
			list.add(get(map, "Map output bytes", 1024 * 1024));
			
		}
		else {
			list.add(get(map, "FILE_BYTES_READ", 1024 * 1024));
			list.add(get(map, "HDFS_BYTES_WRITTEN", 1024 * 1024));
			list.add(get(map, "FILE_BYTES_WRITTEN", 1024 * 1024));	
			
			list.add(get(map, "Reduce input groups", 1000 * 1000));
			list.add(get(map, "Reduce input records", 1000 * 1000));
			list.add(get(map, "Reduce output records", 1000 * 1000));
			list.add(get(map, "Spilled Records", 1000 * 1000));
			list.add(get(map, "Combine input records", 1000 * 1000));
			list.add(get(map, "Combine output records", 1000 * 1000));
			
			list.add(get(map, "Reduce shuffle bytes", 1024 * 1024));
		}
		return list;
	}
	
	public static String get(Map<String, Long> countersMap, String counterName, int degradation) {
		if(countersMap.containsKey(counterName))
			return (float)countersMap.get(counterName) / degradation + "";
		else
			return "0";
	}
	
}
