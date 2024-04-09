package game_01;



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import endities.BulletShoot;
import endities.Enemy;
import endities.Entity;
import endities.Player;
import graficos01.Spritesheet;
import graficos01.UI;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener {
	//quando usamos o Keylistener e MouseListener estamos ultilizado e eventos com teclado e mouse repectivamente
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	
	
	
	private BufferedImage image;
	
	
	public static List<Entity> endities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	
	private boolean showMassageGameOver = true;
	private int framesGameOver = 0 ;
	private boolean restartGame = true;
	public Menu menu;
	
	public Game() {
		
		rand = new Random();
		addKeyListener(this);// adicionar os eventos de teclado
		//addMouseListener(this);// adicionar os eventos de mouse
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT * SCALE));
		initFrame();
		//inicializando objetos.
		
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		endities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		endities.add(player);
		world = new World("/level1.png");
		
		menu = new Menu();
		
		
		
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this); //this é o canvas, frame vai conseguir pegar de fatos todas as propriedades 
		frame.setResizable(false); //não deixar o usuário redimencionar a Janela
		frame.pack(); //é um medoto calcula certas dimenções 
		frame.setLocationRelativeTo(null); // Janela no centro da tela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chamar o metodo frame para quando fechar o programa não fique rodando. 
		frame.setVisible(true); //para quando inicializar já ficar vizivel
	}
	
	public synchronized void start() {
		thread = new Thread(this); 
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	

	public static void main(String[] args) {
		Game g = new Game();
		g.start();
	}
	
	public void tick() { //tick é o update(cuida da logica do jogo)
		
		this.verificar();
		
		if(gameState == "NORMAL") {
			
			
			this.restartGame = false;
			for(int i = 0; i < endities.size(); i++) {
				Entity e = endities.get(i);
				e.tick();
			}
			
			
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
				
			}
			
			if(enemies.size() == 0) {
				// avançar para o próximo level
				
				CUR_LEVEL ++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			//System.out.println("GAME OVER!!!");
			this.verificar();
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMassageGameOver) 
					this.showMassageGameOver = false;
					else 
						this.showMassageGameOver = true;
				
			}
			
			if(restartGame) {
				
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1 ;
				String newWordl = "level"+CUR_LEVEL+".png";
				World.restartGame(newWordl);
			}
			
			
			
		}else if(gameState == "MENU") {
			
			menu.tick();
		}
		
		
		
		
	}
	
	public void verificar () {
	
		if(gameState == "MENU" || gameState == "NORMAL") {
			Sound.music3.play();
		}else if (gameState == "GAME_OVER") {
			Sound.music.play();
		}
	}	
	
	
	
	
	public void render() { //cuida dos gráficos do jogo 
		//rederização funciona de cima para baixo
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0)); // vermelho, verde, azul.
		g.fillRect(0,0,WIDTH,HEIGHT); // linha 73 e 74 são uma inicialização de tela, sempre começar assim.
		
	
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i = 0; i < endities.size(); i++) {
			Entity e = endities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
			
		}
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial",Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo,580,20);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0,WIDTH* SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial",Font.BOLD,40));
			g.setColor(Color.white);
			g.drawString("GAME OVER!!!",(WIDTH*SCALE)/2 - 120, (HEIGHT*SCALE)/2 -20);
			g.setFont(new Font("arial",Font.BOLD,32));
			if(showMassageGameOver)
			g.drawString(">>Pressione Enter para reiniciar<<",(WIDTH*SCALE)/2 - 260, (HEIGHT*SCALE)/2 +50);
		}else if (gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}
	
	public void run () {
		long lastTime = System.nanoTime(); // pega o tempo atual do nosso computador em nano segundos(ele é muito preciso)
		double amountOfTicks = 60.0; // 60 frames por segundo
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime)/ ns;
			lastTime = now;
			if(delta >= 1) {
				tick(); //sempre atualize o jogo antes de renderizar
				render();
				frames++;
				delta--;
				
			}
			if (System.currentTimeMillis() - timer >= 1000) { // quer dizer que passou um segundo desde a última mensagem
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer +=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// andar para a direita se a seta direita ou tecla D, forem clicadas
			player.right = true;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// para a esquerda
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if (gameState == "MENU") {
				menu.up = true;
			}
			
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if (gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		
		
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// andar para a direita se a seta direita ou tecla D, forem clicadas
			player.right = false;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// para a esquerda
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}


	

}
