package Classes;

public class Attendance {

	private String day;
	private String attendance;
	private String departure;
	private double hours;
	
	public Attendance()
	{
		
	}

	
	public Attendance(String day, String attendance, String departure, double hours) {
		super();
		this.day = day;
		this.attendance = attendance;
		this.departure = departure;
		this.hours = hours;
	}



	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}
	
	
}
