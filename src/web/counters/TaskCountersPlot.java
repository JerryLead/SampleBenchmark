package web.counters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.GenericDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;

public class TaskCountersPlot {

	public static String generateImage(File countersFile) {
		boolean isMap = countersFile.toString().contains("_m_");
		List<ArrayList<String>> taskCountersList = parse(countersFile, isMap);
		gnuplot(taskCountersList, isMap);
		return "";

	}
	
	public static List<ArrayList<String>> parse(File countersFile, boolean isMap) {
		List<ArrayList<String>> listlist = new ArrayList<ArrayList<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(countersFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0 || line.charAt(0) == '#')
					continue;
				String parameters[] = line.trim().split(",");
				listlist.add(TaskCounters.generateArrayList(parameters, isMap));
				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listlist;
	}
	
	public static void gnuplot(List<ArrayList<String>> countersList, boolean isMapTask) {
		
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
	
	
	private static void plotReduceBytes(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Reduce shuffle bytes (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Bytes (MB)");  
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        String name = "Reduce shuffle bytes";
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:10");
        p.addPlot(plot);
        
        p.plot();
		
		
	}

	private static void plotReduceRecords(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Combine/Shuffle/Reduce Records (1000 * 1000)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Counts (M)");
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        String name = "Reduce input groups";
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:4");
        p.addPlot(plot);

        name = "Reduce input records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:5");
        p.addPlot(plot);
        
        name = "Reduce output records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:6");
        p.addPlot(plot);
        
        name = "Spilled Records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:7");
        p.addPlot(plot);
        

        name = "Combine input records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:8");
        p.addPlot(plot);
        
        name = "Combine output records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:9");
        p.addPlot(plot);
        
        p.plot();
		
	}

	private static void plotMapBytes(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Map Input/Output Bytes (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Bytes (MB)");
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        String name = "Map input bytes";
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:10");
        p.addPlot(plot);
        
       
        name = "Map output bytes";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:11");
        p.addPlot(plot);
       
        p.plot();
		
	}

	private static void plotMapRecords(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Map/Combine/Shuffle Records (1000 * 1000)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Counts (M)"); 
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle("Map input records");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:5");
        p.addPlot(plot);
    
        String name = "Map output records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:6");
        p.addPlot(plot);
        
        name = "Combine input records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:7");
        p.addPlot(plot);
        
        name = "Combine output records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:8");
        p.addPlot(plot);
 
        name = "Spilled Records";
        plot = new DataSetPlot(dataSet);
        plot.setTitle(name);
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:9");
        p.addPlot(plot);
        
        p.plot();
		
		
	}

	private static void plotHDFS(List<ArrayList<String>> list,
			boolean isMapTask) {
		
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.setTitle("HDFS And File IO Statistics (MB)");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Size (MB)");
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle("FILE_BYTES_READ");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.set("using", "1:2");
        p.addPlot(plot);
      
        if(isMapTask) {
        	plot = new DataSetPlot(dataSet);
            plot.setTitle("HDFS_BYTES_READ");
            plot.getPlotStyle().setStyle(Style.LINESPOINTS);
            plot.set("using", "1:3");
            p.addPlot(plot);
        }
        
        plot = new DataSetPlot(dataSet);
        plot.setTitle("FILE_BYTES_WRITTEN");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        if(isMapTask)
        	plot.set("using", "1:4");
        else
        	plot.set("using", "1:3");
        p.addPlot(plot);

        //p.setPersist(false);
        p.plot();
	}

	
	

	
	public static void main(String[] args) {
		File file = new File("/home/xulijie/develop/hadooplog/myMetrics/",
				"job_201204241029_0001/attempt_201204241029_0001_m_000000_0.counters");
		//File file = new File("/home/xulijie/develop/tasklog/attempt_201204171416_0001_r_000000_0.log");
		//file = new File("/home/xulijie/develop/hadooplog/myMetrics/",
		//				"job_201204241029_0001/attempt_201204241029_0001_r_000000_0.counters");
		file = new File("/home/xulijie/develop/hadooplog/myMetrics/",
				"job_201204281424_0001/attempt_201204281424_0001_m_000001_0.counters");
		String imagePath = TaskCountersPlot.generateImage(file);
		System.out.println(imagePath);
	}
}
