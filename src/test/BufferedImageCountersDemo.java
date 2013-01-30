package test;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import cn.ac.iscas.counters.TaskCountersImage;
import cn.ac.iscas.metrics.TaskMetricsImage;


public class BufferedImageCountersDemo {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.add(new ImagePanel2());
		jframe.setSize(640 * 3, 480 * 3);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
}
class ImagePanel2 extends JPanel {

	//private BufferedImage image;
	private List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public ImagePanel2() {
		//File countersFile = new File("/home/xulijie/develop/hadooplog/myMetrics/",
		//		"job_201204281623_0002/attempt_201204281623_0002_r_000000_0.counters");
		File countersFile = new File("/home/xulijie/develop/hadooplog/myMetrics/",
						"job_201205032110_0001/attempt_201205032110_0001_r_000045_0.counters");
		
		List<ArrayList<String>> countersList = null;

		boolean isMap = false;
		
		long start = System.currentTimeMillis();
		if (countersFile.exists())
			countersList = TaskCountersImage.parse(countersFile, isMap);
		

		
		//imageList.add(TaskCountersImage.plotHDFS(countersList, isMap));
	
		imageList.add(TaskCountersImage.plotMRRecords(countersList, isMap));
		
		imageList.add(TaskCountersImage.plotReduceSplilledRecords(countersList));
		
		imageList.add(TaskCountersImage.plotCombineRecords(countersList, isMap));
		
		imageList.add(TaskCountersImage.plotBytes(countersList, isMap));
		
		long end = System.currentTimeMillis();

		System.out.println("Time cost : " + (end - start) + "ms");
	}

	@Override
	public void paintComponent(Graphics g) {
		
		g.drawImage(imageList.get(0), 0, 0, null);	
		g.drawImage(imageList.get(1), 710, 0, null);
		g.drawImage(imageList.get(2), 0, 480, null);
		g.drawImage(imageList.get(3), 710, 480, null);
		
		
		
	}

}




