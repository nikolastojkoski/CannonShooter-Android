package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class GameObject {

    public static final float PIXELS_TO_METERS = 100f;

    protected ArrayList<Sprite> sprites;
    protected ArrayList<Body> bodies;
    protected ArrayList<Texture> textures;
    protected Sprite mainSprite;
    protected Body mainBody;

    public GameObject(){
        sprites = new ArrayList<Sprite>();
        bodies = new ArrayList<Body>();
        textures = new ArrayList<Texture>();
    }
    public int addSpriteBodyPair(Sprite sprite, Body body){
        sprites.add(sprite);
        bodies.add(body);
        return sprites.size() - 1;
    }
    public Texture makeTexture(String internalPath){
        Texture texture = new Texture(internalPath);
        textures.add(texture);
        return texture;
    }
    public void setMainSpriteBodyPair(int idx){
        mainSprite = sprites.get(idx);
        mainBody = bodies.get(idx);
    }
    public void updateSprites(){

        for(int i = 0; i < sprites.size(); i++){

            Sprite sprite = sprites.get(i);
            Body body = bodies.get(i);

            sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth() / 2,
                    (body.getPosition().y * PIXELS_TO_METERS)-sprite.getHeight()/2);
            sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        }
    }

    public void draw(SpriteBatch batch){
        for(Sprite sprite: sprites){
            batch.draw(sprite, sprite.getX(), sprite.getY(),
                    sprite.getOriginX(), sprite.getOriginY(),
                    sprite.getWidth(), sprite.getHeight(),
                    sprite.getScaleX(), sprite.getScaleY(),
                    sprite.getRotation());
        }
    }

    public void dispose(){
        for(Texture texture: textures){
            texture.dispose();
        }
    }

    public float getX(){return mainSprite.getX();}
    public float getY(){return mainSprite.getY();}
    public float getWidth(){return mainSprite.getWidth();}
    public float getHeight(){return mainSprite.getHeight();}
    public float getRotationDeg(){return mainSprite.getRotation();}
    public float getRotationRad(){return mainBody.getAngle();}
}
