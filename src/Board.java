//Mark Erickson
//CS420 Project 1

import java.util.Comparator;
import java.util.*;

/* This class accepts a two dimensional array and arranges it into the goal state using the A* algorithm 
 * and either the Manhattan distance heuristic or the misplaced tiles heuristic. 
 */
public class Board 
{
	//This method returns true if a given board is solvable or false if it is not.
	public boolean isSolvable(int[][] board)
	{
		boolean solvable = false;
		int inverted = 0;
		for (int i=0;i<3;i++)
		{
			for (int j=0;j<2;j++)
			{
				if (board[i][j] > board[i][j++])
				{
					inverted++;
				}
			}
		}
		if (inverted % 2 ==0) //The number of inverted tiles is even.
		{
			solvable = true;
		}
		
		return solvable;
	}
	
	//This method calculates the number of tiles out of goal position heuristic.
	public int findMissplacedTiles(int[][] board)
	{
		int missplacedTiles = 0;
		int counter = 0;
		for (int i=0;i<3;i++)
		{
			for (int j=0;j<3;j++)
			{
				if(board[i][j] != counter) //The tile is not in order.
				{
					missplacedTiles++;
				}
				counter++;
			}
		}
		return missplacedTiles;
	}
	
	//This method calculates the Manhattan distance heuristic of the current board state.
	public int findManhattan(int[][] board)
	{
		int manhattan = 0;
		for (int x=0; x<3;x++) //Traversing the x direction
		{
			for (int y=0;y<3;y++) //Traversing the y direction.
			{	
				int tile = board[x][y];
				if (tile !=0) //Ignore the zero's position.
				{
					int goalX = tile/3; 
					int goalY = tile%3;
					int xDistance = x-goalX;
					int yDistance = y-goalY;
					manhattan += Math.abs(xDistance) + Math.abs(yDistance);
				}
			}
		}
		return manhattan;
	}
	
	//This method tests if the board is in the final configuration.
	public boolean isGoal(int[][] board)
	{
		int counter = 0;
		for (int i=0;i<3;i++)
		{
			for (int j=0;j<3;j++)
			{
				if (board[i][j] != counter)
				{
					return false;
				}
				counter++;
			}
		}
			
		return true;
	}
	
	//This method calculates the steps to solve the 8-puzzle problem.
	public void aStar(int[][] board, int hChoice, int outChoice)
	{
		Node start = new Node();
		start.setParent(null);
		start.setState(board);
		if (hChoice == 1) //Implement A* using misplacedTiles heuristic.
		{
			start.setHeuristicCost(findMissplacedTiles(start.getState()));
		}
		else //Implement A* using the Manhattan distance heuristic.
		{
			start.setHeuristicCost(findManhattan(start.getState()));
		}
		start.setPathCost(0);
		/*This priority queue holds the frontier and is defined with the 
		 * nodes with highest priority having the lowest total cost f(n) = g(n)+h(n).
		 */
		PriorityQueue<Node> frontier  = new PriorityQueue<Node>(16, new Comparator<Node>(){ 
			public int compare(Node a, Node b){
				return a.getTotalCost() - b.getTotalCost();
			}
		});
		frontier.add(start);
		//HashSet<String> exploredSet = new HashSet<String>(); Old Hash set that didn't work.
		ArrayList<String> explored = new ArrayList<String>(); //This holds the explored set/
		while (frontier.isEmpty() != true) //Q is not empty.
		{
			Node current = frontier.remove(); //Remove node with lowest F(n) from Q.
			if (isGoal(current.getState())) //Is goal?
			{
				frontier.clear();
				explored.clear();
				reconstructPath(current,outChoice);
				return;
			}
			explored.add(current.toKey(current));  //Add current to explored set.
			
			//Find the left neighbor.
			int[][] neighborStateLeft = current.copyArray(current);
			for (int x=0;x<3;x++)
			{
				for (int y=0;y<3;y++)
				{
					if (neighborStateLeft[x][y] == 0)
					{
						if (y-1 >= 0) //Move blank tile to the left
						{
							int temp = neighborStateLeft[x][y-1];
							neighborStateLeft[x][y-1] = neighborStateLeft[x][y];
							neighborStateLeft[x][y] = temp;
							Node neighborLeft = new Node();
							neighborLeft.setState(neighborStateLeft); //Set the node's state to the neighbor state.
						
							if (explored.contains(neighborLeft.toKey(neighborLeft))) //If this neighbor appears in the explored set then it has been evaluated.
							{
								
							}
							else
							{
								if (hChoice == 1)
								{
									neighborLeft.setHeuristicCost(findMissplacedTiles(neighborLeft.getState()));
								}
								else
								{
									neighborLeft.setHeuristicCost(findManhattan(neighborLeft.getState())); //Find the heuristic cost using the current state
								}
								neighborLeft.setPathCost(current.getPathCost()+1);
								neighborLeft.setParent(current);
								frontier.add(neighborLeft); //Adding the new node to the queue
							}
						}
					}
				}
			}
			
			//Find the state with the zero moved to the right.
			int[][] neighborStateRight = current.copyArray(current);
			for (int x=0;x<3;x++)
			{
				for (int y=0;y<3;y++)
				{
					if (neighborStateRight[x][y] == 0)
					{
						if (y+1 < 3) //Move blank tile to the right
						{
							int temp = neighborStateRight[x][y+1];
							neighborStateRight[x][y+1] = neighborStateRight[x][y];
							neighborStateRight[x][y] = temp;
							Node neighborRight = new Node();
							neighborRight.setState(neighborStateRight); //Set the state to the neighbor state.
							if (explored.contains(neighborRight.toKey(neighborRight)))
							{
								
							}
							else
							{
								if (hChoice == 1)
								{
									neighborRight.setHeuristicCost(findMissplacedTiles(neighborRight.getState()));
								}
								else
								{
									neighborRight.setHeuristicCost(findManhattan(neighborRight.getState())); //Find the heuristic cost using the current state
								}
								neighborRight.setPathCost(current.getPathCost()+1);
								neighborRight.setParent(current);
								frontier.add(neighborRight); //Adding the new node to the queue
							}
						}
					}
				}
			}
			
			//Find the state with the zero moved down.
			int[][] neighborStateDown = current.copyArray(current);
			for (int x=0;x<3;x++)
			{
				for (int y=0;y<3;y++)
				{
					if (neighborStateDown[x][y] == 0)
					{
						if (x-1 >= 0) //Move blank tile down
						{
							int temp = neighborStateDown[x-1][y];
							neighborStateDown[x-1][y] = neighborStateDown[x][y];
							neighborStateDown[x][y] = temp;
							Node neighborDown = new Node();
							neighborDown.setState(neighborStateDown); //Set the state to the neighbor state.
							if (explored.contains(neighborDown.toKey(neighborDown))) //If this neighbor appears in the explored set then it has been evaluated.
							{
								
							}
							else
							{
								if (hChoice == 1)
								{
									neighborDown.setHeuristicCost(findMissplacedTiles(neighborDown.getState()));
								}
								else
								{
									neighborDown.setHeuristicCost(findManhattan(neighborDown.getState())); //Find the heuristic cost using the current state
								}
								neighborDown.setPathCost(current.getPathCost()+1);
								neighborDown.setParent(current);
								frontier.add(neighborDown); //Adding the new node to the queue
							}
						}
					}
				}
			}
			
			//Find the next state by moving the zero up.
			int[][] neighborStateUp = current.copyArray(current);
			for (int x=0;x<3;x++)
			{
				for (int y=0;y<3;y++)
				{
					if (neighborStateUp[x][y] == 0)
					{
						if (x+1 <= 2) //Move blank tile up
						{
							int temp = neighborStateUp[x+1][y];
							neighborStateUp[x+1][y] = neighborStateUp[x][y];
							neighborStateUp[x][y] = temp;
							Node neighborUp = new Node();
							neighborUp.setState(neighborStateUp); //Set the state to the neighbor state.

							if (explored.contains(neighborUp.toKey(neighborUp))) //If this neighbor appears in the explored set then it has been evaluated.
							{
								
							}
							else
							{
								if (hChoice == 1)
								{
									neighborUp.setHeuristicCost(findMissplacedTiles(neighborUp.getState()));
								}
								else
								{
									neighborUp.setHeuristicCost(findManhattan(neighborUp.getState())); //Find the heuristic cost using the current state
								}
								neighborUp.setPathCost(current.getPathCost()+1);
								neighborUp.setParent(current);
								frontier.add(neighborUp); //Adding the new node to the queue
							}
						}
					}
				}
			}
		}
	}
	
	//This method shows the depth of a solution and the method used to get there.	
	public void reconstructPath(Node completePuzzle, int outputChoice) 
	{
		int depth = completePuzzle.getPathCost();
		if (outputChoice == 1)
		{
			System.out.print(depth);
		}
		else
		{
			Node current = completePuzzle;
			while(current.getParent() != null)
			{
				int[][]a = current.getState();
				for(int x=0;x<3;x++)
				{
					for(int y=0;y<3;y++)
					{
						System.out.print(a[x][y]);
						if (y==2) System.out.println();
					}
				}
				System.out.println();
				current = current.getParent();
			}
		}
	}
	
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		Board game = new Board();
		int input;
		
		List<Integer> list = new ArrayList<Integer>(); //This array list is used to hold the numbers to be randomized.
		for (int i=0; i<9; i++)
		{
			list.add(i);
		}
		int[][] randomBoard = new int[3][3];	//This is the random game board
		System.out.print("Enter 1 for randomly generated 8-puzzle problem. Enter 2 for manual input: ");
		input = keyboard.nextInt();
		if (input == 1) //Random input is chosen
		{
			int counter = 100;
			while (counter >0)
			{
				Collections.shuffle(list); //Shuffle the list and add it to the board.
			    int count = 0;
				for (int x=0;x<3;x++)
				{
					for (int y=0;y<3;y++)
					{ 
						randomBoard[x][y] = list.get(count);
						count++;
					}
				}
				if (!game.isSolvable(randomBoard))
				{
					System.out.println("Unsolveable");

				}
				else
				{
					System.out.print("h(1) h(2) depth Runtime \n");
					//Start a timer to show the run time to of an instance of A*.
					long startTime = System.currentTimeMillis();
					System.out.print(game.findMissplacedTiles(randomBoard) + "   ");
					System.out.print(game.findManhattan(randomBoard) + "   ");
					game.aStar(randomBoard,2,1); 
					long endTime = System.currentTimeMillis();
					long totalTime = endTime-startTime;
					System.out.print("     " + totalTime);
					System.out.println();
					counter--;
				}
			}
			counter = 100;
			System.out.println("H(1)");
			while (counter >0)
			{
				Collections.shuffle(list);
				int count = 0;
				for (int x=0;x<3;x++)
				{
					for (int y=0;y<3;y++)
					{
						randomBoard[x][y] = list.get(count);
						count++;
					}
				}
				if (!game.isSolvable(randomBoard))
				{
					continue;
				}
				else
				{
					long startTime = System.currentTimeMillis();
					game.aStar(randomBoard,1,1);
					long endTime = System.currentTimeMillis();
					long totalTime = endTime-startTime;
					System.out.println("Runtime: " + totalTime);
					counter--;
				}
			}
		}
		else if (input == 2) //Manual input chosen.
		{
			keyboard.nextLine();
			System.out.println("Enter a board: ");
			String userString = keyboard.nextLine();
			int[][] userBoard = new int[3][3];
			int[] transfer = new int[9];
			userString = userString.replace("\n", " "); //Remove the endline character from the input.
			String[] str = userString.split(" ");
			int i = 0;
			for (String s: str)
			{
				transfer[i] = Integer.parseInt(s); //Parse the string into an int.
				i++;
			}
			i = 0;
			for (int x=0;x<3;x++)
			{
				for (int y=0;y<3;y++)
				{
					userBoard[x][y] = transfer[i];
					i++;
				}
			}
			game.aStar(userBoard,2,2);
			
		}
	}
}