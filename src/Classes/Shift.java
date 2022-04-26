package Classes;

public class Shift {

	private String shiftCode;
	private String userCode;
	private String username;
	private double totalIncome;
	private double totalProfit;
	private String start;
	private String end;
	
	public Shift()
	{
		
	}
	
	

	public Shift(String shiftCode, String userCode, String username, double totalIncome, double totalProfit,
			String start, String end) {
		super();
		this.shiftCode = shiftCode;
		this.userCode = userCode;
		this.username = username;
		this.totalIncome = totalIncome;
		this.totalProfit = totalProfit;
		this.start = start;
		this.end = end;
	}



	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	

	
}
