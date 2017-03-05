package game2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements ActionListener
{
    public static void main(String[] args)
    {
        frame = new JFrame("Tic Tac Toe Game");
        frame.setContentPane(new Game());
        frame.setLocation(300, 150);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
    
    private static JFrame frame;
    private static String playerOneName = "Human", playerTwoName = "Computer";
    private int playerOneScore, playerTwoScore, totalMatchPlayed, counter;
    private String playerOneIcon, playerTwoIcon;
    private boolean playerOnePlaying;
    public final JButton[][] userButtons;
    private final JButton newGameButton;
    private final JLabel headingLabel;
    JTextArea headingArea;
    Board b = new Board();
    
    public Game()
    {
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        setLayout(new BorderLayout());
        
        //For heading
        JPanel heading = new JPanel();
        heading.setBackground(Color.LIGHT_GRAY);
        heading.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.DARK_GRAY));
        heading.setLayout(new BorderLayout());
        add(heading, BorderLayout.NORTH);
        
        headingLabel = new JLabel("Welcome");
        headingLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        headingLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        heading.add(headingLabel, BorderLayout.NORTH);
        
        //For Scores and total match played
        headingArea = new JTextArea();
        headingArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        headingArea.setBackground(Color.LIGHT_GRAY);
        headingArea.setEditable(false);
        headingArea.setMargin(new Insets(0, 25, 0, 0));
        headingArea.setAutoscrolls(true);
        heading.add(headingArea, BorderLayout.SOUTH);
        
        
        ///for Game Buttons
        JPanel gameButtons = new JPanel();
        gameButtons.setBackground(Color.DARK_GRAY);
        gameButtons.setLayout(new GridLayout(3, 3, 2, 2));
        gameButtons.setBorder(BorderFactory.createMatteBorder(10, 15, 10, 15, Color.LIGHT_GRAY));
        add(gameButtons, BorderLayout.CENTER);
        
        userButtons = new JButton[3][3];
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                userButtons[i][j] = new JButton();
                userButtons[i][j].setEnabled(false);
                userButtons[i][j].setBackground(Color.LIGHT_GRAY);
                userButtons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
                userButtons[i][j].setPreferredSize(new Dimension(120, 90));
                userButtons[i][j].addActionListener(new GameListener());
                gameButtons.add(userButtons[i][j]);
            }
        }
        
        //for buttons
        JPanel buttons = new JPanel();
        buttons.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.DARK_GRAY));
        buttons.setLayout(new GridLayout(1, 2));
        add(buttons, BorderLayout.SOUTH);
        
        newGameButton = new JButton("Toss");
        newGameButton.setBackground(Color.GRAY);
        newGameButton.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 2, Color.DARK_GRAY));
        newGameButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        newGameButton.addActionListener(this);
        buttons.add(newGameButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.DARK_GRAY));
        cancelButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        cancelButton.addActionListener(this);
        buttons.add(cancelButton);
        
        totalMatchPlayed = 0;
        playerTwoScore = 0;
        playerOneScore = 0;
        counter = 0;
        
        toss();
    }
    
    
    private void printPlayer()
    {
        StringBuilder str = new StringBuilder();
        if (playerOnePlaying)
            str.append(playerOneName + " turns");
        else
            str.append(playerTwoName + " turns");
        headingLabel.setText(str.toString());
    }
    
    private void printScore()
    {
        StringBuilder str = new StringBuilder();
        str.append(playerOneIcon + " " + playerOneName + "'s Win: " + playerOneScore + "\n");
        str.append(playerTwoIcon + " " + playerTwoName + "'s Win: " + playerTwoScore + "\n");
        str.append("Total Matches Played: " + totalMatchPlayed);
        headingArea.setText(str.toString());
    }
    
    private void toss()
    {
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                userButtons[i][j].setEnabled(true);
        String str = null;
        playerOneIcon = "O";
        playerTwoIcon = "X";
        if (Math.random() < 0.5)
        {
            playerOnePlaying = true;
            str = playerOneName + " wins the toss.";
        }
        else
        {
            playerOnePlaying = false;
            str = playerTwoName + " wins the toss.";
            
        }
        JOptionPane.showMessageDialog(this, str, "Toss Result", JOptionPane.INFORMATION_MESSAGE);
        printPlayer();
        printScore();
        if (!playerOnePlaying)
        {
            Point p = new Point((int)(Math.random()*3), (int)(Math.random()*3));
            userButtons[p.getX()][p.getY()].setText(playerTwoIcon);
            userButtons[p.getX()][p.getY()].setEnabled(false);
            b.placeAMove(p, 1);
        }
    }
    
    private void checkGame(int row, int col)
    {
        userButtons[row][col].setText(playerOneIcon);
        userButtons[row][col].setEnabled(false);
        Point p = new Point(row, col);
        b.placeAMove(p, 2);
        gameDone();
        
        b.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
        p = b.returnBestMove();
        userButtons[p.getX()][p.getY()].setText(playerTwoIcon);
        userButtons[p.getX()][p.getY()].setEnabled(false);
        b.placeAMove(p, 1);
        
        gameDone();
    }
    
    private void gameDone()
    {
        if (b.isGameOver())
        {
            if (b.hasXWon())
                JOptionPane.showMessageDialog(this, "Computer Win.", "Match Result", JOptionPane.INFORMATION_MESSAGE);
            else if (b.hasOWon())
                JOptionPane.showMessageDialog(this, "User Win.\n It's a draw.", "Match Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "No body Win.\n It's a draw.", "Match Result", JOptionPane.INFORMATION_MESSAGE);
            System.exit(500);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {}
    
    private class GameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            if (src == userButtons[0][0])
                checkGame(0, 0);
            else if (src == userButtons[0][1])
                checkGame(0, 1);
            else if (src == userButtons[0][2])
                checkGame(0, 2);
            else if (src == userButtons[1][0])
                checkGame(1, 0);
            else if (src == userButtons[1][1])
                checkGame(1, 1);
            else if (src == userButtons[1][2])
                checkGame(1, 2);
            else if (src == userButtons[2][0])
                checkGame(2, 0);
            else if (src == userButtons[2][1])
                checkGame(2, 1);
            else if (src == userButtons[2][2])
                checkGame(2, 2);
        }
    }
}