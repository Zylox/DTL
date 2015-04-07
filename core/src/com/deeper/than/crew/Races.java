package com.deeper.than.crew;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Races {
	HUMAN("human", 1f, .6f, 50, 75, 5, 100, 1.1f, 1f);
	
	public static final String[] humanNames = {"Bob", "Jeb", "Steve"};
	
	public static float FRAME_TIME = .5f;
	
	private String race;
	private float walkRatio;
	private float swimRatio;
	private float maxOxygen;
	private float coldResist;
	private float damage;
	private float health;
	private float expRatio;
	private float repairRatio;
	private Animation leftAnim;
	private Animation rightAnim;
	private Animation upAnim;
	private Animation downAnim;
	
	private Races(String race, float walkRatio, float swimRatio, float maxOxygen, float coldResist, float damage, float health, float expRatio, float repairRatio){
		this.race = race;
		this.walkRatio = walkRatio;
		this.swimRatio = swimRatio;
		this.maxOxygen = maxOxygen;
		this.coldResist = coldResist;
		this.damage = damage;
		this.health = health;
		this.expRatio = expRatio;
		this.repairRatio = repairRatio;
		leftAnim = null;
		rightAnim = null;
		upAnim = null;
	}
	
	public static void loadAnims(){
		for(Races r : values()){
			r.constructAnims();
		}
	}
	
	private void constructAnims(){
		
		if(race.equals("human")){
			String folder = "crew/HumanMale/";
			
			downAnim = conAnim(folder+"firstDraftHumanFrontStep1.png",folder+"firstDraftHumanFront.png", folder+"firstDraftHumanFrontStep2.png" );
			upAnim = conAnim(folder+"firstDraftHumanBackStep1.png",folder+"firstDraftHumanBack.png", folder+"firstDraftHumanBackStep2.png" );
			rightAnim = conAnim(folder+"firstDraftHumanFaceRightStep1.png",folder+"firstDraftHumanFaceRight.png", folder+"firstDraftHumanFaceRightStep2.png" );
			leftAnim = conAnim(folder+"firstDraftHumanFaceLeftStep1.png",folder+"firstDraftHumanFaceLeft.png", folder+"firstDraftHumanFaceLeftStep2.png" );
		}
		
	}
	
	public String getRandomName(){
		Random ran = new Random();
		if(race.equals("human")){
			return humanNames[ran.nextInt(humanNames.length-1)];
		}
		return "messedUP";
	}
	
	private Animation conAnim(String frameOne, String frameTwo, String frameThree){
		TextureRegion[] images = new TextureRegion[4];
		TextureRegion middle;
		images[0] = new TextureRegion(new Texture(Gdx.files.internal(frameOne)));
		middle = new TextureRegion(new Texture(Gdx.files.internal(frameTwo)));
		images[1] = middle;
		images[2] = new TextureRegion(new Texture(Gdx.files.internal(frameThree)));
		images[3] = middle;
		return new Animation(FRAME_TIME, images);
	}

	public static Races getRace(String raceName){
		if(raceName.equals("human")){
			return HUMAN;
		}
		return null;
	}
	
	public String getRace() {
		return race;
	}

	public Animation getLeftAnim() {
		return leftAnim;
	}

	public Animation getRightAnim() {
		return rightAnim;
	}

	public Animation getUpAnim() {
		return upAnim;
	}

	public Animation getDownAnim() {
		return downAnim;
	}

	public float getWalkRatio() {
		return walkRatio;
	}

	public float getSwimRatio() {
		return swimRatio;
	}

	public float getMaxOxygen() {
		return maxOxygen;
	}

	public float getColdResist() {
		return coldResist;
	}

	public float getDamage() {
		return damage;
	}

	public float getHealth() {
		return health;
	}

	public float getExpRatio() {
		return expRatio;
	}

	public float getRepairRatio() {
		return repairRatio;
	}
	
	
}
