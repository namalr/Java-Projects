import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class MoveToFront {
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        int R = 256;
        char [] ch = new char[R];
        for(int i=0;i<R;i++){
            ch[i] = (char) i;
        }
        
        while(!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            int ind = 0;
            char temp ='a'; 
            for(int i=0;i<R;i++){
                if(ch[i]==c){
                    ind = i;
                    temp = ch[i];
                    break;
                }
            }
            if(ind==0){
                BinaryStdOut.write(0, 8);
            }
            else{
                System.arraycopy(ch,0,ch,1,ind);
                ch[0] = temp;
                BinaryStdOut.write(ind, 8);
            }  
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
         
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        
        int R = 256;
        char [] ch = new char[R];
        for(int i=0;i<R;i++){
            ch[i] = (char) i;
        }
        
        while(!BinaryStdIn.isEmpty()){
            int ind = BinaryStdIn.readInt(8);
            //ind = ind&(0xff);
            if(ind ==0){
                BinaryStdOut.write(ch[0]);
            } 
            else{
                char temp = ch[ind];
                BinaryStdOut.write(ch[ind]);
                System.arraycopy(ch,0,ch,1,ind);
                ch[0] = temp;
            }
        }
        
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        if(args==null){
            throw new java.lang.NullPointerException("operations passed are null");
        }
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
        
}