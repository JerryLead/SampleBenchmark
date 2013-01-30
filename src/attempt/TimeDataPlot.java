package attempt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.iodebug.Debug;



public class TimeDataPlot {
	public static void gnuplot() {
		JavaPlot p = new JavaPlot();
        JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
        
        p.setTitle("Time Data Demo");
        p.getAxis("x").setLabel("Time (sec)");//, "Arial", 20);
        p.getAxis("y").setLabel("Bytes (MB)");

        
        //p.getAxis("x").setBoundaries(0, list.get(list.size() - 1).getTime());
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
       
	}
	public static void main(String[] args) throws ParseException, IOException {
		
		String s = "1335234654";
		long t = Long.parseLong(s);
		DateFormat f = new SimpleDateFormat("HH:mm:ss");
		System.out.println(f.format(t));
		
		File file = new File("/home/xulijie/develop/Data/time.txt");
		File outputFile = new File("/home/xulijie/develop/Data/outputTime.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
		while((line = reader.readLine()) != null) {
			String[] time = line.split("\\s+");
			writer.println(f.format(Long.parseLong(time[0] + "000")) + " " + time[1]);
		}
		
		reader.close();
		writer.close();
	}
}
