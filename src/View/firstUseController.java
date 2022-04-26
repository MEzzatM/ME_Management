package View;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.User;
import DataBase.DBConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class firstUseController implements Initializable{

	@FXML
	private TextField code,name,password,password2;
	@FXML
	private Button save	;
	@FXML
	private Label error;
	private DBConnection connection =new DBConnection();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error.setVisible(false);
		
	}
	
	@FXML
	protected void codeEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			name.requestFocus();
		}
	}
	
	@FXML
	protected void nameEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			password.requestFocus();
		}
	}
	
	@FXML
	protected void passwordEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			password2.requestFocus();
		}
	}

	@FXML
	protected void password2Enter(KeyEvent event) throws SQLException
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			save();
		}
	}
	
	@FXML
	protected void save() throws SQLException
	{
		String ownerCode=code.getText();
		String ownerName=name.getText();
		String ownerPassword=password.getText();
		String ownerPassword2=password2.getText();
		
		//check if any data is empty or the passwords is not equal
		if(ownerCode.isBlank()||ownerName.isBlank()||ownerPassword.isBlank()||!ownerPassword.equals(ownerPassword2))
		{
			error.setVisible(true);
		}
		else
		{
			connection.newUser(new User(ownerCode,ownerName,ownerPassword));	//create new User
			connection.createNewShift(connection.getUser(ownerCode));			//create new Shift
			
			JOptionPane.showInternalMessageDialog(null, "Owner Saved Successfully");
			error.setVisible(false);	
			
			Parent root = null;				//open main page
			try 
			{
				root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Stage stage=new Stage();
			Scene scene =new Scene(root);
			stage.setScene(scene);	
			
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();		//set stage size the same of screen size
			stage.setWidth(screenBounds.getWidth()+12);
			stage.setHeight(screenBounds.getHeight()+6);
			
			stage.setTitle("ME Management System");
			stage.setOnCloseRequest( ev -> {Platform.exit();});
			stage.show();
			
			Stage currentStage = (Stage) name.getScene().getWindow();	//close first use screen
			currentStage.close();
		}
	}
	
}
