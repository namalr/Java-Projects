import edu.princeton.cs.algs4.*;
import java.util.*;

public class PointSET {
    
    private TreeSet<Point2D> points;
    
    public PointSET() {                              // construct an empty set of points 
        points = new TreeSet<Point2D>(new Point2DComparator());
    }
    
    private class Point2DComparator implements Comparator<Point2D>{
        @Override
        public int compare(Point2D p_1, Point2D p_2){
            return p_1.compareTo(p_2);
        }
    }
    
    public boolean isEmpty() {                     // is the set empty? 
        return points.size()==0;
    }
    
    public int size(){                         // number of points in the set 
        return points.size();
    }
    
    public void insert(Point2D p){              // add the point to the set (if it is not already in the set)
        if (p==null){
            throw new  java.lang.NullPointerException();
        }
        else{
            if(!contains(p)){
                points.add(p);
            }
        }
    }
    
    public boolean contains(Point2D p) {           // does the set contain point p? 
        if (p==null){
            throw new  java.lang.NullPointerException();
        }
        else{
            return points.contains(p);
        }
        
    }
    
    public void draw() {                        // draw all points to standard draw 

        for (Point2D p: points){
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect){             // all points that are inside the rectangle 
        if (rect==null){
            throw new  java.lang.NullPointerException();
        }
        else{
            ArrayList<Point2D> points_range = new ArrayList<Point2D>();
            for (Point2D p: points){
                if(rect.contains(p)){
                    points_range.add(p);
                }
            }
            
            return points_range;
                
        }
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p==null){
            throw new  java.lang.NullPointerException();
        }
         if (points.size()==0){
            return null;
        }
        else{
            double min_dis =0;
            Point2D min_point = p;
            int i = 0;
            for (Point2D point: points){
                if(i==0){
                    min_dis = p.distanceSquaredTo(point);
                    min_point = point;
                }
                else{
                    if (p.distanceSquaredTo(point)<min_dis){
                        min_dis = p.distanceSquaredTo(point);
                        min_point = point;
                    }
                }
                i++;
            }
            
            return min_point;
        }
    }

    //public static void main(String[] args){ }                 // unit testing of the methods (optional) 
}