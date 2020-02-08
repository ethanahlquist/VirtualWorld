import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import processing.core.PImage;
import java.util.Random;

public class FoolsGold extends KineticEntity
{
    
   
    private static final Random rand = new Random();


    public static final String FOOLS_KEY = "foolsgold";
    public static final int FOOLS_NUM_PROPERTIES = 5;
    public static final int FOOLS_ID = 1;
    public static final int FOOLS_COL = 2;
    public static final int FOOLS_ROW = 3;
    public static final int FOOLS_ACTION_PERIOD = 4;

    public static final String FOOLS_ID_PREFIX = "fools -- ";
    public static final int FOOLS_CORRUPT_MIN = 40000;
    public static final int FOOLS_CORRUPT_MAX = 60000;
    public static final int FOOLS_REACH = 1;


    public FoolsGold(String id, Point position, List<PImage> images, int actionPeriod)
    {

        super(id, position, images, 0, 0, actionPeriod);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
    Optional<Point> openPt = world.findOpenAround(this.getposition());

      	if (openPt.isPresent())
      	{
            String NewLine;
            Point Location = openPt.get();
            
            List<Point> TotalPointList = Location.diagonals();
                List<Point> subList = new ArrayList<>();
                int count =0;
            
                    for(int i = 0; i < 4; i++){
                        subList = TotalPointList.get(i).BlockCicle();
                        for(Point P : subList)
                            if(!TotalPointList.contains(P))
                                TotalPointList.add(P);
                    }    
                
                int Dist;
                
                for(Point p : TotalPointList){
                    
                    Dist = p.distanceSquared(Location);
                    
                    if(Dist <= 4){
                        NewLine = ("background rocks " + p.getX() + " " + p.getY() );
                        world.processLine(NewLine, imageStore);
                    }
                    else if(Dist < 4 && Dist >=3){
                        world.removeEntityAt(p);
                        NewLine = ("obstacle obstacle_" + p.getX() + "_" + p.getY() + " " + p.getX() + " " + p.getY());
                        world.processLine(NewLine, imageStore);
                    }
                    //else if(Dist < 9 && Dist >=5){
                    //    NewLine = ("background rocks " + p.getX() + " " + p.getY() );
                    //    world.processLine(NewLine, imageStore);
                   // }
                }
                
              
                Shrine entity = new Shrine(String.format("shrine_%s_%s", Location.getX(), Location.getY()), Location, imageStore.getImageList("shrine"), 0, 0, 0, 0, 100);
                
                //new Shrine(id, this, images, 0, resourceLimit, actionPeriod, animationPeriod, 100);
            
                world.addEntity(entity);
                ((KineticEntity)entity).scheduleActions(scheduler, world, imageStore);
                scheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), entity.getactionPeriod());
            
                
      }

      scheduler.scheduleEvent(this,
         Activity.createActivityAction(this, world, imageStore),
         this.getactionPeriod());
    }
    
    public <R> R accept(EntityVisitor<R> visitor){
        return visitor.visit(this);             
    }

}