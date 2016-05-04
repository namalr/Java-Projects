import java.util.*;
import java.lang.*; 
import edu.princeton.cs.algs4.*;

public class SAP {
    
    private int[] post; //for cycle detection, postorder of i-th vertex
    private Queue<Integer> postorder; //iterable of postorder vertices
    private boolean [] marked; //visited nodes for inital DFS for cycle detection and topological sort
    private boolean cycle; //is there cycle?
    private int vertices; //number of vertices in graph
    private int count; //increment variable for postorder
    private Digraph G; //directed graph
    ////////////////////////
    private int ancestor, ancestor_i;
    private int v_curr, w_curr;
    private ArrayList<Integer> v_curr_i;
    private ArrayList<Integer> w_curr_i;
    private double dist_sum, dist_sum_i;
    private boolean [] marked_v;
    private boolean [] marked_w;
    private double [] dist_v;
    private double [] dist_w;
    private Queue<Integer> q_v;
    private Queue<Integer> q_w;

   // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if(G==null){
            throw new java.lang.NullPointerException("this is constructor of SAP");
        }
        
        this.G = new Digraph(G);
        vertices = this.G.V();
        count = 0;
        marked = new boolean[vertices];
        
        post = new int[vertices];
        for(int v = 0;v<vertices;v++){
            post[v] = -1;
        }
        
        postorder = new Queue<Integer>();
        cycle = false;
        
        v_curr = -1;
        w_curr = -1;
        
        v_curr_i = new ArrayList<Integer>();
        w_curr_i = new ArrayList<Integer>();
    
         for(int v=0;v<vertices;v++){
             if(!marked[v]){            
                 dfs(this.G,v);
             }
         }
     
    }
    
    
    private Iterable<Integer> reversePost() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }
    
    private boolean hasCycle(){
        return cycle;
    }
    
    private void dfs(Digraph G, int v){
        marked[v] = true;
        
        for(int w: G.adj(v)){
            
            if(marked[w] && post[w]==-1){
                cycle = true;
            }
            if(!marked[w]){
               
                
                dfs(G,w);
            }
            
        }
        postorder.enqueue(v);
        post[v] = count++;
    
    }
    
    private void BreadthFirstPaths(int v, int w) {
        
        // breadth-first search from a single source
        //if all paths have equal weight, weight 1 etc, this gives shortest path
        
        
        
         while (!q_v.isEmpty() || !q_w.isEmpty()) { 
             if(!q_v.isEmpty()){ 
                 int v_node= q_v.dequeue();
                 
                 if (dist_v[v_node]+1<dist_sum){
                     for (int v_adj : G.adj(v_node)) {
                         if (!marked_v[v_adj]) {
                             //edgeTo[u] = v;
                             dist_v[v_adj] = dist_v[v_node] + 1;
                             marked_v[v_adj] = true;
                             q_v.enqueue(v_adj);
                         }
                         if(marked_w[v_adj] && marked_v[v_adj] &&  (dist_v[v_adj] + dist_w[v_adj] < dist_sum)){ //has been visited by other source, this is hit on first instance
                             dist_sum = dist_v[v_adj] + dist_w[v_adj];
                             ancestor = v_adj; 
                             
                         }
                     }
                 }
             }
             
           
            
             if(!q_w.isEmpty()){
                 int w_node = q_w.dequeue();
                 if(dist_w[w_node]+1<dist_sum){
                     for(int w_adj: G.adj(w_node)){
                         if (!marked_w[w_adj]) {
                             //edgeTo_1[u] = v_1;
                             dist_w[w_adj] = dist_w[w_node] + 1;
                             marked_w[w_adj] = true;
                             q_w.enqueue(w_adj);
                         }
                         if(marked_w[w_adj] && marked_v[w_adj] &&  (dist_v[w_adj] + dist_w[w_adj] < dist_sum)){ //has been visited by other source, this is hit on first instance
                             dist_sum = dist_v[w_adj] + dist_w[w_adj];
                             ancestor = w_adj; 
                             
                         }
                         
                     }
                 }
             }  
             
        }
    } 
    
    
    private void AcyclicSP(int v, int w){
    
        //Stack<Integer> rev_postorder = new Stack<Integer>();
        //rev_postorder = reversePost(); //topological order of DAG   
        
         for(int v_node: reversePost()){
             if(dist_v[v_node]+1<dist_sum){
                 for(int v_adj : G.adj(v_node)){
                     if(dist_v[v_adj]>dist_v[v_node]+1){  //for non-weighted graph, this corresponds to first meeting, i.e. shortest path prior value is infinity
                         dist_v[v_adj] = dist_v[v_node]+1;
                         marked_v[v_adj] = true;  
                     }
                     
                     if(marked_w[v_adj] && marked_v[v_adj] && (dist_v[v_adj] + dist_w[v_adj]<dist_sum)){
                         dist_sum = dist_v[v_adj] + dist_w[v_adj];
                         ancestor = v_adj;                 
                     }
                 }
             }
             if(dist_w[v_node]+1<dist_sum){
                   for(int v_adj : G.adj(v_node)){
                     if(dist_w[v_adj]>dist_w[v_node]+1){
                         dist_w[v_adj] = dist_w[v_node]+1;
                         marked_w[v_adj] = true;
                     }
                     
                     if(marked_w[v_adj] && marked_v[v_adj] && (dist_v[v_adj] + dist_w[v_adj]<dist_sum)){
                         dist_sum = dist_v[v_adj] + dist_w[v_adj];
                         ancestor = v_adj;                 
                     }
                 }
             }
             
         }
    }

   
    
    private void shortest_path(int v, int w){
           //check if theres cycle->if not can use topological sort 
        
        marked_v = new boolean[vertices];
        marked_w = new boolean[vertices];
        
        dist_v = new double[vertices];
        dist_w = new double[vertices];
        
        for(int i=0;i<vertices;i++){
            dist_v[i] = Double.POSITIVE_INFINITY;
            dist_w[i] = Double.POSITIVE_INFINITY;            
        }
        
        dist_v[v] = 0;
        marked_v[v] = true;
        
        dist_w[w] = 0; 
        marked_w[w] = true;
        
        
            if(hasCycle()){
                
                q_v = new Queue<Integer>();
                q_w = new Queue<Integer>();
                q_v.enqueue(v);
                q_w.enqueue(w);
                
                BreadthFirstPaths(v,w);
            }
            else{
                AcyclicSP(v,w);
            }
        
        
        if(dist_sum==Double.POSITIVE_INFINITY){
            dist_sum = -1; //for output, indicates no such path
        }
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        
        if(v<vertices && w<vertices && v>=0 && w>=0){
            //first see v==w
            if(v==w){
                return 0;
            }
            //else v!=w
            else{
                if(w<v){
                    int temp = v;
                    v = w;
                    w = temp;
                } 
                if(v_curr==v && w_curr ==w){
                    return (int) dist_sum;
                }
                
                    v_curr = v;
                    w_curr = w;
                    ancestor = -1;
                    dist_sum = Double.POSITIVE_INFINITY;
                    shortest_path(v,w);
                    return (int) dist_sum;
                
            }
        }
        else{
            //invalid vertices
            throw new java.lang.IndexOutOfBoundsException("Invalid vertics for SAP method length");
        }
    }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        
      if(v<vertices && w<vertices && v>=0 && w>=0){
          if(v==w){
              return w;
          }
          else{
              
              if(w<v){
                  int temp = v;
                  v = w;
                  w = temp;
              }
              if(v_curr==v && w_curr==w){
                  return ancestor;
              }
                    v_curr = v;
                    w_curr = w;
                    ancestor = -1;
                    dist_sum = Double.POSITIVE_INFINITY;
                    shortest_path(v,w);
                    return ancestor;
              
          }
            
      }
      else{
            //invalid vertices
            throw new java.lang.IndexOutOfBoundsException("Invalid vertices for SAP method ancestor");
      }
    }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        
        if(v==null || w==null){
            throw new java.lang.NullPointerException("Null of iterable length method of SAP");
        }
        
        
        for(int v_1:v){
            if(v_1<0 || v_1>= vertices){
                throw new java.lang.IndexOutOfBoundsException("Invalid vertex of first iterable in length for SAP");
            }
        }
        
         for(int w_1:w){
            if(w_1<0 || w_1>= vertices){
                throw new java.lang.IndexOutOfBoundsException("Invalid vertex of second iterable in length for SAP");
            }
        }
        
     
      
        ArrayList<Integer> v_list = new ArrayList<Integer>(); 
        ArrayList<Integer> w_list = new ArrayList<Integer>(); 
        for(int v_1 : v){
            v_list.add(v_1);
        }
        for(int w_1 : w){
            w_list.add(w_1);
        }
        if(arrayequals(v_curr_i, v_list) && arrayequals(w_curr_i,w_list)){
            return (int) dist_sum_i;
        }
        else{
           if(v_list.size()!=0 && w_list.size()!=0){
                v_curr_i = v_list;
                w_curr_i = w_list;
                ancestor_i = -1;
                dist_sum_i = Double.POSITIVE_INFINITY; 
                shortest_path(v,w); 
                
                return (int) dist_sum_i;
            }
            else{
                return -1;
            }
        }

         
    }
    
       // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
         if(v==null || w==null){
            throw new java.lang.NullPointerException("Null given to iterables of ancestor in SAP");
        }
        
        
        for(int v_1:v){
            if(v_1<0 || v_1>= vertices){
                throw new java.lang.IndexOutOfBoundsException("First iterable of ancestor has invalid values");
            }
        }
        
         for(int w_1:w){
            if(w_1<0 || w_1>= vertices){
                throw new java.lang.IndexOutOfBoundsException("Second iterable of ancestor has invalid values");
            }
        }
        
        ArrayList<Integer> v_list = new ArrayList<Integer>(); 
        ArrayList<Integer> w_list = new ArrayList<Integer>(); 
        for(int v_1 : v){
            v_list.add(v_1);
        }
        for(int w_1 : w){
            w_list.add(w_1);
        }
        if(arrayequals(v_curr_i, v_list) && arrayequals(w_curr_i,w_list)){
            return ancestor_i;
        }
        else{
            if(v_list.size()!=0 && w_list.size()!=0){
                v_curr_i = v_list;
                w_curr_i = w_list;
                ancestor_i = -1;
                dist_sum_i = Double.POSITIVE_INFINITY; 
                shortest_path(v,w); 
                
                return ancestor_i;
            }
            else{
                return -1;
            }
        }
       
    }
    
    private boolean arrayequals(ArrayList<Integer> a, ArrayList<Integer> b){
        if(a.size()!=b.size()){
            return false;
        }
        else{
            if(a.size()==0){
                return false;
            }
            Collections.sort(a);
            Collections.sort(b);
            for(int i =0; i<a.size(); i++){
                if(a.get(i)!=b.get(i)){
                    return false;
                }
            }
            
            return true;
        }
    
    }
    
    private void shortest_path(Iterable<Integer> v, Iterable<Integer> w){
        
        marked_v = new boolean[vertices];
        marked_w = new boolean[vertices];
        
        dist_v = new double[vertices];
        dist_w = new double[vertices];
        
        q_v = new Queue<Integer>();
        q_w = new Queue<Integer>();
        
        for(int i=0;i<vertices;i++){
            dist_v[i] = Double.POSITIVE_INFINITY;
            dist_w[i] = Double.POSITIVE_INFINITY;            
        }
        
        for(int v_1 : v){
            marked_v[v_1] = true;
            dist_v[v_1] = 0;
            q_v.enqueue(v_1);
        }
        
        for(int w_1 : w){
            marked_w[w_1] = true;
            dist_w[w_1] = 0;
            q_w.enqueue(w_1);
            
            if(marked_v[w_1]==true){
                ancestor_i = w_1;
                dist_sum_i = 0; //only place where set to zero, ancestor is itself
                break;
            }
        }
        
        if(dist_sum_i!=0){
            if(hasCycle()){
                BreadthFirstPaths(v, w);
            }
            else{
                AcyclicSP(v,w);
            }
        }
        
         if(dist_sum_i==Double.POSITIVE_INFINITY){
            dist_sum_i = -1;
        }
    }
    
    private void BreadthFirstPaths(Iterable<Integer> v, Iterable<Integer> w){
        
       
        
        while((!q_v.isEmpty() || !q_w.isEmpty())){
            
            if(!q_v.isEmpty()){
                int v_node = q_v.dequeue();
                if(dist_v[v_node]+1<dist_sum_i){
                    for(int v_adj : G.adj(v_node)){
                        if(!marked_v[v_adj]){ //first to hit will be shortest dist
                            marked_v[v_adj] = true;
                            dist_v[v_adj] = dist_v[v_node] + 1;
                            q_v.enqueue(v_adj);
                        }
                        
                        if(marked_w[v_adj] && marked_v[v_adj] &&(dist_v[v_adj] + dist_w[v_adj]<dist_sum_i)){
                            ancestor_i = v_adj;
                            dist_sum_i = dist_v[v_adj] + dist_w[v_adj];
                            
                        }
                    }
                }
            }
            
         
            
            if(!q_w.isEmpty()){
                int w_node = q_w.dequeue();
                if(dist_w[w_node]+1<dist_sum_i){
                    for(int w_adj : G.adj(w_node)){
                        if(!marked_w[w_adj]){
                            marked_w[w_adj] = true;
                            dist_w[w_adj] = dist_w[w_node] + 1;
                            q_w.enqueue(w_adj);
                        }
                        
                        if(marked_v[w_adj] && marked_w[w_adj] && (dist_v[w_adj] + dist_w[w_adj]<dist_sum_i)){
                            ancestor_i = w_adj;
                            dist_sum_i = dist_v[w_adj] + dist_w[w_adj];
                            
                        }
                    }
                }
            }
          
            
        }
        
    
    }
    
    private void AcyclicSP(Iterable<Integer> v, Iterable<Integer> w){
        
        
        for(int v_node: reversePost()){
            
             if(dist_v[v_node]+1<dist_sum_i){
                 for(int v_adj : G.adj(v_node)){
                     if(dist_v[v_adj]>dist_v[v_node]+1){  //for non-weighted graph, this corresponds to first meeting, i.e. shortest path prior value is infinity
                         dist_v[v_adj] = dist_v[v_node]+1;
                         marked_v[v_adj] = true;
                         
                     }
                     
                     if(marked_w[v_adj] && marked_v[v_adj] && (dist_v[v_adj] + dist_w[v_adj]<dist_sum_i)){
                         dist_sum_i = dist_v[v_adj] + dist_w[v_adj];
                         ancestor_i = v_adj;
                         
                     }
                     
                 }
             }
             if(dist_w[v_node]+1<dist_sum_i){
                 for(int v_adj : G.adj(v_node)){
                     if(dist_w[v_adj]>dist_w[v_node]+1){
                         dist_w[v_adj] = dist_w[v_node]+1;
                         marked_w[v_adj] = true;
                     }
                     
                     if(marked_w[v_adj] && marked_v[v_adj] && (dist_v[v_adj] + dist_w[v_adj]<dist_sum_i)){
                         dist_sum_i = dist_v[v_adj] + dist_w[v_adj];
                         ancestor_i = v_adj;
                         
                     }
                     
                 }
             }
            
         }
         
        
    }

   // do unit testing of this class
   public static void main(String[] args) {
       In in = new In(args[0]);
       Digraph G = new Digraph(in);
       SAP sap = new SAP(G);
       while (!StdIn.isEmpty()) {
           int v = StdIn.readInt();
           int w = StdIn.readInt();
           int length   = sap.length(v, w);
           int ancestor = sap.ancestor(v, w);
           StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
   }
}
