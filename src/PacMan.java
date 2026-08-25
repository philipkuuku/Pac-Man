import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacMan extends JPanel implements ActionListener,KeyListener{

    class Block{ // this is to store the information fro the food,ghosts and pacman
        int x;
        int y;
        int width;
        int height;
        Image image;
        //to save the original x and y positions
        
        int startX;
        int startY;
        char direction= 'U';
        int velocityX = 0;
        int velocityY = 0;

        //creating a constructor
        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY= y;
        }

        void updateDirection(char direction){
            char prevDirection= this.direction;
            this.direction = direction;
            updateVelocity();
            this.x +=this.velocityX;
            this.y +=this.velocityY;
            for(Block wall:walls){
                if(collision(this,wall)){
                    this.x -=this.velocityX;
                    this.y -=this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }

        }

        void updateVelocity(){
            if(this.direction== 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }
            else if(this.direction== 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction== 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if(this.direction== 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        void reset(){
            this.x = startX;
            this.y = startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount* tileSize;
    private int boardHeight = rowCount*tileSize;


    //storing the images for use

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //the tile map ("background")
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X X X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    //using a hashset to store the elements of each described item at the respective place in the game
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char directions[] = {'U','D','L','R'};
    Random random = new Random();
    int score =0;
    int lives = 3;
    boolean gameOver = false;
    boolean gameDone = false;



    PacMan(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //loading the images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanleft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();
        for(Block ghost:ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);

        }

        gameLoop = new Timer(50,this);//20fps
        gameLoop.start();

    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r =0; r < rowCount;r ++){
            for(int c = 0; c < columnCount; c++){
                String row = tileMap[r];//get current row
                char tileMapChar = row.charAt(c);//get current character

                //to figure out where the char is to draw
                int x =c*tileSize;  //how many columns from left
                int y = r*tileSize; //how many rows from top

                //now we create the logic to represent the tileMap by the respective images using If Statements
                if (tileMapChar == 'X'){//block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if(tileMapChar =='b'){// fill with blueGhost image
                    Block ghost = new Block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='r'){// fill with blueGhost image
                    Block ghost = new Block(redGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='p'){// fill with blueGhost image
                    Block ghost = new Block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='o'){// fill with blueGhost image
                    Block ghost = new Block(orangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='P'){// fill with blueGhost image
                    pacman = new Block(pacmanRightImage,x,y,tileSize,tileSize);
                }
                else if(tileMapChar == ' '){//for the food
                    Block food = new Block(null,x+14,y+14,4,4);
                    foods.add(food);

                }

            }
        }

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x,pacman.y,pacman.width,pacman.height,null);
        
        for (Block ghost : ghosts){//drawing ghosts
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
        }
        //drawing walls
        for (Block wall : walls){//drawing ghosts
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
        }

        //drawing food and setting color to white for visibility
        g.setColor(Color.WHITE);
        for (Block food : foods){//drawing ghosts
            g.fillRect(food.x,food.y,food.width,food.height);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,18));
        if (gameOver){
            g.drawString("GAME OVER:  "+  String.valueOf(score),tileSize/2, tileSize/2);
        }
        else if(gameDone){
            g.drawString("GAME DONE:  "+  String.valueOf(score),tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x"+ String.valueOf(lives)+" Score: "+String.valueOf(score),tileSize/2, tileSize/2);
        }
    }

    public void move(){ // to move pacman
        pacman.x +=pacman.velocityX;
        pacman.y +=pacman.velocityY;

        //checking for collisions
        for(Block wall :walls){
            if(collision(pacman,wall)){
                pacman.x -=pacman.velocityX;
                pacman.y -=pacman.velocityY;
                break;
            }
        }
        //check for pacman ghost collision
        for(Block ghost:ghosts){
            if(collision(pacman,ghost)){
                lives -=1;
                if(lives ==0){
                    gameOver = true;
                    return;

                }
                resetPositions();
            }
            //checking if all food has been eaten
            if (foods.isEmpty()){
                gameDone= true;
            }
            
        }

        //check ghost collisions
        for(Block ghost:ghosts){
            if(ghost.y ==tileSize*9 &&ghost.direction!='U' &&ghost.direction !='D'){
                ghost.updateDirection('U');
            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall:walls){
                if (collision(ghost,wall)||ghost.x<=0||ghost.x + ghost.width >=boardWidth){
                    ghost.x-=ghost.velocityX;
                    ghost.y-=ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }
        //checking food collision
        Block foodEaten = null;
        for(Block food:foods){
            if(collision(pacman,food)){
                foodEaten = food;
                score +=10;
            }
        }
        foods.remove(foodEaten);

    }


    //the logic to check if there is collision

    public boolean collision(Block a, Block b){ ///collision detection formula
        return a.x <b.x + b.width &&
        a.x +a.width > b.x &&
        a.y < b.y+ b.height &&
        a.y + a.height > b.y;
    }


    public void resetPositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        
        for(Block ghost: ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
                }
        }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key pressed is : "+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }

        if (pacman.direction =='U'){
            pacman.image = pacmanUpImage;
        }
        else if(pacman.direction =='L'){
            pacman.image = pacmanLeftImage;
        }
        else if(pacman.direction =='D'){
            pacman.image = pacmanDownImage;
        }
        else if(pacman.direction =='R'){
            pacman.image = pacmanRightImage;
        }

        if(gameOver || gameDone){
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameDone= false;
            gameLoop.start();
        }

    }

}
