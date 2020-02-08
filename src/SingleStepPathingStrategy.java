import java.util.function.*;
import java.util.stream.*;
import java.util.List;

public class SingleStepPathingStrategy extends PathingStrategy
{   
    
    
    
    public SingleStepPathingStrategy(Point start, Point end, Predicate<Point> canPassThrough,
        BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors){
        
            super(start, end, canPassThrough, withinReach, potentialNeighbors);
    }

    public List<Point> computePath(){   
     
        List<Point> returnPos = potentialNeighbors.apply(start)
            .filter(canPassThrough).filter(pt -> !pt.equals(start) 
                && !pt.equals(end)
                && (Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())))
                .filter(pt -> (Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY())))
                .collect(Collectors.toList());
        
        if(returnPos.isEmpty()){
            returnPos.add(start);
        }
        return returnPos;
    }
}