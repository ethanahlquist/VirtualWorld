import java.util.Arrays;
import java.util.function.*;
import java.util.stream.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Objects;


public class AStarStrategy extends PathingStrategy{ 
        
        private WorldModel world;
        private Boolean [][] grid;
        private List<Point> Path;
        //private List<Point> ListOfEdges;
        
        public AStarStrategy(Point start, Point end, Predicate<Point> canPassThrough,
        BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors, WorldModel world){
            
            super(start, end, canPassThrough, withinReach, potentialNeighbors);
            this.world = world;
            this.grid = new Boolean [world.getnumRows()] [world.getnumCols()];
            this.Path = new ArrayList<Point>();
            
        }


        public List<Point> computePath(){
            
        
        for (int row = 0; row < world.getnumRows(); row++)
            {
                Arrays.fill(this.grid[row], false);
            }

       Path.clear();
       
       NewStrategy(start, Path);
       
       Collections.reverse(Path);
       
       return Path;
        
    }
    
    private Boolean NewStrategy(Point currentNode, List<Point> Path){
        
        
        List<Point> PathStore = Path;
        
        
        if(withinBounds(currentNode, grid)  &&      // IN GRID:
         (canPassThrough.test(currentNode) || currentNode.equals(start) )&&        // PASSABLE:
         gridGet(currentNode) != true)              // NOT SEARCHED:
         {
            
            gridSet(currentNode, true);
       
       if(withinReach.test(currentNode, end)){

           return true;
           
       }else{
      
           
      gridSet(currentNode, true);
      
      List<Point> ListOfNodes = 
                potentialNeighbors.apply(currentNode)
                    .sorted((p1, p2) -> heuristic(p1, end) - heuristic(p2, end))
                    .filter(canPassThrough)
                    .collect(Collectors.toList());
      
      //if(ListOfNodes.isEmpty())
      //    return false;
      
      
      for(Point P: ListOfNodes){
          if(NewStrategy(P, PathStore)){  // if PATH = true
              Path.add(P);
              if(P.adjacent(start)){return true;}
              break;
          }
          //System.out.printf("%s: %s \n", "~~~~~~", "We STILL IN THAT LOOP" );
          //if(P.adjacent(start)){return true;}
      }

        return true;
      }
     }
        
    return false;
    }
        
    private int heuristic(Point point1, Point point2){
        return (int)Math.hypot(point1.getX()-point2.getX(), point1.getY()-point2.getY());
    }
    
    public int pathCost(List<Point> path){
        return heuristic(path.get(path.size()), end) + path.size();
    }
    
    private Boolean gridGet(Point P){
        return grid[P.getY()][P.getX()];
    }
    
    private void gridSet(Point P, Boolean logic){
        grid[P.getY()][P.getX()] = logic;
    }
    
    private static boolean withinBounds(Point p, Boolean[][] grid)
   {
      return p.getY() >= 0 && p.getY() < grid.length &&
         p.getX() >= 0 && p.getX() < grid[0].length;
   }
}
