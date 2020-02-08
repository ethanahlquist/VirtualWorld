import java.util.LinkedList;
import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class OreBlob extends Position
{	

    
    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;

    public OreBlob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) 
    {
      super(id, position, images, 0, 0, actionPeriod, animationPeriod, 0);
    }

    
    
        public boolean Condition(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target)
    {
      return (target.isPresent());
    }

    public void SubCode1(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target, long nextPeriod)
    {
      Point tgtPos = target.get().getposition();

      if(this.moveTo(world, target.get(), scheduler))
      {
        Entity quake = tgtPos.createQuake(imageStore.getImageList(Quake.QUAKE_KEY));

        world.addEntity(quake);
        nextPeriod += this.getactionPeriod();
        ((KineticEntity)quake).scheduleActions(scheduler, world, imageStore);
      }
    }

    public void SubCode2(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {}
    
    public void SubCode3(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
      scheduler.scheduleEvent(this,
        Activity.createActivityAction(this, world, imageStore),
        this.getactionPeriod());
    }

    
    public boolean checkInstance(Entity entity) {  return entity.accept(new VeinVisitor()); }

    
    public void LocationFunc(WorldModel world, Entity target, EventScheduler scheduler) 
    { 
      world.removeEntity(target);
      scheduler.unscheduleAllEvents(target);
    }


    public boolean CheckOccupancy(WorldModel world, Point destPos)
    {
      return (world.isOccupied(destPos) && !(world.getOccupant(destPos).get() instanceof Ore));//.accept(new MinerFullVisitor()))); // instanceof Ore.
    }
    
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}
