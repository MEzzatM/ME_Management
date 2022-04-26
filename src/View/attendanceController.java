package View;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Classes.Attendance;
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

public class attendanceController implements Initializable{

	@FXML
	private TextField code,name,type,day,time;
	@FXML
	private Label error;
	@FXML
	private Button save;
	private Employee employee=null;
	private DBConnection connection=new DBConnection();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error.setVisible(false);	
	}

	@FXML
	protected void codeEnter(KeyEvent event) throws SQLException
	{
		if(event.getCode()==KeyCode.ENTER)		//try to get product 
		{
			enter();
		}
	}
	
	@FXML
	protected void enter() throws SQLException
	{
		employee=null;
		clear();
		if(!code.getText().isBlank())
		{
			employee=connection.getEmployee(code.getText());
			if(employee!=null)
			{
				 error.setVisible(false);
				 DateTimeFormatter timeFormate = DateTimeFormatter.ofPattern("HH:mm");
		         LocalDateTime now = LocalDateTime.now();
		         String currentTime=timeFormate.format(now);
		         
		         Date date=new Date();
		         String currentDay = new SimpleDateFormat("EEEE").format(date);
		         
				if(connection.hasOpenedDay(employee.getCode()))
				{
					Attendance attendance=connection.getLastAttendance(employee.getCode());
					if(calculateHours(attendance.getAttendance(),currentTime)>18)
					{
						boolean saved=false;
						try {
							Stage stage = (Stage) code.getScene().getWindow();
							stage.setAlwaysOnTop(false);
								String manualHours=JOptionPane.showInputDialog(null,"you Forget to set Departure Last Day"
										+ "\n Enter How Many Hours You Worked Last Day");
								  stage.setAlwaysOnTop(true);
								attendance.setDeparture("manually");
								attendance.setHours(Double.parseDouble(manualHours));
								connection.employeeDeparture(employee.getCode(),attendance);
								saved=true;
						}catch(Exception e)
						{
							
						}
						if(saved)
						{
							Stage stage = (Stage) code.getScene().getWindow();
							stage.setAlwaysOnTop(false);
							JOptionPane.showMessageDialog(null,"Save Last day hours successfully \n"
									+ "You Can Enter today Attendance now ^_^");
							stage.setAlwaysOnTop(true);
							name.setText(employee.getName());
							type.setText("Attendance");
							day.setText(currentDay);
							time.setText(currentTime);
						}
						else
						{
							error.setVisible(true);
						}
						
					}
					else
					{
						name.setText(employee.getName());
						type.setText("Departure");
						day.setText(currentDay);
						time.setText(currentTime);
					}
						
				}
				else
				{
					name.setText(employee.getName());
					type.setText("Attendance");
					day.setText(currentDay);
					time.setText(currentTime);
				}
				save.requestFocus();
			}
			else
			{
				error.setVisible(true);
			}
			
		}
		else
		{
			error.setVisible(true);
		}
	}
	
	@FXML
	protected void save() throws SQLException
	{
		if(employee!=null)
		{
			 DateTimeFormatter timeFormate = DateTimeFormatter.ofPattern("HH:mm");
	         LocalDateTime now = LocalDateTime.now();
	         String currentTime=timeFormate.format(now);
	         
	         Date date=new Date();
	         String currentDay = new SimpleDateFormat("EEEE").format(date);
	         
			 error.setVisible(false);
				if(type.getText().equals("Departure"))
				{
					Attendance attendance=connection.getLastAttendance(employee.getCode());
					attendance.setDeparture(currentTime);
					attendance.setHours(calculateHours(attendance.getAttendance(),attendance.getDeparture()));
					
					connection.employeeDeparture(employee.getCode(),attendance);
				}
				else
				{
					connection.employeeAttendance(employee.getCode(),new Attendance(currentDay,currentTime,"null",0));
				}
				Stage stage = (Stage) code.getScene().getWindow();
				stage.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null,"Saved Successfully");
				stage.setAlwaysOnTop(true);
				clear();
		}
		else
		{
			error.setVisible(true);
		}
	}
	
	public double calculateHours(String attendance,String deparature)
	{
		double attendanceHour=Double.parseDouble(attendance.split(":")[0]);
		double attendanceMin=Double.parseDouble(attendance.split(":")[1]);
		
		double deparatureHour=Double.parseDouble(deparature.split(":")[0]);
		double deparatureMin=Double.parseDouble(deparature.split(":")[1]);
		
		double diffInMin=(deparatureHour*60+deparatureMin)-(attendanceHour*60+attendanceMin);
		
		if(diffInMin>=0)
		{
			return diffInMin/60;
		}
		else
		{
			return ((24)+(diffInMin/60));
		}
	}
	
	public void clear()
	{
		name.setText("");
		type.setText("");
		day.setText("");
		time.setText("");
		code.requestFocus();
	}
}
