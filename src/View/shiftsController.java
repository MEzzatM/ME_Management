package View;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


import Classes.Shift;
import Classes.Process;

import DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class shiftsController implements Initializable{

	@FXML
	private TextField code;
	@FXML
	private Label error;
	@FXML 
	private TableView<Shift> table;
	@FXML 
	private TableColumn<Shift, String> tableCode;
	@FXML 
	private TableColumn<Shift, String> tableUserCode;
	@FXML 
	private TableColumn<Shift, String> tableUserName;
	@FXML 
	private TableColumn<Shift, Double> tableRevenue;
	@FXML 
	private TableColumn<Shift, Double> tableProfit;
	@FXML 
	private TableColumn<Shift, String> tableStart;
	@FXML 
	private TableColumn<Shift, String> tableEnd;
	
	/////////////////////////////////////////
	//shift info content
	@FXML
	private AnchorPane shiftInfoContainer;
	@FXML
	private TextField shiftCode,shiftUserCode,shiftUserName,shiftStart,shiftEnd,totalRevenue,totalProfit;
	@FXML 
	private TableView<Process> shiftTable;
	@FXML 
	private TableColumn<Process, String> shiftTableCode;
	@FXML 
	private TableColumn<Process, Double> shiftTableTotal;
	@FXML 
	private TableColumn<Process, String> shiftTableTime;
	@FXML 
	private TableColumn<Process, String> shiftTableType;
	@FXML 
	private TableColumn<Process, String> shiftTableDelivery;

	////////////////////////////////////////
	
	private DBConnection connection=new DBConnection();
	private List<Shift> shifts;
	private Shift shift=null;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableCode.setCellValueFactory(new PropertyValueFactory<Shift, String>("shiftCode"));
		tableUserCode.setCellValueFactory(new PropertyValueFactory<Shift, String>("userCode"));
		tableUserName.setCellValueFactory(new PropertyValueFactory<Shift, String>("username"));
		tableRevenue.setCellValueFactory(new PropertyValueFactory<Shift, Double>("totalIncome"));
		tableProfit.setCellValueFactory(new PropertyValueFactory<Shift, Double>("totalProfit"));
		tableStart.setCellValueFactory(new PropertyValueFactory<Shift, String>("start"));
		tableEnd.setCellValueFactory(new PropertyValueFactory<Shift, String>("end"));
		
		
		shiftTableCode.setCellValueFactory(new PropertyValueFactory<Process, String>("processCode"));
		shiftTableTotal.setCellValueFactory(new PropertyValueFactory<Process, Double>("total"));
		shiftTableTime.setCellValueFactory(new PropertyValueFactory<Process, String>("time"));
		shiftTableType.setCellValueFactory(new PropertyValueFactory<Process, String>("type"));
		shiftTableDelivery.setCellValueFactory(new PropertyValueFactory<Process, String>("delivery"));
		
		error.setVisible(false);
		
		shiftInfoContainer.setVisible(false);
		try {
			shifts=connection.getAllShifts();
			table.getItems().addAll(shifts);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@FXML
	protected void enter()
	{
		if(!code.getText().isBlank())
		{
			error.setVisible(false);
			String sfiftCode=code.getText();
			table.getItems().clear();
			for(int i=0;i<shifts.size();i++)
			{
				if(shifts.get(i).getShiftCode().equals(sfiftCode))
				{
					table.getItems().add(shifts.get(i));
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
			enter();
		}
	}

	@FXML
	protected void tableClick(MouseEvent event) throws SQLException
	{
		  shift=null;
          if (event.getClickCount() == 2) 
          {
        	 shift=table.getSelectionModel().getSelectedItem();
        	 if(shift!=null)
        	 {
        		 shiftInfo();
        	 }
          }
	}
	
	@FXML
	protected void shiftInfo() throws SQLException
	{
		if(shift!=null)
		{
			shiftTable.getItems().clear();
			List<Process> shiftProcess=connection.getShiftProcess(shift);
			if(shiftProcess!=null)
			{
				shiftTable.getItems().addAll(shiftProcess);
				
				shiftCode.setText(shift.getShiftCode());
				shiftUserCode.setText(shift.getUserCode());
				shiftUserName.setText(shift.getUsername());
				shiftStart.setText(shift.getStart());
				shiftEnd.setText(shift.getEnd());
				totalRevenue.setText(String.valueOf(shift.getTotalIncome()));
				totalProfit.setText(String.valueOf(shift.getTotalProfit()));
				
				shiftInfoContainer.setVisible(true);
			}
		}
	}
	
	@FXML
	protected void shiftInfoExit()
	{
		shiftInfoContainer.setVisible(false);
	}
}
