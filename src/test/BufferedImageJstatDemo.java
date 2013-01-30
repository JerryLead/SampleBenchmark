package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.ac.iscas.jstat.JstatMetricsImage;


public class BufferedImageJstatDemo {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.add(new ImagePanel4());
		jframe.setSize(640 * 3, 480 * 3);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
}

class ImagePanel4 extends JPanel {

	//private BufferedImage image;
	private List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public ImagePanel4() {
		File metricsFile = new File("/home/xulijie/Desktop/jstat.txt");	
		
		List<ArrayList<String>> metricsList = null;

		boolean isMap = true;
		
		long start = System.currentTimeMillis();
		if (metricsFile.exists())
			metricsList = JstatMetricsImage.parse(metricsFile);
		

		
		imageList.add(JstatMetricsImage.plotEdenAndNewGen(metricsList));
		imageList.add(JstatMetricsImage.plotSurvivorSpace(metricsList));
		imageList.add(JstatMetricsImage.plotOldGen(metricsList));
		imageList.add(JstatMetricsImage.plotPermGen(metricsList));
		imageList.add(JstatMetricsImage.plotGC(metricsList));
	
		
		
		long end = System.currentTimeMillis();

		System.out.println("Time cost : " + (end - start) + "ms");
	}

	@Override
	public void paintComponent(Graphics g) {
		
		g.drawImage(imageList.get(1), 0, 0, null);	
		g.drawImage(imageList.get(0), 640, 0, null);
		g.drawImage(imageList.get(2), 0, 480, null);
		g.drawImage(imageList.get(3), 640, 480, null);
		
		
		
	}

}