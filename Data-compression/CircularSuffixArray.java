import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class CircularSuffixArray {
    
    private int len;
    private int CUTOFF;
    private int [] ind;
    private char [] arr;
    private String str;
    
    public CircularSuffixArray(String s){  // circular suffix array of s
         if(s==null){
            throw new java.lang.NullPointerException("this is in constructor of CicularSuffixArray");
        }
        
        str = s;
        len = s.length();
        CUTOFF = 15;
        
        ind = new int[len];
        arr = new char[len];
        for(int i=0;i<len;i++){
            ind[i] = i;
            arr[i] =  s.charAt(i);
        }
        sort(arr,0,len-1,0);
        
    }
    
    private int charAt(char[] a, int i, int d){
        assert d >= 0 && d <= str.length();
        if (d == str.length()) return -1;
        return a[(ind[i]+d)%len];
    }
    
    private void sort(char [] a, int lo, int hi, int d){ //3-way quick sort, starting at d-th character
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }
        
        int lt = lo, gt = hi;
        int v = charAt(a, lo,d);
        int i = lo + 1;
        
        while (i <= gt) {
            int t = charAt(a,i, d);
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt-1, d);
        if (v >= 0){
            sort(a, lt, gt, d+1);
        }
        sort(a, gt+1, hi, d);
        
    }
    
    private void insertion(char[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a,j,j-1, d); j--)
                exch(a, j, j-1);
    }
    
    private void exch(char[] a, int i, int j) {
        //int temp = a[i];
        
        int t = ind[i];
            
        //a[i] = a[j];
        //a[j] = temp;
        
        ind[i] = ind[j];
        ind[j] = t;
    }
    
    private boolean less(char [] a, int v, int w, int d) {
       
        for(int i=d;i<len;i++){
            if(charAt(a,v,i)<charAt(a,w,i)){
                 return true;
            }
            else if(charAt(a,v,i)>charAt(a,w,i)){
                return false;
            }
        }
        
        return false;
    }
    
    
    public int length() {                  // length of s
        return len;
    }
    public int index(int i){               // returns index of ith sorted suffix
        if(i<0 || i>len-1){
              throw new java.lang.IndexOutOfBoundsException("Invalid index");
        }
        else{
            return ind[i];
        }
    }
    
    public static void main(String[] args){  // unit testing of the methods (optional)
        
        int num = args.length;
        for(int i=0;i<num;i++){
            CircularSuffixArray c = new CircularSuffixArray(args[i]);
            int l = c.length();
            
            StdOut.printf("length = %d\n", l);    
            for(int j=0;j<l;j++){
                int ind = c.index(j);
                StdOut.printf("index %d = %d\n",j, ind); 
            }
        }
        //char ch = '!';
        //int ex = (int) ch;
        //StdOut.printf("exclamation in integer is %d\n", ex); 
        
    }
        
}