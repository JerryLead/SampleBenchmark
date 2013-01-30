package cn.ac.iscas.metrics;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ac.iscas.util.PNGTerminal;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.GenericDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;


public class TaskMetricsImage {
	
	public static List<ArrayList<String>> parse(File metricsFile) {
		List<ArrayList<String>> listlist = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(metricsFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0 || line.charAt(0) == '#')
					continue;
				String parameters[] = line.trim().split("\\s+");
				if (parameters.length == 16) // 16 parameters totally
					listlist.add(TaskMetrics.generateArrayList(parameters));
				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listlist;
	}
	
	public static BufferedImage plotCpuAndIO(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
   
        p.setTitle("CPU and IO Metrics, PID = " + list.get(0).get(1));
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Utilization (%)");

        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");
		
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

       
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.setTitle("usr");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.set("using", "1:3");
        p.addPlot(plot);
        
       
        plot = new DataSetPlot(dataSet);
        plot.setTitle("system");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.set("using", "1:4");
        p.addPlot(plot);
        
        //float[][] guest = transfer(list, 0, 4);
        //DataSetPlot guestPlot = new DataSetPlot(guest);
        //guestPlot.setTitle("guest");
        //p.addPlot(guestPlot);
     
        plot = new DataSetPlot(dataSet);
        plot.setTitle("CPU");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.set("using", "1:6");
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb 'gray'");
        p.addPlot(plot);
        
  
        p.set("y2label", "'Read/Write (KB)'");
        p.set("ytics", "nomirror");
        p.set("y2tics", "");
      
        plot = new DataSetPlot(dataSet);
        plot.setTitle("kB_rd");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.set("using", "1:13");
        plot.getPlotStyle().setLineWidth(2);
        plot.set("axis x1y2");
        p.addPlot(plot);     
        
        plot = new DataSetPlot(dataSet);
        plot.setTitle("kB_wr");
        plot.set("using", "1:14");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(7);
        plot.set("axis x1y2");
        p.addPlot(plot);
        
        p.set("style", "fill transparent solid 0.2 noborder");
        
        PNGTerminal t = new PNGTerminal(900, 480);  
        p.setTerminal(t);   
        p.setPersist(false);
        
        p.plot();
        
        return t.getImage();

	}
	
	public static BufferedImage plotMEM(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
      
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");

        p.setTitle("Memory Metrics, PID = " + list.get(0).get(1));
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
 
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:10");
        plot.setTitle("VSZ");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        p.addPlot(plot);
        
       
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:11");   
        plot.setTitle("RSS");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        p.addPlot(plot);
        
        PNGTerminal t = new PNGTerminal();  
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        return t.getImage();

	}

}

