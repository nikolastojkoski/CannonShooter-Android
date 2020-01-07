package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;

public class Building extends GameObject {

    private float x, y, width, height, groundY;
    private int numFloors;

    public Building(float x, float y, float width, float height, int numFloors, float groundY, World world){
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.numFloors = numFloors;
        this.groundY = groundY;

        float nextFloorY = groundY;
        for(int i=0; i< numFloors; i++){
            nextFloorY = createFloor(nextFloorY, world);
        }
    }

    public int getScore(){
        int score = 0;
        for(Body b: bodies){
            score += b.getLinearVelocity().dot(b.getLinearVelocity());
        }
        //Gdx.app.log("SCORE", "score is: " + score);

        if(score < 100)
            score = 0;

        return score;
    }

    private float createFloor(float groundHeight, World world) {

        Random rand = new Random();
        int numColumns = Math.max(2, rand.nextInt() % 4);

        float curSectionLeft = x;
        float supportingWidth = width / numColumns;
        float columnWidth = 20, columnHeight = 100;

        Texture woodTexture = makeTexture("wood1.jpg");

        //Add wood columns
        for(int i=0; i<numColumns; i++){

            float curColx = curSectionLeft + (supportingWidth / 2);
            Sprite sprite = new SpriteBuilder(woodTexture)
                    .setPosition(curColx - columnWidth/2, groundHeight)
                    .setSize(columnWidth, columnHeight)
                    .setOrigin(columnWidth/2, columnHeight/2)
                    .build();
            Body body = BodyBuilder.makeSimpleBody(sprite, BodyDef.BodyType.DynamicBody, world);
            addSpriteBodyPair(sprite, body);
            curSectionLeft += supportingWidth;

            Gdx.app.log("Building", "add column at: " + (curColx - columnWidth/2) + ", " + groundHeight);
        }

        //Add wood ceiling
        int ceilingHeight = 20;
        Texture stoneTexture = makeTexture("stone.jpg");

        Sprite plankSprite = new SpriteBuilder(stoneTexture)
                .setPosition(x, groundHeight + columnHeight + 5)
                .setSize(width, ceilingHeight)
                .setOrigin(width/2, ceilingHeight/2)
                .build();
        Body plankBody = new BodyBuilder(plankSprite, world)
                .addBoxShape()
                .setBodyType(BodyDef.BodyType.DynamicBody)
                .setProperties(3f, 15f)
                .build();

        addSpriteBodyPair(plankSprite, plankBody);

        Gdx.app.log("Building", "add plank at: " + x + ", " + (groundHeight + columnHeight));

        //Return highest point
        return (groundHeight + columnHeight + ceilingHeight + 10);
    }


}
