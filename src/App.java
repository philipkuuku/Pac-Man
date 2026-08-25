import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        //defining the width and height of the game map
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount* tileSize;
        int boardHeight = rowCount*tileSize;

        //now building the frame(window) using JFrame
        JFrame frame = new JFrame("Pac Man");//instantiating thr JFrame class to create an object
        frame.setVisible(false); //seeting the frame to be visible
        frame.setSize(boardWidth,boardHeight);//setting the width and height of the frame
        frame.setLocationRelativeTo(null);//centerng this. It is made relative to no component hence it being centered by default
        frame.setResizable(false); //preventing the user from being able to resize the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// setting the default close operation to be the frames exit button
    
        //creating an instance of the JPanel
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
