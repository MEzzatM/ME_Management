package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.User;
import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class userController implements Initializable{

	@FXML
	private TextField code,name;
	@FXML
	private Button enter,delete;
	@FXML
	private Label error;
	@FXML 
	private TableView<User> table;
	@FXML 
	private TableColumn<User, String> tableCode;
	@FXML 
	private TableColumn<User, String> tableName;
	@FXML 
	private TableColumn<User, String> tablepassword;
	
	private DBConnection connection=new DBConnection();
	private List<User> users;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<User, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		tablepassword.setCellValueFactory(new PropertyValueFactory<User, String>("pass"));
		
		error.setVisible(false);
		
		try {
			users=connection.getAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.getItems().addAll(users);
		
	}
	
	@FXML
	protected void find()
	{
		name.setText("");
		if(code.getText().isBlank())
		{
			error.setVisible(true);
		}
		else
		{
			error.setVisible(false);
			String userCode=code.getText();
			table.getItems().clear();
			for(int i=0;i<users.size();i++)
			{
				if(users.get(i).getCode().equals(userCode))
				{
					table.getItems().add(users.get(i));
				}
			}
			if(table.getItems().size()==0)
			{
				error.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			find();
		}
	}
	
	@FXML
	public void tableClick(MouseEvent event)
	{
          if (event.getClickCount() == 2) 
          {
        	  User selected=table.getSelectionModel().getSelectedItem();
        	  name.setText(selected.getName());
          }
	}
	
	@FXML
	protected void delete() throws SQLException
	{
		if(!name.getText().isBlank())
		{
			error.setVisible(false);
			Stage stage = (Stage) code.getScene().getWindow();
			stage.setAlwaysOnTop(false);
			int a=JOptionPane.showConfirmDialog(null,"Are you sure?\n"
					+ "Delete this User?");  
			  if(a==JOptionPane.YES_OPTION)
			  {  
				    connection.deleteUser(name.getText());
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
	
	public void clear() throws SQLException
	{
		name.setText("");
		users=connection.getAllUsers();
		table.getItems().clear();
		table.getItems().addAll(users);
		
	}

}
