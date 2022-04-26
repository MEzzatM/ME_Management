package View;


import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.AddedProduct;

import Classes.Product;
import DataBase.DBConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

public class AddController implements Initializable{

	@FXML
	private TextField code,name,purchasePrice,sellingPrice,amount,totalPurchasePrice,totalSellingPrice;
	@FXML
	private Button add,saveAll;
	@FXML
	private Label codeError,error;
	@FXML
	private CheckBox purchasePriceEdit,sellingPriceEdit,automaticAddl;
	@FXML
	private TableView<AddedProduct> table;
	@FXML 
	private TableColumn<AddedProduct, String> tableCode;
	@FXML 
	private TableColumn<AddedProduct, String> tableName;
	@FXML 
	private TableColumn<AddedProduct, Double> tablePurchasePrice;
	@FXML 
	private TableColumn<AddedProduct, Integer> tableAmount;
	@FXML 
	private TableColumn<AddedProduct, Double> tableTotalPurchasePrice;
	@FXML 
	private TableColumn<AddedProduct, Double> tableTotalSellingPrice;
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
	
	private DBConnection connection =new DBConnection();
	private AddedProduct addedProduct=null;
	private Product product=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<AddedProduct, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<AddedProduct, String>("name"));
		tablePurchasePrice.setCellValueFactory(new PropertyValueFactory<AddedProduct, Double>("purchasingPrice"));
		tableAmount.setCellValueFactory(new PropertyValueFactory<AddedProduct, Integer>("amount"));
		tableTotalPurchasePrice.setCellValueFactory(new PropertyValueFactory<AddedProduct, Double>("totalPurchasingPrice"));
		tableTotalSellingPrice.setCellValueFactory(new PropertyValueFactory<AddedProduct, Double>("totalSelllingPrice"));
		///////////////////////////////////
		//find initialization--> this contents is to find Page
		try {products=connection.getAllProducts();} catch (SQLException e) {e.printStackTrace();}
		
		findTableCode.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
		findTableName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		findTablePrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
		findTableAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
		
		findContainer.setVisible(false);
		/////////////////////////////////
		codeError.setVisible(false);
		error.setVisible(false);
		totalPurchasePrice.setText("0.0");
		totalSellingPrice.setText("0.0");
		code.requestFocus();
	}
	
/////////////////////////////
	//find methods
	@FXML
	protected void search()		//for textField every written letter
	{
		String name=Name.getText();	
		findTable.getItems().clear();		//clear the table
		for(int i=0;i<products.size();i++)
		{
			if(products.get(i).getName().contains(name))	
			{
				findTable.getItems().add(products.get(i));		//fill the table with the products which contains entered name
			}
		}
	}
	

	
	@FXML
	protected void close()		//for exit Button
	{
		findContainer.setVisible(false);
		code.requestFocus();
	}
	
	@FXML
	protected void findTableClick(MouseEvent event) throws SQLException		//for findTable  
	{
         if (event.getClickCount() == 2) 
         {
       	     Product selected=findTable.getSelectionModel().getSelectedItem();
       	     if(selected!=null)
       	     {
       	    	code.setText(selected.getCode());
       	    	findContainer.setVisible(false);
       	    	getProduct();
       	     }
             
         }
	}
//////////////////////////////
	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode()==KeyCode.ENTER)		//try to get product 
		{
			if(code.getText().isBlank())	//if code field is empty may want to save all
			{
				if(table.getItems().size()>0)	//only if there is products to save
				{
					codeError.setVisible(false);
					Stage stage = (Stage) code.getScene().getWindow();
					stage.setAlwaysOnTop(false);
					int a=JOptionPane.showConfirmDialog(null,"Are you sure?");  
				    if(a==JOptionPane.YES_OPTION)
				    { 
					    saveAll();
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
				getProduct();
			}
		}	
	}
	
	

	@FXML
	protected void showFind(KeyEvent event)
	{
		if(event.getCode()==KeyCode.CONTROL)		//enter amount
		{
			if(findContainer.isVisible())
			{
				close();
			}
			else
			{
				try {products=connection.getAllProducts();} catch (SQLException e) {e.printStackTrace();}
				findTable.getItems().clear();
				findTable.getItems().addAll(products);
				findContainer.setVisible(true);
				Name.requestFocus();
			}
		}
		
	}
	
	@FXML
	protected void getProduct() throws SQLException
	{
		product=connection.getProduct(code.getText());
		if(product!=null)	//if there is product with this code
		{
			codeError.setVisible(false);
			name.setText(product.getName());
			purchasePrice.setText(String.valueOf(product.getPurchPrice()));
			sellingPrice.setText(String.valueOf(product.getPrice()));
			amount.requestFocus();
		}
		else
		{
			clearFields();
			codeError.setVisible(true);
		}
	}
	
	@FXML
	protected void amountEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			add();
		}
	}
	
	@FXML
	protected void add()
	{
		if(product!=null)
		{
			try 
			{
				addedProduct=new AddedProduct(product.getCode(),product.getName()
						,Double.parseDouble(purchasePrice.getText())
						,Double.parseDouble(sellingPrice.getText()),Integer.parseInt(amount.getText()));
				
				boolean found=false;
				for(int i=0;i<table.getItems().size();i++)
				{
					if(table.getItems().get(i).getCode().equals(product.getCode()))  //if product in the table already
					{
						found=true;
						AddedProduct newAddedProduct=new AddedProduct(addedProduct.getCode(),addedProduct.getName(),addedProduct.getPurchasingPrice(),addedProduct.getSelllingPrice(),addedProduct.getAmount()+table.getItems().get(i).getAmount());		//create new order with new total amount
						table.getItems().set(i, newAddedProduct);													//replace the new order with the old one
						break;						//stop searching in the table 
					}
				}
				if(!found)
				{
					table.getItems().add(addedProduct);
				}
				
				totalPurchasePrice.setText(String.valueOf(Double.parseDouble(totalPurchasePrice.getText())+addedProduct.getTotalPurchasingPrice()));
				totalSellingPrice.setText(String.valueOf(Double.parseDouble(totalSellingPrice.getText())+addedProduct.getTotalSelllingPrice()));
				clearFields();
				product=null;
						
			}catch(Exception ex)
			{
				error.setVisible(true);
			}
		}
		
	}
	
	@FXML
	protected void saveAll() throws SQLException
	{
		if(table.getItems().size()>0)
		{
			connection.add(table.getItems());
			clearAll();
			Stage stage = (Stage) code.getScene().getWindow();
			stage.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null,"Saved Successfully");
			stage.setAlwaysOnTop(true);
			code.requestFocus();
		}
	}
	
	@FXML
	public void tableClick(MouseEvent event)
	{
          ObservableList<AddedProduct> products=table.getItems();
          if (event.getClickCount() == 2) 
          {
        	  AddedProduct selected=table.getSelectionModel().getSelectedItem();
        	  if(selected!=null)
        	  {
        		  code.setText(selected.getCode());
            	  name.setText(selected.getName());
            	  purchasePrice.setText(String.valueOf(selected.getPurchasingPrice()));
            	  sellingPrice.setText(String.valueOf(selected.getSelllingPrice()));
            	  amount.setText(String.valueOf(selected.getAmount()));
                  products.remove(selected);
                  code.requestFocus(); 
        	  }
          }
	}
	
	@FXML
	protected void purchasePriceEdit()
	{
		if(purchasePriceEdit.isSelected())
		{
			purchasePrice.setEditable(true);
		}
		else
		{
			purchasePrice.setEditable(false);
		}
	}
	
	@FXML
	protected void sellingPriceEdit()
	{
		if(sellingPriceEdit.isSelected())
		{
			sellingPrice.setEditable(true);
		}
		else
		{
			sellingPrice.setEditable(false);
		}
	}
	
	public void clearFields()
	{
		code.setText("");
		name.setText("");
		purchasePrice.setText("");
		sellingPrice.setText("");
		amount.setText("");
		purchasePriceEdit.setSelected(false);
		sellingPriceEdit.setSelected(false);
		codeError.setVisible(false);
		error.setVisible(false);
		code.requestFocus();
	}
	
	public void clearAll()
	{
		code.setText("");
		name.setText("");
		purchasePrice.setText("");
		sellingPrice.setText("");
		amount.setText("");
		totalPurchasePrice.setText("0.0");
		totalSellingPrice.setText("0.0");
		purchasePriceEdit.setSelected(false);
		sellingPriceEdit.setSelected(false);
		codeError.setVisible(false);
		error.setVisible(false);
		table.getItems().clear();
		code.requestFocus();
	}
	
	
	
}
