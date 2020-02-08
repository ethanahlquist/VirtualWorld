import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import processing.core.*;
import processing.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 1280;
   public static final int VIEW_HEIGHT = 960;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 1;
   public static final int WORLD_HEIGHT_SCALE = 1;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "gaia.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = .5;//20;//0.08;//

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   /*
        This method allows the user to us their mouse wheel to move the Viewport
   */
   public void mouseWheel(MouseEvent event){
       
         event.getAmount();
         int dy = (int)event.getAmount();
         view.shiftView(0, dy);
   }
   
   
   /*
        This method triggers a World Event in the location clicked by the user:
   */
    public void mousePressed() {
        int x = mouseX/32 + view.getcol();
        int y = mouseY/32 + view.getrow();
        
        Point clickLocation = new Point(x, y);
        
        // Sent to proccessLine within worldView.
        String NewLine = "";
        
        //System.out.printf("( %s)",  mouseButton);
        
        
        //  (37) is the value output to mouseButton when <leftClick>
        if(mouseButton == 3){
            //world.removeEntityAt(clickLocation);
            if(world.isOccupied(clickLocation)){return;}
                if(world.isOccupied(clickLocation)){return;}
            
            // This is a text line of the form found in gaia.sav... this is how worldView reads Entity initiation
            
            
            NewLine = ("blacksmith blacksmith_" + x + "_" + y + " " + x + " " + y);
            world.processLine(NewLine, imageStore);
        }
        
        
        //  (3) is the value output to mouseButton when <middleClick>
        else if(mouseButton == 37){
            //world.removeEntityAt(clickLocation);
            if(world.isOccupied(clickLocation)){return;}


                List<Point> TotalPointList = clickLocation.diagonals();
                List<Point> subList = new ArrayList<>();
                int count = 0;
            
                    for(int i = 0; i < 4; i++){
                        subList = TotalPointList.get(i).BlockCicle();
                        for(Point P : subList)
                            if(!TotalPointList.contains(P))
                                TotalPointList.add(P);
                    }    
                
                int Dist;
                Entity dummy;
                
                for(Point p : TotalPointList){
                    
                    Dist = p.distanceSquared(clickLocation);
                    
                    if(Dist <= 5){
                        NewLine = ("background rocks " + p.getX() + " " + p.getY() );
                        world.processLine(NewLine, imageStore);
                    }
                    if(Dist <= 5 && Dist >= 2){
                        if(world.withinBounds(p) && !p.equals(new Point(clickLocation.getX(), clickLocation.getY() + 2))){
                            dummy = world.getOccupancyCell(p);
                            world.removeEntityAt(p);
                            scheduler.unscheduleAllEvents(dummy);
                            NewLine = ("stonewall stonewall_" + p.getX() + "_" + p.getY() + " " + p.getX() + " " + p.getY());
                            world.processLine(NewLine, imageStore);
                        }
                    }
                   
                }
             
            
                    
                Shrine entity = new Shrine(String.format("shrine_%s_%s", x, y), clickLocation, imageStore.getImageList("shrine"), 0, 0, 0, 0, 100);
            
                world.addEntity(entity);
                ((KineticEntity)entity).scheduleActions(scheduler, world, imageStore);
                scheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), entity.getactionPeriod());
            
        }
        
        //  (39) is the value output to mouseButton when <rightClick>
        else if(mouseButton == 39){
            //world.removeEntityAt(clickLocation);
            if(world.isOccupied(clickLocation)){return;}
            //MinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
            MinerNotFull entity = new MinerNotFull(String.format("miner_%s_%s", x, y), clickLocation, imageStore.getImageList("miner"), 2, 0, 982, 100);
            
                world.addEntity(entity);
                ((KineticEntity)entity).scheduleActions(scheduler, world, imageStore);
                //scheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), entity.getactionPeriod());
            
            // This is a text line of the form found in gaia.sav... this is how worldView reads Entity initiation
            //NewLine = String.format("miner miner_%s_%s %s %s %s %s %s", x, y, x, y, 2, 982, 100);
            //System.out.println(NewLine);
            //world.processLine(NewLine, imageStore);
            //miner miner_17_22 17 22 2 982 100
        }
        
        


            
                //    this.getposition().createShrine(this.getid(), this.getresourceLimit(),
          	//this.getactionPeriod(), this.getanimationPeriod(),
          	//this.getimages());
            
            
        //}
        
        
        // Text is sent to WorldView to be enacted
        //world.processLine(NewLine, imageStore);
        
       
        
        //scheduler.scheduleEvent(entity, Activity.createActivityAction(entity, world, imageStore), entity.getactionPeriod());
    }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
    
   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      
      for (Entity entity : world.getentities())
      { 
         if( entity instanceof KineticEntity)
            ((KineticEntity)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }

}
