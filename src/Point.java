import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import processing.core.PImage;

final class Point
{
   private final int x;
   private final int y;


   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   
   public boolean adjacent(Point p2)
   {
      return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) ||
         (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
   }
   
   public boolean diagonal(Point p2)
   {
       return (Math.abs(this.y - p2.y) == 1) && (Math.abs(this.x - p2.x) == 1);
   }

   
   public int distanceSquared(Point p2)
   {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   
   public Quake createQuake(List<PImage> images)
   {
      return new Quake(Quake.QUAKE_ID, this, images, Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
   }

   
   public Vein createVein(String id, int actionPeriod,
      List<PImage> images)
   {
      return new Vein(id, this, images, actionPeriod);
   }

   
   public Blacksmith createBlacksmith(String id, List<PImage> images)
   {
      return new Blacksmith(id, this, images);
   }

   
   public MinerFull createMinerFull(String id, int resourceLimit, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new MinerFull(id, this, images,
         resourceLimit, actionPeriod, animationPeriod);
   }

   
   public MinerNotFull createMinerNotFull(String id, int resourceLimit, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new MinerNotFull(id, this, images, 0, resourceLimit, actionPeriod, animationPeriod);
   }

   
   public Obstacle createObstacle(String id, List<PImage> images)
   {
      return new Obstacle(id, this, images);
   }

   
   public Ore createOre(String id, int actionPeriod,
      List<PImage> images)
   {
      return new Ore(id, this, images, actionPeriod);
   }

   
   public FoolsGold createFoolsGold(String id, int actionPeriod,
      List<PImage> images)
   {
      return new FoolsGold(id, this, images, actionPeriod);
   }
   
   public OreBlob createOreBlob(String id, int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new OreBlob(id, this, images, actionPeriod, animationPeriod);
   }
   
   public Shrine createShrine(String id, int resourceLimit, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new Shrine(id, this, images, 0, resourceLimit, actionPeriod, animationPeriod, 100);
   }
   
   public EvilMiner createEvilMiner(String id, int resourceLimit, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
       //EvilMiner(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
      return new EvilMiner(id, this, images, resourceLimit, actionPeriod, animationPeriod);
   }
   
   
   public final List<Point> CardinalNeighbors(){
           
           List<Point> returnList = new ArrayList<>();
           
           //Cardinal
           returnList.add(new Point(this.getX(), this.getY() - 1));
           returnList.add(new Point(this.getX(), this.getY() + 1));
           returnList.add(new Point(this.getX() - 1, this.getY()));
           returnList.add(new Point(this.getX() + 1, this.getY()));
           
           //diagonals
           //returnList.add(new Point(this.getX() + 1, this.getY() + 1)); //
           //returnList.add(new Point(this.getX() + 1, this.getY() - 1)); //
           //returnList.add(new Point(this.getX() - 1, this.getY() + 1)); // diagonals
           //returnList.add(new Point(this.getX() - 1, this.getY() - 1)); //
           
           //returnList.add(new Point(this.getX(), this.getY() - 2));
           //returnList.add(new Point(this.getX(), this.getY() + 2));
           //returnList.add(new Point(this.getX() - 2, this.getY()));
           //returnList.add(new Point(this.getX() + 2, this.getY()));
           
           return returnList;
       
            //.add(new Point(point.getX() + 1, point.getY() + 1)) //
            //.add(new Point(point.getX() + 1, point.getY() - 1)) //
            //.add(new Point(point.getX() - 1, point.getY() + 1)) // diagonals
            //.add(new Point(point.getX() - 1, point.getY() - 1)) //
            
            //.add(new Point(point.getX(), point.getY() + 1))
            //.add(new Point(point.getX() - 1, point.getY()))
            //.add(new Point(point.getX() + 1, point.getY()))
           // .build();
   }
   
   public final List<Point> diagonals(){
           
           List<Point> returnList = new ArrayList<>();
           
           //Cardinal
           //returnList.add(new Point(this.getX(), this.getY() - 1));
           //returnList.add(new Point(this.getX(), this.getY() + 1));
           //returnList.add(new Point(this.getX() - 1, this.getY()));
           //returnList.add(new Point(this.getX() + 1, this.getY()));
           
           //diagonals
           returnList.add(new Point(this.getX() + 1, this.getY() + 1)); //
           returnList.add(new Point(this.getX() + 1, this.getY() - 1)); //
           returnList.add(new Point(this.getX() - 1, this.getY() + 1)); // diagonals
           returnList.add(new Point(this.getX() - 1, this.getY() - 1)); //
           
           //returnList.add(new Point(this.getX(), this.getY() - 2));
           //returnList.add(new Point(this.getX(), this.getY() + 2));
           //returnList.add(new Point(this.getX() - 2, this.getY()));
           //returnList.add(new Point(this.getX() + 2, this.getY()));
           
           return returnList;   
   }
   
   
    public final List<Point> BlockCicle(){
           
        List<Point> returnList = this.CardinalNeighbors();
        List<Point> subList = new ArrayList<>();
            
            for(int i = 0; i < 4; i++){
                subList = returnList.get(i).CardinalNeighbors();
                for(Point P : subList)
                    if(!returnList.contains(P))
                        returnList.add(P);
            }
          
          
        //List<Point> returnList = new ArrayList<>();
           
        //returnList.add(new Point(this.getX(), this.getY() - 2));
        //returnList.add(new Point(this.getX(), this.getY() + 2));
        //returnList.add(new Point(this.getX() - 2, this.getY()));
        //returnList.add(new Point(this.getX() + 2, this.getY()));
           
        return returnList;
      }
   
   public int getX() { return x; }
   public int getY() { return y; }


}
