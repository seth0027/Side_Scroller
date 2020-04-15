package sidescroller.scene;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.property.Sprite;
import sidescroller.entity.sprite.*;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import sidescroller.entity.sprite.tile.PlatformTile;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MapBuilder implements  MapBuilderInterface {

    private Tuple rowColCount;
    private Tuple dimension;
    private double scale;
    private Canvas canvas;
    private Entity background;
    private List<Entity> landMass;
    private List<Entity> other;

    protected  MapBuilder(){
    landMass=new ArrayList<>();
    other=new ArrayList<>();

    }

    public static MapBuilder createBuilder(){



        return new MapBuilder();
    }


    @Override
    public MapBuilderInterface setCanvas(Canvas canvas) {
        this.canvas=canvas;
        return this;
    }

    @Override
    public MapBuilderInterface setGrid(Tuple rowColCount, Tuple dimension) {

        this.rowColCount=rowColCount;
        this.dimension=dimension;
        return this;
    }

    @Override
    public MapBuilderInterface setGridScale(double scale) {

        this.scale=scale;
        return this;
    }


    @Override
    public MapBuilderInterface buildBackground(BiFunction<Integer, Integer, Tile> callback) {

        BackgroundSprite backgroundSprite=SpriteFactory.get("Background");

        backgroundSprite.init(scale,dimension,Tuple.pair(0,0));
        backgroundSprite.createSnapshot(canvas,rowColCount,callback);
        HitBox hitBox=HitBox.build(0,0,scale * dimension.x() * rowColCount.y(),scale * dimension.y() * rowColCount.x());
        GenericEntity entity=new GenericEntity(backgroundSprite,hitBox);
        background=entity;

        return this;
    }


    @Override
    public MapBuilderInterface buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {

        LandSprite landSprite=SpriteFactory.get("Land");
        landSprite.init(scale,dimension,Tuple.pair(colPos,rowPos));
        landSprite.createSnapshot(canvas,rowConut,colCount);
        HitBox hitBox=HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
        GenericEntity entity=new GenericEntity(landSprite,hitBox);
        landMass.add(entity);

        return this;
    }
    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Tree". save in a {@link TreeSprite} variable.
     * <br>
     * on the return call {@link TreeSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( colPos, rowPos).
     * <br>
     * on the return call {@link TreeSprite#createSnapshot(Canvas, Tile)} and pass canvas and tile.
     * <br>
     * by default there is no hitbox.
     * <br>
     * finally add the instance of {@link GenericEntity#GenericEntity(Sprite, HitBox)} to other list.
     * <br>
     * </p>
     * @param rowPos - first row from the top.
     * @param colPos - first column from the left.
     * @param tile - a tree type from enum {@link FloraTile}.
     * @return the current instance of this object.
     */

    @Override
    public MapBuilderInterface buildTree(int rowPos, int colPos, Tile tile) {
        TreeSprite treeSprite=SpriteFactory.get("Tree");
        treeSprite.init(scale,dimension,Tuple.pair(colPos,rowPos));
        treeSprite.createSnapshot(canvas,tile);
        GenericEntity entity=new GenericEntity(treeSprite,null);
        other.add(entity);
        return this;

    }

    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Platform". save in a {@link PlatformSprite} variable.
     * <br>
     * on the return call {@link PlatformSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( colPos, rowPos).
     * <br>
     * on the return call {@link PlatformSprite#createSnapshot(Canvas, Tile, int)} and pass canvas, tile, and length.
     * <br>
     * use {@link HitBox#build(double, double, double, double)} and pass (colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), and scale * dimension.y() / 2.
     * <br>
     * finally add the instance of {@link GenericEntity#GenericEntity(Sprite, HitBox)} to other list.
     * <br>
     * </p>
     * @param rowPos - first row from the top.
     * @param colPos - first column from the left.
     * @param length - number of columns which the platform will stretch.
     * @param tile - a platform type from enum {@link PlatformTile}.
     * @return
     */

    @Override
    public MapBuilderInterface buildPlatform(int rowPos, int colPos, int length, Tile tile) {

        PlatformSprite platformSprite=SpriteFactory.get("Platform");
        platformSprite.init(scale,dimension,Tuple.pair(colPos,rowPos));
        platformSprite.createSnapshot(canvas,tile,length);
        HitBox hitBox=HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1),scale * dimension.y() / 2);
        GenericEntity entity=new GenericEntity(platformSprite,hitBox);
        other.add(entity);

        return this;
    }

    @Override
    public Entity getBackground() {
        return background;
    }

    @Override
    public List<Entity> getEntities(List<Entity> list) {

        if(list==null){
            throw new NullPointerException("list is not initialized");
        }


        landMass.forEach(land -> list.add(land));
        other.forEach(otherObject -> list.add(otherObject));

        return list;
    }
}
