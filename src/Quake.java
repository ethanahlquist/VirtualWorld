import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class Quake extends AnimatedEntity
{
    
   
    public static final String QUAKE_ID = "quake";
    public static final String QUAKE_KEY = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) 
    {
             
      super(id, position, images, 0, 0, actionPeriod, animationPeriod, QUAKE_ANIMATION_REPEAT_COUNT);
    }

   

  public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
  {
  	scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
  }

    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }
  
 }
