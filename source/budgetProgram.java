import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
//import javax.swing.JList;
//import javax.swing.JScrollPane;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.*;
//import java.io.*;

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

public class budgetProgram{
	
	private JFrame frame; //frame for the entire program
	JPanel mainPanel; //create main panel until new panels made
	private JButton addPayment;
	private JButton delPayment;
	
	public static void main(String[] args){
		new budgetProgram().go();

	}
	
	
	public void go(){

		frame = new JFrame("Budget Yourself");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		Font titleFont = new Font("sanserif",Font.BOLD,36);

		//insert contents
		DefaultListModel payListModel = new DefaultListModel();
		/*
		for( int i = 0; i < 3; i++){
			payListModel.addElement("Test " + (i+1));
		}*/
		
		//JList for Payments
		JList payList = new JList (payListModel);
		payList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ); //prevents multiple selection
		
		
		//crate box layout manager containing scroller and flowlayout with buttons 
		Box leftBox = new Box(BoxLayout.Y_AXIS);
		
		
		//payScroller creation
		JScrollPane payScroller = new JScrollPane(payList);
		payScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		payScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		FlowLayout payControls = new FlowLayout();
		JPanel payControlsPanel = new JPanel(payControls);
		
		//create actionListeners for buttons
		class AddNewPayment implements ActionListener{
		public void actionPerformed(ActionEvent event){
					new budgetProgram().newPaymentGo(payListModel);
					//payListModel.addElement("Test " + (payListModel.size()+1) );
			}
		}
		
		class RemovePayment implements ActionListener{
			public void actionPerformed(ActionEvent event){
				//may want to add confirmation message? 
				//single selection is forced.
				System.out.println("Index: " + payList.getSelectedIndex() );
				System.out.println(payList.isSelectionEmpty());
				if( !payList.isSelectionEmpty() )
				{
					payListModel.remove( payList.getSelectedIndex() );
					//NEEDS TO IMPLEMENT SAVE DATA METHOD
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
		
		graphPanel.setMaximumSize(new Dimension(650,500) );
		graphPanel.setPreferredSize(new Dimension(650,500) );
		graphPanel.setMinimumSize(new Dimension(650,500) );
		
		statsPanel.setBorder(statsBorder);
		graphPanel.setBorder(graphBorder);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		
		mainPanel.add(graphPanel, gbc);
		gbc.gridy = 1;
		mainPanel.add(statsPanel, gbc);

		//create right comboBox and checkboxes
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setMinimumSize(new Dimension(600,600));
		
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
			scrollablePanel.setLayout( new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
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
		 
	}
	
	public void newPaymentGo(DefaultListModel model){
		
		//make a new panel and populate it with a new instance of a Payment. We want to return that payment.
		JFrame newPayFrame = new JFrame("New Payment");
		Payment newPayment = new Payment();
		JPanel newPaymentPanel = new JPanel();
		newPaymentPanel.setLayout( new BoxLayout(newPaymentPanel,BoxLayout.Y_AXIS));
		
		//add fields to take new Payment input
		
		JTextField newPayName = new JTextField();
		JTextField newPayAmount = new JTextField();
		//JTextField newPayType = new JTextField();
		JTextField newPayDate = new JTextField();
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
					newPayment.setName( newPayName.getText() );
					newPayment.setAmount( Double.parseDouble( newPayAmount.getText() ) );
					newPayment.setPayNote( newPayNote.getText() );
					newPayment.setDatePaid( newPayDate.getText() );
					model.addElement(newPayment); //this adds the NAME to the list, meaning something gets added.
					//now we need to add it to the JList of Payments. Name displays with a ListCellRenderer or toString
					
					//NEEDS TO IMPLEMENT METHOD - SERIALIZATION OF DATA INTO SAVE FILE
					
					//close JFrame
					newPayFrame.setVisible(false);
					newPayFrame.dispose();
					
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
		
		//newPayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newPayFrame.setSize(400,350);
		newPayFrame.setLocationRelativeTo(null);
		newPayFrame.setVisible(true);
	
	}//end newPaymentGo()
	
	
	/*
	What goes into Payments? 
	double Amount, payType Type, string datePaid, string dateAdded, string payNote, string payName
	*/
	public class Payment {
	
		private String payName;
		private double Amount;
		private payType Type;
		private String datePaid;
		private String dateAdded;
		private String payNote;

		private Payment(){
			String dateAdded = new SimpleDateFormat("MM/dd/yyyy").format( new java.util.Date() );
			String datePaid = dateAdded;
		}

		public void setName(String n){
			//validation stuff here
			payName = n;
		}

		public void setAmount(double amt){
			Amount = amt;
		}

		public void setType(){
			
		}

		public void setDatePaid(String date){
			//date validation here
			datePaid = date;
		}

		public void setPayNote(String note){
			//some sort of validation here
			payNote = note;
		}

		public String getName(){
			return payName;
		}

		public double getAmount(){
			return Amount;
		}

		public void getType(){
			
		}

		public String getDatePaid(){
			return datePaid;
		}

		public String getDateAdded(){
			return dateAdded;
		}

		public String getPayNote(){
			return payNote;
		}
	
		public String toString(){
			return payName + " - " + NumberFormat.getCurrencyInstance().format(Amount) + " - " + datePaid;
		}
	
	//a valid Payment REQUIRES an amount and a name.
	}
	
	public class payType{
	
		private String typeName;
		private boolean isEnabled;
		//private int numPayments;
		
		public payType(String s){
			typeName = s;
		}
	
	}
	
	public class totalStats{
	
	//contains avg, max, min floats.
	//contains a method for refreshing calculation of those values with inputs.
	//not sure what the inputs are yet. A list of Payments?
	}
	/*
	//Need a method for saving payType
	public void payTypeSave(){
		
	}
	
	//Need a method for import
	public void importData(){
	
	}
	
	//Need a method for export
	public void exportData(){
	
	}
	//remember that filestream requires a try/catch
	//create an arraylist of all the payTypes and serialize that with the saved payments
*/
}