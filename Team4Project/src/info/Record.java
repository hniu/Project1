package info;

/**
 * The record defines the basic information about each records in the address book
 * @author Team 4
 */
public class Record implements Comparable<Record>{
	//basic attributes for each records
	private String fname;
	private String lname;
	private String zip;
	private String city;
	private String state;
	private String phone;
	private String email;
	private String street;
	//constructor for the record
	Record(String fname, String lname, String zip, String city, String state, String phone, String email, String street){
		this.setCity(city);
		this.setEmail(email);
		this.setFname(fname);
		this.setLname(lname);
		this.setPhone(phone);
		this.setState(state);
		this.setZip(zip);
		this.setStreet(street);
	}
	//constructor with empty parameter
	public Record() {
		// TODO Auto-generated constructor stub
	}
	//get the fname
	public String getFname() {
		return fname;
	}
	//set the fname
	public void setFname(String fname) {
		this.fname = fname;
	}
	//get the lname
	public String getLname() {
		return lname;
	}
	//set the lname
	public void setLname(String lname) {
		this.lname = lname;
	}
	//get the zip
	public String getZip() {
		return zip;
	}
	//set the zip
	public void setZip(String zip) {
		this.zip = zip;
	}
	//get the city
	public String getCity() {
		return city;
	}
	//set the city
	public void setCity(String city) {
		this.city = city;
	}
	//get the state
	public String getState() {
		return state;
	}
	//set the state
	public void setState(String state) {
		this.state = state;
	}
	//get the phone
	public String getPhone() {
		return phone;
	}
	//set the phone
	public void setPhone(String phone) {
		this.phone = phone;
	}
	//get the email
	public String getEmail() {
		return email;
	}
	//set the email
	public void setEmail(String email) {
		this.email = email;
	}
	//get the street
	public String getStreet(){
		return street;
	}
	//set the street
	public void setStreet(String street){
		this.street = street;
	}
	/**
	 * Print the mail information
	 * @return the string of the mail format
	 */
	public String printMailFormat(){
		String strMail = "Standard US Mail Address:\n" 
				+ fname + " " + lname + "\n"
				+ street + "\n"
				+ city + "," + state + "," + zip + " | " + phone
				+ "\nEmail: " + email;
		return strMail;
	}
	/**
	 * Implements the comparable interface and set the criteria for define same record
	 */
	@Override
	public int compareTo(Record o) {
		int flag = 0;
		//define same records with same everything
		if(o.fname.compareToIgnoreCase(this.fname)==0 &&
				o.lname.compareToIgnoreCase(this.lname)==0 && 
				o.zip.compareToIgnoreCase(this.zip) == 0 &&
				o.phone.compareToIgnoreCase(this.phone) == 0 &&
				o.state.compareToIgnoreCase(this.state) == 0 &&
				o.email.compareToIgnoreCase(this.email) == 0 &&
				o.street.compareToIgnoreCase(this.street) == 0 &&
				o.city.compareToIgnoreCase(this.city) == 0){
			flag = 0;
		}else{
			//if they are not same
			//the order is by last name, then zip code and then Dont care.
			flag = this.lname.compareToIgnoreCase(o.lname);
			if(flag == 0){
				flag = this.zip.compareToIgnoreCase(o.zip);
				if(flag == 0){
					flag = -1;
				}
			}
		}
		return flag;
	}
}
