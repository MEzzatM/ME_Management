package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Classes.Product;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class getProductController implements Initializable{
	@FXML
	private TextField productName;
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

	
   private List <Product> products;
   DBConnection connection =new DBConnection();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {products=connection.getAllProducts();} catch (SQLException e) {e.printStackTrace();}
		
		tableCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		tablePrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		tableAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
		
		for(int i=0;i<products.size();i++)
		{
			table.getItems().add(products.get(i));
		}
		productName.requestFocus();
		
	}

	@FXML
	protected void search()
	{
		String name=productName.getText();
		table.getItems().clear();
		for(int i=0;i<products.size();i++)
		{
			if(products.get(i).getName().contains(name))
			{
				table.getItems().add(products.get(i));
			}
		}
	}
	
	@FXML
	protected void exit(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ESCAPE))
		{
			close();
		}
		
	}
	
	@FXML
	protected void close()
	{
		Stage stage = (Stage) productName.getScene().getWindow();
	    stage.close();
	}
}
