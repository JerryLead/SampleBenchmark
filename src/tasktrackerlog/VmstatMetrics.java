package tasktrackerlog;

public class VmstatMetrics {
    //All linux blocks are currently 1024 bytes.
	private int time; //added by LijieXu
	
	private int r; //procs r
	private int b; //procs b
	
	private int swapd; //memory swpd (KB)
	private int free; //memory free (KB)
	private int buff; //memory buff (KB)
	private int cache; //memory cache (KB)
	
	private int si; //swap si Amount of memory swapped in from disk (/s).
	private int so; //swap so Amount of memory swapped to disk (/s).
	
	private int bi; //io bi Blocks received from a block device (blocks/s)
	private int bo; //io bo Blocks sent to a block device (blocks/s).
	
	private int in; //system in: The number of interrupts per second, including the clock.
	private int cs; //system cs: The number of context switches per second.
	
	private int us; //cpu us
	private int sy; //cpu sy
	private int id; //cpu ed
	private int wa; //cpu wa
	
	private String params[];
	public VmstatMetrics(String params[]) {
		this.params = params;
	}
	
	public String getParams(int i) {
		return params[i];
	}
	
}
