
public class EvilVisitor  extends AllFalseEntityVisitor
{
        @Override
	public Boolean visit(EvilMiner evilminer) { return true; }
}