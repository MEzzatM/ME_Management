package View;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


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

public class signInController implements Initializable{

	@FXML
	private TextField name,password;
	@FXML
	private Button enter;
	@FXML
	private Label error;
	private DBConnection connection =new DBConnection();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error.setVisible(false);
		
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
	protected void passEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode().equals(KeyCode.ENTER))		//try to get product 
		{
			signEnter();
		}

	}
	
	
	
	@FXML
	protected void signEnter() throws SQLException
	{
		if(correctUser(name.getText(),password.getText()))
		{
			error.setVisible(false);
			if(!thereIsOpenedShift(name.getText()))
			{
				createNewShift(connection.getUser(name.getText()));
			}
			Parent root = null;
			try 
			{
				root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Stage stage=new Stage();
			Scene scene =new Scene(root);
			stage.setScene(scene);
			//set stage size the same of screen size
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			stage.setWidth(screenBounds.getWidth()+12);
			stage.setHeight(screenBounds.getHeight()+6);
			stage.setTitle("ME Management System");
			stage.setOnCloseRequest( ev -> {Platform.exit();});
			stage.show();
			
			Stage currentStage = (Stage) name.getScene().getWindow();
			currentStage.close();
		}
		else
		{
			error.setVisible(true);
		}

	}
	
	public boolean correctUser (String user,String Passwrd) throws SQLException
	{
		return connection.correctUser(user, Passwrd);
	}
	
	public boolean thereIsOpenedShift(String user) throws SQLException
	{
		return connection.thereIsOpenedShift(user);
	}
	
	public void createNewShift(User user) throws SQLException
	{
		connection.createNewShift(user);
	}
}
