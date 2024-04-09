package endities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game_01.Game;
import world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPECK_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	//VIDA
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(112, 0, 16, 16);
	//ARMA
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	//MUNIÇÃO
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	//INIMIGO 
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144,16,16,16);
	//INIMIGO LEVANDO DANO
	public static BufferedImage ARMA_RIGHT = Game.spritesheet.getSprite( 128, 0, 16, 16);
	//POSIÇÃO DA ARMA PARA O LADO DIREITO
	public static BufferedImage ARMA_LEFT = Game.spritesheet.getSprite( 144, 0, 16, 16);
	//POSIÇÃO DA ARMA PARA O LADO ESQUERDO 
	
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	public boolean debug = false;
	
	private BufferedImage sprite;
	
	private int maskx, masky, mwidth, mheight;
	
	
	public Entity (int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask (int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public int getX() {
		return (int)this.x;
	}


	public void setX(int newX) {
		this.x = newX;
	}


	public int getY() {
		return (int)this.y;
	}


	public void setY(int newY) {
		this.y = newY;
	}


	public int getWidth() {
		return this.width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return this.height;
	}


	public void setHeight(int height) {
		this.height = height;
	}
	
	public void tick  () {
		
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY()+e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY()+e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x,this.getY() - Camera.y , null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX()+ maskx - Camera.x,this.getY()+masky - Camera.y, mwidth, mheight);
	}

}
