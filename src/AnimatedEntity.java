
import processing.core.PImage;
import java.util.List;

public abstract class AnimatedEntity extends KineticEntity
{
	private int repeatCount;
	private int animationPeriod;

	public AnimatedEntity(String id, Point position, List<PImage> images, int resourceLimit, 
                                int resourceCount, int actionPeriod, int animationPeriod, int repeatCount)
	{ 
		super(id, position, images, resourceLimit, resourceCount, actionPeriod);
		this.repeatCount = repeatCount; 
		this.animationPeriod = animationPeriod;
	}

  	public int getanimationPeriod() { return animationPeriod; }

  	public void nextImage() { setImageIndex((getImageIndex() + 1) % getimages().size()); }

   	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent((Entity)this,
           Activity.createActivityAction((Entity)this, world, imageStore),
           this.getactionPeriod());
        scheduler.scheduleEvent((Entity)this, Animation.createAnimationAction(this, repeatCount), this.getanimationPeriod());

    }
}
