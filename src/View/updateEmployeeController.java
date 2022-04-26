package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Employee;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class updateEmployeeController implements Initializable{

	@FXML
	private TextField code,name,position,value,mobile;
	@FXML
	private Label error;
	@FXML
	private Button save;
	
	private List<TextField> textFields=new ArrayList<>();
	
	private DBConnection connection =new DBConnection();
	private Employee employee=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textFields.add(code);
		textFields.add(name);
		textFields.add(position);
		textFields.add(value);
		textFields.add(mobile);
		
		error.setVisible(false);
		code.requestFocus();
		
	}

	@FXML
	protected void save()
	{
		
		if(employee!=null)
		{
			try
			{
				//put convert data in try statement to catch if any entered data is invalid
				//or if any data is incorrect
				error.setVisible(false);
				employee.setPosition(position.getText());
				employee.setHourValue(Double.parseDouble(value.getText()));
				employee.setMobile(mobile.getText());
				connection.updateEmployee(employee);
				Stage stage = (Stage) code.getScene().getWindow();
				stage.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Employee Updated Successfully ^_^");
				stage.setAlwaysOnTop(true);
				clear();
			}
			catch(Exception e)
			{
				error.setText("Enter Correct Data");
				error.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void mobileEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			save();
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException	//when press enter after enter the code 
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
				employee=null;			
				employee=connection.getEmployee(code.getText());
				if(employee!=null)		//if there is product with this code
				{	
					error.setVisible(false);
					name.setText(employee.getName());
					position.setText(employee.getPosition());
					value.setText(String.valueOf(employee.getHourValue()));
					mobile.setText(employee.getMobile());
					
					position.requestFocus();
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
	
	@FXML
	protected void delete() throws SQLException
	{
		if(employee!=null)
		{
			error.setVisible(false);
			Stage stage = (Stage) code.getScene().getWindow();
			stage.setAlwaysOnTop(false);
			int a=JOptionPane.showConfirmDialog(null,"Are you sure?\n"
					+ "Delete this Employee?");  
			  if(a==JOptionPane.YES_OPTION)
			  {  
				    connection.deleteEmployee(employee);
				    JOptionPane.showMessageDialog(null, "Employee deleted successfully");
				    clear();
			  } 
			  stage.setAlwaysOnTop(true);
		}
		else
		{
			error.setText("Enter Correct Data");
			error.setVisible(true);
		}
	}
	public void clear()
	{
		name.setText("");
		position.setText("");
		value.setText("");
		mobile.setText("");
		code.requestFocus();
	}
}
