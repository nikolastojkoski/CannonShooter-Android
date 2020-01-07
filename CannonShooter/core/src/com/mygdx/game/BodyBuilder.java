package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyBuilder {

    private Body body;
    private FixtureDef fixtureDef;
    private Shape shape;
    private BodyDef bodyDef;
    private boolean fromSprite;
    private float width, height;

    //references
    private World world;
    private Sprite sprite;

    public BodyBuilder(Sprite sprite, World world){
        this.world = world;
        this.sprite = sprite;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / MyGdxGame.PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2 ) / MyGdxGame.PIXELS_TO_METERS);
        fromSprite = true;
    }
    public BodyBuilder(float x, float y, float width, float height, World world){
        this.world = world;
        this.width = width;
        this.height = height;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x+width/2) / MyGdxGame.PIXELS_TO_METERS,
                (y+height/2) / MyGdxGame.PIXELS_TO_METERS);
        fromSprite = false;
    }
    public BodyBuilder addCircleShape(int radius){
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / MyGdxGame.PIXELS_TO_METERS);
        shape.setPosition(new Vector2(0,0));
        this.shape = shape;
        return this;
    }
    public BodyBuilder addBoxShape(float hx, float hy){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx / MyGdxGame.PIXELS_TO_METERS,hy / MyGdxGame.PIXELS_TO_METERS);
        this.shape = shape;
        return this;
    }
    public BodyBuilder addBoxShape(){
        if(fromSprite){
            return addBoxShape(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }else{
            return addBoxShape(width/2, height/2);
        }

    }
    public BodyBuilder setBodyType(BodyDef.BodyType type){
        bodyDef.type = type;
        return this;
    }
    public BodyBuilder setProperties(float density, float friction){
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = 0.1f;
        return this;
    }
    public Body build(){
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    public static Body makeSimpleBody(Sprite sprite, BodyDef.BodyType bodyType, World world){
        return new BodyBuilder(sprite, world)
                .addBoxShape()
                .setBodyType(bodyType)
                .setProperties(0.53f, 15f)
                .build();
    }

}
