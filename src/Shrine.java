import java.util.List;
import java.util.Optional;
import java.util.Random;
import processing.core.PImage;

public class Shrine extends AnimatedEntity
{

    public static final String SHRINE_KEY = "shrine";
    public static final int SHRINE_NUM_PROPERTIES = 7;
    public static final int SHRINE_ID = 1;
    public static final int SHRINE_COL = 2;
    public static final int SHRINE_ROW = 3;
    public static final int SHRINE_LIMIT = 4;
    public static final int SHRINE_ACTION_PERIOD = 5;
    public static final int SHRINE_ANIMATION_PERIOD = 6;
    
    //public static final int SHRINE_ACTION_PERIOD = 1100;
    //public static final int SHRINE_ANIMATION_PERIOD = 100;
    public static final int SHRINE_ANIMATION_REPEAT_COUNT = 10;
    
    private static final Random rand = new Random();


	public Shrine(String id, Point position, List<PImage> images, int resourceLimit, 
                                int resourceCount, int actionPeriod, int animationPeriod, int repeatCount) {

    	super(id, position, images, 0, 0, actionPeriod, animationPeriod, SHRINE_ANIMATION_REPEAT_COUNT);
        
	}
        
        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
        {
            Optional<Point> openPt = world.findOpenAround(this.getposition());

            if (openPt.isPresent())
            {
         	Entity foolsgold = openPt.get().createFoolsGold(FoolsGold.FOOLS_ID_PREFIX + this.getid(), //openPt.get
         				FoolsGold.FOOLS_CORRUPT_MIN + rand.nextInt(FoolsGold.FOOLS_CORRUPT_MAX - FoolsGold.FOOLS_CORRUPT_MIN),
            			imageStore.getImageList(FoolsGold.FOOLS_KEY));
            world.addEntity(foolsgold);
            ((KineticEntity)foolsgold).scheduleActions(scheduler, world, imageStore);
            }

            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getactionPeriod());
        }
        
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}