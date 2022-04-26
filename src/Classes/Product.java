package Classes;

public class Product {

	private String code;
	private String name;
	private double purchPrice;    //price of purchasing
	private double price;			//selling price		
	private int amount;
	private int minAmount;
	
	
	
	public Product()
	{
		
	}
	
	
	
	public Product(String code, String name, double purchPrice, double price, int amount, int minAmount) {
		super();
		this.code = code;
		this.name = name;
		this.purchPrice = purchPrice;
		this.price = price;
		this.amount = amount;
		this.minAmount = minAmount;
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
	public double getPurchPrice() {
		return purchPrice;
	}
	public void setPurchPrice(double purchPrice) {
		this.purchPrice = purchPrice;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(int minAmount) {
		this.minAmount = minAmount;
	}
	@Override
	public String toString() {
		return "Product [code=" + code + ", name=" + name + ", purchPrice=" + purchPrice + ", price=" + price + ", amount="
				+ amount + ", minAmount=" + minAmount + "]";
	}
	
	
}
