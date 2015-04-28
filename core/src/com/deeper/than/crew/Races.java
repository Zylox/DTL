package com.deeper.than.crew;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Races {
	HUMAN("human", 1f, .6f, 50, 75, 5, 100, 1.1f, 1f),
	SCAIL("scail", .6f, 1f, 300, 40, 7, 80, 1f, 1f),
	TEKDRONE("tekdrone", 1f, .6f, 100, 90, 3, 100, 1f, 1.5f),
	GOGOR("gogor", .6f, .4f, 40, 100, 5, 150, 1f, 1f),
	MUR("mur", .9f, .8f, 150, 60, 5, 100, 1f, 1f),
	ILLUMI("illumi", 1f, .7f, 50, 75, 5, 70, 1f, 1f),
	GLACIEN("glacien", .8f, .8f, 80, 150, 5, 80, 1f, 1f);
	
	
	public static final String[] humanNames = {"Bob", "Jeb", "Steve", "Jim", "Nick", "Zach", "Will", "Ben", "GIJoe", "Jimmy", "Jake", "Ian", "John", "Bill", "James", "Zebulon", "Horatio",
											   "John", "Michael", "Tim", "Shawn", "Wyatt", "Isaac", "Jan", "Shan", "Thomas", "Jonah", "Nathan", "Matt", "Dakota", "Kitchenset", "Joe",
											   "Ivan", "Tyler", "Goku", "Harlee", "Tom", "Andy", "Matsuda", "Lelouch", "Light", "Lawliet", "Tai", "Izzy", "Ash", "Bambi", "Rock", "Dwayne", "Meller",
											   "Kamina", "Simon", "Kittan", "Aang", "Tenzin", "Meelo", "Sokka", "Varrick", "Bane", "Batman", "Joker", "Clark"};
	
	public static final String[] gogorNames = {"Biggoron", "Dangoro", "Igor", "Darbus", "Darmani", "Daruna", "Gongoron", "Gorko", "Golo", "Goronbeck", "Gortram", "Kagoro", "Brick", "Thing", "Hulk"};
	
	public static final String[] scailNames = {"Ekans", "Arbok", "Asmodeus", "Bailona", "Basilisk", "Belka", "Bijou", "Cleo", "Dragar", "Henry", "Jaja", "Jake", "Kane", "Kaa", "Kali", "Kraken", "Madusa", "Mojo",
												"Monty", "Nigel", "Balboa", "Slither", "Slyther", "Viper", "Ziggy"};
	
	public static final String[] tekdroneNames = {"R2D2", "C3PO", "Talos", "Gnut", "Hal", "IP", "Optimus", "Prime", "Megatron", "Wire", "Gadget", "Android", "Rust", "Combot", "Micro", "Ovroid", "Jet", "Alpha",
													"Mecha", "Axel", "Cole", "Andromeda", "Bit", "Plex", "Circuit"};
	
	public static final String[] murNames = {"Japas", "Laruto", "Lulu", "Oren", "Ruto", "Ralias", "Tijo", "Toto", "Zora", "Mikau", "Ariel", "Posidon", "Raine", "Lake", "Marine", "Glade", "Fjord", "Kai", "Morrissey",
											"Muir", "Seaton", "Thor", "Zeus"};
	
	public static final String[] illumiNames = {"Illum", "Watt", "Volt", "Spark", "Lectro", "Bulb", "Light", "Brighton", "Tesla", "Vodd", "Volgin", "Arc", "Mag", "Fuse", "Pulse", "Pylo", "Wire", "Surge", "Conducto"};
	
	public static final String[] glacienNames = {"Brycen", "Wulfric", "Pryce", "Ice", "Articuno", "Glalie", "Taiga", "Heath", "Aquilo", "Borealis", "Grey", "Polaris", "Lynx", "Timber", "Brisk", "Frosty", "Brumal", 
												"Hiemal", "Frore", "Gelid"};
	
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
		String folder;
		if(race.equals("human")){
			folder = "crew/HumanMale/firstDraftHuman";
			/*downAnim = conAnim(folder+"firstDraftHumanFrontStep1.png",folder+"firstDraftHumanFront.png", folder+"firstDraftHumanFrontStep2.png" );
			upAnim = conAnim(folder+"firstDraftHumanBackStep1.png",folder+"firstDraftHumanBack.png", folder+"firstDraftHumanBackStep2.png" );
			rightAnim = conAnim(folder+"firstDraftHumanFaceRightStep1.png",folder+"firstDraftHumanFaceRight.png", folder+"firstDraftHumanFaceRightStep2.png" );
			leftAnim = conAnim(folder+"firstDraftHumanFaceLeftStep1.png",folder+"firstDraftHumanFaceLeft.png", folder+"firstDraftHumanFaceLeftStep2.png" );*/
		} else if(race.equals("tekdrone")){
			folder = "TekDrone/TekDrone";
		} else if(race.equals("glacien")){
			folder = "Glacien/glacien";
		} else if(race.equals("scail")){
			folder = "Scail/scail";
		} else if(race.equals("mur")){
			folder = "Mur/Mur";
		} else if(race.equals("illumi")){
			folder = "Illumi/illumi";
		} else{
			folder = "Gogor/gogor";
		}
		downAnim = conAnim(folder+"FrontStep1.png",folder+"Front.png", folder+"FrontStep2.png" );
		upAnim = conAnim(folder+"BackStep1.png",folder+"Back.png", folder+"BackStep2.png" );
		rightAnim = conAnim(folder+"FaceRightStep1.png",folder+"FaceRight.png", folder+"FaceRightStep2.png" );
		leftAnim = conAnim(folder+"FaceLeftStep1.png",folder+"FaceLeft.png", folder+"FaceLeftStep2.png" );
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
