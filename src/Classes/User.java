package Classes;

public class User {

	private String code;
	private String name;
	private String pass;
	
	public User()
	{
		
	}
	
	
	
	
	public User(String code, String name, String pass) {
		super();
		this.code = code;
		this.name = name;
		this.pass = pass;
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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}






		
}
