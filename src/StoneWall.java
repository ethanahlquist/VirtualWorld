import java.util.List;
import processing.core.PImage;

public class StoneWall extends Entity{
    public static final String STONEWALL_KEY = "stonewall";
    public static final int STONEWALL_NUM_PROPERTIES = 4;
    public static final int STONEWALL_ID = 1;
    public static final int STONEWALL_COL = 2;
    public static final int STONEWALL_ROW = 3;


	public StoneWall(String id, Point position, List<PImage> images){

    	super(id, position, images, 0, 0);

	}
        
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}
