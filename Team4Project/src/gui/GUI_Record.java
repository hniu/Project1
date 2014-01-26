package gui;

import info.Record;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;


public class GUI_Record {

	protected Shell shlNew;
	private Record inRecord;
	private boolean committed;
	//window style, 0 for insert and 1 for update
	private int style;
	
	/*
	 * Regex for input validation
	 * 1. upper-case character + lower-case characters
	 * 2. ****@***.***
	 * 3. ***-***-****
	 * 4. (*****)
	 */
	
	private String namePatn = "[A-Z][a-zA-Z]*";
	private String emailPatn = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private String phonePatn = "\\d{3}-\\d{3}-\\d{4}";
	private String zipPatn="\\d{5}";
	private Text fname;
	private Text lname;
	private Text address1;
	private Text address2;
	private Text city;
	private Text zip;
	private Text phone;
	private Text email;
	private Combo state;

	
	/**
	 * Constructor with the new record passed in
	 * @param Op int, 0 for add new record, 1 for update the old
	 * @param newRecord Record, the record come from the addressbook window
	 */
	public GUI_Record(int op, Record newRecord) {
		this.inRecord = newRecord;
		this.style = op;
	}
	public boolean isCommited(){
		return committed;
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlNew.open();
		shlNew.layout();
		while (!shlNew.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	/**
	 * Check if all information are in right format
	 * @return
	 */
	public boolean validate()
	{
		boolean flag = false;
		try
		{
		Pattern fnameP=Pattern.compile(namePatn);
		Pattern lnameP=Pattern.compile(namePatn);
		Pattern emailP=Pattern.compile(emailPatn);
		Pattern phoneP=Pattern.compile(phonePatn);
		Pattern phoneZ=Pattern.compile(zipPatn);
		Matcher checkFN=fnameP.matcher(this.fname.getText());
		Matcher checkLN=lnameP.matcher(this.lname.getText());
		Matcher checkEM=emailP.matcher(this.email.getText());
		Matcher checkPH=phoneP.matcher(this.phone.getText());
		Matcher checkZI=phoneZ.matcher(this.zip.getText());


		if(this.city.getText() != "" &&
				this.email.getText() != "" &&
						checkEM.matches() &&
				this.fname.getText() != "" &&
						checkFN.matches() &&
				this.lname.getText() != "" &&
						checkLN.matches() &&
				this.phone.getText() != "" &&
						checkPH.matches() &&
				this.state.getItem(state.getSelectionIndex()) != "" &&
				this.address1.getText() != "" &&
				this.address2.getText() != "" &&
				this.zip.getText() != "" &&
						checkZI.matches()
				){
			 flag = true;
		}
		} catch(Exception e)
		{
			flag =false;
		}
		System.out.println(flag);
		return flag;
	}
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		shlNew = new Shell();
		shlNew.setSize(514, 369);
		shlNew.setText("New Record / Current Record");
		
		CLabel label = new CLabel(shlNew, SWT.NONE);
		label.setText("First Name");
		label.setBounds(10, 10, 82, 21);
		
		CLabel lblStreet = new CLabel(shlNew, SWT.NONE);
		lblStreet.setText("Address 1");
		lblStreet.setBounds(10, 51, 82, 21);
		
		CLabel label_2 = new CLabel(shlNew, SWT.NONE);
		label_2.setText("State");
		label_2.setBounds(250, 145, 43, 21);
		
		CLabel label_3 = new CLabel(shlNew, SWT.NONE);
		label_3.setText("Zip");
		label_3.setBounds(10, 191, 44, 21);
		
		CLabel label_4 = new CLabel(shlNew, SWT.NONE);
		label_4.setText("Email");
		label_4.setBounds(17, 243, 63, 21);
		
		CLabel lblPhone = new CLabel(shlNew, SWT.NONE);
		lblPhone.setText("Phone  (+1)");
		lblPhone.setBounds(250, 191, 88, 21);
		
		CLabel label_6 = new CLabel(shlNew, SWT.NONE);
		label_6.setText("Last Name");
		label_6.setBounds(250, 10, 76, 21);
		
		CLabel lblCity = new CLabel(shlNew, SWT.NONE);
		lblCity.setText("City");
		lblCity.setBounds(10, 145, 43, 21);
		
		committed = false;
		/*
		 *set the preload data for update operation 
		 */
		if(style == 1){
			this.city.setText(inRecord.getCity());
			this.email.setText(inRecord.getEmail());
			this.fname.setText(inRecord.getFname());
			this.lname.setText(inRecord.getLname());
			this.phone.setText(inRecord.getPhone());
			this.state.setText(inRecord.getState());
			this.address1.setText(inRecord.getAddress1());
			this.address2.setText(inRecord.getAddress2());
			this.zip.setText(inRecord.getZip());
		}
		Button btnAdd = new Button(shlNew, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//if all information are input rightly
				if(validate()){
					inRecord.setCity(city.getText());
					inRecord.setEmail(email.getText());
					inRecord.setFname(fname.getText());
					inRecord.setLname(lname.getText());
					inRecord.setPhone(phone.getText());
					inRecord.setState(state.getText());
					inRecord.setAddress1(address1.getText());
					inRecord.setAddress2(address2.getText());
					inRecord.setZip(zip.getText());
				}
				else
				{
					
				}
				committed = true;
				shlNew.dispose();
			}
		});
		btnAdd.setBounds(90, 275, 75, 25);
		btnAdd.setText("Add");
		
		Button btnCancel = new Button(shlNew, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				committed = false;
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(351, 275, 75, 25);
		
		Label lblAddress = new Label(shlNew, SWT.NONE);
		lblAddress.setBounds(10, 101, 70, 20);
		lblAddress.setText("Address 2");
		
		fname = new Text(shlNew, SWT.BORDER);
		fname.setBounds(98, 10, 126, 26);
		
		lname = new Text(shlNew, SWT.BORDER);
		lname.setBounds(332, 5, 113, 26);
		
		address1 = new Text(shlNew, SWT.BORDER);
		address1.setBounds(98, 51, 347, 26);
		
		address2 = new Text(shlNew, SWT.BORDER);
		address2.setBounds(98, 98, 347, 26);
		
		city = new Text(shlNew, SWT.BORDER);
		city.setBounds(98, 143, 126, 26);
		
		
		state = new Combo(shlNew, SWT.READ_ONLY);
		state.setItems(new String[] {"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", 
				"FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", 
				"ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NM", "NV", 
				"NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"});
		state.setBounds(339, 143, 106, 28);
		state.select(0);

		
		zip = new Text(shlNew, SWT.BORDER);
		zip.setBounds(98, 191, 126, 26);
		
		phone = new Text(shlNew, SWT.BORDER);
		phone.setBounds(339, 186, 106, 26);
		
		email = new Text(shlNew, SWT.BORDER);
		email.setBounds(98, 238, 347, 26);
		
		Label label_1 = new Label(shlNew, SWT.NONE);
		label_1.setBounds(356, 212, 70, 20);
		label_1.setText("***-***-****");
	}
}
