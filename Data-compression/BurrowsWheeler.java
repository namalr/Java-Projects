import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class BurrowsWheeler {
    
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode(){
        
            
            String s = BinaryStdIn.readString();
            //BinaryStdIn.close();
            CircularSuffixArray c = new CircularSuffixArray(s);
            
            StringBuilder s_1 = new StringBuilder();
            int len  = c.length();
            int first = 0;
            for(int i=0;i<len;i++){
                int ind = c.index(i);
                if(ind ==0){
                    ind = len-1;
                    first = i;
                }
                else{
                    ind-=1;
                }
                s_1.append(s.charAt(ind));
            }
            
            BinaryStdOut.write(first);
            BinaryStdOut.write(s_1.toString());
            BinaryStdOut.flush();
            BinaryStdOut.close();
        
        
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode(){
        
        
           int first = BinaryStdIn.readInt();
           String s = BinaryStdIn.readString();
           BinaryStdIn.close();
           char [] ch = s.toCharArray();
           
           int N  = ch.length;
           int R = 256;
           int [] count = new int[R+1];
           int [] next = new int[N];
        
           for(int i=0;i<N;i++){
               count[(int)ch[i] + 1]++;
           }
           for(int i =0;i<R;i++){
               count[i+1]+=count[i];
           }
        
           char [] aux = new char[N];
           
           for(int i=0;i<N;i++){
               int pos = count[(int)ch[i]];
               aux[pos] = ch[i];
               next[pos] = i;
               count[(int)ch[i]]++;
           }
        
           for(int i =0;i<N;i++){
               ch[i] = aux[i]; //need this for getting first character in sorted suffixes
           }
           
           int last = 0;
           int ind = first;
           for(int inc = 0; inc<N; inc++){
               BinaryStdOut.write(ch[ind],8);
               last = next[ind];
               ind = next[ind];
           }
           // BinaryStdOut.write(ch[last],8);
           
           
           BinaryStdOut.flush();
           BinaryStdOut.close();
  
    
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args){
         if(args==null){
            throw new java.lang.NullPointerException("operations passed are null");
        }
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}