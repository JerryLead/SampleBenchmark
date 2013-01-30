package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.ac.iscas.jvm.JvmMetricsImage;


public class BufferedImageJvmDemo {

	public static void main(String[] args) {
		JFrame jframe = new JFrame();
		jframe.add(new ImagePanel3());
		jframe.setSize(640 * 3, 480 * 3);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
}

class ImagePanel3 extends JPanel {

	//private BufferedImage image;
	private List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public ImagePanel3() {
		File metricsFile = new File("/home/xulijie/develop/hadooplog/myMetrics/job_201208241427_0001" +
				//"/attempt_201208241427_0001_m_000002_0.jvm");
				"/my.jvm");
		
		
		List<ArrayList<String>> metricsList = null;

		boolean isMap = true;
		
		long start = System.currentTimeMillis();
		if (metricsFile.exists())
			metricsList = JvmMetricsImage.parse(metricsFile);
		

		
		imageList.add(JvmMetricsImage.plotJvmMem(metricsList));
	
		
		
		long end = System.currentTimeMillis();

		System.out.println("Time cost : " + (end - start) + "ms");
	}

	@Override
	public void paintComponent(Graphics g) {
		
		g.drawImage(imageList.get(0), 0, 0, null);	
		//g.drawImage(imageList.get(1), 0, 480, null);
		
		
		
	}

}