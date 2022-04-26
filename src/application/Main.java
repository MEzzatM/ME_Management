package application;
	
import DataBase.DBConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


		

public class Main extends Application {
	private DBConnection connection=new DBConnection();
	@Override
	public void start(Stage stage) {
		try {
			connection.clearAll();
			//System.out.println("alldata cleared successfully");
			String file="";
			if(!connection.isThereUser())
			{
				file="firstUse.fxml";
			}
			else
			{
				file="signIn.fxml";
			}
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/"+file));
	        Scene scene = new Scene(fxmlLoader.load());
	        Rectangle2D screenBounds = Screen.getPrimary().getBounds();		//set stage size the same of screen size
			stage.setWidth(screenBounds.getWidth()+12);
			stage.setHeight(screenBounds.getHeight()+6);
	        stage.setTitle("ME Management System");
	        stage.setScene(scene);
	        stage.setOnCloseRequest( ev -> {Platform.exit();});
	        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
