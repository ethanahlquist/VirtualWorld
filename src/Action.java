
public abstract class Action
{
	private Entity entity;

	public Action(Entity entity)
            {
		this.entity = entity;
            }

	public Entity getEntity() { return entity; }
        
        protected abstract void executeAction(EventScheduler scheduler);

}