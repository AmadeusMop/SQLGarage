import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class Screen extends JFrame {
	
	private JPanel mainPanel;
	private JPanel introPanel;
	private JTabbedPane vehicleInfoPanel;
	private JPanel submitPanel;
	private JPanel resultsPanel;
	
	private GarageConnection connection;
	
	public Screen() {
		/* All non-GUI setup */
		connection = new GarageConnection();
		
		/* GUI element declaration and initialization */
		mainPanel = new JPanel();
		introPanel = new JPanel();
		vehicleInfoPanel = new JTabbedPane();
		submitPanel = new JPanel();
		resultsPanel = new JPanel();
		
		/* introPanel setup */
		//introPanel.setLayout();
		introPanel.add(new JLabel("intro panel"));
		
		/* vehicleInfoPanel setup */
		//vehicleInfoPanel.setLayout();
        VehicleInfoPanel carPanel = new VehicleInfoPanel.carInfoPanel();
        VehicleInfoPanel boatPanel = new VehicleInfoPanel.boatInfoPanel();
        setUpVehicleInfoPanel(carPanel);
        setUpVehicleInfoPanel(boatPanel);
        
        vehicleInfoPanel.addTab("Car", carPanel);
        vehicleInfoPanel.addTab("Boat", boatPanel);
		
		/* submitPanel setup */
		//submitPanel.setLayout();
		submitPanel.add(new JLabel("submit panel"));
		
		/* resultsPanel setup */
		//resultsPanel.setLayout();
		resultsPanel.add(new JLabel("results panel"));
		
		/* mainPanel setup */
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(introPanel);
		mainPanel.add(vehicleInfoPanel);
		mainPanel.add(submitPanel);
		mainPanel.add(resultsPanel);
		add(mainPanel);
		
		setPreferredSize(new Dimension(640, 480));
		
		repaint();
		pack();
		setVisible(true);
	}
	
	private void setUpVehicleInfoPanel(JPanel panel) {
		panel.setLayout(new GridLayout(0, 4));
		panel.add(new JLabel("Make: "));
		panel.add(new JTextField());
		panel.add(new JLabel("Model: "));
		panel.add(new JTextField());
	}
	
	public static void main(String[] args) {
		Screen s = new Screen();
	}
	
	private abstract static class VehicleInfoPanel extends JPanel {
		protected GridBagConstraints constraints;
		private JTextField makeField;
		private JTextField modelField;
		private JSpinner yearSpinner;
		private JSpinner radiusSpinner;
		
		public VehicleInfoPanel() {
			setLayout(new GridBagLayout());
			
			constraints = new GridBagConstraints();
			makeField = new JTextField();
			modelField = new JTextField();
			yearSpinner = new JSpinner(new SpinnerNumberModel(2015, 1900, 2015, 1));
			radiusSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 40, 1));
			
		}
		
		public abstract int getType();
		
		public String getMake() {
			return makeField.getText();
		}
		
		public String getModel() {
			return modelField.getText();
		}
		
		public int getYear() {
			return (Integer)yearSpinner.getValue();
		}
		
		public int getRadius() {
			return (Integer)radiusSpinner.getValue();
		}
		
		public abstract int[] getSpecificInfo();
		
		
		
		private static class carInfoPanel extends VehicleInfoPanel implements ActionListener {
			
			private ButtonGroup typeGroup;
			private JSpinner doorsSpinner;
			private JSpinner fuelCapacitySpinner;
			private JSpinner fuelEfficiencySpinner;
			private JSpinner mileageSpinner;
			private JLabel capacityLabel;
			private JLabel efficiencyLabel;
			
			private int type;
			
			private final String[][] typeSpecificFuelData = new String[][] {
				{ "gallons", "kWh" },
				{ "mpg", "kilowatts" }
			};
			
			public carInfoPanel() {
				super();
				type = 0;
				
				typeGroup = new ButtonGroup();
				doorsSpinner = new JSpinner(new SpinnerNumberModel(2015, 1900, 2015, 1));
				fuelCapacitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
				fuelEfficiencySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 200, 1));
				mileageSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));
				
				JRadioButton gasButton = new JRadioButton("Gas");
				JRadioButton electricButton = new JRadioButton("Electric");
				gasButton.setActionCommand("0");
				electricButton.setActionCommand("1");
				gasButton.addActionListener(this);
				electricButton.addActionListener(this);
				typeGroup.add(gasButton);
				typeGroup.add(electricButton);

				capacityLabel = new JLabel("c");
				efficiencyLabel = new JLabel("e");
				
				add(new JLabel("Engine Type"));
				add(gasButton);
				add(electricButton);
				add(fuelCapacitySpinner);
				add(capacityLabel);
				add(fuelEfficiencySpinner);
				add(efficiencyLabel);
				add(new JLabel("Mileage"));
				add(mileageSpinner);
			}

			public int[] getSpecificInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			public int getType() {
				return type;
			}

			public void actionPerformed(ActionEvent e) {
				type = Integer.parseInt(e.getActionCommand());
				capacityLabel.setText(typeSpecificFuelData[0][type]);
				efficiencyLabel.setText(typeSpecificFuelData[1][type]);
			}
		}
		
		
		private static class boatInfoPanel extends VehicleInfoPanel {
			
			public boatInfoPanel() {
				super();
			}

			public int[] getSpecificInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			public int getType() {
				return 2;
			}
		}
	}
}
