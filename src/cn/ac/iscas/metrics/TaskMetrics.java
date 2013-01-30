package cn.ac.iscas.metrics;

import java.util.ArrayList;

import cn.ac.iscas.util.LongToTime;

public class TaskMetrics {

	public static ArrayList<String> generateArrayList(String[] params) {
		ArrayList<String> list = new ArrayList<String>();
		
		
		// Time
		list.add(LongToTime.longToHHMMSS(Long.parseLong(params[0]) * 1000));
		// PID = Integer.parseInt(params[1]);
		list.add(params[1]);
		// usr = Float.parseFloat(params[2]);
		list.add(params[2]);
		// system = Float.parseFloat(params[3]);
		list.add(params[3]);
		// guest = Float.parseFloat(params[4]);
		list.add(params[4]);
		// CPU = Float.parseFloat(params[5]);
		list.add(params[5]);
		// CPUID = Integer.parseInt(params[6]);
		list.add(params[6]);
		// minflt = Float.parseFloat(params[7]);
		list.add(params[7]);
		// majflt = Float.parseFloat(params[8]);
		list.add(params[8]);
		// VSZ = Long.parseLong(params[9]);
		list.add(Long.parseLong(params[9]) / 1024 + "");
		// RSS = Long.parseLong(params[10]);
		list.add(Long.parseLong(params[10]) / 1024 + "");
		// MEM = Float.parseFloat(params[11]);
		list.add(params[11]);
		// kB_rd = Float.parseFloat(params[12]);
		list.add(params[12]);
		// kB_wr = Float.parseFloat(params[13]);
		list.add(params[13]);
		// kB_ccwr = Float.parseFloat(params[14]);
		list.add(params[14]);
		// Command = params[15];
		return list;
	}
}
