import java.util.List;
import processing.core.PImage;

public class Obstacle extends Entity
{

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;


	public Obstacle(String id, Point position, List<PImage> images){

    	super(id, position, images, 0, 0);

	}
        
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}
