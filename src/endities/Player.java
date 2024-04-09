package endities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


import game_01.Game;
import game_01.Sound;
import world.Camera;
import world.World;

public class Player extends Entity {
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage [] rightPlayer;
	private BufferedImage [] leftPlayer;
	
	private BufferedImage playerDamage; //efeito dano 
	
	private boolean arma = false;
	
	public int ammo = 0; // munição
	
	public boolean isDamaged = false;
	
	private int damageFrames = 0;
	
	public boolean shoot = false;
	
	public double life = 100, maxlife = 100;
	
	
			
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for (int i = 0; i<4; i++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32+ (i*16), 0, 16, 16);
		
		}
		
		for (int i = 0; i<4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32+ (i*16), 16, 16, 16);
			
			}
		
		
	}
	
	public void tick() {
		
		
		
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY())) { 
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))) { 
			moved = true;
			y+=speed;
		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionArma();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 5) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(arma && ammo > 0) {
			Sound.music5.play();
			ammo--;
			int dx = 0;
			int px = 0;
			int py = 6;
			if(dir == right_dir) {
				px = 10;
				dx = 1;
			}else {
				px = -4;
				dx = -1;
			}
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3, null, dx, 0);
			Game.bullets.add(bullet);
			}
		}
		
		


		
		if(life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
			
			//GAME OVER 
			//System.exit(1); fecha a janela quando a vida acaba
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionAmmo () {
		
		for(int i = 0; i < Game.endities.size(); i++) {
			Entity atual = Game.endities.get(i);
			if (atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=100;
					Sound.music4.play();
					//System.out.println("Munição atual:" + ammo);
					Game.endities.remove(atual); // remover a munição que jogador pegou
				}
			}
		}
	}
	
	

	public void checkCollisionArma() {
		for(int i = 0; i < Game.endities.size(); i++) {
			Entity atual = Game.endities.get(i);
			if (atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					arma = true;
					Sound.music4.play();
					//System.out.println("Munição atual:" + ammo);
					Game.endities.remove(atual); // remover a munição que jogador pegou
				}
			}
		}
	}
	
	
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.endities.size(); i++) {
			Entity atual = Game.endities.get(i);
			if (atual instanceof LifePack) {
				if(Entity.isColidding(this, atual)) {
					life+=10; //quantidade de vida do lifepack (10)
					if (life > 100) //para a vida não passar de 100
						life = 100;
					Sound.music4.play();
					Game.endities.remove(atual); // remover o lifepack que jogador pegou
				}
			}
		}
	}
	
	public void render (Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(arma) {
					g.drawImage(Entity.ARMA_RIGHT, this.getX() - Camera.x, this.getY()-Camera.y, null);
					//desenhar arma para a direita
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(arma) {
					g.drawImage(Entity.ARMA_LEFT, this.getX() - Camera.x, this.getY()-Camera.y, null);
					//desenhar arma para a esquerda
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX()- Camera.x, this.getY() - Camera.y, null);
		}
	}
	
		
}
