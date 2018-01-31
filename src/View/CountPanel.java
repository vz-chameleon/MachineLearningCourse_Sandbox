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
import View.ChartPanel.Point;

public class CountPanel extends JPanel implements Observer{
	
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
	private ArrayList<Point> point;
	private ArrayList<Color> color;
	
	public CountPanel(Observable o) {
		super();
		o.addObserver(this);
		horizontal_mergin = 5;
		vertical_mergin = 5;
		point_size = 2;
		marking_size = 0.02;
		mergin_is_percentage = true;
		chart_zone = new Rectangle();
		point = new ArrayList<Point>();
		color = new ArrayList<Color>();
		min_x = 0;
		max_x = 0;
		min_y = 0;
		max_y = 10;
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
		for (Point p : point) {
			if (color.size() >= p.x)
				g.setColor(color.get(p.x));
			Point np = translateCoordonates(p);
			Point nppu = new Point(p.x+1,p.y);
			nppu = translateCoordonates(nppu);
			g.fillRect(np.x, np.y, 10, chart_zone.y+chart_zone.height - np.y);			
		}
	}

	private Point translateCoordonates(Point p) {
		int nx = (int)(chart_zone.x + chart_zone.width * (float)(p.x-min_x)/(max_x-min_x)), ny = (int)(chart_zone.y + chart_zone.height - chart_zone.height * (float)(p.y-min_y)/(max_y-min_y));
		return new Point(nx,ny);
	}
	private Color chooseColor(String dataClass) {
		switch (dataClass) {
		case "Iris-versicolor":
			return Color.CYAN;
		case "Iris-setosa":
			return Color.ORANGE;
		case "Iris-virginica":
			return Color.PINK;
		default :
			return Color.BLACK;
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		SOM som = (SOM) arg0;
		max_x = som.getNeurones().size();
		point = new ArrayList<Point>();
		color = new ArrayList<Color>();
		for (Neuron n : som.getNeurones()) {
			point.add(new Point(n.getNumber(),n.exemple));
			color.add(chooseColor(n.getDataClass()));
			if (n.exemple > max_y)
				max_y = n.exemple;
		}
	}

}
