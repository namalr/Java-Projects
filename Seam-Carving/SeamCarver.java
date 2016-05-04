import java.awt.Color;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class SeamCarver {
    
    
    private int [] pic_color;
    private boolean transpose, call_from_vertical, remove_vertical, call_from_seam;
    private int width, height, width_1, height_1;
    
    public SeamCarver(Picture picture){                // create a seam carver object based on the given picture
        
        if(picture==null){
            throw new java.lang.NullPointerException("this is constructor of Seam");
        }
        
         Picture pic = new Picture(picture);
         transpose = false;
         call_from_vertical = false;
         remove_vertical = false;
         width = pic.width(); //used for transpose and other calculation
         height = pic.height();
         call_from_seam = false;
         
         width_1 = width;
         height_1 = height;
         
         pic_color = new int[width*height];
         for(int i =0;i<width;i++){
             for(int j = 0; j<height; j++){
                 pic_color[i*height + j] = pic.get(i,j).getRGB();  
             }
         }
         
    
    }
    
    public Picture picture() {                         // current picture
        
        if(transpose){
            pic_transpose();
            transpose = false;
        }
        
        return pic_Reconstruct();
        
  
    }
    
    private Picture pic_Reconstruct(){
        //use pic_color to form picture
        //set pic to this
        Picture temp = new Picture(width, height);
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                Color color = new Color(pic_color[i*height+j]);
                temp.set(i,j, color);
            }
        }
        
        return temp;
    
    }
  
    public     int width(){                            // width of current picture
        return width_1;
    }
    
    public     int height() {                          // height of current picture
        return height_1;
    }
    
    public  double energy(int x, int y){               // energy of pixel at column x and row y
        
        if(transpose && !call_from_seam){ //only swap if use queries for energy and internal is transposed
                     int temp = x;
                     x = y;
                     y = temp;
        }
        
        
        if( y>=0 && y<height && x>=0 && x<width){
            if(x==0 || x==width-1 || y==0 || y==height-1){
                return 1000.0;
            }
            else{
                 
                double e_x = energy_x(x,y);
                double e_y = energy_y(x,y);
                
                return Math.sqrt(e_x+e_y);
            }
        }
        
        else{
            throw new  java.lang.IndexOutOfBoundsException("Invalid query for energy");
        }
   }
    
    private double energy_x(int x, int y){
        
         
            //get colors from point (x-1,y) and (x+1,y)
            int c_l = pic_color[(x-1)*height + y];
            int c_r = pic_color[(x+1)*height + y];
            
            double red_d = ((c_l>>16)&0xFF)-((c_r>>16)&0xFF);
            double green_d = ((c_l>>8)&0xFF)-((c_r>>8)&0xFF);
            double blue_d = (c_l&0xFF)-(c_r&0xFF);
            
            return red_d*red_d + green_d*green_d + blue_d*blue_d;
    }
    
     private double energy_y(int x, int y){
              
         //get colors from point (x,y-1) and (x,y+1)
            int c_u = pic_color[x*height + y-1];
            int c_d = pic_color[x*height + y+1];
            
            double red_d = ((c_u>>16)&0xFF)-((c_d>>16)&0xFF);
            double green_d = ((c_u>>8)&0xFF)-((c_d>>8)&0xFF);
            double blue_d = (c_u&0xFF)-(c_d&0xFF);
            
         return red_d*red_d + green_d*green_d + blue_d*blue_d;
    }
    
     public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
         
        if(!call_from_vertical && transpose){
                 pic_transpose();
                 transpose = false;
        }
             
        if(call_from_vertical && !transpose){
                 pic_transpose();
                 transpose = true;
        }
             
        call_from_vertical = false;
         
         
        if(height>=1){
             
             double[] energy_matrix = new double[height];
             double[] energy_current = new double[height];
             int[] min_path_matrix = new int[width*height];
             int ind =0; //tracking from prior column
             int ind_min = 0; //current column 
             
             for(int j =0; j<height; j++){
                 energy_matrix[j] = 1000.0;
                 min_path_matrix[j] = -1;
             }
             
             for(int i=1;i<width;i++){
                 
                 double min = Double.POSITIVE_INFINITY; //make sure to reinitialize for each column
                 ind_min = 0; //only used for last column
                 
                 for(int j=0;j<height;j++){
                   
                         double min_energy = energy_matrix[j];
                         ind = (i-1)*height + j; //need (i-1)*height since we have matrix of paths
                         
                         if(j+1<height){
                             if(min_energy > energy_matrix[j+1]){
                                 min_energy = energy_matrix[j+1];
                                 ind = (i-1)*height +j+1;
                             }
                         }
                         if(j-1>=0){
                              if(min_energy > energy_matrix[j-1]){
                                  min_energy = energy_matrix[j-1];
                                  ind = (i-1)*height +j-1;
                              }
                         }
                         
                         call_from_seam = true;

                         energy_current[j] = min_energy + energy(i,j);
                         call_from_seam = false;
                         
                         min_path_matrix[i*height+j] = ind;
                         
                         if(energy_current[j]<min){
                             min = energy_current[j];
                             ind_min = i*height+j; 
                         } 
                 }

                 energy_matrix = energy_current;
                 energy_current = new double[height];
             }

             
             int[] s = new int[width];
             int arr_ind = width-1;
             for(int x = ind_min; x!=-1; x=min_path_matrix[x]){
                 int j = x%height;
                 s[arr_ind--] = j;
             }
            
             return s;
             
         }
         
      else{
          throw new java.lang.IllegalArgumentException(" ");
      }
    }

             
     public   int[] findVerticalSeam(){                 // sequence of indices for vertical seam
         
         
        call_from_vertical = true;  //to differentiate from calling VerticalSeam vs. HorizontalSeam
        return findHorizontalSeam();
         
     }
     
     private void pic_transpose(){
        
         //this changes pic object
         int [] temp = new int[width*height];
         
         for (int i=0; i<width;i++){
             for(int j=0;j<height;j++){
                 temp[j*width + i] = pic_color[i*height+j];
             }
         }
         
         pic_color = temp; 
         
         int w = height; //reset global variables used in other methods for transposed pic
         height = width;
         width = w;
         
     
     }
     
     public    void removeHorizontalSeam(int[] seam){   // remove horizontal seam from current picture
         
         if(seam==null){
             throw new java.lang.NullPointerException("null sent to seam removal method");
         }
         
         
         if(!remove_vertical && transpose){
             pic_transpose();
             transpose = false;
         }
         
         //other two cases correspond to not transpose and we want horizontal seam
         //or image is transposed and we get vertical seam of original by running this method on transpose
         
         if(remove_vertical && !transpose){
            
             pic_transpose();
             transpose = true;
         }
         
         remove_vertical = false; //reset signal to indicate call from vertical seam is no longer active
         
         if(height<=1){
             throw new java.lang.IllegalArgumentException("Image is only a line");
         }
         
         //current check wastes time tranposing image and doing nothing if invalid seams, and alternating between horizontal and vertical
         int len = 0;
         int prev = 0;
         for(int i: seam){
             if(i<0 || i>height-1){
                 throw new java.lang.IllegalArgumentException("invalid range for pixel to be removed");
             }
             if(len==0){
                 prev = i;
             }
             else{
                 if(i-prev<-1 || i-prev>1){
                     throw new java.lang.IllegalArgumentException("consecutive elements in seam too far apart");
                 }
                 prev = i;
             }
             len++;
         }
         if(len!=width){
             throw new java.lang.IllegalArgumentException("length of seam to be removed not valid");
         }
         
         update_HorizontalSeam(seam);
         //have to update color array
         
     } 
     
     private void update_HorizontalSeam(int[] seam){
         
         int [] temp = new int[width*(height-1)];
        
         int len = 0;
         //int ind = 0;
         
         System.arraycopy(pic_color, 0, temp, 0, seam[0]);
         int prev = seam[0];
         int ind = seam[0];
         for(int i =1;i<width;i++){
             
             //int ind_arr = 0;
             //for(int j=0;j<height;j++){
             //    if(j!=seam[i]){
             //        temp[i*(height-1)+ind_arr] = pic_color[i*height+j];
             //       ind_arr++; 
             //    }
             //}
             len = seam[i]+i*height-prev-1;   //recall that seam is not indexed according to 1D array
             System.arraycopy(pic_color, prev+1,temp,ind, len);
             prev= seam[i]+i*height;
             ind +=len;
         }
         
         System.arraycopy(pic_color, prev+1, temp, ind, width*height-prev-1);
         
         pic_color = temp;
         
         height--; //this ensure dimensions of picture is updated
             if(transpose){
                 width_1 = height;
             }
             else{
                 height_1 = height;
             }
     
     }
     
     
     public    void removeVerticalSeam(int[] seam){     // remove vertical seam from current picture
         
         remove_vertical = true;
         removeHorizontalSeam(seam);
         
     }
}