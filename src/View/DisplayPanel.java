package View;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import KohonenCard.Neuron;
import KohonenCard.SOM;

public class DisplayPanel extends JPanel implements Observer{
	SOM model;
	int n1, n2;
	Color BACKGROUND_COLOR = Color.LIGHT_GRAY, LINE_COLOR = Color.GRAY;
	
	public DisplayPanel(SOM som, int p1, int p2) {
		model = som;
		som.addObserver(this);
		
		this.setBorder(new LineBorder(Color.BLACK,5));
		n1 = p1;
		n2 = p2;
	}
	
	@Override
	public void paint(Graphics g)
	{
		int xsize = (int) this.getSize().getWidth(), ysize = (int) this.getSize().getHeight();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, xsize, ysize);
		g.setColor(Color.BLACK);
		for (float i = 0.1f; i <= 1.0f; i+=0.1f) {
			g.drawLine((int)(i*xsize), 0, (int)(i*xsize), ysize);
			g.drawLine(0, (int)(i*ysize), xsize, (int)(i*ysize));
		}
		for (Neuron neuron : model.getNeurones()) {
			g.setColor(chooseColor(neuron.getDataClass()));
			int x = (int)(neuron.getWeight(n1)*xsize), y = (int)(neuron.getWeight(n2)*ysize);
			g.fillOval(x-5, y-5, 10, 10);
			g.setColor(LINE_COLOR);
			for (Neuron neighboor : neuron.getNeighbours()) {
				if (neighboor.getNumber() > neuron.getNumber()) {
					int x2 = (int)(neighboor.getWeight(n1)*xsize), y2 = (int)(neighboor.getWeight(n2)*ysize);
					g.drawLine(x, y, x2, y2);
				}
			}
		}
		if (model.actual_data != null) {
			g.setColor(Color.RED);
			int x = (int)(model.actual_data.getFeatures()[n1]*xsize), y = (int)(model.actual_data.getFeatures()[n2]*ysize);
			g.fillOval(x-10, y-10, 20, 20);
		}
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
	public void update(Observable o, Object arg) {
		this.repaint();
	}
}
