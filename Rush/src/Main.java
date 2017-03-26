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
    	
        // the transcription of the 93 moves, total 24132 configurations problem
        // from http://cs.ulb.ac.be/~fservais/rushhour/index.php?window_size=20&offset=0
    	
       /* String map = "333BCC" +
                       "B22BCC" +
                       "B-##CC" +
                       "22B---" +
                       "-BB-22" +
                       "-B2222";
    	*/
    	
    	/* String map = "333A-B" +
			            "A22AAB" +
			            "A.##AB" +
			            "22A---" +
			            "-AA-22" +
			            "-A2222";*/

    	String map = "-22--B" +
			         "333A-B" +
			         "##BAAB" +
			         "--B-A-" +
			         "--B---" +
			         "-22---";
    	
    	Solver solver = new Solver(map,6,6, true);
    	solver.applyAlgorithm();
	}
}