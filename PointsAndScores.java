package game2;

public class PointsAndScores
{
    private int score;
    private Point point;
    
    public PointsAndScores(int score, Point point)
    {
        this.score = score;
        this.point = point;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public Point getPoint()
    {
        return this.point;
    }
}