
public class Node
{
   private int[][] state; //Stores the order of the board
   private Node parent; //Parent pointer
   private int pathCost; //g(n) cost
   private int heuristicCost; //h(n) cost
  
   //Constructor for Node
   public Node()
   {
	   parent = null;
   }
   
   public void setParent(Node parent)
   {
	   this.parent = parent;
   }
   
   public Node getParent()
   {
	   return parent;
   }
    
   //Set the state of the node to a different state.
   public void setState(int[][] board)
   {
	   state = new int[3][3];
	   for (int i=0;i<3;i++)
	   {   
		   for (int j=0;j<3;j++)
		   {   
			   state[i][j] = board[i][j];
		   }
	   }
   }
   
   public int[][] getState()
   {
	   return state;
   }
   
   //This method returns an array with an equal value to the Node's state not just a copy.
   public int[][] copyArray(Node n)
   {
	   int[][] copy = new int[3][3];
	   for (int x=0;x<3;x++)
	   {
		   for (int y=0;y<3;y++)
		   {
			   copy[x][y] = state[x][y];
		   }
	   }
	   return copy;
   }
   public void setPathCost(int c)
   {
	   pathCost = c;
   }
   
   public int getPathCost()
   {
	   return pathCost;
   }
   
   public void setHeuristicCost(int c)
   {
	   heuristicCost = c;
   }
   
   public int getHeuristicCost()
   {
	   return heuristicCost;
   }

   //returns the sum of the nodes g(n) and h(n). 
   public int getTotalCost()
   {
	   return (this.getPathCost()+ this.getHeuristicCost());
   } 
   
   //This method is used by the explored set to store the nodes based on their f(n) cost.
   public String toKey(Node n)
   {
	   String key = ""; //Holds the order of each nodes state
	   int[][] array = n.getState();
	   for (int i=0;i<3;i++)
	   {
		   for (int j=0;j<3;j++)
		   {
			   key += array[i][j];
		   }
	   }
	   return key;
   }

   
/* Older methods where I tried to use hash map.
 *   public String hashCode(Node n)
   {
	   String hash = "";
	   int[][] array = n.getState();
	   for (int x=0;x<3;x++)
	   {
		   for (int y=0;y<3;y++)
		   {
			   hash += array[x][y] + " ";
		   }
	   }
	   return hash;
   }
*/        
/*public boolean equals(Object obj)
     {
	  boolean flag = false;
	  String s1 = "";
	  String s2 = "";
	  Node node = (Node) obj;
	  int[][] a1 = this.getState();
	  int[][] a2 = node.getState();
	   for (int x=0;x<3;x++)
	   {
		   for (int y=0;y<3;y++)
		   {
			   s1 += a1[x][y] + " ";
			   s2 += a2[x][y] + " ";
		   }
	   }
	   if (!s2.equals(s1))
		   return false;
	   
	   return true;
   }
   */ 
}
