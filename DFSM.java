import java.util.Scanner;
import java.io.*;

public class DFSM
{
	private static MachineState stateList[] = null;
	private static int state = 0;//If initial state is not 0, change this accordingly.

    public static void main(String args[]) throws IOException
    {
		System.out.println("Please input whole path file for machine.");
		System.out.println("Example: \"C:\\Users\\rschr\\Desktop\\AutomataProjects\\Test.txt\"");
        Scanner console = new Scanner(System.in);
        String fileIn = console.nextLine();
        //System.out.println(fileIn);
        Scanner file = new Scanner(new File(fileIn));
        stateList = buildMachine(file);

		String userIn = " ";
		while(!userIn.equals("END"))
		{
			System.out.println("Enter a sequence to verify by your machine.");
			System.out.println("Enter 'END' to exit.");
			userIn = console.nextLine();
			//System.out.println(userIn);

			if(!userIn.equals("END"))
			{
				state = 0;
				String temp = userIn;
				for(int i = 0; i < userIn.length(); i++)
				{
					System.out.println("state = " + state);
					System.out.println("temp = " + temp);
					System.out.println("Next char = " + temp.charAt(0));
					state = runString(temp);
					temp = temp.substring(1);
					//System.out.println("From Main: stateList[0] = " + stateList[1].getTran(0));
					//System.out.println("From Main: stateList[1] = " + stateList[1].getTran(1));
				}
			}
		}
    }

    private static int runString(String in)
    {
		return stateList[state].makeTran(in.charAt(0));
	}

	private static MachineState getState(int index)
	{
		return stateList[index];
	}

	private static MachineState[] buildMachine(Scanner file)//Get file from users input, read file line by line, store each line accordingly
	{
		String langSet = file.nextLine();
		int states = Integer.parseInt(file.nextLine());
		String acceptingStates = file.nextLine();
		MachineState returnValue[] = new MachineState[states];

		for(int i = 0; i < states; i++)
		{
		 	returnValue[i] = new MachineState(i, langSet, acceptingStates, file.nextLine());//Create new MachineStates based on Strings from file
		}
		return returnValue;
	}

}

class MachineState
{
	final public int index;
	boolean accepting = false;
	int tranList[] = null;//List for transitions based on char from user input
	char alphabet[] = null;//Alphabet of the language to check each char

	public MachineState(int indexNum, String lang, String acceptStates, String tranListIn)
	{
		index = indexNum;
		alphabet = setAlphabet(lang);
		tranList = setTranList(tranListIn);

		for(int i = 0; i < acceptStates.length(); i++)
		{
			char test = acceptStates.charAt(i);
			if(Character.isDigit(test) && (Character.getNumericValue(test) == index))
			{
				accepting = true;
			}
		}
		//System.out.println("index = " + index);
		//System.out.println("accepting = " + accepting);
	}

	private char[] setAlphabet(String in)//Takes String from file, converts into char[] with each char a member of the alphabet
	{
		char returnValue[] = new char[(in.length()/3) + 1];
		int index1 = 0;
		for(int i = 0; i < in.length(); i++)
		{
			char test = in.charAt(i);
			if(test != ',' && test != ' ')
			{
				returnValue[index1++] = test;
				//System.out.println("state#" + index + " alphabet[" + index1 + "] = " + returnValue[index1 - 1]);
			}
		}

		return returnValue;
	}

	public int getTran(int in)
	{
		return tranList[in];
	}

	public char getAlphabet(int in)
	{
		return alphabet[in];
	}

	private int[] setTranList(String in)//Takes String from file, converts to int[] with destination values of the states indexed
	{									//so they can be retrieved later based on char input from users String
		System.out.println("setTranList in = " + in);
		int temp[] = new int[(in.length() / 11) + 1];
		int index = 0;
		for(int i = 4; i < in.length(); i+=11)
		{
			if(index < alphabet.length && getAlphabet(index) == in.charAt(i))//Checks to see if the index is inbounds, and that the indexed char matches the input
			{																 //taken from the String
				System.out.println("in.charAt(" + i + ") = " + in.charAt(i));
				temp[index++] = Character.getNumericValue(in.charAt(i + 3));//When it does, store that value in this index
				System.out.println("setTranList temp = " + temp[index - 1]);
				System.out.println(in.charAt(i + 3));
				//System.out.println("getAlphabet[" + index + "] = " + getAlphabet(index - 1));
			}
		}
		return temp;
	}

	public int makeTran(char a)
	{
		for(int i = 0; i < alphabet.length; i++)
		{
			if(alphabet[i] == a)
			return getIndex(i);
		}

		return -1;
	}

	public int getIndex(int i)
	{
		return tranList[i];
	}
}