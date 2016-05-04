import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;


public class WordNet {
    
    private SAP sp;
    private Map<String, ArrayList<Integer>> mmap;
    private Map<Integer, String> mmap_inv; //the way this is implemented, might as well make string
    //private string[] synsets;
    
    private boolean cycle; //determine if theres cycle
    private int [] post;
    private int count;
    private boolean [] marked;
    private Digraph G;
    private int m_root; //multiple roots
    

   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        if(synsets==null || hypernyms==null){
            
            throw new java.lang.NullPointerException("this is in constructor of wordnet");
        }
            
            In in_syn  = new In(synsets);
            In in_hyp = new In(hypernyms);
            String s;
            String [] fields;
            String [] nouns;
            int vertices, node;
            mmap = new HashMap<String, ArrayList<Integer>>();
            mmap_inv = new HashMap<Integer, String>();
            
            vertices = 0;
            //structure of this is synset id, space separated nouns, definition
            //separated by columns, second field further separated by space
            while(!in_syn.isEmpty()){
                s = in_syn.readLine();
                fields = s.split(",");
                nouns = fields[1].split("\\s+"); 
                node = Integer.parseInt(fields[0]);
                
                mmap_inv.put(node, fields[1]); //no need to check since node grows in sequential order
                
                for(int i = 0; i<nouns.length ; i++){
                    if(mmap.get(nouns[i])==null){
                        mmap.put(nouns[i], new ArrayList<Integer>());
                    }
                    mmap.get(nouns[i]).add(node);
                   // mmap_inv.get(node).add(fields[1]);
                
                }
               
                vertices++;
            }
            
            G = new Digraph(vertices);
            
            
            while(!in_hyp.isEmpty()){
                s = in_hyp.readLine();
                fields = s.split(","); //first value is node, following are what edges it has
                int v = Integer.parseInt(fields[0]);
                for(int i =1; i<fields.length; i++ ){
                    G.addEdge(v, Integer.parseInt(fields[i]));
                }
            }
            
            cycle = false;
            count = 0;
            marked = new boolean[vertices];
            post = new int[vertices];
            
            for(int v = 0;v<vertices;v++){
                post[v] = -1;
            }
            
            for(int v=0; v<vertices; v++){
                if(!marked[v]){            
                    dfs(G,v);
                }
            }
            
            if(cycle){
                throw new java.lang.IllegalArgumentException("Passed graph has cycle");
            }
            
             if(m_root>1){
                throw new java.lang.IllegalArgumentException("Passed DAG has more than one root");
            }
            
            sp = new SAP(G);
    }
    
    private void dfs(Digraph G, int v){
        marked[v] = true;
        
        if(G.outdegree(v)==0 && G.indegree(v)>0){
            m_root++; //want nodes which have other nodes pointing to them
            //dont count floating nodes which are not specified by hypernyns file
        }
        
        for(int w: G.adj(v)){
            
            if(marked[w] && post[w]==-1){
                cycle = true;
            }
            if(!marked[w]){
                dfs(G,w);
            }
        }
        post[v] = count++;
    }

   // returns all WordNet nouns
    public Iterable<String> nouns(){
        return mmap.keySet();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word){
        if(word==null){
            throw new java.lang.NullPointerException("passed word to isNoun is null");
        }
        return mmap.containsKey(word);
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if(nounA==null || nounB==null){
            throw new java.lang.NullPointerException("passed word to distance is null");
        }
        
        if(isNoun(nounA) && isNoun(nounB)){
            ArrayList<Integer> v = mmap.get(nounA);
            ArrayList<Integer> w = mmap.get(nounB);
            return sp.length(v, w);
         }
        else{
           throw new java.lang.IllegalArgumentException("One of the words to distance is not in wordnet");  //invalid input, sap returns -1 if not common ancestor
        }
    }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if(nounA==null || nounB==null){
            throw new java.lang.NullPointerException("One word to Wordnet method sap is null");
        }
        
        if(isNoun(nounA) && isNoun(nounB)){
              ArrayList<Integer> v = mmap.get(nounA);
              ArrayList<Integer> w = mmap.get(nounB);
              int a = sp.ancestor(v, w);
              //preprocessing guarantees this returns the root for single rooted DAG
              //if there is no other common ancestor
              String s = mmap_inv.get(a);
              return s;
              
        }
        else{
           throw new java.lang.IllegalArgumentException("One of the words to sap is not in wordnet");  //invalid input, sap returns -1 if not common ancestor
        }
        
    }

   // do unit testing of this class
   public static void main(String[] args){
       
       WordNet w = new WordNet(args[0], args[1]);
       
       while (!StdIn.isEmpty()) {
           String  nounA = StdIn.readString();
           String nounB = StdIn.readString();
           int length   = w.distance(nounA, nounB);
           String ancestor = w.sap(nounA, nounB);
           StdOut.printf("length = %d, ancestor =%s \n", length, ancestor);
       }
       
   }
}