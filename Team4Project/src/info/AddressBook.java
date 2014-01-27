package info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * The address book keep all records and provide functions including
 * sorting, searching, add, delete, update and so on.
 * @author Team 4
 *
 */
public class AddressBook{
	//file on the disk
	private File f;
	//file to save
	private File file2save;
	//is file modified
	private boolean isModified;
	//all records in the system
	private TreeSet<Record> records;
	
	/**
	 * create an instance for the physical address book on the disk
	 * @param fileName, String the addressbook name on the disk
	 */
	public AddressBook(File file){
		//create the set
		this.records = new TreeSet<Record>();
		this.setFile(file);
		//load file
		this.loadF(file);
	}
	
	/**
	 * Constructor for address book
	 */
	public AddressBook(){
		records = new TreeSet<Record>();
	}
	/**
	 * Load the file into records of the address book
	 * @param file, the txt file on the disk
	 */
	public void loadF(File file) {
		if(file!=null){
			this.setFile(file);
			try {
				//read the file into records set
				Scanner inputFile = new Scanner(file);
				while(inputFile.hasNext())
				{
					//Hanqing	Zhao	5419132015	OR	97401	Eugene	hanqing@uoregon.edu
					String[] reader=inputFile.nextLine().split("\t");
					String fname = reader[0];
					String lname = reader[1];
					String phone = reader[2];
					String state = reader[3];
					String zip = reader[4];
					String city = reader[5];
					String email = reader[6];
					String address1 = reader[7];
					String address2 = reader[8];
					Record r = new Record(fname,lname,zip, city,state, phone, email, address1, address2);					this.addRecord(r);
					this.isModified = false;
				}
				inputFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * check if the records exist in the system
	 * @param Record r
	 * @return true if r exists else false
	 */
	public boolean checkExist(Record r){
		return this.records.contains(r);
	}
	/**
	 * check if the address book is empty
	 * @return true for empty, else false
	 */
	public boolean isEmpty(){
		return this.records.size() == 0;
	}

	/**
	 * is the physical file edited
	 * @return true if the file modified
	 */
	public boolean isEditted(){
		return this.isModified;
	}
	/**
	 * Set the modified address book to true
	 */
	public void setModified(){
		this.isModified = true;
	}
	/**
	 * Add the record to the address book
	 * @param r, Record saves the person address
	 * @return true if saving into the set successfully
	 */
	public boolean addRecord(Record r){
		return records.add(r);
	}
	/**
	 * Remove a record from the address book
	 * @param r, the object need to remove from the address book
	 * @return true if remove successfully
	 */
	public boolean delRecord(Record r){
		return records.remove(r);
	}
	/**
	 * Get the records set for show on the front
	 * @return the whole records set
	 */
	public TreeSet<Record> getRecords(){
		return this.records;
	}
	/**
	 * Get the file loaded into the address book
	 * @return file, the file to open
	 */
	public File getFile() {
		return f;
	}
	/**
	 * Set the file
	 * @param f file, define the file descriptor
	 */
	public void setFile(File f) {
		this.f = f;
	}
	/**
	 * Get the file descriptor for saving
	 * @return file, the file target to save
	 */
	public File getFile2Save(){
		return this.file2save;
	}
	/**
	 * Set the saving desination file
	 * @param f file, the file used for saving
	 */
	public void setFile2Save(File f){
		this.file2save = f;
	}
	
	/**
	 * SaveFunction is to save or save the record as into a txt file.
	 */
	public void saveFunction()
	{
		if(this.isEditted())
		{
			String s=file2save.getPath();
			FileWriter pw;
			try {
				pw = new FileWriter(s,false);
				PrintWriter op=new PrintWriter(pw);
				Iterator<Record> iter = records.iterator();

				while(iter.hasNext())
				{
					Record cur = iter.next();
					op.println(cur.getFname()+"\t"+cur.getLname()+"\t"+cur.getPhone()+"\t"+cur.getState()+"\t"+cur.getZip()+"\t"+cur.getCity()+"\t"+cur.getEmail()+"\t"+cur.getAddress1()+"\t"+cur.getAddress2());				}
				op.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
