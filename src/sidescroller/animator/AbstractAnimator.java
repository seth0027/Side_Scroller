package sidescroller.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.FpsCounter;
import sidescroller.entity.Grid;
import sidescroller.entity.property.Drawable;
import sidescroller.scene.MapSceneInterface;
import utility.Tuple;

import javafx.scene.canvas.Canvas;

public abstract class AbstractAnimator extends AnimationTimer implements AnimatorInterface {


    protected MapSceneInterface map;
    protected Tuple mouse;
    private Canvas canvas;
    private FpsCounter fps;
    private Grid grid;
    public AbstractAnimator(){
        mouse=new Tuple();

        fps=new FpsCounter(10,25);
       Drawable<?> fpsSprite=fps.getDrawable();
       fpsSprite.setFill(Color.BLACK);
       fpsSprite.setStroke(Color.WHITE);
       fpsSprite.setWidth(1);
    }

    @Override
    public void setCanvas(Canvas canvas) {
    this.canvas=canvas;
    }

    @Override
    public void clearAndFill(GraphicsContext gc, Color background) {


        gc.setFill(background);
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public void handle(long now){
        GraphicsContext gc=canvas.getGraphicsContext2D();


        if(map.getDrawFPS()){
            fps.calculateFPS(now);
        }

        handle(gc,now);

        if(map.getDrawGrid()){
            if(grid==null){
              grid=new Grid(map.getGridCount(),canvas.getWidth(),canvas.getHeight()) ;

              Drawable<?> gridSprite=grid.getDrawable();

              gridSprite.setStroke(Color.BLACK);
              gridSprite.setWidth(1);
              gridSprite.setScale(map.getScale());
              gridSprite.setTileSize(map.getGridSize());
            }

            grid.getDrawable().draw(gc);
        }

        if(map.getDrawFPS()){

            fps.getDrawable().draw(gc);
        }




    }





    @Override
    public void setMapScene(MapSceneInterface map) {
        this.map=map;

    }
}
