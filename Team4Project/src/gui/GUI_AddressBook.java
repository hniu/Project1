package gui;
import info.AddressBook;
import info.Record;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.ui.internal.dialogs.ViewComparator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;


public class GUI_AddressBook {

	protected Shell shellAB;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private File file;
	private AddressBook ab;
	private TreeSet<Record> adds;
	private Record selected;
	private HashMap<Integer, Record> map;
	private Table table;
	private TableColumn colLname;
	private TableColumn colZip;
	
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
			ab.setFile2Save(new File(fd.getFilterPath()+"\\"+fd.getFileName()));
			ab.saveFunction();
		}
	}
	/**
	 * This method used to import or open files into the list on the interface
	 * @param i, int 0 for import and 1 for open a new file
	 */
	private void helpLoad(int selection)
	{
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
				shellAB.setText("Address Book App"+"("+fd.getFileName()+")");

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
		if(adds != null){
			table.removeAll();
			Iterator<Record> iter = adds.iterator();
			int i = 1;
			//fill in all the table records
			while(iter.hasNext()){
				Record cur = iter.next();
				map.put(i, cur);
				TableItem item=new TableItem(table,SWT.NONE);
				item.setText(new String[] {i+"",cur.getLname(),cur.getFname(), cur.getAddress1(), cur.getAddress2(), cur.getCity(), cur.getState(), cur.getZip(), cur.getPhone(), cur.getEmail()});
				i++;
			}
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
				if(adds.add(newRecord)){
					ab.setModified();
				}
			}
		}else{
			//update the current record
			if(selected != null){
				recordWin = new GUI_Record(1, selected);
				recordWin.open();
				ab.setModified();
			}else{
				error();
			}
		}
		refTable();
	}
	/**
	 * Check save before quit the window
	 */
	private void saveBeforeExit(){
		if(ab.isEditted())
		{
            MessageBox messageBox = new MessageBox(shellAB,SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
            messageBox.setText("QUIT");
            messageBox.setMessage("The file has been changed. Do you want to save the file?");
            int rc = messageBox.open();
            if (rc == SWT.YES) 
            {
            	//if the virtual book is empty
            	if(!ab.isEmpty()){
            		if(ab.getFile2Save() == null){
            			saveAs();
            		}else{
            			ab.saveFunction();
            		}
            	}
            	ab.saveFunction();
            }
		}
	}
	/**
	 * error dialog
	 */
	private void error(){
		//set the message box to show if no record selected
		MessageBox mb = new MessageBox(shellAB, SWT.OK);
		if(selected == null){
			mb.setMessage("No Record Selected!");
		}
		mb.open();
	}
	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shellAB = new Shell();
		shellAB.addShellListener(new ShellAdapter() {
			@Override
			//If will show the a message box for saving edited information.
			public void shellClosed(ShellEvent e) 
			{
				saveBeforeExit();
			}
		});
		shellAB.setSize(980, 381);
		shellAB.setText("MotiBook");
		shellAB.setLayout(null);
		//initial the address book, each window has one addressbook
		ab = new AddressBook();
		//create the map with line and record
		adds=ab.getRecords();
		map = new HashMap<Integer,Record>();
		Menu menu = new Menu(shellAB, SWT.BAR);
		shellAB.setMenuBar(menu);
		
		MenuItem mntmMenu = new MenuItem(menu, SWT.CASCADE);
		mntmMenu.setText("File");
		
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
				if(ab.getFile2Save() == null){
					saveAs();
				}else{
					ab.saveFunction();
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
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveBeforeExit();
				shellAB.dispose();
			}
		});
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
		btnAdd.setText("Add");
		
		Button btnUpdate = new Button(shellAB, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addOrUpdateRecord(1);
			}
		});
		btnUpdate.setText("Update");
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
				if(table.getSelectionIndex() != -1){
					ab.delRecord(map.get(table.getSelectionIndex() + 1));
					refTable();
					ab.setModified();
				}else{
					error();
				}
			}
		});
		btnDeleteOne.setText("Delete");
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
		btnMailingFormat.setBounds(507, 275, 113, 25);
		formToolkit.adapt(btnMailingFormat, true, true);
		
		table = new Table(shellAB, SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				selected = map.get(table.getSelectionIndex() + 1);
			}
		});
		table.setBounds(10, 10, 942, 259);
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		//define the table columns
		TableColumn column0=new TableColumn(table,SWT.NONE);
		column0.setWidth(41);
		column0.setText("No.");
		
		colLname=new TableColumn(table,SWT.NONE);
		colLname.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		colLname.setWidth(100);
		colLname.setText("Last Name");
		table.setSortColumn(colLname);
		
		TableColumn column1=new TableColumn(table,SWT.NONE);
		column1.setWidth(100);
		column1.setText("First Name");
		
		TableColumn column21=new TableColumn(table,SWT.NONE);
		column21.setWidth(100);
		column21.setText("Address 1");
		
		TableColumn column22=new TableColumn(table,SWT.NONE);
		column22.setWidth(100);
		column22.setText("Address 2");
		
		TableColumn column3=new TableColumn(table,SWT.NONE);
		column3.setWidth(93);
		column3.setText("City");
		
		TableColumn column4=new TableColumn(table,SWT.NONE);
		column4.setWidth(46);
		column4.setText("State");
		
		colZip=new TableColumn(table,SWT.NONE);
		colZip.setWidth(73);
		colZip.setText("ZIP Code");
		
		TableColumn column6=new TableColumn(table,SWT.NONE);
		column6.setWidth(118);
		column6.setText("Phone Number");
		
		TableColumn column7=new TableColumn(table,SWT.NONE);
		column7.setWidth(168);
		column7.setText("Email Address");
		
	}
}
