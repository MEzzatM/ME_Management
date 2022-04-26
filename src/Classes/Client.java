package Classes;

public class Client {

	private String name;
	private String address;
	private String mobile;
	public Client()
	{
		
	}
	
	
	public Client(String name, String address, String mobile) {
		super();
		this.name = name;
		this.address = address;
		this.mobile = mobile;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
