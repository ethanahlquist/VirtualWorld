import java.util.function.*;
import java.util.stream.*;
import java.util.List;

abstract class PathingStrategy
{
 /*
 * Returns a prefix of a path from the start point to a point within reach
 * of the end point. This path is only valid ("clear") when returned, but
 * may be invalidated by movement of other entities.
 *
 * The prefix includes neither the start point nor the endpoint.
 */
    public Point start;
    public Point end;
    public Predicate<Point> canPassThrough;
    public BiPredicate<Point, Point> withinReach;
    public Function<Point, Stream<Point>> potentialNeighbors;
    
    public PathingStrategy(Point start, Point end, Predicate<Point> canPassThrough,
        BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors){
            this.start = start;
            this.end = end;
            this.canPassThrough =  canPassThrough;
            this.withinReach = withinReach;
            this.potentialNeighbors = potentialNeighbors;
    }
    
    
 protected abstract List<Point> computePath();
 
 public static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS = point -> Stream.<Point>builder()
            //.add(new Point(point.getX() + 1, point.getY() + 1)) //
            //.add(new Point(point.getX() + 1, point.getY() - 1)) //
            //.add(new Point(point.getX() - 1, point.getY() + 1)) // diagonals
            //.add(new Point(point.getX() - 1, point.getY() - 1)) //
            .add(new Point(point.getX(), point.getY() - 1))
            .add(new Point(point.getX(), point.getY() + 1))
            .add(new Point(point.getX() - 1, point.getY()))
            .add(new Point(point.getX() + 1, point.getY()))
            .build();
}