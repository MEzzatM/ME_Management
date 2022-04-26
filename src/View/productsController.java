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


public class productsController implements Initializable{

	@FXML
	private TextField code;
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
	
	private DBConnection connection=new DBConnection();
	private List<Product> products;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		tablePrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		tableAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
		
		error.setVisible(false);
		
		try {
			products=connection.getAllProducts();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.getItems().addAll(products);
	}
	
	@FXML
	protected void find()
	{
		if(code.getText().isBlank())
		{
			error.setVisible(true);
		}
		else
		{
			error.setVisible(false);
			String productCode=code.getText();
			table.getItems().clear();
			for(int i=0;i<products.size();i++)
			{
				if(products.get(i).getCode().equals(productCode))
				{
					table.getItems().add(products.get(i));
				}
			}
			if(table.getItems().size()==0)
			{
				error.setVisible(true);
				table.getItems().addAll(products);
			}
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			find();
		}
	}

}
