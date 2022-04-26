package Classes;

public class Order {
	
	private Product product;
	
	private String productCode;
	private String productName;
	private double productPrice;
	
	private int amount;
	private double totalPrice;
	
	public Order()
	{
		
	}

	
	public Order(Product product, int amount) {
		super();
		this.product = product;
		this.amount = amount;
		this.totalPrice = amount*product.getPrice();
		this.productCode=product.getCode();
		productName=product.getName();
		this.productPrice=product.getPrice();
	}

	public double getProductPrice() {
		return productPrice;
	}


	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	


}
