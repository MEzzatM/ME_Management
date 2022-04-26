package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Employee;
import Classes.User;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class newUserController implements Initializable{

	@FXML
	private TextField code,name,position,password1,password2;
	@FXML
	private Button enter,save;
	@FXML
	private Label error;
	
	private DBConnection connection=new DBConnection();
	private Employee employee=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error.setVisible(false);
		
	}
	
	@FXML
	protected void find() throws SQLException
	{
		clear();
		employee=connection.getEmployee(code.getText());
		if(employee!=null)
		{
			error.setVisible(false);
			name.setText(employee.getName());
			position.setText(employee.getPosition());
			password1.requestFocus();
		}
		else
		{
			error.setVisible(true);
		}
	}

	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			find();
		}
	}
	
	@FXML
	protected void password1(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			password2.requestFocus();
		}
	}
	
	@FXML
	protected void password2(KeyEvent event) throws SQLException
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			save();
		}
	}
	
	@FXML
	protected void save() throws SQLException
	{
		if(employee!=null)
		{
			if(password1.getText().equals(password2.getText())&&connection.getUser(employee.getCode())==null)
			{
				error.setVisible(false);
				connection.newUser(new User(employee.getCode(),employee.getName(),password1.getText()));
				Stage stage = (Stage) code.getScene().getWindow();
				stage.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null,"saved successfully ^_^");
				stage.setAlwaysOnTop(true);
				clear();
				code.requestFocus();
			}
			else
			{
				error.setVisible(true);
			}
		}
	}
	
	public void clear()
	{
		name.setText("");
		position.setText("");
		password1.setText("");
		password2.setText("");
		
	}
}
