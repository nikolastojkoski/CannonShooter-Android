package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Cannon extends GameObject {

    //Constants
    static final int numColumns = 4;
    static final float platformWidth = 200;

    //Position
    private float leftX, platformHeight;
    private Sprite shaftSprite;
    private Body shaftBody;

    //Cannonballs
    private ArrayList<Cannonball> cannonballs;
    private float nextCannonballX, nextCannonballY;
    private float nextCannonballVX = 13, nextCannonballVY = 0;
    private Cannonball lastCannonball;
    private boolean isCannonBallLaunched = false;
    private int numCannonballs = 3;

    //World reference
    private World world;

    public Cannon(int leftX, int platformHeight, int groundY, World world){
        super();
        this.leftX= leftX;
        this.platformHeight = platformHeight;
        this.world = world;

        cannonballs = new ArrayList<Cannonball>();
        //cannonballLight = new PointLight(rayHandler, 1000, Color.GOLD, 300, 0,0);

        makePlatform(platformHeight, groundY);
        makeCannon(groundY + platformHeight);

        //todo: make method - setupInitialCannonballPosition
        nextCannonballX = shaftSprite.getX() + shaftSprite.getWidth() / 2 + 75;
        nextCannonballY = shaftSprite.getY() + shaftSprite.getHeight() / 2 - 25;
        rotateCannonballPosition(-45);
    }

    public void shoot(){

        if(numCannonballs == 0)
            return;

        lastCannonball = new Cannonball((int) nextCannonballX, (int)nextCannonballY,
                nextCannonballVX * 1.4f, nextCannonballVY * 1.4f, world);
        cannonballs.add(lastCannonball);
        isCannonBallLaunched = true;
        numCannonballs--;
    }
    private void makeCannon(int platformHeight){

        shaftSprite = new SpriteBuilder(makeTexture("cannon-shaft.png"))
                .setPosition(leftX + platformWidth / 2 - 55, platformHeight)
                .setSize(111, 96)
                .setOrigin(111/2, 96/2)
                .build();
        shaftBody = BodyBuilder.makeSimpleBody(shaftSprite, BodyDef.BodyType.StaticBody, world);
        addSpriteBodyPair(shaftSprite, shaftBody);

        Sprite baseSprite = new SpriteBuilder(makeTexture("cannon-base.png"))
                .setPosition(leftX + platformWidth / 2 - 55, platformHeight)
                .setSize(120, 108)
                .build();
        Body baseBody = BodyBuilder.makeSimpleBody(baseSprite, BodyDef.BodyType.StaticBody, world);
        addSpriteBodyPair(baseSprite, baseBody);

    }

    //todo: dali treba ova?
    //bodyDef.angle = (float)Math.toRadians(sprite.getRotation());

    private void makePlatform(int platformHeight, int groundY){

        //todo: cannon platform should be a Building
        Texture woodTexture = makeTexture("wood1.jpg");

        float plankHeight = 20;
        float columnWidth = 20, columnHeight = platformHeight - plankHeight;
        float currentX = leftX;
        float supportingWidth = platformWidth / numColumns;

        for(int i = 0; i < numColumns; i++){
            Sprite columnSprite = new SpriteBuilder(woodTexture)
                    .setPosition(currentX, groundY)
                    .setSize(columnWidth, columnHeight)
                    .build();
            Body columnBody = BodyBuilder.makeSimpleBody(columnSprite, BodyDef.BodyType.StaticBody, world);
            addSpriteBodyPair(columnSprite, columnBody);
            currentX += supportingWidth;
        }

        Texture stoneTexture = makeTexture("stone.jpg");
        Sprite plankSprite = new SpriteBuilder(stoneTexture)
                .setPosition(leftX - 15, groundY + columnHeight)
                .setSize(platformWidth, plankHeight)
                .build();
        Body plankBody = BodyBuilder.makeSimpleBody(plankSprite, BodyDef.BodyType.StaticBody, world);
        addSpriteBodyPair(plankSprite, plankBody);
    }

    @Override
    public void updateSprites() {
        super.updateSprites();
        for(Cannonball cannonball: cannonballs){
            cannonball.updateSprites();
        }
        if(isCannonBallLaunched()){
            //cannonballLight.setPosition(lastCannonball.getX(), lastCannonball.getY());
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for(Cannonball cannonball: cannonballs){
            cannonball.draw(batch);
        }
    }

    public void rotate(float angle){
        //todo: dali mora na sekoe da se mnozi so pixels to meters?
        shaftBody.setTransform(shaftBody.getPosition(), shaftBody.getAngle() - (float) Math.toRadians(angle));

        rotateCannonballPosition(angle);

    }
    private void rotateCannonballPosition(float angle){
        float cosA = (float)Math.cos(Math.toRadians(-angle));
        float sinA = (float)Math.sin(Math.toRadians(-angle));

        float tx = nextCannonballX - shaftBody.getPosition().x * PIXELS_TO_METERS;
        float ty = nextCannonballY - shaftBody.getPosition().y * PIXELS_TO_METERS;

        nextCannonballX = tx*cosA - ty*sinA + shaftBody.getPosition().x * PIXELS_TO_METERS;
        nextCannonballY = tx*sinA + ty*cosA + shaftBody.getPosition().y * PIXELS_TO_METERS;

        nextCannonballVX = 0.185f * (nextCannonballX - shaftBody.getPosition().x * PIXELS_TO_METERS);
        nextCannonballVY = 0.185f * (nextCannonballY - shaftBody.getPosition().y * PIXELS_TO_METERS);
    }

    public Cannonball getLastCannonball(){
        return lastCannonball;
    }
    public boolean isCannonBallLaunched(){
        return isCannonBallLaunched;
    }
    public float getLeftX(){
        return leftX;
    }
    public float getPlatformWidth(){
        return platformWidth;
    }
    public float getPlatformHeight(){
        return platformHeight;
    }
    public int getNumCannonballs(){return numCannonballs;}
}
