package gui;

import info.Record;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GUI_Record {

	protected Shell shlNew;
	private Text fname;
	private Text street;
	private Text state;
	private Text zip;
	private Text email;
	private Text phone;
	private Text lname;
	private Record inRecord;
	private Text city;
	private boolean committed;
	//window style, 0 for insert and 1 for update
	private int style;
	
	/*
	 * Regex for input validation
	 * 1. name has no number
	 * 2.
	 */
	private String namePatn = "[a-zA-Z]+";
	private String emalPatn = "";
	private String phonePatn = "";
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
	public boolean validate(){
		boolean flag = false;
		if(this.city.getText() != "" &&
				this.email.getText() != "" &&
				this.fname.getText() != "" &&
				this.lname.getText() != "" &&
				this.phone.getText() != "" &&
				this.state.getText() != "" &&
				this.street.getText() != "" &&
				this.zip.getText() != ""){
			flag = true;
		}
		return flag;
	}
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlNew = new Shell();
		shlNew.setSize(576, 265);
		shlNew.setText("New Record / Current Record");
		
		CLabel label = new CLabel(shlNew, SWT.NONE);
		label.setText("First Name");
		label.setBounds(10, 10, 63, 21);
		
		fname = new Text(shlNew, SWT.BORDER);
		fname.setBounds(80, 10, 117, 21);
		
		CLabel lblStreet = new CLabel(shlNew, SWT.NONE);
		lblStreet.setText("Street");
		lblStreet.setBounds(273, 10, 43, 21);
		
		street = new Text(shlNew, SWT.BORDER);
		street.setBounds(315, 10, 221, 21);
		
		CLabel label_2 = new CLabel(shlNew, SWT.NONE);
		label_2.setText("State");
		label_2.setBounds(273, 67, 43, 21);
		
		state = new Text(shlNew, SWT.BORDER);
		state.setBounds(315, 67, 83, 21);
		
		CLabel label_3 = new CLabel(shlNew, SWT.NONE);
		label_3.setText("Zip");
		label_3.setBounds(272, 181, 44, 21);
		
		zip = new Text(shlNew, SWT.BORDER);
		zip.setBounds(315, 181, 83, 21);
		
		email = new Text(shlNew, SWT.BORDER);
		email.setBounds(80, 181, 187, 21);
		
		CLabel label_4 = new CLabel(shlNew, SWT.NONE);
		label_4.setText("Email");
		label_4.setBounds(10, 181, 63, 21);
		
		phone = new Text(shlNew, SWT.BORDER);
		phone.setBounds(80, 124, 117, 21);
		
		CLabel label_5 = new CLabel(shlNew, SWT.NONE);
		label_5.setText("Phone");
		label_5.setBounds(10, 124, 63, 21);
		
		CLabel label_6 = new CLabel(shlNew, SWT.NONE);
		label_6.setText("Last Name");
		label_6.setBounds(10, 67, 63, 21);
		
		lname = new Text(shlNew, SWT.BORDER);
		lname.setBounds(80, 67, 117, 21);
		
		CLabel lblCity = new CLabel(shlNew, SWT.NONE);
		lblCity.setText("City");
		lblCity.setBounds(273, 124, 43, 21);
		
		city = new Text(shlNew, SWT.BORDER);
		city.setBounds(315, 124, 83, 21);
		
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
			this.street.setText(inRecord.getStreet());
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
					inRecord.setStreet(street.getText());
					inRecord.setZip(zip.getText());
				}else{
					
				}
				committed = true;
				shlNew.dispose();
			}
		});
		btnAdd.setBounds(461, 68, 75, 25);
		btnAdd.setText("Add");
		
		Button btnCancel = new Button(shlNew, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				committed = false;
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(461, 123, 75, 25);
	}
}
