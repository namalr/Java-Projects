import edu.princeton.cs.algs4.WeightedQuickUnionUF;
    
public class Percolation {
    
    private WeightedQuickUnionUF item; //hold the grid as 1-D, 0 to initualize
    private int[] state; //corresponding states whether open or not
    private int size;
    private WeightedQuickUnionUF item_1;
    
    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        if (N<1) {
            throw new java.lang.IllegalArgumentException("invalid size");
        }
        size = N;
        //sites = 0;
        item = new WeightedQuickUnionUF(N*N+2);
        item_1 = new WeightedQuickUnionUF(N*N+2);
        state = new int[N*N];
        for (int i=0;i<N*N;i++){
            state[i] = 0;
        }
        
    }
    public void open(int i, int j) {          // open site (row i, column j) if it is not open already
        if ((i<1) || (i>size) || (j<1) || (j>size)) {
            throw new java.lang.IndexOutOfBoundsException("your message goes here");
        }
        if (state[(i-1)*size+j-1]==0){
            state[(i-1)*size+j-1]=1;
            //sites+=1;
            //check top, bottom, left, right
            //(i-2)*N +j-1, i*N+j-1, (i-1)*N+j-1-1. (i-1)*N+j-1+1
            //check neighbours and see if any are open, if they are form connected set
        
            if (i<size){
                ///dealing with virtual nodes
                if (i==1){
                    item.union((i-1)*size+j-1, size*size); //connect to top
                    item_1.union((i-1)*size+j-1, size*size);
                }
                if (state[i*size +j-1]==1){
                    item.union(i*size +j-1,(i-1)*size+j-1); //looks at row below, same column
                    item_1.union(i*size +j-1,(i-1)*size+j-1);
                }
            }
            if (j>=2){
                if (state[(i-1)*size +j-2]==1){
                    item.union((i-1)*size +j-2,(i-1)*size+j-1); //looks at same row, left column
                    item_1.union((i-1)*size +j-2,(i-1)*size+j-1); 
                }
            }
            if (j<size){
                if (state[(i-1)*size +j]==1){
                    item.union((i-1)*size +j,(i-1)*size+j-1); //looks at same row, right column
                    item_1.union((i-1)*size +j,(i-1)*size+j-1);
                }
            }
            
            if (i>=2){
                
                if (state[(i-2)*size +j-1]==1){
                    item.union((i-2)*size +j-1,(i-1)*size+j-1); //looks at row above, same colum
                    item_1.union((i-2)*size +j-1,(i-1)*size+j-1);
                }
                              ///dealing with virtual nodes
                if (i==size){ //connect to bottom
                    item_1.union((i-1)*size+j-1,size*size+1); //only connect with virtual bottom if in last row and can connect to top already
                    
                }
            }
                
        }
        
            
    }
    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        if ((i<1) || (i>size) || (j<1) || (j>size)) {
            throw new java.lang.IndexOutOfBoundsException("your message goes here");
        }
        return (state[(i-1)*size+j-1]==1);
    }
    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        if ((i<1) || (i>size) || (j<1) || (j>size)) {
            throw new java.lang.IndexOutOfBoundsException("your message goes here");
        }
        //check if this node and virtual top node is connected
        return (item.connected((i-1)*size+j-1, size*size));
        
    }
    public boolean percolates(){             // does the system percolate?
        //check if the virtual bottom and top nodes are connected
        return item_1.connected(size*size,size*size+1);
    }

   //public static void main(String[] args)  // test client (optional)
}