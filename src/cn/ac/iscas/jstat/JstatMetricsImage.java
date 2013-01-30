package cn.ac.iscas.jstat;

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
import com.panayotis.gnuplot.JavaPlot.Key;
import com.panayotis.gnuplot.dataset.GenericDataSet;
import com.panayotis.gnuplot.layout.AutoGraphLayout;
import com.panayotis.gnuplot.layout.StripeLayout;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;
import com.panayotis.iodebug.Debug;


public class JstatMetricsImage {
	/*
	 * NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC      PGCMN    PGCMX     PGC       PC     YGC    FGC 
 	 * 13632.0 174720.0 172992.0 32512.0 31872.0 108608.0    27328.0   349568.0   315584.0   315584.0  21248.0 262144.0 193664.0 193664.0    542    13
	 * 1351667183
	 * Timestamp        S0C    S1C    S0U    S1U      EC       EU        OC         OU       PC     PU    YGC     YGCT    FGC    FGCT     GCT   
	 * 325829.7 32512.0 31872.0 23155.4  0.0   108608.0 19119.4   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325831.7 32512.0 31872.0 23155.4  0.0   108608.0 47124.5   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325833.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325835.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 * 325837.7 32512.0 31872.0 23155.4  0.0   108608.0 47624.0   315584.0   149325.8  193664.0 184174.3    542   11.467  13     13.135   24.603
	 */
	public static List<ArrayList<String>> parse(File jstatFile) {
		List<ArrayList<String>> listlist = new ArrayList<ArrayList<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(jstatFile));
			String line;
			String dateStrSec;
			
			line = reader.readLine().trim(); //NGCMN    NGCMX     NGC     S0C   S1C
			if(!line.startsWith("NGCMN"))
				return null;
			line = reader.readLine();
			String[] gccapacity = line.trim().split("\\s+");
			if(gccapacity.length != 16)
				return null;
			long NGCMN = (long)Float.parseFloat(gccapacity[0]);
			long NGCMX = (long)Float.parseFloat(gccapacity[1]);
			long NGC = (long)Float.parseFloat(gccapacity[2]);
			long OGCMN = (long)Float.parseFloat(gccapacity[6]);
			long OGCMX = (long)Float.parseFloat(gccapacity[7]);
			long PGCMN = (long)Float.parseFloat(gccapacity[10]);
			long PGCMX = (long)Float.parseFloat(gccapacity[11]);

			dateStrSec = reader.readLine().trim(); //(Date s) 1351667183
			if(dateStrSec == null)
				return null;

			line = reader.readLine().trim();
			if(line == null || !line.startsWith("Timestamp"))
				return null;
			
			JstatMetrics jsm = new JstatMetrics(dateStrSec, NGCMN, NGCMX, NGC, OGCMN, OGCMX, PGCMN, PGCMX);
			
			while ((line = reader.readLine()) != null) {
				String parameters[] = line.trim().split("\\s+");
				if (parameters.length == 16) // 16 parameters totally
					listlist.add(jsm.generateArrayList(parameters));
				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listlist;
	}
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public static BufferedImage plotOldGen(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
		
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");

        p.setTitle("Old Generation Heap Memory Usage");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
 
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_RIGHT);

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);
        
        
             
       
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:4");   
        //plot.set("using", "1:4:(" + rgb(50,204,50) + ")");   
        plot.setTitle("OC");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(50,204,50) + "'");
        p.addPlot(plot);            
        
        plot = new DataSetPlot(dataSet);
        //plot.set("using", "1:5:(" + rgb(85,84,202) + ")");
        plot.set("using", "1:5");
        plot.setTitle("OU");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(85,84,202) + "'");
        p.addPlot(plot); 
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:2");   
        plot.setTitle("OGCMN");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineType(6);
        plot.getPlotStyle().set("lc", "rgb 'green'");
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:3");   
        plot.setTitle("OGCMX");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(10);
        plot.getPlotStyle().set("lc", "rgb 'red'");
        p.addPlot(plot);
       
        
        p.set("style", "fill transparent solid 1.0 noborder");
  
        //PNGTerminal t = new PNGTerminal();  
        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
                System.getProperty("file.separator") + "Desktop/Old.eps");
        t.setColor(true);
        t.set("solid");
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        //return t.getImage();
        return null;
	}
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public static BufferedImage plotPermGen(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
      
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");

        p.setTitle("Perm Generation Heap Memory Usage");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
 
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.BELOW);

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:9");
        plot.setTitle("used");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        //plot.getPlotStyle().set("fs solid 1.0");
        plot.getPlotStyle().set("lc", "rgb 'dark-blue'");
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:8");   
        plot.setTitle("commited");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
      
        plot.getPlotStyle().set("lc", "rgb 'green'");
        p.addPlot(plot);

        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:6");   
        plot.setTitle("min");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineType(7);
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:7");   
        plot.setTitle("max");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        p.addPlot(plot);
             
        p.set("style", "fill transparent solid 0.5 noborder");
        
        PNGTerminal t = new PNGTerminal();  
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        return t.getImage();

	}	
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public static BufferedImage plotGC(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
   
        p.setTitle("GC Counts and corresponding Time Cost");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        //p.getAxis("y").setLabel("Count");
        p.getAxis("y").setLabel("GC Time Cost (sec)");
        
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		p.set("style", "data lines");
		
        p.setKey(JavaPlot.Key.TOP_RIGHT);
        
        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

       
        DataSetPlot plot = new DataSetPlot(dataSet);
        /*
        plot.setTitle("Young GC");
        plot.getPlotStyle().setStyle(Style.IMPULSES);
        plot.set("using", "1:10");
        p.addPlot(plot);
           
        plot = new DataSetPlot(dataSet);
        plot.setTitle("Full GC");
        plot.getPlotStyle().setStyle(Style.BOXES);
        plot.set("using", "1:12");
        p.addPlot(plot);
	
  
        p.set("y2label", "'Time Cost (sec)'");
        p.set("ytics", "nomirror");
        p.set("y2tics", "");
        */
        
        plot = new DataSetPlot(dataSet);
        plot.setTitle("YGCT");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.set("using", "1:11");
        plot.getPlotStyle().setLineWidth(2);
        //plot.set("axis x1y2");
        p.addPlot(plot);     
        
        plot = new DataSetPlot(dataSet);
        plot.setTitle("FGCT");
        plot.set("using", "1:13");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        //plot.getPlotStyle().setLineType(7);
        //plot.set("axis x1y2");
        p.addPlot(plot);
        /*
        plot = new DataSetPlot(dataSet);
        plot.setTitle("GCT");
        plot.set("using", "1:14");
        plot.getPlotStyle().setStyle(Style.LINES);
        //plot.getPlotStyle().setLineWidth(2);
        //plot.getPlotStyle().setLineType(7);
        plot.set("axis x1y2");
        p.addPlot(plot);
        */
        //p.set("style", "fill transparent solid 0.2 noborder");
        
        //PNGTerminal t = new PNGTerminal(900, 480);  
        //PNGTerminal t = new PNGTerminal();  
        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
                System.getProperty("file.separator") + "Desktop/GC.eps");
        t.setColor(true);
        t.set("solid");
        p.setTerminal(t);   
        p.setPersist(false);
        
        p.plot();
        
        //return t.getImage();
        return null;
	}
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
		public static BufferedImage plotGCCount(List<ArrayList<String>> list) {
			JavaPlot p = new JavaPlot();
	   
	        p.setTitle("GC Counts");
	        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
	        //p.getAxis("y").setLabel("Count");
	        p.getAxis("y").setLabel("GC Count");
	        
	        p.set("xdata", "time");
			p.set("timefmt", "'%H:%M:%S'");
			p.set("xtics", "nomirror");	
			p.set("format", "x '%M:%S'");
			p.set("style", "data lines");
			
	        p.setKey(JavaPlot.Key.TOP_LEFT);
	        
	        GenericDataSet dataSet = new GenericDataSet(true);
	        dataSet.addAll(list);

	       
	        DataSetPlot plot = new DataSetPlot(dataSet);
	       
	        plot.setTitle("YGC");
	        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
	        plot.set("using", "1:10");
	        p.addPlot(plot);
	           
	        plot = new DataSetPlot(dataSet);
	        plot.setTitle("FGC");
	        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
	        plot.getPlotStyle().setLineType(3);
	        plot.set("using", "1:12");
	        p.addPlot(plot);
	
	        
	     
	        /*
	        plot = new DataSetPlot(dataSet);
	        plot.setTitle("GCT");
	        plot.set("using", "1:14");
	        plot.getPlotStyle().setStyle(Style.LINES);
	        //plot.getPlotStyle().setLineWidth(2);
	        //plot.getPlotStyle().setLineType(7);
	        plot.set("axis x1y2");
	        p.addPlot(plot);
	        */
	        //p.set("style", "fill transparent solid 0.2 noborder");
	        
	        //PNGTerminal t = new PNGTerminal(900, 480);  
	        //PNGTerminal t = new PNGTerminal();  
	        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
	                System.getProperty("file.separator") + "Desktop/GC.eps");
	        t.setColor(true);
	        t.set("solid");
	        p.setTerminal(t);   
	        p.setPersist(false);
	        
	        p.plot();
	        
	        //return t.getImage();
	        return null;
		}
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public static BufferedImage plotSurvivorSpace(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
	
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		p.setKey(Key.TOP_RIGHT);
		
        p.setMultiTitle("Survivor Space (S0 and S1) Heap Memory Usage");
        //p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        AutoGraphLayout lo = new AutoGraphLayout();
        lo.setColumns(1); 
        p.getPage().setLayout(lo);

        //S0C S1C S0U S1U
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:18");
        plot.setTitle("S0C");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:20");   
        plot.setTitle("S0U");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(3);
        p.addPlot(plot);
        
        p.newGraph();
       
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:19");   
        plot.setTitle("S1C");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:21");   
        plot.setTitle("S1U");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(3);
        p.addPlot(plot);
        
        //PNGTerminal t = new PNGTerminal();  
        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
                System.getProperty("file.separator") + "Desktop/S0_S1.eps");
        t.setColor(true);
        t.set("solid");
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        //return t.getImage();
        return null;
	}
	
	//Timestamp OGCMN OGCMX OC OU	PGCMN PGCMX PC PU	YGC YGCT FGC FGCT GCT	NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
	public static BufferedImage plotEdenAndNewGen(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
		//JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		
        AutoGraphLayout lo = new AutoGraphLayout();
        lo.setColumns(1); 
        p.getPage().setLayout(lo);
        
        p.setMultiTitle("Eden Space and New Gen Heap Memory Usage");
        //p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
        p.setKey(Key.TOP_RIGHT);

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        //EC EU
        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:22");   
        plot.setTitle("EC");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet); 
        plot.set("using", "1:23");   
        plot.setTitle("EU");
        plot.getPlotStyle().setStyle(Style.LINESPOINTS);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(3);
        p.addPlot(plot);
       
        p.newGraph();
        //NGCMN NGCMX NGC S0C S1C S0U S1U EC EU
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:17");   
        plot.setTitle("NGC");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(50,204,50) + "'");
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:24");
        plot.setTitle("NGU");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(85,84,202) + "'");
        p.addPlot(plot);   
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:15");
        plot.setTitle("NGCMN");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(2);
        plot.getPlotStyle().setLineType(4);
        plot.getPlotStyle().set("lc", "rgb 'green'");
        p.addPlot(plot);      

        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:16");   
        plot.setTitle("NGCMX");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(5);
        plot.getPlotStyle().setLineType(1);
        p.addPlot(plot);
        
        p.set("style", "fill transparent solid 1.0 noborder");
        
        //PNGTerminal t = new PNGTerminal();  
       
        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
                System.getProperty("file.separator") + "Desktop/Eden_New.eps");
        t.setColor(true);
        t.set("solid");
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        //return t.getImage();
        return null;
	}	
	
	public static BufferedImage plotOldGenAndGC(List<ArrayList<String>> list) {
		JavaPlot p = new JavaPlot();
		//JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        p.set("xdata", "time");
		p.set("timefmt", "'%H:%M:%S'");
		p.set("style", "data lines");
		p.set("xtics", "nomirror");	
		p.set("format", "x '%M:%S'");
		
        //AutoGraphLayout lo = new AutoGraphLayout();
		StripeLayout lo = new StripeLayout();
		lo.setColumns(1); 
        p.getPage().setLayout(lo);
        
        p.setMultiTitle("Memory Usage of Old Generation and GC Count");
        //p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Memory (MB)");
        p.setKey(Key.TOP_RIGHT);
		
		

        GenericDataSet dataSet = new GenericDataSet(true);
        dataSet.addAll(list);

        DataSetPlot plot = new DataSetPlot(dataSet);
        plot.set("using", "1:4");   
        //plot.set("using", "1:4:(" + rgb(50,204,50) + ")");   
        plot.setTitle("OC");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(50,204,50) + "'");
        p.addPlot(plot);            
        
        plot = new DataSetPlot(dataSet);
        //plot.set("using", "1:5:(" + rgb(85,84,202) + ")");
        plot.set("using", "1:5");
        plot.setTitle("OU");
        plot.getPlotStyle().setStyle(Style.FILLEDCURVES);
        plot.getPlotStyle().set("y1=0");
        plot.getPlotStyle().set("lc", "rgb '" + rgb(85,84,202) + "'");
        p.addPlot(plot); 
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:2");   
        plot.setTitle("OGCMN");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineType(6);
        plot.getPlotStyle().set("lc", "rgb 'green'");
        p.addPlot(plot);
        
        plot = new DataSetPlot(dataSet);
        plot.set("using", "1:3");   
        plot.setTitle("OGCMX");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(10);
        plot.getPlotStyle().set("lc", "rgb 'red'");
        p.addPlot(plot);
       
        p.newGraph();
       
        
        plot = new DataSetPlot(dataSet);
        plot.setTitle("YGC");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineWidth(3);
        plot.set("using", "1:10");
        p.addPlot(plot);
           
        plot = new DataSetPlot(dataSet);
        plot.setTitle("FGC");
        plot.getPlotStyle().setStyle(Style.LINES);
        plot.getPlotStyle().setLineType(2);
        plot.getPlotStyle().setLineWidth(3);
        plot.getPlotStyle().set("lc", "rgb 'blue'");
        plot.set("using", "1:12");
        p.addPlot(plot);
        
        p.set("style", "fill transparent solid 1.0 noborder");
       
        //PNGTerminal t = new PNGTerminal();  
        PostscriptTerminal t = new PostscriptTerminal(System.getProperty("user.home") +
                System.getProperty("file.separator") + "Desktop/Old.eps");
        t.setColor(true);
        t.set("solid");
        p.setTerminal(t);   
        p.setPersist(false);
        p.plot();
        
        //return t.getImage();
        return null;
	}
		
	public static String rgb(int r, int g, int b) {
		
		return "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
	}
	public static void main(String[] args) {
		/*
		List<ArrayList<String>> listlist = parse(new File("/home/xulijie/Desktop/jstat.txt"));
		for(ArrayList<String> list : listlist) {
			for(String s : list)
				System.out.print(s + " ");
			System.out.println();
			
		}
		*/
		File metricsFile = new File("/home/xulijie/Desktop/jstat.txt");	
		
		List<ArrayList<String>> metricsList = null;

		boolean isMap = true;
		
		long start = System.currentTimeMillis();
		if (metricsFile.exists())
			metricsList = JstatMetricsImage.parse(metricsFile);
		

		
		JstatMetricsImage.plotEdenAndNewGen(metricsList);
		JstatMetricsImage.plotSurvivorSpace(metricsList);
		JstatMetricsImage.plotOldGenAndGC(metricsList);
		JstatMetricsImage.plotPermGen(metricsList);
		JstatMetricsImage.plotGCCount(metricsList);
	}
}


