package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import endities.Bullet;
import endities.Enemy;
import endities.Entity;
import endities.LifePack;
import endities.Player;
import endities.Weapon;
import game_01.Game;
import graficos01.Spritesheet;

public class World {
	
	public static Tile [] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World (String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
			int [] pixels = new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx< map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx+(yy*map.getWidth())];
					tiles[xx+(yy* WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if ( pixelAtual == 0xFF000000) {
						tiles[xx+(yy* WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						//chão (FLOOR)
					}else if (pixelAtual == 0xFFFFFFFF) {
						tiles[xx+(yy* WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
						//parede (WALL)
					}else if (pixelAtual == 0xFF4454FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if (pixelAtual == 0xFFFF0000) {
						//Enemy
						
						Enemy en = new Enemy(xx*16,yy*16,16,16, Entity.ENEMY_EN);
						Game.endities.add(en);
						Game.enemies.add(en);
						
					}else if (pixelAtual == 0xFFFF7F7F) {
						//Weapon (arma)
						Game.endities.add(new Weapon(xx*16,yy*16,16,16, Entity.WEAPON_EN));
					}else if (pixelAtual == 0xFFFF7FED) {
						//Life pack (vida)
						Game.endities.add(new LifePack(xx*16,yy*16,16,16, Entity.LIFEPECK_EN));
					}else if (pixelAtual == 0xFFFFE97F) {
						//bullet (munição)
						Game.endities.add(new Bullet(xx*16,yy*16,16,16, Entity.BULLET_EN));
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext /TILE_SIZE;
		int y1 = ynext /TILE_SIZE;
		
		int x2 = (xnext+ TILE_SIZE-1) /TILE_SIZE;
		int y2 = ynext /TILE_SIZE;
		
		int x3 = xnext /TILE_SIZE;
		int y3 = (ynext + TILE_SIZE-1) /TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE-1) /TILE_SIZE;
		int y4 = (ynext + TILE_SIZE-1) /TILE_SIZE;
		
		
		return !(
				 (tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile )
				 );
		
	}
	
	public static void restartGame(String level) {
		Game.endities.clear();
		Game.enemies.clear();
		Game.endities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.endities.add(Game.player);
		Game.world = new World("/"+ level);
		return;
	}
	
	
	public void render (Graphics g) {
		int xstart = Camera.x >> 4; // esse ">>4" é a mesma coisa que: Camera.x/16
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 00 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles [xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
