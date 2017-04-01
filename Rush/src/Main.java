import api.Solver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
   @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("app/view/MainView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();              
    }

    public static void main(String[] args) {
       // launch(args);

    	String map = "--11--" +
			         "-222A-" +
			         ">>-BA-" +
			         "---B--" +
			         "---B--" +
			         "--11--";
    	
    	Solver solver = new Solver(map,6,6, true);
    	solver.applyAlgorithm();
	}
}