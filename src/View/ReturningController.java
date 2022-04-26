package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Order;
import Classes.Product;
import DataBase.DBConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ReturningController  implements Initializable{

		//selling container content
		@FXML
		private TextField productCode,productName,productPrice,amount,total;
		@FXML
		private CheckBox automaticAdd;
		@FXML
		private Label codeError,amountError;
		@FXML 
		private TableView<Order> table;
		@FXML 
		private TableColumn<Order, String> tableCode;
		@FXML 
		private TableColumn<Order, String> tableName;
		@FXML 
		private TableColumn<Order, Double> tablePrice;
		@FXML 
		private TableColumn<Order, Integer> tableAmount;
		@FXML 
		private TableColumn<Order, Double> tableTotalPrice;
	
///////////////////////////////
//find Container content
@FXML
private AnchorPane findContainer;
@FXML
private TextField Name;
@FXML 
private TableView<Product> findTable;
@FXML 
private TableColumn<Product, String> findTableCode;
@FXML 
private TableColumn<Product, String> findTableName;
@FXML 
private TableColumn<Product, Double> findTablePrice;
@FXML 
private TableColumn<Product, Integer> findTableAmount;


private List <Product> products;
//////////////////////////////
		Product product; 
		DBConnection connection=new DBConnection();
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Order, String>("productCode"));
		tableName.setCellValueFactory(new PropertyValueFactory<Order, String>("productName"));
		tablePrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("productPrice"));
		tableAmount.setCellValueFactory(new PropertyValueFactory<Order, Integer>("amount"));
		tableTotalPrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalPrice"));
		///////////////////////////////////
		//find initialization
		try {products=connection.getAllProducts();} catch (SQLException e) {e.printStackTrace();}
		
		findTableCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		findTableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		findTablePrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		findTableAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
		
		findContainer.setVisible(false);
		/////////////////////////////////
		codeError.setVisible(false);
		amountError.setVisible(false);
		total.setText("0.0");
	}
///////////////////////////////
//find methods
@FXML
protected void search()		//for textField
{
	String name=Name.getText();	//every written letter
	findTable.getItems().clear();
	for(int i=0;i<products.size();i++)
	{
		if(products.get(i).getName().contains(name))
		{
			findTable.getItems().add(products.get(i));
		}
	}
}



@FXML
protected void close()		//for exit Button
{
	findContainer.setVisible(false);
	productCode.requestFocus();
}

@FXML
protected void findTableClick(MouseEvent event) throws SQLException		//for findTable  
{
	if (event.getClickCount() == 2) 
	{
		Product selected=findTable.getSelectionModel().getSelectedItem();
		if(selected!=null)
		{
			productCode.setText(selected.getCode());
			findContainer.setVisible(false);
			enter();
		}

	}
}
///////////////////////////////
	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode()==KeyCode.ENTER)		//try to get product 
		{
			enter();
		}	
	}
	@FXML
	protected void enter() throws SQLException
	{
		if(productCode.getText().isBlank())
		{	
			if(table.getItems().size()>0)
			{
			  codeError.setVisible(false);
			  Stage stage = (Stage) productCode.getScene().getWindow();
			  stage.setAlwaysOnTop(false);
			  int a=JOptionPane.showConfirmDialog(null,"Save This Operation?");  
			  if(a==JOptionPane.YES_OPTION)
			  {  
				    codeError.setVisible(false);
					saveReturnOperation();
			  }
			  stage.setAlwaysOnTop(true);
			}
			else
			{
				codeError.setVisible(true);
			}
		}
		else
		{
			product = connection.getProduct(productCode.getText());
			if(product!=null)
			 {
				codeError.setVisible(false);
				if(automaticAdd.isSelected())
				{
					addOrder(product,1);
				}
				else
				{
					productName.setText(product.getName());
					productPrice.setText(String.valueOf(product.getPrice()));
					amount.requestFocus();
				}
			}
			else
			{
				codeError.setVisible(true);
				clearFields();
			}
		}
	}
	
	@FXML
	protected void containerEnter(KeyEvent event) throws SQLException	//if any button clicked in container
	{
		if(event.getCode()==KeyCode.ALT)		
		{
			if(automaticAdd.isSelected())
			{
				automaticAdd.setSelected(false);
			}else
			{
				automaticAdd.setSelected(true);
			}
			autoIncrement();
		}
		else if(event.getCode()==KeyCode.CONTROL)		//enter amount
		{
			if(findContainer.isVisible())
			{
				close();
			}
			else
			{
				showFind();
				Name.requestFocus();
			}	
		}	
	}
	
	@FXML
	protected void showFind()
	{
		try {products=connection.getAllProducts();} catch (SQLException e) {e.printStackTrace();}
		findTable.getItems().clear();
		findTable.getItems().addAll(products);
		findContainer.setVisible(true);
	}
	
	@FXML
	public void tableClick(MouseEvent event)
	{
		  ObservableList<Order> orders=table.getItems();
		  if (event.getClickCount() == 2) 
		  {
			   Order order=table.getSelectionModel().getSelectedItem();
	    	   if(order!=null)
	    	   {
	    		   productCode.setText(order.getProductCode());
		    	   productName.setText(order.getProductName());
		    	   productPrice.setText(String.valueOf(order.getProductPrice()));
		    	   amount.setText(String.valueOf(order.getAmount()));
		    	   orders.remove(order);
		    	   productCode.requestFocus();
	    	   }
		       
		  }
    }
	@FXML
	protected void amountEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))		//enter amount
		{
			AmountEnter();
		}
	}
	
	@FXML
	protected void AmountEnter()
	{
		if(product!=null)
		{
			int n=0;
			boolean number=false;
			try
			{
				n=Integer.parseInt(amount.getText());
				number=true;
			}catch(Exception ex)
			{
				amountError.setVisible(true);
			}
			if(number)
			{
				if(n%1==0)
				{
					amountError.setVisible(false);
					addOrder(product,n);
				}
				else
				{
					amountError.setVisible(true);
				}
			}
		}
	}
	
	@FXML
	protected void autoIncrement()		//when automatic add check box is selected
	{
		if(automaticAdd.isSelected())
		{
			amount.setText("1");
		}
		else
		{
			amount.setText("");
		}
	}
	
	
	public void saveReturnOperation() throws SQLException
	{
		List <Order> orders=table.getItems();
		connection.saveReturnProcess(orders);
		clearAll();
	}
	
	public void addOrder(Product product,int amount)
	{
		Order order =new Order(product,amount);
		boolean found=false;
		for(int i=0;i<table.getItems().size();i++)
		{
			if(table.getItems().get(i).getProductCode().equals(product.getCode()))  //if product in the table already
			{
				Order newOrder=new Order(product,table.getItems().get(i).getAmount()+amount);		//create new order with new total amount
				table.getItems().set(i, newOrder);													//replace the new order with the old one
				total.setText(String.valueOf(Double.parseDouble(total.getText())+order.getTotalPrice()));	//change total value
				clearFields();										//clear text fields and labels
				found=true;	
				break;						//stop searching in the table 
			}
		}
		if(!found)	//if not found the product in the table already
		{
			table.getItems().add(order);		//add the new order
			total.setText(String.valueOf(Double.parseDouble(total.getText())+order.getTotalPrice()));	//change total value
			clearFields();		//clear fields	
		}
	}
	
	
	public void clearFields()
	{
		productName.setText("");
		productPrice.setText("");
		if(!automaticAdd.isSelected())
		{
			amount.setText("");
		}
		productCode.requestFocus();
		amountError.setVisible(false);
		codeError.setVisible(false);
	}
	
	public void clearAll()
	{
		productCode.setText("");
		productName.setText("");
		productPrice.setText("");
		amount.setText("");
		total.setText("0.0");
		automaticAdd.setSelected(false);
		amountError.setVisible(false);
		codeError.setVisible(false);
		table.getItems().clear();
		productCode.requestFocus();
		
		Stage stage = (Stage) productName.getScene().getWindow();
	    stage.close();;
	}
	
	
	
}
