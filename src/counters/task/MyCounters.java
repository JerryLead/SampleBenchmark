package counters.task;

import java.util.HashMap;
import java.util.Map;

public class MyCounters {
	private long time;
	private Map<String, Long> countersMap = new HashMap<String, Long>();
	
	public MyCounters(String line) {
		String params[] = line.split(",");
		time = Long.parseLong(params[0]);
		for(int i = 1; i < params.length; i++) {
			String kv[] = params[i].split(":");
			assert(kv.length == 2);
			countersMap.put(kv[0], Long.parseLong(kv[1]));
		}
	}
	
	public long get(String counterName) {
		if(countersMap.containsKey(counterName))
			return countersMap.get(counterName);
		else
			return 0;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
}
