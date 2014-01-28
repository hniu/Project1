package gui;

import info.Record;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseEvent;


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
	private String phonePatn = "[0-9]{3}-[0-9]{3}-[0-9]{4}";
	private String zipPatn="[0-9]{5}";
	private Text fname;
	private Text lname;
	private Text address1;
	private Text address2;
	private Text city;
	private Text zip;
	private Text phone;
	private Text email;
	private Combo state;
	private Label error;
	private Pattern fnameP;
	private Pattern lnameP;
	private Pattern emailP;
	private Pattern phoneP;
	private Pattern zipP;
	private Matcher checkFN;
	private Matcher checkLN;
	private Matcher checkEM;
	private Matcher checkPH;
	private Matcher checkZI;
	private boolean valid;
	
	/**
	 * Constructor with the new record passed in
	 * @param Op int, 0 for add new record, 1 for update the old
	 * @param newRecord Record, the record come from the addressbook window
	 */
	public GUI_Record(int op, Record newRecord) 
	{
		this.inRecord = newRecord;
		this.style = op;
		this.valid = false;
	}
	/**
	 * check if the window is submitted
	 * @return true if add/update btn is clicked
	 */
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
		if(this.city.getText() != "" &&
				this.email.getText() != "" &&
				this.fname.getText() != "" &&
				this.lname.getText() != "" &&
				this.phone.getText() != "" &&
				this.state.getItem(state.getSelectionIndex()) != "" &&
				this.address1.getText() != "" &&
				this.address2.getText() != "" &&
				this.zip.getText() != ""
				){
				return true;
		}
		return false;
	}
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() 
	{
		fnameP=Pattern.compile(namePatn);
		lnameP=Pattern.compile(namePatn);
		emailP=Pattern.compile(emailPatn);
		phoneP=Pattern.compile(phonePatn);
		zipP=Pattern.compile(zipPatn);
		
		shlNew = new Shell();
		shlNew.setSize(514, 411);
		shlNew.setText("New Record / Current Record");
		
		CLabel label = new CLabel(shlNew, SWT.NONE);
		label.setText("First Name");
		label.setBounds(10, 10, 82, 21);
		
		CLabel lblStreet = new CLabel(shlNew, SWT.NONE);
		lblStreet.setText("Address 1");
		lblStreet.setBounds(10, 56, 82, 21);
		
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
		
		Button btnAdd = new Button(shlNew, SWT.NONE);
		btnAdd.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				MessageBox mb = new MessageBox(shlNew, SWT.OK);
				if(!validate()){
					mb.setMessage("No empty field(s)!");
					mb.open();
				}else{
					checkLN=lnameP.matcher(lname.getText());
					checkFN=fnameP.matcher(fname.getText());
					checkZI=zipP.matcher(zip.getText());
					checkPH=phoneP.matcher(phone.getText());
					checkEM=emailP.matcher(email.getText());
					if(!checkFN.matches())
					{
						mb.setMessage("* The first letter of the first name must be captalized!");
						mb.open();
					}else if(!checkLN.matches())
					{
						mb.setMessage("* The first letter of the first name must be captalized!");
						mb.open();
					}else if(!checkZI.matches())
					{
						mb.setMessage("* The ZIP code must be 5 digits");
						mb.open();
					}else if(!checkPH.matches())
					{
						mb.setMessage("* The phone number must follow the format: ***-***-****");
						mb.open();
					}
					else  if(!checkEM.matches())
					{
						mb.setMessage("* The email format is not correct!");
						mb.open();
					}else{
						error.setText("");
						valid=true;
					}
				}
			}
		});
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(valid){
				//if all information are input rightly
					inRecord.setCity(city.getText());
					inRecord.setEmail(email.getText());
					inRecord.setFname(fname.getText());
					inRecord.setLname(lname.getText());
					inRecord.setPhone(phone.getText());
					inRecord.setState(state.getText());
					inRecord.setAddress1(address1.getText());
					inRecord.setAddress2(address2.getText());
					inRecord.setZip(zip.getText());
					committed = true;
					shlNew.dispose();
				}
			}
		});
		btnAdd.setBounds(67, 275, 113, 25);
		btnAdd.setText("Add/Update");
		
		Button btnCancel = new Button(shlNew, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				committed = false;
				shlNew.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(301, 275, 113, 25);
		
		//error label
		error = new Label(shlNew, SWT.NONE);
		error.setAlignment(SWT.CENTER);
		error.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		error.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		error.setBounds(10, 318, 478, 21);
		
		Label lblAddress = new Label(shlNew, SWT.NONE);
		lblAddress.setBounds(10, 101, 70, 20);
		lblAddress.setText("Address 2");
		
		fname = new Text(shlNew, SWT.BORDER);
		fname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkFN=fnameP.matcher(fname.getText());
				if(!checkFN.matches())
				{
					error.setText("* The first letter of the first name must be captalized!");
				}else{
					error.setText("");
				}
			}
		});
	
		fname.setBounds(98, 10, 126, 26);
		lname = new Text(shlNew, SWT.BORDER);
		lname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkLN=lnameP.matcher(lname.getText());
				if(!checkLN.matches())
				{
					error.setText("* The first letter of the last name must be captalized!");
				}else{
					error.setText("");
				}
			}
		});
		lname.setBounds(332, 10, 113, 26);
		address1 = new Text(shlNew, SWT.BORDER);
		address1.setBounds(98, 56, 347, 26);
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
		zip.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkZI=zipP.matcher(zip.getText());
				if(!checkZI.matches())
				{
					error.setText("* The ZIP code must be 5 digits");
				}else{
					error.setText("");
				}
			}
		});
		zip.setBounds(98, 191, 126, 26);
		
		phone = new Text(shlNew, SWT.BORDER);
		phone.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkPH=phoneP.matcher(phone.getText());
				if(!checkPH.matches())
				{
					error.setText("* The phone number must follow the format: ***-***-****");
				}
				else
				{
					error.setText("");
				}
			}
		});
		phone.setBounds(339, 186, 106, 26);
		
		email = new Text(shlNew, SWT.BORDER);
		email.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				checkEM=emailP.matcher(email.getText());
				if(!checkEM.matches())
				{
					error.setText("* The email format is not correct!");
				}
				else
				{
					error.setText("");
				}
			}
		});
		email.setBounds(98, 238, 347, 26);
		city.setText("Eugene");
		lname.setText("Hanqing");
		fname.setText("Zhao");
		address1.setText("Kinsrow 3438");
		address2.setText("APT128");
		zip.setText("97401");
		email.setText("hanqing@uoregon.edu");
		phone.setText("541-913-2015");
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
		
		
	}
}
