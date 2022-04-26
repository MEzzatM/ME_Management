package Classes;

public class Employee {

	private String code;
	private String name;
	private String position;
	private double hourValue;
	private String mobile;
	
	public String getCode() {
		return code;
	}

	
	
	public Employee(String code, String name, String position, double hourValue, String mobile) {
		super();
		this.code = code;
		this.name = name;
		this.position = position;
		this.hourValue = hourValue;
		this.mobile = mobile;
	}



	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getHourValue() {
		return hourValue;
	}

	public void setHourValue(double hourValue) {
		this.hourValue = hourValue;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	
	public Employee()
	{
		
	}

	
	
	
	
}
