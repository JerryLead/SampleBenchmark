package tasktrackerlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tasklog.PidMetrics;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;




public class VmstatMetricsPlot {
	
	public static List<VmstatMetrics> parse(File file) {
		List<VmstatMetrics> vmstatMetricsList = new ArrayList<VmstatMetrics>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0)
					continue;
				if (line.startsWith(" r")) {
					vmstatMetricsList.clear(); //only plot the last vmstat metrics
					continue;
				}
				String parameters[] = line.trim().split("\\s+");
				if (parameters.length == 16) // 16 parameters totally
					vmstatMetricsList.add(new VmstatMetrics(parameters));
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
			
		return vmstatMetricsList;

	}
	
	public static int[][] transferToInt(List<VmstatMetrics> list, int index1, int index2) {
		assert(index2 < 16 && index2 >= 0);
		int[][] points = new int[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			points[i][0] = i;
			points[i][1] = Integer.parseInt(list.get(i).getParams(index2));
			
			if(index2 >=2 && index2 <= 9)
				points[i][1] = points[i][1] / 1024; //Memory KB --> MB
			else if(index2 == 14) 
				points[i][1] = 100 - points[i][1];
		}
		return points;
	}
	public static void gnuplot(List<VmstatMetrics> list) {
		
		plotCpu(list);
		plotMemory(list);
		plotIO(list);
		plotSystem(list);
	}
	

	public static void plotCpu(List<VmstatMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("TaskTracker CPU Metrics");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Utilization (%)");

        
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).get);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        int[][] usr = transferToInt(list, 0, 12);
        DataSetPlot plot = new DataSetPlot(usr);
        plot.setTitle("usr");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
       
        int[][] sy = transferToInt(list, 0, 13);
        plot = new DataSetPlot(sy);
        plot.setTitle("system");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        int[][] wa = transferToInt(list, 0, 15);
        plot = new DataSetPlot(wa);
        plot.setTitle("wait");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        int[][] total = transferToInt(list, 0, 14);
        plot = new DataSetPlot(total);
        plot.setTitle("total");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb 'gray'");
        //plot.set("axis", "x1y1");
        
        p.addPlot(plot);
       
        p.set("style", "fill transparent solid 0.3 noborder");
        
        /*
   
        p.set("y2label", "'Read/Write (KB)'");
        p.set("ytics", "nomirror");
        p.set("y2tics", "");

        float[][] kB_rd = transferToFloat(list, 0, 12);
        DataSetPlot kB_rdPlot = new DataSetPlot(kB_rd);
        kB_rdPlot.setTitle("kB_rd");
        kB_rdPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        kB_rdPlot.set("axis", "x1y2");
        p.addPlot(kB_rdPlot);
       
        float[][] kB_wr = transferToFloat(list, 0, 13);
        DataSetPlot kB_wrPlot = new DataSetPlot(kB_wr);
        kB_wrPlot.setTitle("kB_wr");
        kB_rdPlot.set("axis", "x1y2");
        p.addPlot(kB_wrPlot);
        */
        p.plot();

	}
	
	public static void plotMemory(List<VmstatMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Tasktracker Memoery Metrics");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");

        
        //p.getAxis("x").setBoundaries(0, list.size());
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        int[][] swpd = transferToInt(list, 0, 2);
        DataSetPlot swpdPlot = new DataSetPlot(swpd);
        swpdPlot.setTitle("swpd");
        swpdPlot.getPlotStyle().setStyle(Style.LINES);   
        p.addPlot(swpdPlot);
        
       
        int[][] free = transferToInt(list, 0, 3);
        DataSetPlot freePlot = new DataSetPlot(free);
        freePlot.setTitle("free");
        freePlot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(freePlot);
        
        int[][] buff = transferToInt(list, 0, 4);
        DataSetPlot buffPlot = new DataSetPlot(buff);
        buffPlot.setTitle("buff");
        buffPlot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(buffPlot);
        
        int[][] cache = transferToInt(list, 0, 5);
        DataSetPlot cachePlot = new DataSetPlot(cache);
        cachePlot.setTitle("cache");
        cachePlot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(cachePlot);
 
        p.plot();

	}
	
	public static void plotIO(List<VmstatMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("TaskTracker Disk IO Metrics");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("IO (MB)");

        
        //p.getAxis("x").setBoundaries(0, list.size());
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        int[][] si = transferToInt(list, 0, 6);
        DataSetPlot plot = new DataSetPlot(si);
        plot.setTitle("si");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        int[][] so = transferToInt(list, 0, 7);
        plot = new DataSetPlot(so);
        plot.setTitle("so");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);

        int[][] bi = transferToInt(list, 0, 8);
        plot = new DataSetPlot(bi);
        plot.setTitle("bi");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        int[][] bo = transferToInt(list, 0, 9);
        plot = new DataSetPlot(bo);
        plot.setTitle("bo");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        p.plot();
	}
	
	public static void plotSystem(List<VmstatMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("TaskTracker System Metrics");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Number");

        
        //p.getAxis("x").setBoundaries(0, list.size());
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        int[][] in = transferToInt(list, 0, 10);
        DataSetPlot plot = new DataSetPlot(in);
        plot.setTitle("in");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);
        
        int[][] cs = transferToInt(list, 0, 11);
        plot = new DataSetPlot(cs);
        plot.setTitle("cs");
        plot.getPlotStyle().setStyle(Style.LINES);
        p.addPlot(plot);

       
        
        p.plot();
	}
       
	public static void main(String[] args) {
		File file = new File("/home/xulijie/develop/hadooplog/tasktrackerMetrics/vmstat.log");
		//File file = new File("/home/xulijie/develop/tasklog/attempt_201204171416_0001_r_000000_0.log");
		List<VmstatMetrics> list = VmstatMetricsPlot.parse(file);
		gnuplot(list);
	}
}
