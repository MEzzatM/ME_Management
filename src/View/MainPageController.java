package View;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import Classes.Shift;
import DataBase.DBConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainPageController  implements Initializable{


	@FXML
	private BorderPane mainContainer;
	//side bar labels
	@FXML
	private Label logo,salesProcess,storage,products,employees,control,signOut,about;
	//main container pages
	@FXML
	private AnchorPane logoPage,aboutPage;
	
	@FXML
	private AnchorPane salesProcessPage;
		//labels
		@FXML
		private Label sell,return1;
		//image view
		@FXML
		private ImageView sellingImage,returnImage;
		
	@FXML
	private AnchorPane storagePage;
		//labels
		@FXML
		private Label addLabel,endSoonLabel;
		//image view
		@FXML
		private ImageView addImage,endSoonImage;
		
	@FXML
	private AnchorPane productsPage;
		//labels
		@FXML
		private Label newLabel,updateLabel,searchLabel;
		//image view
		@FXML
		private ImageView newImage,updateImage,searchImage;
		
	@FXML
	private AnchorPane employeesPage;
		//labels
		@FXML
		private Label employeesLabel,newEmployeeLabel,attendanceLabel,updateEmployeeLabel;
		//image view
		@FXML
		private ImageView employeesImage,newEmployeeImage,attendanceImage,updateEmployeeImage;
		
	@FXML
	private AnchorPane controlPage;
		//labels
		@FXML
		private Label newUserLabel,usersLabel,slariesLabel,shiftsLabel;
		//image view
		@FXML
		private ImageView newUserImage,usersImage,salariesImage,shiftsImage;
		
	@FXML
	private AnchorPane signOutPage;
		//labels
		@FXML
		private Label signOutLabel;
		//image view
		@FXML
		private ImageView signOutImage;
		
		
		
		List<String> containers;
		List<AnchorPane> pages;
		List<Label> labels;
		List<ImageView> images; 
		boolean[] isVisible=new boolean[15];
		private DBConnection connection=new DBConnection();
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		containers=new ArrayList<>();		//add containers to a List
			containers.add("selling");containers.add("returning");
			containers.add("add");containers.add("endingSoon");
			containers.add("newProduct");containers.add("updateProduct");containers.add("products");
			containers.add("employees");containers.add("newEmployee");containers.add("attendance");containers.add("updateEmployee");
			containers.add("newUser");containers.add("users");containers.add("salaries");containers.add("shifts");
			
		images=new ArrayList<>();			//add images to a List (index of image container) = (index of image ) 
			images.add(sellingImage);images.add(returnImage);
			images.add(addImage);images.add(endSoonImage);
			images.add(newImage);images.add(updateImage);images.add(searchImage);
			images.add(employeesImage);images.add(newEmployeeImage);images.add(attendanceImage);images.add(updateEmployeeImage);
			images.add(newUserImage);images.add(usersImage);images.add(salariesImage);images.add(shiftsImage);
		
		pages=new ArrayList<>();	//add Pages to a List
			pages.add(logoPage);
			pages.add(salesProcessPage);pages.add(storagePage);pages.add(productsPage);
			pages.add(employeesPage);pages.add(controlPage);pages.add(signOutPage);pages.add(aboutPage);
			
		labels=new ArrayList<>();	//add labels to a List
			labels.add(logo);
			labels.add(salesProcess);labels.add(storage);labels.add(products);
			labels.add(employees);labels.add(control);labels.add(signOut);labels.add(about);
			
		showPage(logoPage);	
		
		/* program is consist of 16 Containers ( mainContainer --> contain images  + selling --> manage selling process +...etc) 
		 * logic of open and close containers
		 * I made List of Strings every String is equal to container name
		 * and made list of Images every Image index is the same with its container 
		 * when click image --> get its index in Image's List and Open the Container which is in the same Index of the Image
		 * How to OpenContainer --> 
		 * after get Index of ContainerName to Open --> Open fxml file with name =ContainerName+".fxml"
		 */
	}
	

	@FXML
	protected void selectPage(MouseEvent event) 
	{
		Label label=(Label) event.getSource();
		for(int i=0;i<labels.size();i++)
		{
			if(label==labels.get(i)) 
			{
				if(i==0)		//if click ME logo
				{
					colorSideBarLabel(null); 	//re-color all labels
				}
				else
				{
					colorSideBarLabel(labels.get(i)); 		//color the selected label and re color all other labels to default color
				}
				showPage(pages.get(i));				//show selected page	
			}
		}
	}
	
	@FXML
	protected void enterImage(MouseEvent event)		//when mouse enter any image --> increase size of image
	{
		ImageView image = (ImageView) event.getSource();	//get selected ImageView
		image.setLayoutX(image.getLayoutX()-10);		//current layoutX-10
		image.setLayoutY(image.getLayoutY()-10);		//current layoutY-10
		image.setFitWidth(image.getFitWidth()+20);		//current Width+20
		image.setFitHeight(image.getFitHeight()+20);	//current Height + 20
	}
	
	@FXML
	protected void exitImage(MouseEvent event)		//when mouse exit from the image
	{
		ImageView image = (ImageView) event.getSource();	//get selected ImageView
		image.setLayoutX(image.getLayoutX()+10);		//current layoutX + 10
		image.setLayoutY(image.getLayoutY()+10);		//current layoutY + 10
		image.setFitWidth(image.getFitWidth()-20);		//current Width - 20
		image.setFitHeight(image.getFitHeight()-20);	//current Height - 20
		
	}

	@FXML
	protected void clickImage(MouseEvent event) throws SQLException
	{
		ImageView image = (ImageView) event.getSource();	//get selected ImageView
		
		if(image==signOutImage)	//if signOut
		{
			Stage currentStage = (Stage) signOutImage.getScene().getWindow();
			currentStage.setFullScreen(false);
			Shift shift=connection.getCurrentShift();
			String message="User Code: "+shift.getUserCode()+
					"\nUser Name: "+shift.getUsername()+
					"\n\nTotal Revenue: "+String.valueOf(shift.getTotalIncome())+
					"\n\nStart Time: "+shift.getStart()+
					"\nEnd Time: "+shift.getEnd()+"";

			  int a=JOptionPane.showConfirmDialog(null,message+"\n \n End Shift Now ? ");  	//ask if user really want to signOut 
			  if(a==JOptionPane.YES_OPTION)			//if click yes
			   {  
				    connection.signOut();	//close current Shift
				    
					JOptionPane.showMessageDialog(null, "Shift Ended Successfully ^_^\n"+message);	//show a message
					
					//close current stage
					currentStage = (Stage) signOutImage.getScene().getWindow();
					currentStage.close();

					//open signIn Page
					Parent root = null;
					try 
					{
						root = FXMLLoader.load(getClass().getResource("signIn.fxml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					Stage stage=new Stage();
					Scene scene =new Scene(root);
					stage.setScene(scene);
					stage.setTitle("ME Management System");
					stage.setOnCloseRequest( ev -> {Platform.exit();});
					stage.show();	
			  }  
			
		}
		else
		{
			for(int i=0;i<images.size();i++)
			{
				if(image==images.get(i))
				{
					if(isVisible[i])
					{ 
							JOptionPane.showMessageDialog(null," This Page is already shown");
					}
					else
					{
						if(containers.get(i).equals("newEmployee")||containers.get(i).equals("updateEmployee")
								||containers.get(i).equals("users")||containers.get(i).equals("newUser")
								||containers.get(i).equals("salaries")||containers.get(i).equals("shifts"))
						{
							JPasswordField field = new JPasswordField();
							int frame = JOptionPane.showConfirmDialog(null, field, "Owner Only Can Access To: "+containers.get(i)+"", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (frame == JOptionPane.OK_OPTION) 
							{
							  String ownerPassword = new String(field.getPassword());
								//String ownerPassword=JOptionPane.showInputDialog(null,"Owner Only Can Access To: "+containers.get(i));
								if(connection.isTheOwner(ownerPassword))
								{
									newStage(containers.get(i));				//open fxml file (index of image in image list == index of file name in container list)
									isVisible[i]=true;
								}
								else
								{
									JOptionPane.showMessageDialog(null, "Invalid Password!");
								}
							}
							
							
						}
						else
						{
							newStage(containers.get(i));				//open fxml file (index of image in image list == index of file name in container list)
							isVisible[i]=true;
						}
						
					}
					break;
				}
			}
		}				
	}
	
	
	public void colorSideBarLabel(Label label)
	{	
		for(int i=0;i<labels.size();i++)
		{
			if(label!=labels.get(i))		//color label to default color
			{
				labels.get(i).setStyle("-fx-background-color:   #7a48dd;");
			}  
			else 					//color the selected label to select color
			{
				labels.get(i).setStyle("-fx-background-color:  #0cb4cc;");
			}		
		}
	}
	
	public void showPage(AnchorPane page)
	{
		for(int i=0;i<pages.size();i++)
		{
			if(page!=pages.get(i))
			{
				pages.get(i).setVisible(false);
			}
			else 
			{
				pages.get(i).setVisible(true);
			}
		}
	}
	
	public void newStage(String name)
	{
		Parent root = null;
		try 
		{
			root = FXMLLoader.load(getClass().getResource(name+".fxml"));
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
		stage.setOnCloseRequest( ev -> {nonVisible(name);});
		stage.show();
	}
	
	public void nonVisible(String name)
	{
		for(int i=0;i<containers.size();i++)
		{
			if(name.equals(containers.get(i)))
			{
				isVisible[i]=false;
			}
		}
	}
	}
