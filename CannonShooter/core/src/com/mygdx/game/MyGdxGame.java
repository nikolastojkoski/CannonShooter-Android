package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter {

	public static final float PIXELS_TO_METERS = 100f;
	public enum GamePhase{
		CANNON_WAITING,
		PROJECTILE_FLYING,
		GAME_OVER
	}
	private GamePhase gamePhase;
	private SpriteBatch batch;
	private World world;
	private OrthographicCamera camera;

	//gui
	private SpriteBatch batchGui;
	private BitmapFont guiFont;
	private int totalScore = 0;
	private ArrayList<Sprite> remainingCannonballs;

	//Game Objects
	private ArrayList<GameObject> gameObjects;
	private ArrayList<Building> buildings;
	private Cannon cannon;
	private Sprite backgroundSprite;

	//Ground
	//private Terrain terrain;
	private ArrayList<Sprite> groundSprites;
	private Texture groundTexture, backgroundTexture;
	private Body groundBody;

	//Misc
	public static boolean drawSprite = true;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;


	@Override
	public void create () {
		initialize();
	}


	@Override
	public void render () {

		world.step(1f/60f, 7, 7);

		for(GameObject gameObject: gameObjects){
			gameObject.updateSprites();
		}

		//camera follows cannon/cannonball
		if(cannon.isCannonBallLaunched() && cannon.getLastCannonball().isActive()){
			Cannonball lastCannonball = cannon.getLastCannonball();
			camera.position.set(Math.max(lastCannonball.getX(), Gdx.graphics.getWidth() / 2),
					Math.max(Gdx.graphics.getHeight() / 2, lastCannonball.getY()), 0);
			Gdx.app.log("camera", "camera on ball");
		} else{
        	camera.position.set(Math.max(cannon.getLeftX() + cannon.getPlatformWidth() / 2, Gdx.graphics.getWidth() / 2),
					Math.max(Gdx.graphics.getHeight() / 2, cannon.getPlatformHeight() + 100 ), 0);
			Gdx.app.log("camera", "camera on cannon");
		}
		camera.update();


		//get Score
		for(Building b: buildings){
			totalScore += b.getScore();
		}

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		//Draw Background
		backgroundSprite.draw(batch);

		//Draw ground
		for(Sprite groundSprite: groundSprites){
			groundSprite.draw(batch);
		}

		//Draw gameObjects
		for(GameObject gameObject: gameObjects){
			gameObject.draw(batch);
		}
		batch.end();

		//Draw gui
		batchGui.begin();

		guiFont.draw(batchGui, "Score: " + totalScore, Gdx.graphics.getWidth() - 380, Gdx.graphics.getHeight() - 50);

		for(int i = 0; i < cannon.getNumCannonballs(); i++){
			remainingCannonballs.get(i).draw(batchGui);
		}

		if(cannon.getNumCannonballs() == 0 && !cannon.getLastCannonball().isActive()){
			gamePhase = GamePhase.GAME_OVER;
		    batchGui.draw(new Texture("medieval-battle-wallpaper.jpg"), 0, 0);
		    guiFont.setColor(Color.RED);
			guiFont.draw(batchGui, "Your Score: " + totalScore, Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight()/2 + 100);
			batchGui.draw(new Texture("button-playagain.png"), Gdx.graphics.getWidth()/2 - 200,
					Gdx.graphics.getHeight()/2 - 130, 400, 140);
		}

		batchGui.end();
	}


	public void initialize(){
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		world = new World(new Vector2(0, -18.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		gameObjects = new ArrayList<GameObject>();

		batchGui = new SpriteBatch();
		guiFont = new BitmapFont(Gdx.files.internal("impactFont.fnt"), false);
		guiFont.setColor(Color.NAVY);
		guiFont.getData().setScale(0.8f);
		remainingCannonballs = new ArrayList<Sprite>();

		Texture cannonBallTexture = new Texture("cannonball2.png");
		for(int i = 0; i < 3; i++){
			Sprite cannonball = new Sprite(cannonBallTexture);
			cannonball.setPosition(50+70*i, Gdx.graphics.getHeight()-100);
			cannonball.setSize(50,50);
			remainingCannonballs.add(cannonball);
		}

		backgroundTexture = new Texture("background-cliffs.jpg");
		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.setPosition(0, -600 );


		//Terrain
		//terrain = new Terrain(36, backgroundSprite.getWidth(), world);
		makePlatform();

		//Buildings
		buildings = new ArrayList<Building>();
		for(int i = 0; i < 5; i++){
			Building b = new Building(1000 + i*450, 100,
					100+(i%5)*50, 100 + i*50,
					3+i%3, 100, world);
			buildings.add(b);
			gameObjects.add(b);
		}

		//Cannon
		cannon = new Cannon(200, 100, 100, world);
		gameObjects.add(cannon);
		gamePhase = GamePhase.CANNON_WAITING;

		totalScore = 0;

        Gdx.input.setInputProcessor(new GestureDetector
                (new TouchController(cannon,this)));

	}

	private void makePlatform(){
		groundTexture = new Texture("brown-platform.png");
		groundSprites = new ArrayList<Sprite>();


		groundBody = new BodyBuilder(0,0,backgroundSprite.getWidth(), 100, world)
				.setBodyType(BodyDef.BodyType.StaticBody)
				.addBoxShape()
				.setProperties(0.1f,10f)
				.build();

		groundTexture = new Texture("rock-block.jpg");
		float totalWidth = 0;
		while(totalWidth < backgroundSprite.getWidth()){
		    Sprite groundBlock = new Sprite(groundTexture);
		    groundBlock.setPosition(totalWidth, 36);
			groundSprites.add(groundBlock);

			Sprite groundBlock2 = new Sprite(groundTexture);
			groundBlock2.setPosition(totalWidth, -28);
			groundSprites.add(groundBlock2);

			totalWidth += groundBlock.getWidth();
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		backgroundTexture.dispose();
		groundTexture.dispose();

		for(GameObject gameObject: gameObjects){
			gameObject.dispose();
		}
	}

	public GamePhase getGamePhase(){
		return gamePhase;
	}
}
