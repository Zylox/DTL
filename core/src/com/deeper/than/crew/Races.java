package com.deeper.than.crew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Races {
	HUMAN(1f, .6f, 50, 75, 5, 100, 1.1f, 1f);
	
	
	public static float FRAME_TIME = .5f;
	
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
	
	private Races(float walkRatio, float swimRatio, float maxOxygen, float coldResist, float damage, float health, float expRatio, float repairRatio){
		this.walkRatio = walkRatio;
		this.swimRatio = swimRatio;
		this.maxOxygen = maxOxygen;
		this.coldResist = coldResist;
		this.damage = damage;
		this.health = health;
		this.expRatio = expRatio;
		this.repairRatio = repairRatio;
		
	}
	
	private void constructAnims(){
		
		if(this == HUMAN){
			String folder = "crew/HumanMale";
			
			downAnim = conAnim(folder+"firstDraftHumanFrontStep1.png",folder+"firstDraftHumanFront.png", folder+"firstDraftHumanFrontStep2.png" );
			upAnim = conAnim(folder+"firstDraftHumanBackStep1.png",folder+"firstDraftHumanBack.png", folder+"firstDraftHumanBackStep2.png" );
			rightAnim = conAnim(folder+"firstDraftHumanFaceRightStep1.png",folder+"firstDraftHumanFaceRight.png", folder+"firstDraftHumanFaceRightStep2.png" );
			leftAnim = conAnim(folder+"firstDraftHumanFaceLeftStep1.png",folder+"firstDraftHumanFaceLeft.png", folder+"firstDraftHumanFaceLeftStep2.png" );
		}
		
	}
	
	private Animation conAnim(String frameOne, String frameTwo, String frameThree){
		TextureRegion[] images = new TextureRegion[3];
		images[0] = new TextureRegion(new Texture(Gdx.files.internal(frameOne)));
		images[1] = new TextureRegion(new Texture(Gdx.files.internal(frameTwo)));
		images[2] = new TextureRegion(new Texture(Gdx.files.internal(frameThree)));
		return new Animation(FRAME_TIME, images);
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
