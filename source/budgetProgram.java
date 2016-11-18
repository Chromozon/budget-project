import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.*;
import java.io.*;

/*frames
frame
newPayFrame
*/

/*Panels
mainPanel
payControlsPanel
statsPanel
rightPanel
*/

/* Layout Managers
payControls
leftGridBag
*/


//Compile and Run
//cd ../source && javac -d ../classes budgetProgram.java && cd ../classes && java budgetProgram

public class budgetProgram implements Serializable{
	
	private JFrame frame; //frame for the entire program
	JPanel mainPanel; //create main panel until new panels made
	private JButton addPayment;
	private JButton delPayment;
	
	public static void main(String[] args){
		new budgetProgram().go();

	}
	
	
	public void go(){
		GridBagConstraints gbc = new GridBagConstraints();
		frame = new JFrame("Budget Yourself");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		Font titleFont = new Font("sanserif",Font.BOLD,36);

		//create the default model
		DefaultListModel payListModel = new DefaultListModel();
		
		//JList for Payments
		JList payList = new JList (payListModel);
		payList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ); //prevents multiple selection
		
		//import any existing data. I wanted to make this its own method, but this worked best with the current design.
		try {
			FileInputStream inFile = new FileInputStream("budgetYourselfData.data");
			ObjectInputStream is = new ObjectInputStream(inFile);
			JList inList = (JList) is.readObject();
			ListModel inModel = inList.getModel();
				for(int i = 0; i < inModel.getSize(); i++){
					payListModel.addElement(inModel.getElementAt(i));
				}

			} catch (FileNotFoundException ex){
				//do nothing
			}
			catch(Exception ex){
				ex.printStackTrace();
			}



		//create center stats area
		JPanel statsPanel = new JPanel();
		JPanel graphPanel = new JPanel();
		//create border to put into panel, because '90s
		TitledBorder statsBorder = new TitledBorder(new LineBorder(Color.BLACK), "Stats",TitledBorder.CENTER,TitledBorder.ABOVE_TOP);
		statsBorder.setTitleColor(Color.BLACK);
		TitledBorder graphBorder  = new TitledBorder( new LineBorder(Color.BLUE), "Graph",TitledBorder.CENTER, TitledBorder.ABOVE_TOP);
		graphBorder.setTitleColor(Color.BLACK);
		
		statsPanel.setMaximumSize(new Dimension(650,400));
		statsPanel.setPreferredSize(new Dimension(650, 300));
		statsPanel.setMinimumSize(new Dimension(650,300));
		JPanel statsPanelLeft = new JPanel();
		JPanel statsPanelRight = new JPanel();
		statsPanel.setLayout( new BoxLayout(statsPanel, BoxLayout.LINE_AXIS) );
		//statsPanelLeft.setBackground(Color.green);
		statsPanelLeft.setLayout( new GridBagLayout() );
		statsPanelLeft.setPreferredSize( new Dimension(325,300) );
		statsPanelRight.setPreferredSize( new Dimension(325,300) );
		statsPanel.add(statsPanelLeft);
		statsPanel.add( new JSeparator(SwingConstants.VERTICAL) );
		statsPanel.add(statsPanelRight);
		
		
		graphPanel.setMaximumSize(new Dimension(650,500) );
		graphPanel.setPreferredSize(new Dimension(650,500) );
		graphPanel.setMinimumSize(new Dimension(650,500) );
		
		statsPanel.setBorder(statsBorder);
		graphPanel.setBorder(graphBorder);
		mainPanel.setLayout(new GridBagLayout());

		
		mainPanel.add(graphPanel, gbc);
		gbc.gridy = 1;
		gbc.insets = new Insets(10,0,0,0);
		mainPanel.add(statsPanel, gbc);



		//create stats class
		class totalStats
		{//Inner class of budgetProgram.go()
	
		//contains avg, max, min floats.
		//contains a method for refreshing calculation of those values with inputs.
		//not sure what the inputs are yet. A list of Payments?
	
			private double avg;
			private double max;
			private double min;
			private double selAvg;
			private double selMax;
			private double selMin;
			private JLabel totalAvgLabel;
			private JLabel totalMaxLabel;
			private JLabel totalMinLabel;
			private JLabel selAvgLabel;
			private JLabel selMaxLabel;
			private JLabel selMinLabel;
			ArrayList<String> displayStats;//need hash?
			private JList selectedList;
		
			public totalStats(){
				avg = 0;
				max = 0;
				min = 0;
				selAvg = 0;
				selMax = 0;
				selMin = 0;
				this.updateStats(payList);
			}
		
			public void updateStats(JList payList)
			{
				//Yeah, this section is ugly.
				
				ListModel model = payList.getModel();
				
				//go through JList and do math on entire list of payments
				//go through JList and do math on entire list of SELECTED payments
				double total = 0;
				for(int i=0; i < model.getSize(); i++)
				{
					Payment pay = (Payment) model.getElementAt(i);
					double  currentPay =  pay.getAmount();
					total = total + currentPay;
					
					if(currentPay > max)
					{
						max = currentPay;
					}
					
					if(i==0)
					{
						//for the first item, set baseline value
						min = currentPay;
					}
					
					if(currentPay < min)
					{
						min = currentPay;
					}
				}//end iterate
				
				//add post-iterate logic
				avg = total / model.getSize();
				
				
				
				
				//build UI with calculated values
				JLabel totalAvgLabel = new JLabel("<HTML>The total average: " + NumberFormat.getCurrencyInstance().format(avg) + "</HTML>");
				JLabel totalMaxLabel = new JLabel("<HTML>The total max: " + NumberFormat.getCurrencyInstance().format(max) + "</HTML>");
				JLabel totalMinLabel = new JLabel("<HTML>The total min: " + NumberFormat.getCurrencyInstance().format(min) + "</HTML>");
				JLabel selAvgLabel = new JLabel("<HTML>The selected average: " + NumberFormat.getCurrencyInstance().format(selAvg) + "</HTML>");
				JLabel selMaxLabel = new JLabel("<HTML>The selected max: " + NumberFormat.getCurrencyInstance().format(selMax) + "</HTML>");
				JLabel selMinLabel = new JLabel("<HTML>The selected min: " + NumberFormat.getCurrencyInstance().format(selMin) + "</HTML>");
				
				//reset GBC
				gbc.fill = GridBagConstraints.VERTICAL;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				gbc.ipady = 0;
				gbc.ipadx = 0;
				gbc.gridy = 0;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.EAST;
				statsPanelLeft.add(totalAvgLabel,gbc);
				gbc.gridx = 0;
				gbc.gridy = 1;
				statsPanelLeft.add(totalMaxLabel,gbc);
				gbc.gridx = 0;
				gbc.gridy = 2;
				statsPanelLeft.add(totalMinLabel,gbc);
				gbc.gridx = 0;
				gbc.gridy = 3;			
				statsPanelLeft.add(selAvgLabel,gbc);
				gbc.gridx = 0;
				gbc.gridy = 4;
				statsPanelLeft.add(selMaxLabel,gbc);
				gbc.gridx = 0;
				gbc.gridy = 5;
				statsPanelLeft.add(selMinLabel,gbc);
							
			}//end updateStats
		
		
		}//end totalStats
		
		//Create content for the stats panel to display. Will use this to refresh stats. Might refer for graph?
		totalStats currentStats = new totalStats();
		
		
		class RefreshStats implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				currentStats.updateStats(payList);
			}
		}
		JButton refreshStatsButton = new JButton("Refresh");
		refreshStatsButton.addActionListener( new RefreshStats() );
		mainPanel.add(refreshStatsButton, gbc);


		//crate box layout manager containing scroller and flowlayout with buttons 
		Box leftBox = new Box(BoxLayout.PAGE_AXIS);
		
		//payScroller creation
		JScrollPane payScroller = new JScrollPane(payList);
		payScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		payScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		FlowLayout payControls = new FlowLayout();
		JPanel payControlsPanel = new JPanel(payControls);
		
		//create actionListeners for buttons
		class AddNewPayment implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
					new budgetProgram().newPaymentGo(payListModel, payList);
			}
		}
		
		class RemovePayment implements ActionListener{
			public void actionPerformed(ActionEvent event){
				//may want to add confirmation message? 
				//single selection is forced.
				//System.out.println("Index: " + payList.getSelectedIndex() );
				//System.out.println(payList.isSelectionEmpty());
				if( !payList.isSelectionEmpty() )
				{
					payListModel.remove( payList.getSelectedIndex() );
					new budgetProgram().exportData(payList);
				}
			}
		}
		
		//create the Add button and its functionality
		addPayment = new JButton("Add");
		addPayment.addActionListener( new AddNewPayment() );
		payControlsPanel.add(addPayment);
		
		//create the Remove button and its  functionality
		delPayment = new JButton("Remove");
		delPayment.addActionListener ( new RemovePayment() );
		payControlsPanel.add(delPayment); //button implements actionListener, updateStats
		
		
		//payControlsPanel.setBackground(Color.GREEN);
		payControlsPanel.setMaximumSize(new Dimension(450,50));
		
		//add payScroller to layout manager
		leftBox.add(payScroller);
		leftBox.add(payControlsPanel);	
		

		//create right comboBox and checkboxes
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setMinimumSize(new Dimension(200,600));
		
		//create comboBox, need to create public class and implement actionListener, will updateStats
		String[] dateRanges = {"Today", "This Week","This Month","Last Month", "This Year"};
		JComboBox dateRange = new JComboBox(dateRanges);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		rightPanel.add(dateRange, gbc);
		
		//gridBagLayout for the scrolling list of checkboxes
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.ipady = 400;
		gbc.ipadx = 125;
		
		//**** for testing. These need to be generated from the payment objects.
		String[] payTypes = {"Fun","Food","Gas","Rent","Utilities", "Athletics","Health"};

			//typeBorder setBorder for scrollablePanel goes in typeScroller goes in rightPanel
			JPanel scrollablePanel = new JPanel();
			TitledBorder typeBorder = new TitledBorder(new LineBorder(Color.black),"Payment Types",TitledBorder.CENTER,TitledBorder.BELOW_TOP);
			scrollablePanel.setBorder(typeBorder);
			scrollablePanel.setLayout( new BoxLayout(scrollablePanel, BoxLayout.PAGE_AXIS));
			//create a JCheckBox for each String in payTypes
			for(String s: payTypes){
				scrollablePanel.add( new JCheckBox(s) );
			}
			
		//add scrollablePanel to typeScroller	
		JScrollPane typeScroller = new JScrollPane(scrollablePanel);	
		typeScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		typeScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//add typeScroller to rightPanel
		rightPanel.add(typeScroller, gbc);
		

		 
		 //Add base panels to frame
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.getContentPane().add(BorderLayout.WEST,leftBox);
		frame.getContentPane().add(BorderLayout.EAST,rightPanel);
		frame.setSize(1200,1000);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
	 
	}//end go()
	
	public void newPaymentGo(DefaultListModel model, JList payList){
		
		//make a new panel and populate it with a new instance of a Payment. We want to return that payment.
		JFrame newPayFrame = new JFrame("New Payment");
		String today = new SimpleDateFormat("MM/dd/yyyy").format( new java.util.Date() );
		Payment newPayment = new Payment();
		JPanel newPaymentPanel = new JPanel();
		newPaymentPanel.setLayout( new BoxLayout(newPaymentPanel,BoxLayout.PAGE_AXIS));
		
		//add fields to take new Payment input

		JTextField newPayName = new JTextField();
		JTextField newPayAmount = new JTextField();
		//JTextField newPayType = new JTextField();
		JTextField newPayDate = new JTextField(today);
		JTextArea newPayNote = new JTextArea(6,20);
		JLabel newPayNameLabel = new JLabel("Payment Name: ");
		newPayNameLabel.setLabelFor(newPayName);
		JLabel newPayAmountLabel = new JLabel("Payment Amount: ");
		newPayAmountLabel.setLabelFor(newPayAmount);
		JLabel newPayDateLabel = new JLabel("Payment Date: ");
		newPayDateLabel.setLabelFor(newPayDate);
		JLabel newPayNoteLabel = new JLabel("Payment Note: ");
		newPayNoteLabel.setLabelFor(newPayNote);
		
		//add button to save
		//button will run a method to save all the inputs
		
		//Need a method for "saving" Payment? Validates and adds it to the JList model.
		//calls all the setter methods of the new payment from the fields in newPaymentGo() and adds it to JList
		
		class paymentSaveListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
				{
				try{
					newPayment.setName( newPayName.getText() );
					newPayment.setAmount( Double.parseDouble( newPayAmount.getText() ) );
					newPayment.setPayNote( newPayNote.getText() );
					newPayment.setDatePaid( newPayDate.getText() );
					model.addElement(newPayment); //this adds the NAME to the list, meaning something gets added.
					//now we need to add it to the JList of Payments. Name displays with a ListCellRenderer or toString
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					//SERIALIZATION OF DATA INTO SAVE FILE
					finally {
					new budgetProgram().exportData(payList);
					
					//close JFrame
					newPayFrame.setVisible(false);
					newPayFrame.dispose();
					}
				}
		}
		JButton saveNewPayment = new JButton("Save Payment");
		saveNewPayment.addActionListener( new paymentSaveListener() );
		
		//build UI
		newPaymentPanel.add(newPayNameLabel);
		newPaymentPanel.add(newPayName);
		newPaymentPanel.add(newPayAmountLabel);
		newPaymentPanel.add(newPayAmount);
		newPaymentPanel.add(newPayDateLabel);
		newPaymentPanel.add(newPayDate);
		newPaymentPanel.add(newPayNoteLabel);
		newPaymentPanel.add(newPayNote);
		newPaymentPanel.add(Box.createRigidArea( new Dimension(95, 0) ) );
		newPaymentPanel.add(Box.createRigidArea( new Dimension(0, 25) ) );
		newPaymentPanel.add(saveNewPayment);
		newPayFrame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		newPayFrame.getContentPane().add(newPaymentPanel,gbc);
		
		newPayFrame.setSize(400,350);
		newPayFrame.setLocationRelativeTo(null);
		newPayFrame.setVisible(true);
	
	}//end newPaymentGo()
	
	
	/*
	What goes into Payments? 
	double Amount, payType Type, string datePaid, string dateAdded, string payNote, string payName
	*/
	public class Payment implements Serializable
	{
	
		private String payName;
		private double Amount;
		private payType Type;
		private String datePaid;
		private String dateAdded;
		private String payNote;

		private Payment()
		{

		}

		public void setName(String n)
		{
			//validation stuff here
			payName = n;
		}

		public void setAmount(double amt)
		{
			Amount = amt;
		}

		public void setType(){
			
		}

		public void setDatePaid(String date)
		{
			//date validation here
			datePaid = date;
		}
		
		public void setDateAdded(String date)
		{
			//date validation here
			dateAdded = date;
		}

		public void setPayNote(String note)
		{
			//some sort of validation here
			payNote = note;
		}

		public String getName()
		{
			return payName;
		}

		public double getAmount()
		{
			return Amount;
		}

		public void getType()
		{
			
		}

		public String getDatePaid()
		{
			return datePaid;
		}

		public String getDateAdded()
		{
			return dateAdded;
		}

		public String getPayNote()
		{
			return payNote;
		}
	
		public String toString()
		{
			return payName + " - " + NumberFormat.getCurrencyInstance().format(Amount) + " - " + datePaid;
		}
	
	//a valid Payment REQUIRES an amount and a name.
	}
	
	public class payType
	{
	
		private String typeName;
		private boolean isEnabled;
		//private int numPayments;
		
		public payType(String s)
		{
			typeName = s;
		}
	
	}
	
	//Need a method for saving payType
	public void payTypeSave(JList payList){

	}
	
	//Need a method for export
	public void exportData(JList payList)
	{
			//write with outputstream
		try {
			FileOutputStream f_out = new FileOutputStream("budgetYourselfData.data");
			ObjectOutputStream o_out = new ObjectOutputStream(f_out);
			o_out.writeObject(payList);
			o_out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//remember that filestream requires a try/catch
	//create an arraylist of all the payTypes and serialize that with the saved payments
}