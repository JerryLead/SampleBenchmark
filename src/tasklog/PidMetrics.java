package tasklog;

public class PidMetrics {
	
		private long Time; // Time
		private int PID; // PID
		private float usr; // %usr
		private float system; // %system
		private float guest; // %guest
		private float CPU; // %CPU
		private int CPUID; // CPU
		private float minflt; // minflt/s
		private float majflt; // majflt/s
		private long VSZ; // VSZ
		private long RSS; // RSS
		private float MEM; // %MEM
		private float kB_rd; // kB_rd/s
		private float kB_wr; // kB_wr/s
		private float kB_ccwr; // kB_ccwr/s
		private String Command; // Command

		private String[] params;


		public PidMetrics(String[] params) {

			Time = Long.parseLong(params[0]);
		
			PID = Integer.parseInt(params[1]);
			usr = Float.parseFloat(params[2]);
			system = Float.parseFloat(params[3]);
			guest = Float.parseFloat(params[4]);
			CPU = Float.parseFloat(params[5]);
			CPUID = Integer.parseInt(params[6]);
			minflt = Float.parseFloat(params[7]);
			majflt = Float.parseFloat(params[8]);
			VSZ = Long.parseLong(params[9]);
			RSS = Long.parseLong(params[10]);
			MEM = Float.parseFloat(params[11]);
			kB_rd = Float.parseFloat(params[12]);
			kB_wr = Float.parseFloat(params[13]);
			kB_ccwr = Float.parseFloat(params[14]);
			Command = params[15];
			
			this.params = params;
		}
		
		public String toString() {
			return Time + " " + PID + " " + usr + " " + system + " " + guest + " " + CPU + " " + CPUID +
					" " + minflt + " " + majflt + " " + VSZ + " " + RSS + " " + MEM + " " + kB_rd + " " +
					kB_wr + " " + kB_ccwr + " " + Command;
		}
		
		public String getParams(int i) {
			if(i == 0)
				return String.valueOf(Time);
			return params[i];
		}
		public long getTime() {
			return Time;
		}

		public void setTime(long time) {
			Time = time;
		}

		public int getPID() {
			return PID;
		}

		public void setPID(int pID) {
			PID = pID;
		}

		public float getUsr() {
			return usr;
		}

		public void setUsr(float usr) {
			this.usr = usr;
		}

		public float getSystem() {
			return system;
		}

		public void setSystem(float system) {
			this.system = system;
		}

		public float getGuest() {
			return guest;
		}

		public void setGuest(float guest) {
			this.guest = guest;
		}

		public float getCPU() {
			return CPU;
		}

		public void setCPU(float cPU) {
			CPU = cPU;
		}

		public int getCPUID() {
			return CPUID;
		}

		public void setCPUID(int cPUID) {
			CPUID = cPUID;
		}

		public float getMinflt() {
			return minflt;
		}

		public void setMinflt(float minflt) {
			this.minflt = minflt;
		}

		public float getMajflt() {
			return majflt;
		}

		public void setMajflt(float majflt) {
			this.majflt = majflt;
		}

		public long getVSZ() {
			return VSZ;
		}

		public void setVSZ(long vSZ) {
			VSZ = vSZ;
		}

		public long getRSS() {
			return RSS;
		}

		public void setRSS(long rSS) {
			RSS = rSS;
		}

		public float getMEM() {
			return MEM;
		}

		public void setMEM(float mEM) {
			MEM = mEM;
		}

		public float getkB_rd() {
			return kB_rd;
		}

		public void setkB_rd(float kB_rd) {
			this.kB_rd = kB_rd;
		}

		public float getkB_wr() {
			return kB_wr;
		}

		public void setkB_wr(float kB_wr) {
			this.kB_wr = kB_wr;
		}

		public float getkB_ccwr() {
			return kB_ccwr;
		}

		public void setkB_ccwr(float kB_ccwr) {
			this.kB_ccwr = kB_ccwr;
		}

		public String getCommand() {
			return Command;
		}

		public void setCommand(String command) {
			Command = command;
		}

}
