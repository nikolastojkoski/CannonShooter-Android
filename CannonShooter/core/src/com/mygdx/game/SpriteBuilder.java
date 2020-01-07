package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteBuilder {

    private Sprite sprite;

    public SpriteBuilder(Texture texture){
        sprite = new Sprite(texture);
    }
    public SpriteBuilder setPosition(float x, float y){
        sprite.setPosition(x, y);
        return this;
    }
    public SpriteBuilder setSize(float width, float height){
        sprite.setSize(width, height);
        return this;
    }
    public SpriteBuilder setOrigin(float originX, float originY){
        sprite.setOrigin(originX, originY);
        return this;
    }
    public Sprite build(){
        return sprite;
    }

}
