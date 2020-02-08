import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;


   private static final int PROPERTY_KEY = 0;


   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   
   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -Ore.ORE_REACH; dy <= Ore.ORE_REACH; dy++)
      {
         for (int dx = -Ore.ORE_REACH; dx <= Ore.ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (this.withinBounds(newPt) &&
               !this.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   
   public void tryAddEntity(Entity entity)
   {
      if (this.isOccupied(entity.getposition()))
      {
         throw new IllegalArgumentException("position occupied");
      }
      

      this.addEntity(entity);
   }

   
   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
         pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   
   public boolean isOccupied(Point pos)
   {
      return this.withinBounds(pos) &&
         this.getOccupancyCell(pos) != null;
   }

   
   public void addEntity(Entity entity)
   {
      if (this.withinBounds(entity.getposition()))
      {
         this.setOccupancyCell(entity.getposition(), entity);
         this.entities.add(entity);
         
      }
   }

   
   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getposition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         
         entity.setposition(pos);
      }
   }

   
   public void removeEntity(Entity entity)
   {
      this.removeEntityAt(entity.getposition());
   }

   
   public void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
         && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);
         
         entity.setposition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   
   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds(pos))
      {
         return Optional.of(ImageStore.getCurrentImage(this.getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   
   public void setBackground(Point pos, Background background)
   {
      if (this.withinBounds(pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }

   
   public Optional<Entity> getOccupant(Point pos)
   {
      if (this.isOccupied(pos))
      {
         return Optional.of(this.getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   
   public Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   
   public void setOccupancyCell(Point pos,
      Entity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   
   public Background getBackgroundCell(Point pos)
   {
      return this.background[pos.getY()][pos.getX()];
   }

   
   public void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   
   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   
   public boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case Background.BGND_KEY:
            return this.parseBackground(properties, imageStore);
         case MinerFull.MINER_KEY:
            return this.parseMiner(properties, imageStore);
         case Obstacle.OBSTACLE_KEY:
            return this. parseObstacle(properties, imageStore);
         case Ore.ORE_KEY:
            return this.parseOre(properties, imageStore);
         case Blacksmith.SMITH_KEY:
            return this.parseSmith(properties, imageStore);
         case Vein.VEIN_KEY:
            return this.parseVein(properties, imageStore);
         case Shrine.SHRINE_KEY:
            return this.parseShrine(properties, imageStore);
         case StoneWall.STONEWALL_KEY:
             return this.parseStoneWall(properties, imageStore);
         }
      }

      return false;
   }

   
   public boolean parseBackground(String [] properties,
      ImageStore imageStore)
   {
      if (properties.length == Background.BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
            Integer.parseInt(properties[Background.BGND_ROW]));
         String id = properties[Background.BGND_ID];
         setBackground(pt,
            new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == Background.BGND_NUM_PROPERTIES;
   }

   
   public boolean parseMiner(String [] properties, ImageStore imageStore)
   {
      if (properties.length == MinerFull.MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MinerFull.MINER_COL]),
            Integer.parseInt(properties[MinerFull.MINER_ROW]));
         Entity entity = pt.createMinerNotFull(properties[MinerFull.MINER_ID],
            Integer.parseInt(properties[MinerFull.MINER_LIMIT]),
            Integer.parseInt(properties[MinerFull.MINER_ACTION_PERIOD]),
            Integer.parseInt(properties[MinerFull.MINER_ANIMATION_PERIOD]),
            imageStore.getImageList(MinerFull.MINER_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == MinerFull.MINER_NUM_PROPERTIES;
   }

 
   public boolean parseObstacle(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
            Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));
         Entity entity = pt.createObstacle(properties[Obstacle.OBSTACLE_ID],
         imageStore.getImageList(Obstacle.OBSTACLE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }

   
      public boolean parseStoneWall(String [] properties, ImageStore imageStore)
   {
      if (properties.length == StoneWall.STONEWALL_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[StoneWall.STONEWALL_COL]),
            Integer.parseInt(properties[StoneWall.STONEWALL_ROW]));
         Entity entity = pt.createObstacle(properties[StoneWall.STONEWALL_ID],
         imageStore.getImageList(StoneWall.STONEWALL_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
   }
  
   public boolean parseOre(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Ore.ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Ore.ORE_COL]),
            Integer.parseInt(properties[Ore.ORE_ROW]));
         Entity entity = pt.createOre(properties[Ore.ORE_ID],
            Integer.parseInt(properties[Ore.ORE_ACTION_PERIOD]),
            imageStore.getImageList(Ore.ORE_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == Ore.ORE_NUM_PROPERTIES;
   }


   public boolean parseSmith(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Blacksmith.SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Blacksmith.SMITH_COL]),
            Integer.parseInt(properties[Blacksmith.SMITH_ROW]));
         Entity entity = pt.createBlacksmith(properties[Blacksmith.SMITH_ID],
         imageStore.getImageList(Blacksmith.SMITH_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == Blacksmith.SMITH_NUM_PROPERTIES;
   }


   public boolean parseVein(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Vein.VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Vein.VEIN_COL]),
            Integer.parseInt(properties[Vein.VEIN_ROW]));
         Entity entity = pt.createVein(properties[Vein.VEIN_ID],
            Integer.parseInt(properties[Vein.VEIN_ACTION_PERIOD]),
            imageStore.getImageList(Vein.VEIN_KEY));
         this.tryAddEntity(entity);
      }

      return properties.length == Vein.VEIN_NUM_PROPERTIES;
   }

   public boolean parseShrine(String [] properties, ImageStore imageStore)
   {
      if (properties.length == Shrine.SHRINE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Shrine.SHRINE_COL]),
            Integer.parseInt(properties[Shrine.SHRINE_ROW]));
         Entity entity = pt.createShrine(properties[Shrine.SHRINE_ID],
            Integer.parseInt(properties[Shrine.SHRINE_LIMIT]),
            Integer.parseInt(properties[Shrine.SHRINE_ACTION_PERIOD]),
            Integer.parseInt(properties[Shrine.SHRINE_ANIMATION_PERIOD]),
            imageStore.getImageList(Shrine.SHRINE_KEY));
         this.tryAddEntity(entity);
      }
      
      return properties.length == Shrine.SHRINE_NUM_PROPERTIES;
   }

   public int getnumCols() { return numCols; }
   public int getnumRows() { return numRows; }
   public Background[][] getBackground() { return background; }
   public Entity[][] getoccupancy() { return occupancy; }
   public Set<Entity> getentities() { return entities;}

}
