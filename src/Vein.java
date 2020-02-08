import java.util.List;
import processing.core.PImage;
import java.util.Random;
import java.util.Optional;

public class Vein extends KineticEntity
{
   public static final String VEIN_KEY = "vein";
   public static final int VEIN_NUM_PROPERTIES = 5;
   public static final int VEIN_ID = 1;
   public static final int VEIN_COL = 2;
   public static final int VEIN_ROW = 3;
   public static final int VEIN_ACTION_PERIOD = 4;

   private static final Random rand = new Random();

    public Vein(String id, Point position, List<PImage> images, int actionPeriod)
    {
        
      super(id, position, images, 0, 0, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
      	Optional<Point> openPt = world.findOpenAround(this.getposition());

      	if (openPt.isPresent())
      	{
         	Entity ore = openPt.get().createOre(Ore.ORE_ID_PREFIX + this.getid(), //openPt.get
         				Ore.ORE_CORRUPT_MIN + rand.nextInt(Ore.ORE_CORRUPT_MAX - Ore.ORE_CORRUPT_MIN),
            			imageStore.getImageList(Ore.ORE_KEY));
         world.addEntity(ore);
         ((KineticEntity)ore).scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
         Activity.createActivityAction(this, world, imageStore),
         this.getactionPeriod());
    }
    
     public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
     }

}
