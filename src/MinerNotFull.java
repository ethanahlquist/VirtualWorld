import java.util.LinkedList;
import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class MinerNotFull extends AbstractMiner
{
    private Entity target;
    public MinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) 
    {

      super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, 0);
    }


    
    public boolean Condition(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target)
    {
      return (!target.isPresent() || 
              !this.moveTo(world, target.get(), scheduler) ||
              !this.transformNotFull(world,scheduler,imageStore));
    }


    public void SubCode1(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target, long nextPeriod)
    {
      scheduler.scheduleEvent(this,
        Activity.createActivityAction(this, world, imageStore),
        this.getactionPeriod());
    }

    public void SubCode2(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {}
    
    public void SubCode3(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {}

   
      
   

    public boolean checkInstance(Entity entity) {  return entity.accept(new OreVisitor()) || entity.accept(new GoldVisitor()); }

   	    

    
    public void LocationFunc(WorldModel world, Entity target, EventScheduler scheduler) 
    { 
      this.target = target;
      
      setresourceCount();
        
      world.removeEntity(target);
      scheduler.unscheduleAllEvents(target);
    }

        
    

  public boolean CheckOccupancy(WorldModel world, Point destPos)
  {
    return world.isOccupied(destPos);
  }

   
   public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.getresourceCount() >= this.getresourceLimit())
      {
        transform(world,scheduler,imageStore);
        return true;
      }
    return false;
   }
   
   public Entity transformEntity(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
       if (target.accept(new GoldVisitor())){
            EvilMiner miner = this.getposition().createEvilMiner(this.getid(), this.getresourceLimit(),
                this.getactionPeriod()/2, this.getanimationPeriod(),
                imageStore.getImageList("evilminer"));
        return miner;
       } else {
           MinerFull miner = this.getposition().createMinerFull(this.getid(), this.getresourceLimit(),
                this.getactionPeriod(), this.getanimationPeriod(),
                this.getimages());
           return miner;
           //Shrine entity = new Shrine(String.format("shrine_%s_%s", Location.getX(), Location.getY()), Location, imageStore.getImageList("shrine"), 0, 0, 0, 0, 100);
       }
    }
    
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }
   
}
