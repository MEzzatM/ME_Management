package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


import Classes.Product;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class EndingSoonController implements Initializable{

	@FXML
	private TextField productCode;
	@FXML
	private Button find;
	@FXML
	private Label error;
	@FXML 
	private TableView<Product> table;
	@FXML 
	private TableColumn<Product, String> tableCode;
	@FXML 
	private TableColumn<Product, String> tableName;
	@FXML 
	private TableColumn<Product, Double> tablePrice;
	@FXML 
	private TableColumn<Product, Integer> tableAmount;
	@FXML 
	private TableColumn<Product, Integer> tableMinAmount;
	
	private DBConnection connection =new DBConnection();
	List<Product> products;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		tablePrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		tableAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
		tableMinAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("minAmount"));
		
		error.setVisible(false);
		try {
			getEndingSoon();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@FXML
	protected void findProduct()
	{
		if(productCode.getText().isBlank())
		{
			error.setVisible(true);
		}
		else
		{
			error.setVisible(false);
			String code=productCode.getText();
			table.getItems().clear();
			for(int i=0;i<products.size();i++)
			{
				if(products.get(i).getCode().equals(code))
				{
					table.getItems().add(products.get(i));
				}
			}
		}
	}
	
	

	public void getEndingSoon() throws NumberFormatException, SQLException
	{
		products=connection.endingSoon();
		for(int i=0;i<products.size();i++)
		{
			table.getItems().add(products.get(i));
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			findProduct();	
		}
		
	}
}
