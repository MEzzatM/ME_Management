package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Classes.Employee;
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

public class EmploeesController implements Initializable{

	@FXML
	private TextField code;
	@FXML
	private Button find;
	@FXML
	private Label error;
	@FXML 
	private TableView<Employee> table;
	@FXML 
	private TableColumn<Employee, String> tableCode;
	@FXML 
	private TableColumn<Employee, String> tableName;
	@FXML 
	private TableColumn<Employee, String> tablePosition;
	@FXML 
	private TableColumn<Employee, Double> tableValue;
	@FXML 
	private TableColumn<Employee, String> tableMobile;
	
	private DBConnection connection=new DBConnection();
	private List<Employee> employees;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Employee, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		tablePosition.setCellValueFactory(new PropertyValueFactory<Employee, String>("position"));
		tableValue.setCellValueFactory(new PropertyValueFactory<Employee, Double>("hourValue"));
		tableMobile.setCellValueFactory(new PropertyValueFactory<Employee, String>("mobile"));
		
		error.setVisible(false);
		
		try {
			employees=connection.getAllEmployees();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.getItems().addAll(employees);
	}
	
	@FXML
	protected void find()
	{
		if(code.getText().isBlank())
		{
			error.setVisible(true);
		}
		else
		{
			error.setVisible(false);
			String employeeCode=code.getText();
			table.getItems().clear();
			for(int i=0;i<employees.size();i++)
			{
				if(employees.get(i).getCode().equals(employeeCode))
				{
					table.getItems().add(employees.get(i));
				}
			}
			if(table.getItems().size()==0)
			{
				error.setVisible(true);
				table.getItems().addAll(employees);
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
	
}
