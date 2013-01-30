package cn.ac.iscas.jstat;

import java.util.ArrayList;
import java.util.List;

import cn.ac.iscas.util.LongToTime;

public class JstatMetrics {
	private long startTimeSec;
	//New Generation
	private long nGCMN;
	private long nGCMX;
	private long nGC;
	//Old Generation
	private long oGCMN;
	private long oGCMX;
	//Perm Generation
	private long pGCMN;
	private long pGCMX;
	
	//used for computing timestamps
	private long elpasedTimeSec = -1;
	
	//used for computing gc infos
	private int yGC = 0;
	private int fGC = 0;
	private float yGCT = 0;
	private float fGCT = 0;
	private float gCT = 0;
	
	public JstatMetrics(String dateStrSec, long nGCMN, long nGCMX, long nGC, long oGCMN, long oGCMX, long pGCMN, long pGCMX) {
		this.startTimeSec = Long.parseLong(dateStrSec);
		this.nGCMN = nGCMN;
		this.nGCMX = nGCMX;
		this.nGC = nGC;
		this.oGCMN = oGCMN;
		this.oGCMX = oGCMX;
		this.pGCMN = pGCMN;
		this.pGCMX = pGCMX;
	}

	/*
	 * Timestamp        S0C    S1C    S0U    S1U      EC       EU        OC         OU       PC     PU    YGC     YGCT    FGC    FGCT     GCT   
	 * 325829.7 32512.0 31872.0 23155.4  0.0   108608.0 19119.4   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325831.7 32512.0 31872.0 23155.4  0.0   108608.0 47124.5   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325833.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325835.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325837.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 */
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public ArrayList<String> generateArrayList(String[] params) {
		ArrayList<String> list = new ArrayList<String>();		
		// Timestamp
		long timestampSec = (long)Float.parseFloat(params[0]);
		if(elpasedTimeSec == -1)
			elpasedTimeSec = timestampSec;
		timestampSec = startTimeSec + timestampSec - elpasedTimeSec;
		
		list.add(LongToTime.longToHHMMSS(timestampSec * 1000));

		
		// OGCMN OGCMX OC OU
		list.add(String.valueOf(oGCMN / 1024));
		list.add(String.valueOf(oGCMX / 1024));
		list.add(kBtoMB(params[7]));
		list.add(kBtoMB(params[8]));
		
		// PGCMN PGCMX PC PU
		list.add(String.valueOf(pGCMN / 1024));
		list.add(String.valueOf(pGCMX / 1024));
		list.add(kBtoMB(params[9]));
		list.add(kBtoMB(params[10]));
		
		// YGC YGCT FGC FGCT GCT
		list.addAll(incToSingle(Integer.parseInt(params[11]), 
				Float.parseFloat(params[12]), 
				Integer.parseInt(params[13]), 
				Float.parseFloat(params[14]), 
				Float.parseFloat(params[15])));
		
		
		// NGCMN NGCMX NGC S0C S1C S0U S1U EC EU NGU
		list.add(String.valueOf(nGCMN / 1024));
		list.add(String.valueOf(nGCMX / 1024));
		//list.add(String.valueOf(nGC / 1024));
		list.add(String.valueOf((Float.parseFloat(params[1]) + Float.parseFloat(params[2]) + Float.parseFloat(params[5])) / 1024));
		list.add(kBtoMB(params[1])); 
		list.add(kBtoMB(params[2]));
		list.add(kBtoMB(params[3]));
		list.add(kBtoMB(params[4]));
		list.add(kBtoMB(params[5]));
		list.add(kBtoMB(params[6]));
		list.add(String.valueOf((Float.parseFloat(params[3]) + Float.parseFloat(params[4]) + Float.parseFloat(params[6])) / 1024));
		return list;

	}
	
	private List<String> incToSingle(int nYGC, float nYGCT, int nFGC, float nFGCT, float nGCT) {
		List<String> gcList = new ArrayList<String>();
		/*
		gcList.add(String.valueOf(nYGC - yGC));
		gcList.add(String.valueOf(nYGCT - yGCT));
		gcList.add(String.valueOf(nFGC - fGC));
		gcList.add(String.valueOf(nFGCT - fGCT));
		gcList.add(String.valueOf(nGCT - gCT));
		*/
		gcList.add(String.valueOf(nYGC - 0));
		gcList.add(String.valueOf(nYGCT - yGCT));
		gcList.add(String.valueOf(nFGC - 0));
		gcList.add(String.valueOf(nFGCT - fGCT));
		gcList.add(String.valueOf(nGCT - gCT));
		
		this.yGC = nYGC;
		this.yGCT = nYGCT;
		this.fGC = nFGC;
		this.fGCT = nFGCT;
		this.gCT = nGCT;
		return gcList;
	}

	public String kBtoMB(String floatKB) {
		Float f = Float.parseFloat(floatKB);
		return String.valueOf((int)(f / 1024));
	}
}
