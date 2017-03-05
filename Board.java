package game2;

import java.util.List;
import java.util.ArrayList;

public class Board
{
    List<Point> availablePoints;
    List<PointsAndScores> rootsChildrenScore = new ArrayList<>();
    int[][] board = new int[3][3];

    public int evaluateBoard()
    {
        int score = 0;
        int X, O;
        
        for (int i=0; i<3; i++)
        {
            X=0; O=0;
            for (int j=0; j<3; j++)
            {
                if (board[i][j] == 1)
                    X++;
                else if (board[i][j] == 2)
                    O++;
            }
            score += changeInScore(X, O);
        }
        
        for (int j=0; j<3; j++)
        {
            X=0; O=0;
            for (int i=0; i<3; i++)
            {
                if (board[i][j] == 1)
                    X++;
                else if (board[i][j] == 2)
                    O++;
            }
            score+=changeInScore(X, O);
        }
        
        X=0; O=0;
        for (int i=0; i<3; i++)
        {
            if (board[i][i] == 1)
                X++;
            else if (board[i][i] == 2)
                O++;
        }
        score+=changeInScore(X, O);

        X = 0; O = 0;
        for (int i = 0; i<3; i++)
        {
            if (board[i][2-i] == 1)
                X++;
            else if (board[i][2-i] == 2)
                O++;
        }
        score+=changeInScore(X, O);
        
        return score;
    }
    
    private int changeInScore(int X, int O)
    {
        if (X == 3)
            return 100;
        else if (X == 2 && O == 0)
            return 10;
        else if (X == 1 && O == 0)
            return 1;
        else if (O == 3)
            return -100;
        else if (O == 2 && X == 0)
            return -10;
        else if (O == 1 && X == 0)
            return -1;
        return 0;
    }
    
    int uptoDepth = -1;
    
    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn)
    {
        if(beta <= alpha)
        {
            //System.out.println("Pruning at depth = " + depth);
            if(turn == 1)
                return Integer.MAX_VALUE;
            else
                return Integer.MIN_VALUE;
        }
        
        if(depth == uptoDepth || isGameOver())
            return evaluateBoard();
        
        List<Point> pointsAvailable = getAvailableStates();
        
        if(pointsAvailable.isEmpty())
            return 0;
        
        if(depth == 0)
            rootsChildrenScore.clear();
        
        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
        
        for(int i=0; i<pointsAvailable.size(); i++)
        {
            Point point = pointsAvailable.get(i);
            int currentScore = 0;
            
            if(turn == 1){
                placeAMove(point, 1);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 2);
                maxValue = Math.max(maxValue, currentScore); 
                
                alpha = Math.max(currentScore, alpha);
                
                if(depth == 0)
                    rootsChildrenScore.add(new PointsAndScores(currentScore, point));
            }
            else if(turn == 2)
            {
                placeAMove(point, 2);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 1); 
                minValue = Math.min(minValue, currentScore);
                
                beta = Math.min(currentScore, beta);
            }
            
            board[point.getX()][point.getY()] = 0; 
            
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
                break;
        }
        return turn == 1 ? maxValue : minValue;
    }

    public boolean isGameOver()
    {
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    public boolean hasXWon()
    {
        if (board[0][0] == 1 && board[0][0] == board[1][1] && board[0][0] == board[2][2])
            return true;
        else if ( board[0][2] == 1 && board[0][2] == board[1][1] && board[0][2] == board[2][0])
            return true;
        
        for (int i = 0; i < 3; ++i)
        {
            if (board[i][0] == 1 && board[i][0] == board[i][1] && board[i][0] == board[i][2])
                return true;
            else if (board[0][i] == 1 && board[0][i] == board[1][i] && board[0][i] == board[2][i])
                return true;
        }
        return false;
    }

    public boolean hasOWon()
    {
        if (board[0][0] == 2 && board[0][0] == board[1][1] && board[0][0] == board[2][2])
            return true;
        else if ( board[0][2] == 2 && board[0][2] == board[1][1] && board[0][2] == board[2][0])
            return true;
        
        for (int i = 0; i < 3; ++i)
        {
            if (board[i][0] == 2 && board[i][0] == board[i][1] && board[i][0] == board[i][2])
                return true;
            else if (board[0][i] == 2 && board[0][i] == board[1][i] && board[0][i] == board[2][i])
                return true;
        }
        return false;
    }

    public List<Point> getAvailableStates()
    {
        availablePoints = new ArrayList<>();
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                if (board[i][j] == 0)
                    availablePoints.add(new Point(i, j));
            }
        }
        return availablePoints;
    }

    public void placeAMove(Point point, int player)
    {
        board[point.getX()][point.getY()] = player;
    }

    public Point returnBestMove()
    {
        int MAX = Integer.MIN_VALUE;
        int best = -1;

        for (int i=0; i<rootsChildrenScore.size(); i++)
        {
            if (MAX < rootsChildrenScore.get(i).getScore()) {
                MAX = rootsChildrenScore.get(i).getScore();
                best = i;
            }
        }
        return rootsChildrenScore.get(best).getPoint();
    }

    void takeHumanInput(int x, int y)
    {
        Point point = new Point(x, y);
        placeAMove(point, 2);
    }

    public int[][] displayBoard()
    {
        return this.board;
    } 
    
    public void resetBoard()
    {
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
                board[i][j] = 0;
        }
    }
}