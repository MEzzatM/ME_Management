package View;

import java.net.URL;
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

public class NewProductController implements Initializable{

	@FXML
	private TextField code,name,purchasingPrice,sellingPrice,minAmount;
	@FXML
	private Label error;
	@FXML
	private Button save;
	
	private List<TextField> textFields=new ArrayList<>();
	
	private DBConnection connection =new DBConnection();
	
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
		
		try
		{
			//put convert data in try statement to catch if any entered data is invalid
			//or if any data is incorrect
			error.setVisible(false);
			String productCode=String.valueOf(code.getText());	//check that the code is an integer number
			String productName=name.getText();
			double productPurchasingPrice=Double.parseDouble(purchasingPrice.getText());
			double productSellingPrice=Double.parseDouble(sellingPrice.getText());
			int ProductMinAmount=Integer.parseInt(minAmount.getText());
			boolean correctData=true;
			//if name is empty 
			//or if code start with zero 
			//or amount start with zero
			if(name.getText().isBlank()||minAmount.getText().startsWith("0"))
			{
				correctData=false;
			}
			
			if(correctData)
			{
				if(connection.productWithSameCode(productCode))	//if there is product with same code
				{
					error.setText("There is Product with same code");
					error.setVisible(true);
				}
				else if(connection.productWithSameName(productName))   //if there is product with same name
				{
					error.setText("There is Product with same Name");  
					error.setVisible(true);
				}
				else	// pass all filtration tests --> save the new product
				{
					Stage stage = (Stage) code.getScene().getWindow();
					stage.setAlwaysOnTop(false);
					connection.newProduct(new Product(productCode,productName,productPurchasingPrice,productSellingPrice,0,ProductMinAmount));
					JOptionPane.showMessageDialog(null, "Product Saved Successfully ^_^");
					stage.setAlwaysOnTop(true);
					clear();
				}
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
	
	public void clear()
	{
		code.setText("");
		name.setText("");
		purchasingPrice.setText("");
		sellingPrice.setText("");
		minAmount.setText("");
		code.requestFocus();
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
	
}
