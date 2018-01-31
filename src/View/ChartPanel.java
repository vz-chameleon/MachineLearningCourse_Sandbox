package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import KohonenCard.Neuron;
import KohonenCard.SOM;

public class ChartPanel extends JPanel implements Observer{
	
	public class Point{
		public int x, y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private int horizontal_mergin, vertical_mergin, point_size;
	double marking_size;
	private Rectangle chart_zone;
	private boolean mergin_is_percentage;
	private int min_x,max_x,min_y,max_y;
	private List<List<Point>> data_lists;
	private List<String> data_class;
	private List<Color> data_color;
	
	public ChartPanel(Observable o) {
		super();
		o.addObserver(this);
		horizontal_mergin = 5;
		vertical_mergin = 5;
		point_size = 2;
		marking_size = 0.02;
		mergin_is_percentage = true;
		data_lists = new ArrayList<List<Point>>();	
		data_color = new ArrayList<Color>();
		data_class = new ArrayList<String>();
		chart_zone = new Rectangle();
		min_x = 0;
		max_x = 0;
		min_y = 0;
		max_y = 0;
	}
	
	public void addDataPoint(String data_class, int data_list_index, int data_x, int data_y) {
		if (data_list_index < data_lists.size()) {
			data_lists.get(data_list_index).add(new Point(data_x,data_y));
			if (data_x < min_x)
				min_x = ((data_x/10)-1) * 10;
			if (data_y < min_y)
				min_y = ((data_y/10)-1) * 10;
			if (data_x > max_x)
				max_x = ((data_x/10)+1) * 10;
			if (data_y > max_y)
				max_y = ((data_y/10)+1) * 10;
		} else {
			data_lists.add(new ArrayList<Point>());
			data_lists.get(data_lists.size()-1).add(new Point(data_x,data_y));
			Color c = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
			switch (data_class) {
				case "Iris-versicolor":
					c = Color.CYAN;
					break;
				case "Iris-setosa":
					c = Color.ORANGE;
					break;
				case "Iris-virginica":
					c = Color.PINK;
					break;					
			}
			data_color.add(c);
		}
	}

	@Override
	public void paint(Graphics g)
	{
		chart_zone.x = (int)(this.getWidth() * ((float)horizontal_mergin/100));
		chart_zone.y = (int)(this.getHeight() * ((float)vertical_mergin/100));
		chart_zone.width = this.getWidth() - 2 * chart_zone.x;
		chart_zone.height = this.getHeight() - 2 * chart_zone.y;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		int x1, x2, y1, y2;
		x1 = chart_zone.x;
		x2 = x1;
		y1 = chart_zone.y + chart_zone.height;
		y2 = chart_zone.y;
		g.drawLine(x1,y1,x2,y2);
		for (double k = 0; k <= 1.0; k+=0.1) {
			int x3 = (int)(x1+k*chart_zone.width);
			int y3 = (int)(y1-k*chart_zone.height);
			g.drawLine(x3,y1 ,x3, (int)(y1 - chart_zone.height*marking_size));
			g.drawString((int)(min_x + k * (max_x-min_x))+"", x3, y1+25);
			g.drawLine(x1 ,y3, (int)(x1 + chart_zone.width*marking_size),y3);
			g.drawString((int)(min_y + k * (max_y-min_y))+"", x1-25, y3);
		}
		x2 = x1 + chart_zone.width;
		y2 = y1;
		g.drawLine(x1,y1,x2,y2);

		for (int i = 0; i < data_lists.size(); i++) {
			g.setColor(data_color.get(i));
			Point lp = translateCoordonates(data_lists.get(i).get(0));
			for (Point p : data_lists.get(i)) {
				Point np = translateCoordonates(p);
				g.fillRect(np.x-point_size, np.y-point_size, 2*point_size, 2*point_size);
				if (p.x >= 1.1)
					g.drawLine(lp.x, lp.y, np.x, np.y);
				lp = np;
			}
		}
	}

	private Point translateCoordonates(Point p) {
		int nx = (int)(chart_zone.x + chart_zone.width * (float)(p.x-min_x)/(max_x-min_x)), ny = (int)(chart_zone.y + chart_zone.height - chart_zone.height * (float)(p.y-min_y)/(max_y-min_y));
		return new Point(nx,ny);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof HashMap) {
			HashMap<String,Integer> map = (HashMap<String,Integer>) arg1; 
			for (HashMap.Entry<String,Integer> entry : map.entrySet()) {
				int index = data_class.indexOf(entry.getKey());
				if (index == -1) {
					data_class.add(entry.getKey());
					index = data_class.size()-1;
				}
				addDataPoint(entry.getKey(),index,((SOM)arg0).actual_epoch,entry.getValue());
			}
			repaint();
		}		
	}

}
