
public interface EntityVisitor <R> {  // GOOD
    R visit(Ore ore);
    R visit(Vein vein);
    R visit(MinerFull minerfull);
    R visit(MinerNotFull minernotfull);
    R visit(OreBlob oreblob);
    R visit(Quake quake);
    R visit(Obstacle obstacle);
    R visit(Blacksmith blacksmith);
    R visit(EvilMiner zombieminer);
    R visit(Background background);
    R visit(Shrine shrine);
    R visit(FoolsGold FoolsGold);
    R visit(StoneWall stonewall);

}
