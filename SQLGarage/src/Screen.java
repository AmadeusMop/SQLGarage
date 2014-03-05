import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.*;


@SuppressWarnings("serial")
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
	
	public static void main(String[] args) {
		Screen s = new Screen();
	}
	
	private abstract static class VehicleInfoPanel extends JPanel {
		
		/* Field declarations */
		protected GridBagConstraints C;
		private JTextField makeField;
		private JTextField modelField;
		private JSpinner yearSpinner;
		private JSpinner radiusSpinner;
		
		/* Constructor */
		public VehicleInfoPanel() {
			setLayout(new GridBagLayout());
			
			C = new GridBagConstraints();
			C.gridx = 0;
			C.gridy = 0;
			C.weightx = 0.5;
			C.weighty = 0.5;
			
			makeField = new JTextField();
			makeField.setPreferredSize(new Dimension(100, 20));
			
			modelField = new JTextField();
			modelField.setPreferredSize(new Dimension(100, 20));
			
			yearSpinner = new JSpinner(new SpinnerNumberModel(2015, 1900, 2015, 1));
			
			radiusSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 40, 1));
			
			C.gridx = 0;
			add(new JLabel("Make"), C);
			C.gridx = 1;
			add(makeField, C);

			C.gridx = 2;
			add(new JLabel("Model"), C);
			C.gridx = 3;
			add(modelField, C);

			C.gridx = 4;
			add(new JLabel("Year"), C);
			C.gridx = 5;
			add(yearSpinner, C);
			
			C.gridy = 1;
			C.gridx = 0;
			add(new JLabel("Steering Wheel Radius"), C);
			add(radiusSpinner, C);
			
			C.gridy = 2;
		}
		
		/* Abstract method declarations */
		public abstract int getType();
		
		public abstract int[] getSpecificInfo();
		
		/* Non-abstract method declarations */
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
		
		/* carInfoPanel definition */
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
				doorsSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 16, 1));
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
				typeGroup.setSelected(gasButton.getModel(), true);

				capacityLabel = new JLabel("gallons");
				efficiencyLabel = new JLabel("mpg");
				
				/*add(new JLabel("Engine Type"));
				add(gasButton);
				add(electricButton);
				
				add(new JLabel("Fuel"));
				add(fuelCapacitySpinner);
				add(capacityLabel);
				add(fuelEfficiencySpinner);
				add(efficiencyLabel);
				
				add(new JLabel("Doors"));
				add(doorsSpinner);
				
				add(new JLabel("Mileage"));
				add(mileageSpinner);*/
			}

			public int[] getSpecificInfo() {
				return new int[] { getDoors(), getFuelCapacity(), getFuelEfficiency(), getMileage() };
			}

			public int getType() {
				return type;
			}
			
			private int getDoors() {
				return (Integer)doorsSpinner.getValue();
			}
			
			private int getFuelCapacity() {
				return (Integer)fuelCapacitySpinner.getValue();
			}
			
			private int getFuelEfficiency() {
				return (Integer)fuelEfficiencySpinner.getValue();
			}
			
			private int getMileage() {
				return (Integer)mileageSpinner.getValue();
			}

			public void actionPerformed(ActionEvent e) {
				type = Integer.parseInt(e.getActionCommand());
				capacityLabel.setText(typeSpecificFuelData[0][type]);
				efficiencyLabel.setText(typeSpecificFuelData[1][type]);
			}
		}
		
		/* boatInfoPanel definition */
		private static class boatInfoPanel extends VehicleInfoPanel {
			private JSpinner rangeSpinner;
			
			public boatInfoPanel() {
				super();
				
				rangeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));
				add(new JLabel("Range"));
				add(rangeSpinner);
			}

			public int[] getSpecificInfo() {
				return new int[] { getRange() };
			}
			
			private int getRange() {
				return (Integer)rangeSpinner.getValue();
			}

			public int getType() {
				return 2;
			}
		}
	}
}
