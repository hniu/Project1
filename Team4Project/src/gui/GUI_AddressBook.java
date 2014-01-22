package gui;
import info.AddressBook;
import info.Record;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;


public class GUI_AddressBook {

	protected Shell shellAB;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private File file;
	private AddressBook ab;
	private TreeSet<Record> adds;
	private Record selected;
	private HashMap<Integer, Record> map;
	private Table table;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI_AddressBook window = new GUI_AddressBook();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shellAB.open();
		shellAB.layout();
		while (!shellAB.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Save as function using as save as and export
	 */
	private void saveAs(){
		ab.setModified();
		FileDialog fd = new FileDialog(shellAB, SWT.SAVE);
		//file extension 
		String[] ext = {"*.txt"};
		fd.setFilterExtensions(ext);
		fd.open();
		if(!fd.getFileName().equals(""))
		{
			try {
				System.out.println(fd.getFilterPath()+"\\"+fd.getFileName());
				ab.setFile2Save(new File(fd.getFilterPath()+"\\"+fd.getFileName()));
				ab.saveFunction();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/**
	 * This method used to import or open files into the list on the interface
	 * @param i, int 0 for import and 1 for open a new file
	 */
	private void helpLoad(int selection){
		//create an open-file dialog
		FileDialog fd = new FileDialog(shellAB, 0);
		//file extension 
		String[] ext = {"*.txt"};
		fd.setFilterExtensions(ext);
		fd.open();
		//gets the file
		if(!fd.getFileName().equals("")){
			String fileName = fd.getFilterPath().toString()+"\\"+fd.getFileName().toString();
			//each time load a new add, remove all from the list
			table.removeAll();
			if(selection == 1){
				if(adds != null){
					adds.removeAll(adds);
				}
			}
			//load the file
			file = new File(fileName);
			//load file into address book
			ab.loadF(file);
			//define which file is to save
			if(selection == 1){
				//set the open one as saving file
				ab.setFile2Save(file);
			}else{//import
				if(ab.getFile2Save()==null){
					ab.setFile2Save(file);
				}
			}
			//get records in the adds
			adds = ab.getRecords();
			refTable();
		}
	}
	
	/**
	 * Refresh the table content
	 */
	private void refTable(){
		table.removeAll();
		Iterator<Record> iter = adds.iterator();
		int i = 1;
		//fill in all the table records
		while(iter.hasNext()){
			Record cur = iter.next();
			map.put(i, cur);
			TableItem item=new TableItem(table,SWT.NONE);
			item.setText(new String[] {i+"",cur.getLname(),cur.getFname(), cur.getStreet(), cur.getCity(), cur.getState(), cur.getZip(), cur.getPhone(), cur.getEmail()});
			i++;
		}
	}
	/**
	 * Add or update the new record
	 * @param selection int, 0 for insert new records and 1 for update old record
	 */
	private void addOrUpdateRecord(int selection){
		//insert new record
		GUI_Record recordWin = null;
		if(selection == 0){
			Record newRecord = new Record();
			recordWin = new GUI_Record(0, newRecord);
			recordWin.open();
			//when the add win close, then add records
			if(recordWin.isCommited()){
				adds.add(newRecord);
			}
		}else{
			//update the current record
			
			if(selected != null){
				recordWin = new GUI_Record(1, selected);
				recordWin.open();
			}
		}
		refTable();
	}
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shellAB = new Shell();
		shellAB.setSize(839, 366);
		shellAB.setText("Address Book App");
		shellAB.setLayout(null);
		//initial the address book, each window has one addressbook
		ab = new AddressBook();
		//create the map with line and record
		map = new HashMap<Integer,Record>();
		Menu menu = new Menu(shellAB, SWT.BAR);
		shellAB.setMenuBar(menu);
		
		MenuItem mntmMenu = new MenuItem(menu, SWT.CASCADE);
		mntmMenu.setText("Menu");
		
		Menu menu_1 = new Menu(mntmMenu);
		mntmMenu.setMenu(menu_1);
		
		MenuItem mntmNew = new MenuItem(menu_1, SWT.NONE);
		/**
		 * Click new in menu and the new a new window created
		 */
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//create a new instance of main window
				GUI_AddressBook newWin = new GUI_AddressBook();
				newWin.open();
			}
		});
		mntmNew.setText("New");
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);

				
		/**
		 * Click the open in the menu event
		 */
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//load into the interface by open method
				helpLoad(1);
			}
		});
		mntmOpen.setText("Open...");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ab.saveFunction();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmSave.setText("Save");
		
		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				saveAs();
			}
		});
		mntmSaveAs.setText("Save As...");
		
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.setText("Exit");
		
		Button btnAdd = new Button(shellAB, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addOrUpdateRecord(0);
			}
		});
		btnAdd.setBounds(22, 275, 75, 25);
		formToolkit.adapt(btnAdd, true, true);
		btnAdd.setText("Add One");
		
		Button btnUpdate = new Button(shellAB, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addOrUpdateRecord(1);
			}
		});
		btnUpdate.setText("Update One");
		btnUpdate.setBounds(216, 275, 75, 25);
		formToolkit.adapt(btnUpdate, true, true);
		
		Button btnExport = new Button(shellAB, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveAs();
			}
		});
		btnExport.setText("Export");
		btnExport.setBounds(410, 275, 75, 25);
		formToolkit.adapt(btnExport, true, true);
		
		Button btnImport = new Button(shellAB, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			//import file do not remove the old in the list
			@Override
			public void widgetSelected(SelectionEvent e) {
				//open file by do not import behavior which does not delete old records
				helpLoad(0);
				//any import operation will modify the file
				ab.setModified();
			}
		});
		btnImport.setText("Import");
		btnImport.setBounds(313, 275, 75, 25);
		formToolkit.adapt(btnImport, true, true);
		
		Button btnDeleteOne = new Button(shellAB, SWT.NONE);
		btnDeleteOne.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ab.delRecord(map.get(table.getSelectionIndex() + 1));
				refTable();
				ab.setModified();
			}
		});
		btnDeleteOne.setText("Delete One");
		btnDeleteOne.setBounds(119, 275, 75, 25);
		formToolkit.adapt(btnDeleteOne, true, true);
		
		Button btnMailingFormat = new Button(shellAB, SWT.NONE);
		btnMailingFormat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//set the message box to show the mailling format
				MessageBox mb = new MessageBox(shellAB, SWT.OK);
				if(selected == null){
					mb.setMessage("No Record Selected!");
				}else{
					mb.setMessage(selected.printMailFormat());
				}
				mb.open();
			}
		});
		btnMailingFormat.setText("Mailing Format");
		btnMailingFormat.setBounds(507, 275, 94, 25);
		formToolkit.adapt(btnMailingFormat, true, true);
		
		table = new Table(shellAB, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				selected = map.get(table.getSelectionIndex() + 1);
			}
		});
		table.setBounds(10, 10, 803, 259);
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		//define the table columns
		TableColumn column0=new TableColumn(table,SWT.NONE);
		column0.setWidth(34);
		column0.setText("No.");
		
		TableColumn column=new TableColumn(table,SWT.NONE);
		column.setWidth(100);
		column.setText("Last Name");
		
		TableColumn column1=new TableColumn(table,SWT.NONE);
		column1.setWidth(100);
		column1.setText("First Name");
		
		TableColumn column2=new TableColumn(table,SWT.NONE);
		column2.setWidth(100);
		column2.setText("Stree");
		
		TableColumn column3=new TableColumn(table,SWT.NONE);
		column3.setWidth(100);
		column3.setText("City");
		
		TableColumn column4=new TableColumn(table,SWT.NONE);
		column4.setWidth(46);
		column4.setText("State");
		
		TableColumn column5=new TableColumn(table,SWT.NONE);
		column5.setWidth(65);
		column5.setText("ZIP Code");
		
		TableColumn column6=new TableColumn(table,SWT.NONE);
		column6.setWidth(100);
		column6.setText("Phone Number");
		
		TableColumn column7=new TableColumn(table,SWT.NONE);
		column7.setWidth(154);
		column7.setText("Email Address");
		
	}
}
