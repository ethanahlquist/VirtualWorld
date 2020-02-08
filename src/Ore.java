import java.util.List;
import processing.core.PImage;
import java.util.Random;

public class Ore extends KineticEntity
{
    
   
    private static final Random rand = new Random();


    public static final String ORE_KEY = "ore";
    public static final int ORE_NUM_PROPERTIES = 5;
    public static final int ORE_ID = 1;
    public static final int ORE_COL = 2;
    public static final int ORE_ROW = 3;
    public static final int ORE_ACTION_PERIOD = 4;

    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;
    public static final int ORE_REACH = 1;


    public Ore(String id, Point position, List<PImage> images, int actionPeriod)
    {

        super(id, position, images, 0, 0, actionPeriod);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
      	Point pos = this.getposition();  

      	world.removeEntity(this);
      	scheduler.unscheduleAllEvents(this);

      		Entity blob = pos.createOreBlob(this.getid() + OreBlob.BLOB_ID_SUFFIX,
         			this.getactionPeriod() / OreBlob.BLOB_PERIOD_SCALE,
         			OreBlob.BLOB_ANIMATION_MIN + rand.nextInt(OreBlob.BLOB_ANIMATION_MAX - OreBlob.BLOB_ANIMATION_MIN),
         			imageStore.getImageList(OreBlob.BLOB_KEY));


      	world.addEntity(blob);
      	((KineticEntity)blob).scheduleActions(scheduler, world, imageStore);
    }
    
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}
