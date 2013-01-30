package counters.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;

public class CountersPlot {
	
	private static void gnuplot(List<MyCounters> countersList, boolean isMapTask) {
		plotHDFS(countersList, isMapTask);
		if(isMapTask) {
			plotMapRecords(countersList);
		
			plotMapBytes(countersList);
		}
		else {
			plotReduceRecords(countersList);
			plotReduceBytes(countersList);
		}
	}

	

	private static void plotMapBytes(List<MyCounters> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Map Input/Output Bytes (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Bytes (MB)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        String name = "Map input bytes";
        float[][] temp = getCountersValue(list, name, 1024 * 1024);
        DataSetPlot plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
       
        name = "Map output bytes";
        temp = getCountersValue(list, name, 1024 * 1024);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
       
        p.plot();
		
	}



	private static void plotMapRecords(List<MyCounters> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Map/Combine/Shuffle Records (1000 * 1000)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Counts (M)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        String name = "Map input records";
        float[][] temp = getCountersValue(list, name, 1000 * 1000);
        DataSetPlot plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
       
        name = "Map output records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        name = "Combine input records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        name = "Combine output records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
 
        name = "Spilled Records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        p.plot();
		
	}

	

	private static void plotReduceRecords(List<MyCounters> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Combine/Shuffle/Reduce Records (1000 * 1000)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Counts (M)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        String name = "Reduce input groups";
        float[][] temp = getCountersValue(list, name, 1000 * 1000);
        DataSetPlot plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
      
 
        name = "Reduce input records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        name = "Reduce output records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        name = "Spilled Records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        

        name = "Combine input records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        name = "Combine output records";
        temp = getCountersValue(list, name, 1000 * 1000);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        p.plot();
		
	}

	private static void plotReduceBytes(List<MyCounters> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Reduce shuffle bytes (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Bytes (MB)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        String name = "Reduce shuffle bytes";
        float[][] temp = getCountersValue(list, name, 1024 * 1024);
        DataSetPlot plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        p.plot();
		
	}


	//key is HDFS_BYTES_READ, degradation is HDFS_BYTES_READ/1024 * 1024
	private static float[][] getCountersValue(List<MyCounters> list, String key, int degradation) {
		float[][] timeAndValue = new float[list.size()][2];
		
		for(int i = 0; i < list.size(); i++) {
			timeAndValue[i][0] = list.get(i).getTime();
			timeAndValue[i][1] = ((float) list.get(i).get(key)) / degradation;
		}
		return timeAndValue;
	}
	private static void plotHDFS(List<MyCounters> list, boolean isMap) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("HDFS And File IO Statistics (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Size (MB)");

        
        p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        String name = "FILE_BYTES_READ";
        float[][] temp = getCountersValue(list, name, 1024 * 1024);
        DataSetPlot plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
        
        if(isMap) {
        	name = "HDFS_BYTES_READ";
            temp = getCountersValue(list, name, 1024 * 1024);
            plot = new DataSetPlot(temp);
            plot.setTitle(name);
            plot.getPlotStyle().setStyle(Style.LINESPOINTS);
            p.addPlot(plot);
        }
        
        name = "FILE_BYTES_WRITTEN";
        temp = getCountersValue(list, name, 1024 * 1024);
        plot = new DataSetPlot(temp);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        p.addPlot(plot);
 
        p.plot();
	}



	private static List<MyCounters> parse(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			List<MyCounters> list = new ArrayList<MyCounters>();
			String line;
			while((line = reader.readLine()) != null) {
				list.add(new MyCounters(line));
			}
			return list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static void normalizeTime(List<MyCounters> countersList) {
		
		long startTime = countersList.get(0).getTime();
		for (MyCounters c : countersList) {
			c.setTime(c.getTime() - startTime);
		}
	}
	
	public static void main(String[] args) {
		File file = new File("/home/xulijie/develop/hadooplog/myMetrics/",
				"job_201204241029_0001/attempt_201204241029_0001_m_000000_0.counters");
		
		//file = new File("/home/xulijie/develop/hadooplog/myMetrics/",
		//		"job_201204241029_0001/attempt_201204241029_0001_r_000000_0.counters");
		
		List<MyCounters> countersList = CountersPlot.parse(file);
		boolean isMapTask = false;
		if(file.toString().contains("_m_"))
			isMapTask = true;
		
		if(countersList != null && !countersList.isEmpty()) {
			normalizeTime(countersList);
			gnuplot(countersList, isMapTask);
		}
	}
	
	
}
