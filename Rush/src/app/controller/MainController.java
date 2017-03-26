package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.geometry.*;

public class MainController {
	
	final int maxRow	= 34;
	final int maxColumn = maxRow;
	final double boxLen  = 15;
	final int totalSize = 510;
	//34*15 = 510 
	
	double[] regionXY = {0,0};
	
	@FXML private SplitPane mainSpitter;
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane mainSpitter_pane1;
    @FXML private AnchorPane mainSpitter_pane2;
    @FXML private VBox defaultRegion;

    boolean swap = false;
    
//////////////////////////////////////////////////// JAVAFX EVENTS        
    @FXML
    public void initialize() {
    	   	
        for( int i=0; i <maxRow; i++) {
        	regionXY[0] = 0;
            for( int j=0; j<maxColumn; j++) {          	
          	   Rectangle r = new Rectangle();

               r.setX(regionXY[0]);
               r.setY(regionXY[1]);
               r.setWidth(boxLen);
               r.setHeight(boxLen);
               
               if(swap){
            	   r.setFill(Color.CHOCOLATE);
            	   swap = false;
               }
               else{
            	   swap = true;
            	   r.setFill(Color.AQUAMARINE);
               }
               this.mainSpitter_pane1.getChildren().add(r);
               regionXY[0] = regionXY[0] + boxLen;
            }
            
            if(swap)
            	swap = false;
            else
            	swap = true;

        	regionXY[1] = regionXY[1] + boxLen;
        }

    }
      
}
