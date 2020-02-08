import processing.core.PImage;
import java.util.List;

public abstract class AbstractMiner extends Position
{
	public AbstractMiner(String id, Point position,
							List<PImage> images, int resourceLimit, 
							int resourceCount, int actionPeriod, 
							int animationPeriod, int repeatCount) 
	{
		super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod,0);
	}

   	public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
   	{
            
      	Entity miner = transformEntity(world, scheduler, imageStore);       

      	world.removeEntity(this);
      	scheduler.unscheduleAllEvents(this);

      	world.addEntity(miner);
      	((KineticEntity)miner).scheduleActions(scheduler, world, imageStore);
   	}
        
        protected abstract Entity transformEntity(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
       
}
