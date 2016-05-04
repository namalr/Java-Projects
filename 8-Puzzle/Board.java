import java.util.*;
import edu.princeton.cs.algs4.*;

public class Board {
    
    private int[] b;
    private int man;
    private int ham;
    private int N;
    private int zero_ind;
    
    
    public Board(int[][] blocks) {          // construct a board from an N-by-N array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
       
        this.N = blocks.length;
        this.b = new int[N*N];
        for (int i =0; i<N;i++){
            for (int j = 0;j<N;j++){
                this.b[i*N+j] =blocks[i][j]; 
                if(blocks[i][j]==0){
                    this.zero_ind = i*N+j;
                }
            }
        }
        this.man = -1;
        this.ham = -1;
    }
    
    public int dimension() {                // board dimension N
        return this.N;
    }
    
    public int hamming()  {                 // number of blocks out of place
        
        if (ham<0){
            int score = 0;
            for(int i = 1; i<N*N;i++){
                if(this.b[i-1]!=i){
                    score++;
                }
            }
            this.ham = score;
        }
        return this.ham;
    }
    public int manhattan() {  
        // sum of Manhattan distances between blocks and goal
        if (this.man<0){
            
            int sum = 0;
            int row = 0;
            int col = 0;
            int row_cur = 0;
            int col_cur = 0;
            int diff_1 = 0;
            int diff_2 = 0;
            
            for(int i=1;i<N*N;i++){
                if (this.b[i-1]!=i && this.b[i-1]!=0){ //final index checked is b[N*N-1-1], i.e. not last one
                    int a = this.b[i-1]; //number that is out of place
                
                    row = (a-1)/this.N; //integer division gives row according to zero indexing
                    col = (a-1)-row*N; //different is column offset
                
                    row_cur = (i-1)/this.N; //where it currently is
                    col_cur = (i-1)-N*row_cur;
                
                    diff_1 = row-row_cur;
                    diff_2 = col-col_cur;
                    if (diff_1<0){
                        diff_1 = -diff_1;
                    }
                    if (diff_2<0){
                        diff_2 = -diff_2;
                    }
                    sum+=(diff_1+diff_2);
                }
            }
            if(this.b[N*N-1]!=0){
                int a = this.b[N*N-1]; //copy of above code snippet
                
                row = (a-1)/this.N; //integer division gives row according to zero indexing
                col = (a-1)-row*N; //different is column offset
                
                row_cur = N-1; //where it currently is, this is with zero indexing for columns and rows!
                col_cur = N-1; //looking at last element of 1D array, so this is known
                
                diff_1 = row-row_cur;
                diff_2 = col-col_cur;
                if (diff_1<0){
                    diff_1 = -diff_1;
                }
                if (diff_2<0){
                    diff_2 = -diff_2;
                }
                sum+=(diff_1+diff_2);
            
            }
            this.man = sum;
        }
        
        return this.man;
    }
    
    public boolean isGoal() {               // is this board the goal board?
        if (this.ham>=0){
            return this.ham==0; 
        }
        else if(this.man>=0){
            return this.man==0;
        }
        else{
            return hamming()==0;
        }
    }
    
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        
        int[][] p_1 = new int[this.N][this.N];
        
        int num = this.zero_ind;
        int r_1 = StdRandom.uniform(0,N*N);
        int r_2 = StdRandom.uniform(0,N*N);
        while(r_2 ==num || r_1==num || r_2==r_1){
            if (r_2 ==num){
                r_2 = StdRandom.uniform(0,N*N);
            }
            if (r_1==num){
                r_1 = StdRandom.uniform(0,N*N);
            }
            if (r_1==r_2){
                r_1 = StdRandom.uniform(0,N*N);
            }
        }
         
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                if((i*N+j)==r_1){
                    p_1[i][j] = b[r_2];
                }
                else if((i*N+j)==r_2){
                    p_1[i][j] = b[r_1];
                }
                else{
                    p_1[i][j] = b[i*N+j];
                }         
            }
        }

        Board b_1 = new Board(p_1);
        
        return b_1;
    }
    
    public boolean equals(Object y){        // does this board equal y?
     // if y is uninitialized, alutomatically false
        if (y == null) return false;

        // Check that y is of Board class and cast
        if (y.getClass() != this.getClass()) return false;

        // Cast the Object y as a board
        Board that = (Board) y;
        if(this.ham>=0 && that.ham>0){ //check if valid values
            if (this.ham!=that.ham){
                return false;
            }
        }
        if(this.man>=0 && that.man>=0){ //do this check if hamming not calculated for either
            if (this.man!=that.man){
                return false;
            }
        }
        return Arrays.equals(this.b, that.b);    //final if no other checks eval to true
    }
    
    public Iterable<Board> neighbors() {     // all neighboring boards
        return new Neighbours();
    }
    
    private class Neighbours implements Iterable<Board>{
        public Iterator<Board> iterator(){
            return new NeighbourIterator();
        }
    }
    
    private class NeighbourIterator implements Iterator<Board>{
        
        private ArrayList<Board> boards;
        
        private int row;
        private int col;
        private int[][] p_board;
        
        public NeighbourIterator(){
            
            boards = new ArrayList<Board>();
            row = zero_ind/N;
            col = zero_ind - row*N;
            p_board = new int[N][N];
            
            for(int i = 0; i<N; i++){
                for(int j = 0; j<N; j++){
                    p_board[i][j] = b[i*N+j];       
                }
            }
            
            if (row==0 && col==0){
                
                swap_add(0,0,0,1);
                swap_add(0,0,1,0);
            }
            else if(row==N-1 && col==0){
                
                swap_add(N-1,0, N-1,1);
                swap_add(N-1,0,N-2,0);
                
            }
            else if(row ==0 && col==N-1){
                
                swap_add(0,N-1, 0,N-2);
                swap_add(0,N-1, 1,N-1);
                
            }
            else if(row ==N-1 && col==N-1){
                
                swap_add(N-1,N-1, N-1,N-2);
                swap_add(N-1,N-1, N-2,N-1);
                
            }
            else if(col!=0 && col!=N-1){
                
                swap_add(row,col,row,col+1);
                swap_add(row,col,row,col-1);   //can further replace this set of 4 with another function.....
                
                if (row==0){
                    swap_add(row,col, row+1,col);
                    
                }
                else if(row==N-1){
                    swap_add(row,col, row-1,col);
                }
                else{
                    swap_add(row,col, row+1,col);
                    swap_add(row,col, row-1,col);
                }
            }
            else if(col==0 || col==N-1){
                
                swap_add(row,col, row+1,col);
                swap_add(row,col, row-1,col);
                if (col==0){
                    swap_add(row,col, row,col+1);
                }
                else{
                    swap_add(row,col, row,col-1);
                }
            }
        }
        
        
        private void swap(int ind_1, int ind_2, int ind_3, int ind_4){
            int temp = p_board[ind_1][ind_2];
            p_board[ind_1][ind_2] = p_board[ind_3][ind_4];
            p_board[ind_3][ind_4] = temp;
        }
        
        private void swap_add(int ind_1, int ind_2, int ind_3, int ind_4){
            swap(ind_1, ind_2, ind_3, ind_4);
            Board b_1 = new Board(p_board);
            boards.add(b_1);
            swap(ind_1, ind_2, ind_3, ind_4); //undo swap for other calls
        }
        
        
        public boolean hasNext(){
            return boards.size()!=0;
        }
        
        public void remove() {          
            throw new java.lang.UnsupportedOperationException();
        }
        
        public Board next() {
            if(hasNext()){
                Board b_1 = boards.get(0);
                boards.remove(0);
                return b_1;
            }
            else{
                throw new java.util.NoSuchElementException();
            }
        
        }
    } 
        
    public String toString() {              // string representation of this board (in the output format specified below)
        
        StringBuilder s = new StringBuilder();
        s.append(Integer.toString(this.N));
        s.append('\n');
        for(int i = 0; i<N;i++){
            for(int j = 0; j<N;j++){
               s.append(Integer.toString(b[i*N+j]));
               if (j!=N-1){
                   s.append(" ");
               }
            }
            if (i!=N-1){
                s.append('\n');
            }
        }
        return s.toString();
    }

    //public static void main(String[] args) // unit tests (not graded)
}