
package problem81;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Problem81 {

	static int width=80;
	static int height=80;
        
        static int start=1;
        static int end=width*width;
        
        static ArrayList<Integer> matrix =new ArrayList<Integer>();
        static Map<Integer, Integer> cameFrom = new HashMap();
        
        //static ArrayList<Integer> test=new ArrayList<Integer>();
	
	//The Heuristic Function
	public static int h(int start,int end)
	{
            
		int val[]=getMat(start);
		int row=val[0];
		int col=val[1];
		
		return (width-row)+(width-col);
	}
	
	
	//Return node with lowest f_score in openSet
	public static int getLowF(ArrayList<Integer> openSet,
			ArrayList<Integer> f_score)
	{
		int min=Integer.MAX_VALUE;
		int node=-1;
		for(int i=0;i<openSet.size();i++)
		{
			if(f_score.get(openSet.get(i)) < min)
			{
				node=openSet.get(i);
				min=f_score.get(node);
			}
		}
		return node;
	}
	
        //Remove a particular node from openSet
	public static ArrayList<Integer> removeNode(ArrayList<Integer> openSet,int node)
	{
		for(int i=0;i<openSet.size();i++)
		{
			if(openSet.get(i)==node)
			{
				openSet.remove(i);
				return openSet;
			}
		}
		return openSet;
	}
	
        //Return row and columns of a node
	public static int[] getMat(int node)
	{	
		int[] result=new int[2];
                
		int row=(int) Math.ceil((double)node/(double)width);
                int col=-1;
                if(node%width == 0)
                    col=5;
                else
                    col=node%width;
		
		result[0]=row;
		result[1]=col;
                
		return result;
	}
	
        //Return Node no. from rows and columns
	public static int getIndex(int row, int col)
	{
		return ((row-1)*width)+col;
	}
	
        //Check if rows and columns are valid
	public static boolean valid(int row,int col)
	{
		if(row>0 && row<height+1 && col > 0 && col<width+1)
			return true;
		
		return false;
			
	}
	
        //Get the **required neighbours of the node
	public static ArrayList<Integer> getNeighbour(int node)
	{
		int[] mat=getMat(node);
		int row=mat[0];
		int col=mat[1];
                
		
		ArrayList<Integer> neighbour=new ArrayList<Integer>();
                /*
		if(valid(row-1,col)) //up
		{
			neighbour.add(getIndex(row-1,col));
		}
		*/
		if(valid(row+1,col)) //down
		{
			neighbour.add(getIndex(row+1,col));
		}
		/*
		if(valid(row,col-1)) //left
		{
			neighbour.add(getIndex(row,col-1));
		}
		*/
		if(valid(row,col+1)) //right
		{
			neighbour.add(getIndex(row,col+1));
		}
		
		return neighbour;
	}
	
        //Read the values from the file
	public static void setValues()
	{
            matrix.clear();
            int count=0;
		
		try{
			  //Update the path of the file 
			  FileInputStream fstream = new FileInputStream("d://matrix.txt");
			 
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  
			  while ((strLine = br.readLine()) != null)   {
				  String[] test=strLine.split(",");
				  for(int i=0;i<test.length;i++)
                                  {
					  matrix.add(Integer.parseInt(test[i]));
                                          ++count;
                                  }
			  }
			  
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
                
	}
	
        //Get the cost of Node
	public static int costOf(int node)
	{
		
		try{
                    return matrix.get(node-1);
		}
		catch(Exception e)
		{
                        
			System.out.println(node+" Cost Error");
			return -1;
		}
	}
        
        //Get the minimun value in the ArrayList
        public static int getMin(ArrayList<Integer> check)
        {
            int min=Integer.MAX_VALUE;
            for(int i=0;i<check.size();i++)
            {
                if(check.get(i) < min)
                    min=check.get(i);
            }
            
            return min;
        }
        
        //display the traverse Cost
        public static void displayCost()
        {
            System.out.println("The minimum Path is: ");
            int from=end;
            int cost=costOf(from);
            while(from!=start)
            {
                int to =cameFrom.get(from);
                System.out.println(from+" <- "+to);
                cost+=costOf(to);
                from=to;
            }
            System.out.println("Path traced!");
            
            System.out.println("The cost of traversing is: "+cost);
        }
        
	
        //Find the shortest Path
	public static void findPath()
	{
                
                setValues();
                
		ArrayList<Integer> closedSet=new ArrayList<Integer>();
		ArrayList<Integer> openSet=new ArrayList<Integer>();
		openSet.add(start); //add start in openSet
		
		
		ArrayList<Integer> g_score=new ArrayList<Integer>(Collections.nCopies((width*width)+1, 0));
		ArrayList<Integer> f_score=new ArrayList<Integer>(Collections.nCopies((width*width)+1, 0));
		g_score.add(start, costOf(start));
		f_score.add(start,g_score.get(start)+h(start,width));
                
                cameFrom.clear();
                
		
		while(!openSet.isEmpty())
		{
			int current=getLowF(openSet,f_score);
			
			if(current== end) //Goal Condition
                        {
				displayCost();
                                return;
                        }
			
			removeNode(openSet,current);
			closedSet.add(current);
			
			//For each neighbour of current
			for(int neighbour : getNeighbour(current))
			{
				int tentitive_g_score=g_score.get(current)+costOf(neighbour);
                                
				int tentitive_f_score=tentitive_g_score + h(neighbour,width*width);
				
				if(closedSet.contains(neighbour) && tentitive_f_score >= f_score.get(neighbour))
					continue;
				
				if(!openSet.contains(neighbour) || tentitive_f_score < f_score.get(neighbour))
				{
                                        
					cameFrom.put(neighbour,current);

					g_score.set(neighbour, tentitive_g_score);
					f_score.set(neighbour, tentitive_f_score);
					
					if(!openSet.contains(neighbour))
						openSet.add(neighbour);
				}
				
			}
			
		}
                
                System.out.println("Sorry,Can't Find the Path");
		
	}
	
	public static void main(String[] args)
	{
            findPath();
	}
}

