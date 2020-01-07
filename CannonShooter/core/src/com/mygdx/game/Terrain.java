//package com.mygdx.game;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.World;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class Terrain extends GameObject {
//
//    private float groundY, levelWidth;
//    private int numFloors;
//    private World world;
//    private Texture groundTexture;
//    private final int groundBlockWidth = 20;
//    private final int groundBlockHeight = 20;
//
//    //todo: maybe make a BuildingBuilder class?
//    public Terrain(float groundY, float levelWidth, World world){
//        super();
//        this.groundY = groundY;
//        this.levelWidth = levelWidth;
//        this.world = world;
//        generate();
//    }
//
//    private void generate(){
//
//        groundTexture = makeTexture("rock-block.jpg");
//
//        int currentWidth = 0;
//        float lastHeight = groundY;
//        Random random = new Random();
//
//        // modes:
//        //
//        // mountain/ridge - 0
//        // levelGround - 1
//        //
//        // buildLength = 16 blocks
//        // build Order - 3 1 3 R 3 R 3 R 3, R = rand(1,2)
//
//        final int maxModeLength = 16;
//        int currentMaxLength = 16;
//        int currentMode = 1;
//        int currentModeLength = 0;
//        int previousHeight = 1;
//
//
//        for(int x = 0; x <= levelWidth; x += groundBlockWidth){
//
//            if(currentModeLength == currentMaxLength){
//                currentMode = 1 - currentMode;
//                currentModeLength = 0;
//                if(currentMode == 0){
//                    currentMaxLength = Math.max(14, random.nextInt(25));
//                }
//                else
//                    currentMaxLength = 6 + random.nextInt(8);
//            }
//
//            if(currentMode == 0){
//                //mountain
//                int currentHeight = previousHeight + random.nextInt(4);
//                if(currentModeLength > currentMaxLength / 2)
//                    currentHeight = Math.max(1 , previousHeight - random.nextInt(4));
//
//                float currentBlockY = groundY;
//                for(int i = 0; i < currentHeight; i++){
//                    makeBlock(x, currentBlockY);
//                    currentBlockY += groundBlockHeight;
//                }
//                currentModeLength++;
//                previousHeight = currentHeight;
//            }
//            else if(currentMode == 1){
//                //level ground
//                makeBlock(x, groundY);
//                currentModeLength++;
//            }
//
//        }
//    }
//
//    private void makeBlock(float x, float y){
//        Sprite sprite = new SpriteBuilder(groundTexture)
//                .setPosition(x, y)
//                .setSize(groundBlockWidth, groundBlockHeight).build();
//        Body body = BodyBuilder.makeSimpleBody(sprite, BodyDef.BodyType.StaticBody, world);
//        addSpriteBodyPair(sprite, body);
//    }
//
//}
