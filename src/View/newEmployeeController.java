package View;

import java.net.URL;
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

public class newEmployeeController implements Initializable{

	@FXML
	private TextField code,name,position,value,mobile;
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
		textFields.add(position);
		textFields.add(value);
		textFields.add(mobile);
		
		error.setVisible(false);
		code.requestFocus();
	}
	
	@FXML
	protected void save()
	{
		if(code.getText().isBlank()||name.getText().isBlank()||position.getText().isBlank()||
				value.getText().isBlank()||mobile.getText().isBlank())
		{
			error.setText("Enter Correct Data");
			error.setVisible(true);
		}else
		{
			try
			{
				
				//put convert data in try statement to catch if any entered data is invalid
				//or if any data is incorrect
				error.setVisible(false);
				String employeeCode=String.valueOf(code.getText());	//check that the code is an integer number
				String employeeName=name.getText();
				String employeePosition=position.getText();
				double employeeValue=Double.parseDouble(value.getText());
				String employeeMobile=mobile.getText();
				
				if(connection.employeeWithSameCode(employeeCode))	//if there is product with same code
				{
					error.setText("There is Employee with same code");
					error.setVisible(true);
				}
				else if(connection.employeeWithSameName(employeeName))   //if there is product with same name
				{
					error.setText("There is Employee with same Name");  
					error.setVisible(true);
				}
				else	// pass all filtration tests --> save the new product
				{
					connection.newEmployee(new Employee(employeeCode,employeeName,employeePosition,employeeValue,employeeMobile));
					Stage stage = (Stage) code.getScene().getWindow();
					stage.setAlwaysOnTop(false);
					JOptionPane.showMessageDialog(null, " Employee Saved Successfully ^_^");
					stage.setAlwaysOnTop(true);
					clear();
				}
			}
			catch(Exception e)
			{
				error.setText("Enter Correct Data");
				error.setVisible(true);
			}
		}	
	}
	
	public void clear()
	{
		code.setText("");
		name.setText("");
		position.setText("");
		value.setText("");
		mobile.setText("");
		code.requestFocus();
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
