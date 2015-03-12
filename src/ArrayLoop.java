

import java.util.Observable;

public class ArrayLoop extends Observable
{
	private int[] array;
	private int numCells = 0;
	public int currentCell = 0;

	public ArrayLoop(int numCells)
	{
		setArray(new int[numCells]);
		setNumCells(numCells);
	}

	public void addInt(int value)
	{
		if (currentCell < numCells)
		{
			array[currentCell] = value;
			currentCell++;
		}
		else
		{
			for (int count = 0; count < numCells; count++)
			{
				if (count + 1 != numCells)
					array[count] = array[count + 1];
			}
			array[numCells - 1] = value;
		}
	}

	public int getInt(int cell)
	{
		return array[cell];
	}

	public int getNumCells()
	{
		return numCells;
	}

	public void setNumCells(int numCells)
	{
		this.numCells = numCells;
	}

	public int[] getArray()
	{
		return array;
	}

	public void setArray(int[] array)
	{
		this.array = array;
	}

}
