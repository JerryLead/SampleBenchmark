package cn.ac.iscas.jvm;

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



public class JvmMetricsImage {
	
	public static List<ArrayList<String>> parse(File jvmFile) {
		List<ArrayList<String>> listlist = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(jvmFile));
			String line;
			line = reader.readLine(); //omit the first line as "Time	JVMUsed	Total	Max"
			
			while ((line = reader.readLine()) != null) {
				String parameters[] = line.trim().split("\t");
				if (parameters.length == 4) // 16 parameters totally
					listlist.add(JvmMetrics.generateArrayList(parameters));
				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listlist;
	}
	
	public static BufferedImage plotJvmMem(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
      
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");

        p.setTitle("JVM Memory Metrics");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
 
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:2");
        plot.setTitle("JVMUsed");
        
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb 'red'");
        p.addPlot(plot);
        
       
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:3");   
        plot.setTitle("Total");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb 'blue'");
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:4");   
        plot.setTitle("Max");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(1);
        p.addPlot(plot);
        
        p.set("style", "fill transparent solid 0.4 noborder");
        
        PNGTerminal t = new PNGTerminal();  
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        return t.getImage();

	}


}
