package sidescroller.animator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.player.Player;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;

import java.util.Iterator;
import java.util.function.Consumer;

public class Animator extends AbstractAnimator{
    private Color background= Color.ANTIQUEWHITE;




    @Override
    public void handle(GraphicsContext gc, long now) {

        this.updateEntities();
        this.clearAndFill(gc,background);
        this.drawEntities(gc);

    }

    @Override
    public void drawEntities(GraphicsContext gc) {

        Consumer<Entity> draw=entity ->{
            if(entity!=null && entity.isDrawable()){
                entity.getDrawable().draw(gc);

                if(map.getDrawBounds() && entity.hasHitbox()){
                    entity.getHitBox().getDrawable().draw(gc);
                }

            }
        };
        draw.accept(map.getBackground());

        for(Entity e : map.staticShapes())
            draw.accept(e);

        for(Entity e: map.players())
            draw.accept(e);



    }

    @Override
    public void updateEntities() {

        map.players().forEach(Entity::update);

        map.staticShapes().forEach(Entity::update);

        if(map.getDrawBounds()){


            map.players().forEach(player ->
                player.getHitBox().getDrawable().setStroke(Color.RED)
            );
        }
        map.staticShapes().forEach(shape -> this.proccessEntityList(map.players().iterator(),shape.getHitBox()) );

    }

    @Override
    public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {

        while(iterator.hasNext()){


            Entity entity=iterator.next();
            HitBox bounds=entity.getHitBox();

            if(!map.inMap(bounds)){

               this.updateEntity(entity,iterator);
            }
            else if(shapeHitBox!=null && bounds.intersectBounds(shapeHitBox)){

                if(map.getDrawBounds()){

                    bounds.getDrawable().setStroke(Color.BLUEVIOLET);
                }
                this.updateEntity(entity,iterator);

            }
        }


    }

    @Override
    public void updateEntity(Entity entity, Iterator<Entity> iterator) {

        if(entity instanceof Player){
            ((Player) entity).stepBack();
        }
    }


}
