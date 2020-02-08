
public class AnyEntityVisitor extends AllFalseEntityVisitor {
    
    @Override
	public Boolean visit(EvilMiner zombieminer){return true;}
    @Override
	public Boolean visit(Background background){return true;}
    @Override
	public Boolean visit(Ore ore){return true;}
    @Override
	public Boolean visit(Vein vein){return true;}
    @Override
	public Boolean visit(MinerFull minerfull){return true;}
    @Override
	public Boolean visit(MinerNotFull minernotfull){return true;}
    @Override
	public Boolean visit(OreBlob oreblob){return true;}
    @Override
	public Boolean visit(Quake quake){return true;}
    @Override
	public Boolean visit(Obstacle obstacle){return true;}
    @Override
	public Boolean visit(Blacksmith blacksmith) { return true; }
    //@Override
//	public Boolean visit(Blacksmith blacksmith) { return true; }
    //@Override
//	public Boolean visit(Blacksmith blacksmith) { return true; }
    //@Override
//	public Boolean visit(Blacksmith blacksmith) { return true; }
    
}
/*
    public Boolean visit(ZombieMiner zombieminer){return true;}
    public Boolean visit(Background background){return true;}
    
    public Boolean visit(Ore ore){return true;}
    public Boolean visit(Vein vein){return true;}
    public Boolean visit(MinerFull minerfull){return true;}
    public Boolean visit(MinerNotFull minernotfull){return true;}
    public Boolean visit(OreBlob oreblob){return true;}
    public Boolean visit(Quake quake){return true;}
    public Boolean visit(Obstacle obstacle){return true;}
    public Boolean visit(Blacksmith blacksmith){return true;}
*/