package Classes;

public class AddedProduct {

	private String code;
	private String name;
	private double purchasingPrice;
	private double selllingPrice;
	private int amount;
	private double totalPurchasingPrice;
	private double totalSelllingPrice;
	
	public AddedProduct()
	{}

	
	
	public AddedProduct(String code, String name, double purchasingPrice, double selllingPrice, int amount) {
		super();
		this.code = code;
		this.name = name;
		this.purchasingPrice = purchasingPrice;
		this.selllingPrice = selllingPrice;
		this.amount = amount;
		this.totalPurchasingPrice = amount*purchasingPrice;
		this.totalSelllingPrice = amount*selllingPrice;
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

	public double getPurchasingPrice() {
		return purchasingPrice;
	}

	public void setPurchasingPrice(double purchasingPrice) {
		this.purchasingPrice = purchasingPrice;
	}

	public double getSelllingPrice() {
		return selllingPrice;
	}

	public void setSelllingPrice(double selllingPrice) {
		this.selllingPrice = selllingPrice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotalPurchasingPrice() {
		return totalPurchasingPrice;
	}

	public void setTotalPurchasingPrice(double totalPurchasingPrice) {
		this.totalPurchasingPrice = totalPurchasingPrice;
	}

	public double getTotalSelllingPrice() {
		return totalSelllingPrice;
	}

	public void setTotalSelllingPrice(double totalSelllingPrice) {
		this.totalSelllingPrice = totalSelllingPrice;
	}
	
	
}
