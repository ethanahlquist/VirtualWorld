import java.util.LinkedList;
import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class MinerFull extends AbstractMiner// implements Visitable
{
    public static final String MINER_KEY = "miner";
    public static final int MINER_NUM_PROPERTIES = 7;
    public static final int MINER_ID = 1;
    public static final int MINER_COL = 2;
    public static final int MINER_ROW = 3;
    public static final int MINER_LIMIT = 4;
    public static final int MINER_ACTION_PERIOD = 5;
    public static final int MINER_ANIMATION_PERIOD = 6;

    public MinerFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
      super(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod, 0);
    }

    

    public boolean Condition(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target)
    {
      return (target.isPresent() && this.moveTo(world, target.get(), scheduler));
    }
    

    public void SubCode1(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target, long nextPeriod)
    {
      transform(world,scheduler,imageStore); 
    }

    public void SubCode2(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
      scheduler.scheduleEvent(this,
        Activity.createActivityAction(this, world, imageStore),
        this.getactionPeriod());
    }

    public void SubCode3(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {}


    public boolean checkInstance(Entity entity) { return entity.accept(new SmithVisitor());}


    public void LocationFunc(WorldModel world, Entity target, EventScheduler scheduler) {}


    public boolean CheckOccupancy(WorldModel world, Point destPos)
    {
    return world.isOccupied(destPos);
    }



    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
      transform(world,scheduler,imageStore);
    }
    
    public Entity transformEntity(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Entity miner = this.getposition().createMinerNotFull(this.getid(), this.getresourceLimit(),
          	this.getactionPeriod(), this.getanimationPeriod(),
          	this.getimages());
        return miner;
    }
    
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }
}

