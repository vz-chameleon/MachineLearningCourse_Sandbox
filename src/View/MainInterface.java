package View;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import KohonenCard.SOM;

public class MainInterface extends JFrame implements Observer{
	JTextField jtfsig, jtflr, jtfepoch;
	SOM model;
	
	public int factorial(int n) {
		int res = 1;
		for (int i = 1; i <= n; i++)
			res *= i;
		return res;
	}
	public MainInterface(SOM som) {
		
		super("Kohonen Card");
		setSize (1080,1080);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		model = som;
		model.addObserver(this);
		GridLayout big_layout = new GridLayout(1,2);
		big_layout.setHgap(5);
		this.setLayout(big_layout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		
		JPanel overall_display = new JPanel();
		int n = som.getDataSize();
		int d = factorial(n)/(factorial(2)*factorial(n-2));
		int i1 = 0, i2 = 1;
		GridLayout layout;
		if (n == 2)
			layout = new GridLayout(1,1);
		else
			layout = new GridLayout(2,0);
		layout.setHgap(5);
		layout.setVgap(5);
		overall_display.setLayout(layout);
		for (int i = 0; i < d; i++) {
			DisplayPanel dp = new DisplayPanel(som, i1, i2);
			dp.setSize(300,300);
			overall_display.add(dp);
			i2++;
			if(i2 >= n) {
				i1++;
				i2 = i1+1;
			}
		}
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		this.add(overall_display, c);
		this.add(new CountPanel(som));
		
		/*JPanel parameters = new JPanel();
		parameters.setLayout(new GridLayout(0,2));		
		
		JLabel jl1= new JLabel("Learning Rate");
		jtflr = new JTextField("0.5");
		JLabel jl2 = new JLabel("Sigma");
		jtfsig = new JTextField("0.25");
		JLabel jl3 = new JLabel("Epoch");
		jtfepoch = new JTextField("0");
		parameters.add(jl1);
		parameters.add(jtflr);
		parameters.add(jl2);
		parameters.add(jtfsig);
		parameters.add(jl3);
		parameters.add(jtfepoch);
		c.gridwidth = 1;
		c.gridx = 3;
		this.add(parameters, c);*/
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		/*jtflr.setText(model.learning_rate+"");
		jtfsig.setText(model.sigma+"");
		jtfepoch.setText(model.actual_epoch+"");*/
	}

}
