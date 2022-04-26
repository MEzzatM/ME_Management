package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Attendance;
import Classes.Salary;
import DataBase.DBConnection;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class salariesControl implements Initializable{

	@FXML
	private TextField code,name,total;
	@FXML
	private Button enter,info,pay,payAll;
	@FXML
	private Label codeError,payError;
	@FXML 
	private TableView<Salary> table;
	@FXML 
	private TableColumn<Salary, String> tableCode;
	@FXML 
	private TableColumn<Salary, String> tableName;
	@FXML 
	private TableColumn<Salary, Double> tableHours;
	@FXML 
	private TableColumn<Salary, Double> tableValue;
	@FXML 
	private TableColumn<Salary, Double> tableTotal;
	
	/////////
	//information page content
	@FXML
	private AnchorPane infoContainer;
	@FXML
	private TextField employeeCode,employeeName,employeeHours,employeeValue,employeeSalary;
	@FXML 
	private TableView<Attendance> employeeTable;
	@FXML 
	private TableColumn<Attendance, String> employeeTableDay;
	@FXML 
	private TableColumn<Attendance, String> employeeTableAttendance;
	@FXML 
	private TableColumn<Attendance, String> employeeTableDeparture;
	@FXML 
	private TableColumn<Attendance, Double> employeeTableHours;
	

	/////////
	private DBConnection connection =new DBConnection();
	private List<Salary> salaries;
	private  Salary salary;
	private double totalSalary=0;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Salary, String>("code"));
		tableName.setCellValueFactory(new PropertyValueFactory<Salary, String>("name"));
		tableHours.setCellValueFactory(new PropertyValueFactory<Salary, Double>("workedHours"));
		tableValue.setCellValueFactory(new PropertyValueFactory<Salary, Double>("hourValue"));
		tableTotal.setCellValueFactory(new PropertyValueFactory<Salary, Double>("total"));
		
		employeeTableDay.setCellValueFactory(new PropertyValueFactory<Attendance, String>("day"));
		employeeTableAttendance.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendance"));
		employeeTableDeparture.setCellValueFactory(new PropertyValueFactory<Attendance, String>("departure"));
		employeeTableHours.setCellValueFactory(new PropertyValueFactory<Attendance, Double>("hours"));
		
		codeError.setVisible(false);
		payError.setVisible(false);
		
		infoContainer.setVisible(false);
		
		try {
			salaries=connection.getSalaries();
			table.getItems().addAll(salaries);
			for(int i=0;i<salaries.size();i++)
			{
				totalSalary+=salaries.get(i).getTotal();
			}
			total.setText(String.valueOf(totalSalary));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@FXML
	protected void enter()
	{
		if(!code.getText().isBlank()&&table.getItems().size()>0)
		{
			codeError.setVisible(false);
			String employeeCode=code.getText();
			table.getItems().clear();
			for(int i=0;i<salaries.size();i++)
			{
				if(salaries.get(i).getCode().equals(employeeCode))
				{
					table.getItems().add(salaries.get(i));
				}
			}
			if(table.getItems().size()==0)
			{
				codeError.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void codeEnter(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER))
		{
			enter();
		}
	}
	
	@FXML
	protected void tableClick(MouseEvent event) throws SQLException
	{
		  salary=null;
          if (event.getClickCount() == 2) 
          {
        	 salary=table.getSelectionModel().getSelectedItem();
        	 if(salary!=null)
        	 {
        		 info();
        	 }
          }
	}
	
	@FXML
	protected void pay() throws SQLException
	{
			ObservableList<Salary> salaries=table.getItems();
			payError.setVisible(false);
			connection.paySalary(salary);
			for(int i=0;i<salaries.size();i++)
			{
				if(salaries.get(i)==salary)
				{
					total.setText(String.valueOf(Double.parseDouble(total.getText())-salaries.get(i).getTotal()));
					salaries.remove(i);
				}
			}
			Stage stage = (Stage) code.getScene().getWindow();
			stage.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, "Paied Successfully");
			stage.setAlwaysOnTop(true);
			exit();
	}
	
	@FXML
	protected void payAll() throws SQLException
	{
		ObservableList<Salary> salaries=table.getItems();
		if(salaries.size()>0)
		{
			for(int j=0;j<salaries.size();j++)
			{
				total.setText(String.valueOf(Double.parseDouble(total.getText())-salaries.get(j).getTotal()));
				connection.paySalary(salaries.get(j));
				salaries.remove(j);
			}
			Stage stage = (Stage) code.getScene().getWindow();
			stage.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, "Paied All Successfully");
			stage.setAlwaysOnTop(true);
			exit();
		}
		else
		{
			payError.setVisible(true);
		}
	}
	
	@FXML
	protected void info() throws SQLException
	{
		if(salary!=null)
		{
			employeeTable.getItems().clear();
			List<Attendance> attendance=connection.getAttendance(salary.getCode());
			if(attendance!=null)
			{
				employeeTable.getItems().addAll(attendance);
				employeeCode.setText(salary.getCode());
				employeeName.setText(salary.getName());
				double hours=0;
				for(int i=0;i<attendance.size();i++)
				{
					hours+=attendance.get(i).getHours();
				}
				employeeHours.setText(String.valueOf(hours));
				employeeValue.setText(String.valueOf(salary.getHourValue()));
				employeeSalary.setText(String.valueOf(hours*salary.getHourValue()));
				infoContainer.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void exit()
	{
		infoContainer.setVisible(false);
	}
}
