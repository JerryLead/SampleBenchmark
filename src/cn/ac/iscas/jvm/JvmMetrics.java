package cn.ac.iscas.jvm;

import java.util.ArrayList;

import cn.ac.iscas.util.LongToTime;

public class JvmMetrics {

	public static ArrayList<String> generateArrayList(String[] params) {
		ArrayList<String> list = new ArrayList<String>();
				
		// Time
		list.add(LongToTime.longToHHMMSS(Long.parseLong(params[0]) * 1000));
		// JVMUsed = Integer.parseInt(params[1]);
		list.add(params[1]);
		// Total = Integer.parseInt(params[2]);
		list.add(params[2]);
		// Max = Integer.parseInt(params[3]);
		list.add(params[3]);
		
		return list;

	}

}
