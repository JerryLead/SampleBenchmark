package attempt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.dataset.GenericDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;

class TimeAndCount {
	public TimeAndCount(String time, String value1, String value2) {
		this.time = time;
		this.value1 = Integer.parseInt(value1);
		this.value2 = Integer.parseInt(value2);
	}
	public String getTime() {
		return time;
	}
	public String getValue1() {
		return value1 + "";
	}
	public String getValue2() {
		return value2 + "";
	}
	private String time;
	private int value1;
	private int value2;
}
public class DatePlotTest {

	
	public static void main(String[] args) throws NumberFormatException, ArrayIndexOutOfBoundsException, IOException {
		File file = new File("/home/xulijie/develop/Data/outputTime.txt");
		JavaPlot p = new JavaPlot();
		JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
		
		p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");
		//p.set("format", "%H:%M:%S");
		
		//set style data fsteps
		
		//set trange [ * : * ] noreverse nowriteback  # (currently ["31/12/99\t2359":"01/01/00\t0000"] )
		//set urange [ * : * ] noreverse nowriteback  # (currently ["31/12/99\t2359":"01/01/00\t0000"] )
		//DataSet dataSet = new FileDataSet(file);
		BufferedReader in = new BufferedReader(new FileReader(file));
		List<TimeAndCount> list = new ArrayList<TimeAndCount>();
		String line;
		GenericDataSet dataSet = new GenericDataSet(true);
		while((line = in.readLine()) != null) {
			String params[] = line.split("\\s+");
			TimeAndCount tc = new TimeAndCount(params[0], params[1], params[2]);
			list.add(tc);
		}
		for(TimeAndCount tc : list) {
			ArrayList<String> l = new ArrayList<String>();
			l.add(tc.getTime());
			l.add(tc.getValue1());
			l.add(tc.getValue2());
			dataSet.add(l);
		}
		
		
		DataSetPlot plot = new DataSetPlot(dataSet);
		DataSetPlot plot2 = new DataSetPlot(dataSet);;
		plot.getPlotStyle().setStyle(Style.LINESPOINTS);
		plot.set("using 1:2");
		plot.setTitle("value1");
        p.addPlot(plot);
        plot2.set("using 1:3");
        plot.setTitle("value2");
        p.addPlot(plot2);
        p.plot();
        
        
	}
}
