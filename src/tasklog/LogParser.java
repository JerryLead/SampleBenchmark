package tasklog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;

public class LogParser {

	public static List<PidMetrics> parse(File file) {
		List<PidMetrics> pidMetricsList = new ArrayList<PidMetrics>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0 || line.charAt(0) == '#')
					continue;
				String parameters[] = line.trim().split("\\s+");
				if (parameters.length == 16) // 16 parameters totally
					pidMetricsList.add(new PidMetrics(parameters));
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(pidMetricsList.size() > 1) {
			long minTime = pidMetricsList.get(0).getTime();
			for(PidMetrics metrics : pidMetricsList) {
				metrics.setTime(metrics.getTime() - minTime);
			}
		}
			
		return pidMetricsList;

	}
	
	public static void gnuplot(List<PidMetrics> list) {
		//JavaPlot p = new JavaPlot();
		plotCPU(list);
		//p.newGraph();
		plotMEM(list);
		//p.newGraph();
		//plotIO(list);
		//p.plot();
		
		
	}
	
	public static float[][] transferToFloat(List<PidMetrics> list, int index1, int index2) {
		assert(index1 < 16 && index1 >= 0 && index1 < 16 && index1 >= 0);
		float[][] points = new float[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			points[i][0] = Float.parseFloat(list.get(i).getParams(index1));
			points[i][1] = Float.parseFloat(list.get(i).getParams(index2));
		}
		return points;
	}
	
	public static long[][] transferToLong(List<PidMetrics> list, int index1, int index2) {
		assert(index1 < 16 && index1 >= 0 && index1 < 16 && index1 >= 0);
		long[][] points = new long[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			points[i][0] = Long.parseLong(list.get(i).getParams(index1));
			points[i][1] = Long.parseLong(list.get(i).getParams(index2));
			if(index1 == 9 || index1 == 10)
				points[i][0] = points[i][0] / 1024;
			if(index2 == 9 || index2 == 10)
				points[i][1] = points[i][1] / 1024;
		}
		return points;
	}
	
	public static void plotCPU(List<PidMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("CPU Metrics, PID = " + list.get(0).getPID());
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Utilization (%)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime() + 2);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        float[][] usr = transferToFloat(list, 0, 2);
        DataSetPlot usrPlot = new DataSetPlot(usr);
        usrPlot.setTitle("usr");
        usrPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(usrPlot);
        
       
        float[][] system = transferToFloat(list, 0, 3);
        DataSetPlot systemPlot = new DataSetPlot(system);
        systemPlot.setTitle("system");
        p.addPlot(systemPlot);
        
        //float[][] guest = transfer(list, 0, 4);
        //DataSetPlot guestPlot = new DataSetPlot(guest);
        //guestPlot.setTitle("guest");
        //p.addPlot(guestPlot);
       
        /*
        float[][] cpu = transferToFloat(list, 0, 5);
        DataSetPlot cpuPlot = new DataSetPlot(cpu);
        cpuPlot.setTitle("CPU");
        cpuPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(cpuPlot);
        */
        
        //PlotStyle stl = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        //stl.setStyle(Style.POINTS);
        //stl.setLineType(NamedPlotColor.GOLDENROD);
        //stl.setPointType(1);
        //stl.setPointSize(1);
   
        
        p.plot();

	}
	
	public static void plotMEM(List<PidMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Memoery Metrics, PID = " + list.get(0).getPID());
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime() + 2);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        long[][] vsz = transferToLong(list, 0, 9);
        //long[][] vsz2 = new long[vsz.length][1];
        //for(int i = 0; i < vsz.length; i++) 
        //	vsz2[i][0] = vsz[i][1];
        
        
        DataSetPlot vszPlot = new DataSetPlot(vsz);
        vszPlot.setTitle("VSZ");
        vszPlot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        
        vszPlot.getPlotStyle().set("y1=0");
        vszPlot.getPlotStyle().set("lc", "rgb 'green'");
        vszPlot.set("axis", "x1y1");
        
        
        p.addPlot(vszPlot);
        
       
        long[][] rss = transferToLong(list, 0, 10);
        DataSetPlot rssPlot = new DataSetPlot(rss);
        rssPlot.setTitle("RSS");
        rssPlot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        rssPlot.getPlotStyle().set("y1=0");
        rssPlot.getPlotStyle().set("lc", "rgb 'purple'");
        rssPlot.set("axis", "x1y1");
        p.addPlot(rssPlot);
        
        //float[][] guest = transfer(list, 0, 4);
        //DataSetPlot guestPlot = new DataSetPlot(guest);
        //guestPlot.setTitle("guest");
        //p.addPlot(guestPlot);
        
        //float[][] cpu = transfer(list, 0, 5);
        //DataSetPlot cpuPlot = new DataSetPlot(cpu);
        //cpuPlot.setTitle("CPU");
        //cpuPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        //p.addPlot(cpuPlot);
        
        //PlotStyle stl = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        //stl.setStyle(Style.POINTS);
        //stl.setLineType(NamedPlotColor.GOLDENROD);
        //stl.setPointType(1);
        //stl.setPointSize(1);
   
        p.setTitle("Disk I/O Metrics, PID = " + list.get(0).getPID());
        //p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        //p.getAxis("y2").setLabel("Read/Write (KB)");
        p.set("y2label", "'Read/Write (KB)'");
        p.set("ytics", "nomirror");
        p.set("y2tics", "");
        
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime() + 2);
        //p.setKey(JavaPlot.Key.TOP_RIGHT);

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
        
        
        p.plot();

	}
	
	public static void plotIO(List<PidMetrics> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Disk I/O Metrics, PID = " + list.get(0).getPID());
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Read/Write (KB)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime() + 2);
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        float[][] kB_rd = transferToFloat(list, 0, 12);
        DataSetPlot kB_rdPlot = new DataSetPlot(kB_rd);
        kB_rdPlot.setTitle("kB_rd");
        kB_rdPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        
        p.addPlot(kB_rdPlot);
       
        float[][] kB_wr = transferToFloat(list, 0, 13);
        DataSetPlot kB_wrPlot = new DataSetPlot(kB_wr);
        kB_wrPlot.setTitle("kB_wr");
        p.addPlot(kB_wrPlot);
        
        /*
        float[][] kB_ccwr = transferToFloat(list, 0, 14);
        DataSetPlot kB_ccwrPlot = new DataSetPlot(kB_ccwr);
        kB_ccwrPlot.setTitle("kB_ccwr");
        kB_ccwrPlot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(kB_ccwrPlot);
        */
        //PlotStyle stl = ((AbstractPlot) p.getPlots().get(0)).getPlotStyle();
        //stl.setStyle(Style.POINTS);
        //stl.setLineType(NamedPlotColor.GOLDENROD);
        //stl.setPointType(1);
        //stl.setPointSize(1);
   
        
        p.plot();

	}
	
	public static void main(String[] args) {
		File file = new File("/home/xulijie/develop/tasklog/attempt_201204171416_0001_m_000000_0.log");
		//File file = new File("/home/xulijie/develop/tasklog/attempt_201204171416_0001_r_000000_0.log");
		List<PidMetrics> list = LogParser.parse(file);
		gnuplot(list);
	}

	

}

