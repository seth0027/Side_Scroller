package sidescroller.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.property.Sprite;
import sidescroller.entity.sprite.LandSprite;
import sidescroller.entity.sprite.SpriteFactory;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;

public class MapScene implements MapSceneInterface{

    private Tuple count;
    private Tuple size;
    private double scale;
    private AnimatorInterface animator;
    private List<Entity> players;
    private  List<Entity> staticShapes;
    private BooleanProperty drawBounds;
    private BooleanProperty drawFPS;
    private BooleanProperty drawGrid;
    private Entity background;


    public MapScene(){
        players=new ArrayList<>();
        staticShapes=new ArrayList<>();
        drawGrid= new SimpleBooleanProperty();
        drawBounds=new SimpleBooleanProperty();
        drawFPS=new SimpleBooleanProperty();

    }



    @Override
    public BooleanProperty drawFPSProperty() {
        return drawFPS;
    }

    @Override
    public boolean getDrawFPS() {
        return drawFPS.get();
    }

    @Override
    public BooleanProperty drawBoundsProperty() {
        return drawBounds;
    }

    @Override
    public boolean getDrawBounds() {
        return drawBounds.get();
    }

    @Override
    public BooleanProperty drawGridProperty() {
        return drawGrid;
    }

    @Override
    public boolean getDrawGrid() {
        return drawGrid.get();
    }

    @Override
    public MapSceneInterface setRowAndCol(Tuple count, Tuple size, double scale) {
        this.count=count;
        this.size=size;
        this.scale=scale;
        return this;
    }

    @Override
    public Tuple getGridCount() {
        return null;
    }

    @Override
    public Tuple getGridSize() {
        return null;
    }

    @Override
    public double getScale() {
        return 0;
    }

    @Override
    public MapSceneInterface setAnimator(AnimatorInterface newAnimator) {
        if(animator!=null){
            animator.stop();
        }
        if(newAnimator==null){
            throw new NullPointerException("new Animator argument is null ");
        }

        animator=newAnimator;

        return this;
    }

    @Override
    public Entity getBackground() {
        return background;
    }

    @Override
    public void start() {
        if(animator!=null){
            animator.start();
        }

    }

    @Override
    public void stop() {
        if(animator!=null){
            animator.stop();
        }

    }

    @Override
    public List<Entity> staticShapes() {
        return staticShapes;
    }

    @Override
    public List<Entity> players() {
        return players;
    }

    @Override
    public MapSceneInterface createScene(Canvas canvas) {

       MapBuilder mb=MapBuilder.createBuilder();


       mb.setCanvas(canvas).setGridScale(scale).setGrid(count,size);
        mb.buildTree(2,2, FloraTile.TREE);
        mb.buildLandMass(4,1,5,3);
      mb.buildBackground((row,col)->
      {    return BackgroundTile.NIGHT_CLOUD;
    });



      background= mb.getBackground();
      List<Entity> entities=new ArrayList<>();
      staticShapes=mb.getEntities(entities);

        return this;
    }

    @Override
    public boolean inMap(HitBox hitbox) {
        return background.getHitBox().containsBounds(hitbox);
    }


}
