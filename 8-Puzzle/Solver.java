import java.util.*;
 
import edu.princeton.cs.algs4.*;


public class Solver {
    
    
    private MinPQ<SearchNode> boards;
    
    private int flag1; //initialize to zero, if first is solved set to one, if second is solved, set to 2
    private int m;
    private ArrayList<Board> sol;
    private SearchNode goal; 
    
    private static class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int moves;
        
        public int compareTo(SearchNode that){
            if ((board.manhattan()+moves)> that.board.manhattan()+that.moves){
                return 1;
            }
            else if ((board.manhattan()+moves) == that.board.manhattan()+that.moves){
                return 0;
            }
            else{
                return -1;
            }
        }
        
        public Comparator<SearchNode> SearchNodeOrder(){
            return new SearchNodeComparator();
        }
        
        private class SearchNodeComparator implements Comparator<SearchNode>{
            public int compare(SearchNode one, SearchNode two){
                return one.compareTo(two);
            }
        }
    } 
    
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        if (initial==null){
            throw new java.lang.NullPointerException();
        }
        
        SearchNode s = new SearchNode();
        SearchNode s_twin = new SearchNode();
        
        this.goal = new SearchNode();
        
        s.board = initial;
        s.previous = null;
        s.moves = 0;
            
        s_twin.board = initial.twin();
        s_twin.previous = null;
        s_twin.moves = 0;
      
        boards = new MinPQ<SearchNode>(2, s.SearchNodeOrder());
        boards.insert(s);
        boards.insert(s_twin);
        
        flag1 =0;
        
        while (flag1==0){
            SearchNode curr = new SearchNode();
            curr = boards.delMin();
            if (curr==null){
                throw new java.lang.NullPointerException();
            }
            if(curr.board.isGoal()){
                flag1=1;
                this.goal = curr;
                break;
            }
            for(Board b:curr.board.neighbors()){
                if (curr.previous==null){
                    SearchNode node = new SearchNode();
                    node.board = b;
                    node.moves = curr.moves+1;
                    node.previous = curr;
                    boards.insert(node);
                }
                else if(!b.equals(curr.previous.board)){
                    SearchNode node = new SearchNode();
                    node.board = b;
                    node.moves = curr.moves+1;
                    node.previous = curr;
                    boards.insert(node);
                }
            }
        }
        
        m = this.goal.moves;
        //int m_1 = m;
        sol = new ArrayList<Board>();
        flag1 = 0; //reset back to zero after breaking while loop
        while(this.goal!=null){
            if (this.goal.board!=null){
                sol.add(this.goal.board);
            }
            if(this.goal.board.equals(initial)){
                flag1=1;
            }
            goal = goal.previous;
        }
        Collections.reverse(sol); //revese arraylist
    }
    public boolean isSolvable() {            // is the initial board solvable?
        return flag1==1;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if(isSolvable()){
            return m;
        }
        else{
            return -1;
        }
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
    
        if (!isSolvable()){
            return null;
        }
        else{
            return sol;
        }
    
    }
        
        
        
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
       // StdOut.println("The board is:"+initial.toString());
        //StdOut.println("Current Manhanttan distance is:"+initial.manhattan());
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }    
    }
        
}