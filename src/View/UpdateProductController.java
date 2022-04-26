package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Product;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class UpdateProductController implements Initializable{

	@FXML
	private TextField code,name,purchasingPrice,sellingPrice,minAmount;
	@FXML
	private Label error;
	@FXML
	private Button save;
	
	private List<TextField> textFields=new ArrayList<>();
	private DBConnection connection =new DBConnection();
	Product product=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textFields.add(code);
		textFields.add(name);
		textFields.add(purchasingPrice);
		textFields.add(sellingPrice);
		textFields.add(minAmount);
		
		error.setVisible(false);
		
		code.requestFocus();
		
	}

	
	
	@FXML
	protected void save()
	{
		
		if(product!=null && !minAmount.getText().startsWith("0"))
		{
			try
			{
				//put convert data in try statement to catch if any entered data is invalid
				//or if any data is incorrect
				error.setVisible(false);
				product.setPurchPrice(Double.parseDouble(purchasingPrice.getText()));
				product.setPrice(Double.parseDouble(sellingPrice.getText()));
				product.setMinAmount(Integer.parseInt(minAmount.getText()));
				boolean correctData=true;
				//if code or name is empty 
				//or if code start with zero 
				//or amount start with zero
				if(minAmount.getText().startsWith("0"))
				{
					correctData=false;
				}
				
				if(correctData)			// pass all filtration tests --> update the product
				{			  
					connection.updateProduct(product);
					Stage stage = (Stage) code.getScene().getWindow();
					stage.setAlwaysOnTop(false);
					JOptionPane.showMessageDialog(null, "Product Updated Successfully ^_^");
					stage.setAlwaysOnTop(true);
					clear();
				}
				else
				{
					error.setText("Enter Correct Data");
					error.setVisible(true);
				}
			}
			catch(Exception e)
			{
				error.setText("Enter Correct Data");
				error.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void minAmountEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			save();
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			clear();
			if(code.getText().isBlank())
			{
				error.setText("Enter Correct Data");
				error.setVisible(true);
			}
			else
			{
				product=null;			
				product=connection.getProduct(code.getText());
				if(product!=null)		//if there is product with this code
				{	
					error.setVisible(false);
					name.setText(product.getName());
					purchasingPrice.setText(String.valueOf(product.getPurchPrice()));
					sellingPrice.setText(String.valueOf(product.getPrice()));
					minAmount.setText(String.valueOf(product.getMinAmount()));
					purchasingPrice.requestFocus();
				}
				else
				{
					error.setText("Enter Correct Data");
					error.setVisible(true);
				}
			}	
		}
	}

	
	@FXML
	protected void pressEnter(KeyEvent event)	//will be used by first four text Fields 
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			TextField field=(TextField) event.getSource();
			for(int i=0;i<textFields.size();i++)
			{
				if(field==textFields.get(i))
				{
					textFields.get(i+1).requestFocus();
				}
			}
		}
	}
	
	public void clear()
	{
		for(int i=1;i<textFields.size();i++)
		{
			textFields.get(i).setText("");
		}
		code.requestFocus();
	}
}
