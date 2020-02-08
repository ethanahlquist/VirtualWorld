
import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.LinkedList;

public abstract class Entity
{
	
	private String id;
	private Point position;
	private List<PImage> images;
	private int imageIndex;
	private int resourceLimit;
	private int resourceCount;
	
	

	public Entity(String id, Point position, 
						List<PImage> images, int resourceLimit, 
						int resourceCount)
	{
	this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
      	this.resourceCount = resourceCount;
      	
      	
	}

	public Point getposition() { return position; }
	public void setposition(Point point) { this.position = point; }
	public List<PImage> getimages() { return images; }
	public String getid() { return id; }
	public int getImageIndex() { return imageIndex; }

	protected void setImageIndex(int integer) { imageIndex = integer; }
	protected int getresourceCount() { return resourceCount; }
	protected int getresourceLimit() { return resourceLimit; }
        protected void setresourceCount() { this.resourceCount = resourceCount +1; }
	
	public abstract <R> R accept(EntityVisitor<R> visitor);
        
}
