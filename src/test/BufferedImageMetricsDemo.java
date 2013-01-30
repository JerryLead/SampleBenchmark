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


public class BufferedImageMetricsDemo {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.add(new ImagePanel());
		jframe.setSize(640 * 3, 480 * 3);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
}

class ImagePanel extends JPanel {

	//private BufferedImage image;
	private List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public ImagePanel() {
		File metricsFile = new File("/home/xulijie/develop/hadooplog/myMetrics/",
				"job_201204281623_0002/attempt_201204281623_0002_m_000001_0.metrics");
		
		
		List<ArrayList<String>> metricsList = null;

		boolean isMap = true;
		
		long start = System.currentTimeMillis();
		if (metricsFile.exists())
			metricsList = TaskMetricsImage.parse(metricsFile);
		

		
		imageList.add(TaskMetricsImage.plotCpuAndIO(metricsList));
	
		imageList.add(TaskMetricsImage.plotMEM(metricsList));
		
		long end = System.currentTimeMillis();

		System.out.println("Time cost : " + (end - start) + "ms");
	}

	@Override
	public void paintComponent(Graphics g) {
		
		g.drawImage(imageList.get(0), 0, 0, null);	
		g.drawImage(imageList.get(1), 0, 480, null);
		
		
		
	}

}

