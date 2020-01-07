package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class TouchController implements GestureDetector.GestureListener{

    private Cannon cannon;
    private MyGdxGame game;

    private final Texture blockTexture = new Texture("block.png");

    public TouchController(Cannon cannon, MyGdxGame game){
        super();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.cannon = cannon;
        this.game = game;
    }

    @Override
    public boolean longPress(float x, float y) {
        MyGdxGame.drawSprite = !MyGdxGame.drawSprite;
        cannon.shoot();
        return true;
    }
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        //cannon.rotate( 4*10^{-6}x^2-0.0038x+1 * distance(touch, cannon) * deltaY * 0.24f);
        if(game.getGamePhase() == MyGdxGame.GamePhase.CANNON_WAITING){
            Gdx.app.log("Touch-Pan", "x,y,dx,dy: " + x +  ", " + y + ", " + deltaX + ", " +deltaY);
            cannon.rotate(deltaY * 0.24f );
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(game.getGamePhase() == MyGdxGame.GamePhase.GAME_OVER){
            game.dispose();
            game.initialize();
        }
        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }
    @Override
    public boolean fling(float velocityX, float velocityY, int button) { return false; }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }
    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    @Override
    public void pinchStop() {

    }
}
