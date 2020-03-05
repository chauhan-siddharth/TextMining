import java.util.ArrayList;
/**
 * ISTE-612-2195 Lab #2
 * Your name here
 * Date
 */

public class PositionalIndex {
	String[] myDocs;
	ArrayList<String> termDictionary;                  
	ArrayList<ArrayList<Doc>> docLists;
	
	/**
	 * Construct a positional index 
	 * @param docs List of input strings or file names
	 * 
	 */
	public PositionalIndex(String[] docs)
	{
		//TASK1: TO BE COMPLETED
	}
	
	/**
	 * Return the string representation of a positional index
	 */
	public String toString()
	{
		String matrixString = new String();
		ArrayList<Doc> docList;
		for(int i=0;i<termDictionary.size();i++){
				matrixString += String.format("%-15s", termDictionary.get(i));
				docList = docLists.get(i);
				for(int j=0;j<docList.size();j++)
				{
					matrixString += docList.get(j)+ "\t";
				}
				matrixString += "\n";
			}
		return matrixString;
	}
	
	/**
	 * 
	 * @param post1 first postings
	 * @param post2 second postings
	 * @return merged result of two postings
	 */
	public ArrayList<Doc> intersect(ArrayList<Doc> post1, ArrayList<Doc> post2)
	{
		//TASK2: TO BE COMPLETED
	}
	
	/**
	 * 
	 * @param query a phrase query that consists of any number of terms in the sequential order
	 * @return ids of documents that contain the phrase
	 */
	public ArrayList<Doc> phraseQuery(String[] query)
	{
		//TASK3: TO BE COMPLETED
	}

	
	public static void main(String[] args)
	{
      String[] docs = {"data text warehousing over big data",
                       "dimensional data warehouse over big data",
                       "nlp before text mining",
                       "nlp before text classification"};
                       
		PositionalIndex pi = new PositionalIndex(docs);
		System.out.print(pi);
		//TASK4: TO BE COMPLETED: design and test phrase queries with 2-5 terms
	}
}

/**
 * 
 * Document class that contains the document id and the position list
 */
class Doc{
	int docId;
	ArrayList<Integer> positionList;
	public Doc(int did)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
	}
	public Doc(int did, int position)
	{
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}
	
	public void insertPosition(int position)
	{
		positionList.add(new Integer(position));
	}
	
	public String toString()
	{
		String docIdString = ""+docId + ":<";
		for(Integer pos:positionList)
			docIdString += pos + ",";
		docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
		return docIdString;		
	}
}
