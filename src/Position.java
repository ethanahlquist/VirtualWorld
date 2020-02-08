import java.util.Optional;
import java.util.LinkedList;
import processing.core.PImage;
import java.util.function.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Position extends AnimatedEntity
{       
    
    private List<Point> Path;
    private int PathCount;
    
	public Position(String id, Point position,
		List<PImage> images, int resourceLimit, 
		int resourceCount, int actionPeriod, 
              int animationPeriod, int repeatCount) 
	{ 
		super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, repeatCount);
                this.Path = new ArrayList<>();
                this.PathCount = 0;
	}

  
    public Point nextPosition(WorldModel world, Point destPos)
    {   
       
       if(Path.isEmpty() || PathCount >= Path.size()-1 || CheckOccupancy(world, Path.get(PathCount +1))){
            //(this.accept(new EvilVisitor()) == false)
            
            Predicate<Point> canPassThrough;
            if (this.accept(new EvilVisitor()))
                canPassThrough = (Point N) -> world.withinBounds(N);
            else
                canPassThrough = (Point N) -> world.withinBounds(N) && !CheckOccupancy(world, N);
            
            BiPredicate<Point, Point> withinReach = (Point P, Point N) -> P.adjacent(N);
            
            AStarStrategy A_star = new AStarStrategy(getposition(), destPos, 
                    canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS, world);
        
            Path = A_star.computePath();  
            
            //if(this.getid().contains("miner_12_23") ){                                                 
            //    Path = A_star.computePath(); 
            //}
            
            //System.out.printf("%s: %s \n", "Path of Entity ", getid());
                                                            
            if(Path.isEmpty()) return getposition();
            
            //if(this.getid().contains("miner_12_23") ){                                                 
            //    System.out.printf("%s: %s \n", "Path of Entity ", Path);
                
            //}
            
            PathCount = 0;
        
            } else { PathCount += 1; }
        
        //Path.get(PathCount);
        
        //System.out.printf("%s \n",PathCount);

        return Path.get(PathCount);
    }
    
    
    public Point nextPositionSSPG(WorldModel world, Point destPos) 
    {
        
        Predicate<Point> canPassThrough = (Point N) -> world.withinBounds(N) && !CheckOccupancy(world, N);
        BiPredicate<Point, Point> withinReach = (Point P, Point N) -> P.adjacent(N);
        
        SingleStepPathingStrategy SSPS = new SingleStepPathingStrategy(getposition(), destPos, 
                canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        
        List<Point> newPosList = SSPS.computePath();
               
        
        Point newPos = newPosList.get(0); 

        return newPos;
    }




   	public Optional<Entity> findnearest(WorldModel world, Point pos, Entity e)
   	{
      	List<Entity> ofType = new LinkedList<>();
      	for (Entity entity : world.getentities())
      	{
         	if (checkInstance(entity))
         	{
            	ofType.add(entity);
         	}
      	}
      	return nearestEntity(ofType, pos);
   	}
        
        

   	public Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
   	{
      	if (entities.isEmpty())
      	{
         	return Optional.empty();
      	}
      	else
      	{
         	Entity nearest = entities.get(0);
         	int nearestDistance = nearest.getposition().distanceSquared(pos);

         	for (Entity other : entities)
         	{
            	int otherDistance = other.getposition().distanceSquared(pos);

            	if (otherDistance < nearestDistance)
            	{
               	nearest = other;
               	nearestDistance = otherDistance;
            	}
         	}

         	return Optional.of(nearest);
      	}
   	}
    
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (this.getposition().adjacent(target.getposition()))
        {
        	LocationFunc(world, target, scheduler);
          return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getposition());

            if (!getposition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity((Entity) this, nextPos);
            }
            return false;
        }
      }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> Target = this.findnearest(world, this.getposition(), this);
        long nextPeriod = this.getactionPeriod();

        if (Condition(world,imageStore,scheduler,Target))
        {
          SubCode1(world, imageStore,scheduler,Target, nextPeriod);
        }
        else
        {
          SubCode2(world,imageStore,scheduler);
        }
        SubCode3(world,imageStore,scheduler);
    }


    protected abstract boolean checkInstance(Entity entity);
    protected abstract void LocationFunc(WorldModel world, Entity target, EventScheduler scheduler);
    protected abstract boolean CheckOccupancy(WorldModel world, Point destPos);

    protected abstract boolean Condition(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target);
    protected abstract void SubCode1(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Optional<Entity> target, long nextPeriod);
    protected abstract void SubCode2(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    protected abstract void SubCode3(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
