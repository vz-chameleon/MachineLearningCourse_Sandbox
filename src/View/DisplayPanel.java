package View;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import KohonenCard.Neuron;
import KohonenCard.SOM;
import data.Sample;

public class DisplayPanel extends JPanel implements Observer{
	SOM model;
	int n1, n2;
	Color BACKGROUND_COLOR = Color.LIGHT_GRAY, LINE_COLOR = Color.GRAY;
	int border_mergin = 3;
	
	public DisplayPanel(SOM som, int p1, int p2) {
		model = som;
		som.addObserver(this);
		
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		n1 = p1;
		n2 = p2;
	}
	
	@Override
	public void paint(Graphics g)
	{
		int xsize = (int) this.getSize().getWidth(), ysize = (int) this.getSize().getHeight();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, xsize, ysize);
		g.setColor(BACKGROUND_COLOR);
		xsize = xsize - 2*border_mergin;
		ysize = ysize - 2*border_mergin;
		g.fillRect(border_mergin, border_mergin, xsize, ysize);
		g.setColor(Color.BLACK);
		for (float i = 0.1f; i <= 1.0f; i+=0.1f) {
			g.drawLine((int)(i*xsize), 0, (int)(i*xsize), ysize);
			g.drawLine(0, (int)(i*ysize), xsize, (int)(i*ysize));
		}
		for (Neuron neuron : model.getNeurones()) {
			g.setColor(chooseColor(neuron.getDataClass()));
			int x = border_mergin + (int)(neuron.getWeight(n1)*xsize), y = border_mergin + (int)(neuron.getWeight(n2)*ysize);
			int size = 5;
			if (neuron.exemple != -1) {
				size = neuron.exemple + 1;
			}
			g.fillOval(x-size, y-size, 2*size, 2*size);
			g.setColor(LINE_COLOR);
			for (Neuron neighboor : neuron.getNeighbours()) {
				if (neighboor.getNumber() > neuron.getNumber()) {
					int x2 = border_mergin + (int)(neighboor.getWeight(n1)*xsize), y2 = border_mergin + (int)(neighboor.getWeight(n2)*ysize);
					g.drawLine(x, y, x2, y2);
				}
			}
		}
		for (Sample data : model.actual_data) {
			g.setColor(chooseColor(data.getClassLabel()));
			int x = (int)(data.getFeatures()[n1]*xsize), y = (int)(data.getFeatures()[n2]*ysize);
			g.fillOval(x-3, y-3, 6, 6);
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
