package Classes;

public class ProcessContent {

	private String code;
	private String content;
	private String amount;
	
	public ProcessContent()
	{}

	public ProcessContent(String code, String content, String amount) {
		super();
		this.code = code;
		this.content = content;
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
