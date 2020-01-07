package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Cannonball extends GameObject {

    static final int width = 20;
    static final int height = 20;
    static final int radius = 10;

    private Body body;
    private boolean isActive = true;

    public Cannonball(int x, int y, float vx, float vy, World world){
        super();

        Sprite sprite = new SpriteBuilder(makeTexture("cannon-ball.png"))
                .setPosition(x, y)
                .setSize(width, height)
                .setOrigin(width/2, height/2)
                .build();

        body = new BodyBuilder(sprite, world)
                .addCircleShape(radius)
                .setProperties(7.8f, 4f)
                .build();

        body.setLinearVelocity(vx, vy);

        setMainSpriteBodyPair(addSpriteBodyPair(sprite, body));
    }

    public boolean isActive(){
        if(isActive && (Math.pow(body.getLinearVelocity().x, 2) + Math.pow(body.getLinearVelocity().y, 2) < 0.025)){
            isActive = false;
        }
        return isActive;
    }
}
