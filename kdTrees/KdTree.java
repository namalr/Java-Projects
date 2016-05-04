import java.util.*;
 
import edu.princeton.cs.algs4.*;

public class KdTree {
    
    //private static final boolean RED   = true; 
    //private static final boolean BLACK = false;
    private Node head;
    private int size;
    private ArrayList<Point2D> points_range; 
    private double min_dis;
    private Point2D min_point;
    
    public  KdTree() {  // construct an empty set of points
        size = 0;
    }
    
    private class Node{
        Point2D point;
        Node left, right;
        //boolean color; // can add another bool to say up or down, add rectHV for suggested implementation of nearest neighbor
    }
    
//    private boolean isRed(Node x){
//        if(x==null){
//            return false;
//        }
//        else{
//            return x.color==RED;
//        }
//    }
    
    public  boolean isEmpty(){   // is the set empty? 
        return size==0;
    }
    
    public  int size(){    // number of points in the set 
        return size;
    }
    
    public  void insert(Point2D p) {  // add the point to the set (if it is not already in the set)
        if(p==null){
            throw new java.lang.NullPointerException();
        }
        else{
           if(!contains(p)){
                Node x_new = new Node();
                x_new.point = p;
                x_new.left = null;
                x_new.right = null;
                //x_new.color = RED;
                head = insert(head,x_new,0);
                size++;
            }
        }
    }
    
    private Node insert(Node h, Node x_new, int level){
        
        
        if(h==null){
            return x_new;  //base case, where insertion occurs , assume no duplicate point in current tree and taken care of 
        }
        else{
            if(level%2==0){
                if(x_new.point.x()<h.point.x()){
                    h.left = insert(h.left, x_new, level+1); //need to update children nodes and travel down this path
                }
                else if(x_new.point.x()>=h.point.x()){
                    h.right = insert(h.right, x_new,level+1);
                }
            }
            else{
                 if(x_new.point.y()<h.point.y()){
                    h.left = insert(h.left, x_new, level+1); //need to update children nodes and travel down this path
                }
                else if(x_new.point.y()>=h.point.y()){
                    h.right = insert(h.right, x_new,level+1);
                }
            }
//            
//            if(isRed(h.right)&&!isRed(h.left)){
//                h = RotateLeft(h);
//            }
//            if(isRed(h.left) && isRed(h.left.left)){
//                h = RotateRight(h);
//            }
//            if(isRed(h.left) && isRed(h.right)){
//                flipColors(h);
//            }
//            
            return h;
        }
            
   }
    
//    private Node RotateLeft(Node h){ //children must exist otherwise rotation doesnt make sense
//        Node x = h.right;
//        h.right = x.left;
//        x.left = h;
//        x.color = h.color;
//        h.color = RED;
//        return x;
//    }
//    
//    private Node RotateRight(Node h){
//        Node x = h.left;
//        h.left = x.right;
//        x.right = h;
//        x.color = h.color;
//        h.color = RED;
//        return x;
//    }
//    
//    private void flipColors(Node h){
//        h.color = RED;
//        h.left.color = BLACK;
//        h.right.color = BLACK;
//        
//    }
//    
    
    public  boolean contains(Point2D p){            // does the set contain point p? 
        Node point_node = head;
        if(size==0){
            return false;
        }
        int i = 0;
        while (point_node!=null){
            if(i%2==0){ //use x-co-ordinate
                
                if(p.x()<point_node.point.x()){
                    point_node = point_node.left;
                }
                else if(p.x()>=point_node.point.x()){
                     if(p.equals(point_node.point)){
                        return true;
                    }
                    point_node = point_node.right;
                }
            }
            else{ //use y-coordinate
                if (p.y()<point_node.point.y()){
                    point_node = point_node.left;
                }
                else if(p.y()>=point_node.point.y()){
                    if(p.equals(point_node.point)){
                        return true;
                    } 
                    point_node = point_node.right;
                }
            }
            i++;
        }
        return false;
    }
    
    public  void draw() {  // draw all points to standard draw 
        Node h = head;
        transverse(h);
    
    }
    
    private void transverse(Node h){ //recursively transverse through binary tree and draw each point reached
        if(h!=null){
            h.point.draw();
        }
        if(h.left!=null){
            transverse(h.left);
        }
        if(h.right!=null){
            transverse(h.right);
        }
        
    }
    
    
    
    public Iterable<Point2D> range(RectHV rect){             // all points that are inside the rectangle
        
        if(rect==null){
            throw new  java.lang.NullPointerException();
        }
        
        Node h = head;
        points_range = new ArrayList<Point2D>(); //refresh global variable
        int i =0;
        double x_1 = rect.xmin();
        double x_2 = rect.xmax();
        double y_1 = rect.ymin();
        double y_2 = rect.ymax();
        range_point(h, i, x_1, y_1, x_2, y_2);
        
        return points_range;
     
    }
    
    private void range_point(Node h, int i, double x_1, double y_1,double x_2, double y_2){ //recursively go through subtrees to see if contained
        if(h!=null){ //check if null passed by initisl or subsequent function calls
            
            RectHV rect = new RectHV(x_1, y_1, x_2, y_2);
            if(rect.contains(h.point)){
                        points_range.add(h.point);  //adding to global variable
             }
            if(i%2==0){
                if(rect.xmin()>=h.point.x()){
                    //go right
                    range_point(h.right,i+1,x_1,y_1,x_2,y_2);
                }
                else if(rect.xmax()<h.point.x()){
                    //go left
                    range_point(h.left,i+1,x_1,y_1,x_2,y_2);
                }
                else{
                    
                    range_point(h.right,i+1,x_1,y_1,x_2,y_2);
                    range_point(h.left,i+1,x_1,y_1,x_2,y_2);
                }
            }
            else{
                if(rect.ymin()>=h.point.y()){
                    //go right (up)
                    range_point(h.right,i+1,x_1,y_1,x_2,y_2);
                }
                else if(rect.ymax()<h.point.y()){
                    //go left (down)
                   range_point(h.left,i+1,x_1,y_1,x_2,y_2);
                }
                else{
                    //test if in
                    // if(rect.contains(h.point)){
                    //    points_range.add(h.point);
                    //}
                    range_point(h.right,i+1,x_1,y_1,x_2,y_2);
                    range_point(h.left,i+1,x_1,y_1,x_2,y_2);
                }
            }
        }
    }
    
    public Point2D nearest(Point2D p){             // a nearest neighbor in the set to point p; null if the set is empty 
        if(p==null){
            throw new  java.lang.NullPointerException();
        }
          if(size==0){
            return null;
        }
        
        Node h = head;
        min_point = head.point;
        
        min_dis = p.distanceSquaredTo(head.point);
        //int i =0;
        //need to iteratively pass rectangle to points?
        //easier to just have in each node when inserting....
        point_nearest(p,h, 0, 0,0,1,1);
        return min_point;
    }
    
    private void point_nearest(Point2D p, Node h, int i, double x_1, double y_1, double x_2, double y_2){
        if(h!=null){  
            
            if( p.distanceSquaredTo(h.point)<min_dis){
                min_dis = p.distanceSquaredTo(h.point);
                min_point = h.point;
            }
            
            if(i%2==0){ //vertical split
                //figure out which side of split query point is
                if(p.x()<h.point.x()){
                    point_nearest(p,h.left,i+1,x_1,y_1,h.point.x(),y_2); //check subtree on same side
                    RectHV rect = new RectHV(h.point.x(),y_1, x_2,y_2);
                    if(min_dis>rect.distanceSquaredTo(p)){  //only go to this subtree if dis is less
                        point_nearest(p,h.right,i+1,h.point.x(),y_1, x_2,y_2);
                    }
                }
                else{ //query is on the right side
                    point_nearest(p,h.right,i+1,h.point.x(),y_1, x_2,y_2);
                    RectHV rect = new RectHV(x_1,y_1,h.point.x(),y_2);
                    if(min_dis>rect.distanceSquaredTo(p)){
                        point_nearest(p,h.left,i+1,x_1,y_1,h.point.x(),y_2);
                    }
                }
            }
            else{ //horizontal split
                if(p.y()<h.point.y()){
                    point_nearest(p,h.left,i+1,x_1,y_1,x_2,h.point.y());
                    RectHV rect = new RectHV(x_1,h.point.y(), x_2,y_2);
                     if(min_dis>rect.distanceSquaredTo(p)){
                        point_nearest(p,h.right,i+1,x_1,h.point.y(), x_2,y_2);
                    }
                }
                else{
                    point_nearest(p,h.right,i+1,x_1,h.point.y(), x_2,y_2);
                    RectHV rect = new RectHV(x_1,y_1,x_2,h.point.y());
                     if(min_dis>rect.distanceSquaredTo(p)){
                        point_nearest(p,h.left,i+1,x_1,y_1,x_2,h.point.y());
                    }
                }
            }
        }
    
    }

   //public static void main(String[] args)                  // unit testing of the methods (optional) 
}