//import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class Outcast {
    
    private WordNet w;
    
    public Outcast(WordNet wordnet){         // constructor takes a WordNet object
        
        if(wordnet==null){
            
            throw new java.lang.NullPointerException("this is in constructor of outkast");
        }
    
        w = wordnet;
    }
 
    public String outcast(String[] nouns){   // given an array of WordNet nouns, return an outcast
        
        if(nouns==null){
            
            throw new java.lang.NullPointerException("this is outcast from list of nouns");
        }
        
        //stupid solution is keep track of max, iterate through n^2 calculations
        int max_dist = 0;
        int ind = 0;
        for(int i =0; i<nouns.length; i++){
            int cur_dist = 0;
            for(int j =0; j<nouns.length;j++){
                if(i==0){
                    max_dist += w.distance(nouns[0], nouns[j]);
                }
                else{
                    cur_dist += w.distance(nouns[i], nouns[j]);
                }
            }
            if(cur_dist > max_dist){
                max_dist = cur_dist;
                ind = i;
            }
        }
        
        return nouns[ind];
    }
    
    
    public static void main(String[] args){  // see test client below
        
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}