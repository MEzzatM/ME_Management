package Classes;

public class Salary {

	private String code;
	private String name;
	private double workedHours;
	private double hourValue;
	private double total;
	
	public Salary()
	{}

	
	
	public Salary(String code, String name, double workedHours, double hourValue) {
		super();
		this.code = code;
		this.name = name;
		this.workedHours = workedHours;
		this.hourValue = hourValue;
		this.total = workedHours*hourValue;
	}



	public String getCode() {
		return code;
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

	public double getWorkedHours() {
		return workedHours;
	}

	public void setWorkedHours(double workedHours) {
		this.workedHours = workedHours;
		this.total = workedHours*this.hourValue;
	}

	public double getHourValue() {
		return hourValue;
	}

	public void setHourValue(double hourValue) {
		this.hourValue = hourValue;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	
}
