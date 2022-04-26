package Classes;

public class Process {
	
	private String processCode;
	private double total;
	private String time;
	private String type;
	private String delivery;
	
	public Process()
	{
		
	}

	public Process(String processCode, double total, String time,String type, String delivery) {
		super();
		this.processCode = processCode;
		this.total = total;
		this.time = time;
		this.type=type;
		this.delivery = delivery;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	
}
