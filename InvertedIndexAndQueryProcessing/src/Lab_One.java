/*
Lab 1
 */

import java.util.*;
import java.io.*;

/**
 * Lab 01 : Knowlede Processing Techniques [KPT]
 * Name: Siddharth Chauhan
 */

public class Lab_One {
    private static ArrayList<String> docs = new ArrayList<String>();
    private static String[] stopWords = new String[0];
    private static ArrayList<String> termList = new ArrayList<String>();
    private static ArrayList<ArrayList<String>> allTokens = new ArrayList<ArrayList<String>>();
    private static ArrayList<ArrayList<String>> allStems = new ArrayList<ArrayList<String>>();
    private static ArrayList<Integer> docList;
    private static ArrayList<ArrayList<Integer>> docLists = new ArrayList<ArrayList<Integer>>();
    private static ArrayList<String>documentDetails= new ArrayList<>();

    /**
     * Reads multiple files from a directory.
     *
     * @param filesList - contains the list of files
     * @return docs - content of files as ArrayList of string
     */
    private static ArrayList<String> readFileFromPath(File[] filesList) {
        int index = 0;
        docs.add(" ");
        for (File f : filesList) {
            String newLine = new String();
            if (f.isFile()) {
                try {
                    Scanner sc = new Scanner(f);
                    String allLines = new String();
                    while (sc.hasNextLine()) {
                        allLines += sc.nextLine().toLowerCase();
                    }
                    docs.add((allLines));
                    documentDetails.add(index,f.getName().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return docs;
    }

    /**
     * Reads words from each documents and split them using delimiters.
     *
     * @param  fileResults- contains the list of files
     * @return store - contains tokens created after using delimiters
     */
    private static List<String[]> tokenCreation(ArrayList<String> fileResults) {
        ArrayList<String[]> store = new ArrayList<>();
        String allLines = new String();
        String[] tokens = new String[0];
        for (String str : fileResults) {
            tokens = str.split("[ '.,&#?!:;$%+()\\-\\/*\"]+");
            store.add(tokens);
        }
        return store;
    }

    /**
     * Reads the words from the stopWordFile
     * And store them in sorted order.
     *
     * @param stopWordsFile - contains words to be removed from tokens(store).
     * @retun
     */
    private static String[] stopWordsFile(File stopWordsFile) {
        try {
            String stop = "";
            Scanner sc = new Scanner(stopWordsFile);
            while (sc.hasNextLine()) {
                stop += sc.nextLine().toLowerCase() + " ";
            }
            stopWords = stop.split("[ ']+");
            Arrays.sort(stopWords);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    /**
     * search and remove the stop words
     * and perform tokenization
     *
     *
     * @param stopList and tokens - contains words to be removed from tokens(store).
     * @return allstems - contains all the tokens after stemming
     */
    private static List<ArrayList<String>> searchAndRemoveStopWords(String[] stopList, List<String[]> tokens) {//not complete this function check if hashset can be used
        ArrayList<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
        for (String[] i : tokens) {
            ArrayList<String> initialTokens = new ArrayList<>();
            for (String j : i) {
                if (searchStopWords(j, stopList) == -1)
                    initialTokens.add(j);
            }
            allTokens.add(initialTokens);
        }
        for (ArrayList<String> sk : allTokens) {
            ArrayList<String> stemms = new ArrayList<String>();
            Stemmer st = new Stemmer();
            for (String sp : sk) {

                st.add(sp.toCharArray(), sp.length());
                st.stem();
                stemms.add(st.toString());
                st = new Stemmer();
            }
            allStems.add(stemms);
        }
        return allStems;
    }

    /**
     * search the stop words
     * to be removed using binary search
     * @param  token and stopList- contains word to be check in stopList.
     * @return mid - returns the index of found token
     */
    private static int searchStopWords(String token, String[] stopList) {
        int lo = 0;
        int hi = stopList.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int result = token.compareTo(stopList[mid]);
            if (result < 0) hi = mid - 1;
            else if (result > 0) lo = mid + 1;
            else return mid;
        }
        return -1;
    }
    /**
     * Creates inverted index
     * @param doc - contains arrayList of stemmed tokens.
     *
     */
    private static String invertedIndex(List<ArrayList<String>> doc) {
        for (int i = 0; i < doc.size(); i++) {
            String myDoc[] = doc.get(i).toArray(new String[0]);
            for (String words : myDoc) {
                if (!termList.contains(words)) {
                    termList.add(words);
                    docList = new ArrayList<Integer>();
                    docList.add(i);
                    docLists.add(docList);
                } else {
                    int index = termList.indexOf(words);
                    docList = docLists.get(index);
                    if (!docList.contains(i)) {
                        docList.add(i);
                        docLists.set(index, docList);
                    }

                }

            }
        }

        String outputString = new String();
        for (int i = 0; i < termList.size(); i++) {
            outputString += String.format("%-15s", termList.get(i));
            docList = docLists.get(i);
            for (int j = 0; j < docList.size(); j++) {
                outputString += docList.get(j) + "\t";
            }
            outputString += "\n";
        }
        return outputString;
    }

    /**
     * Search algorithm for single keyword
     * @param singleQuery : keyword to be searched
     *
     */
    private static void searchSingleString(String singleQuery) {
        if(termList.contains(stemmingForKeywords(singleQuery))) {
            ArrayList<Integer> temp=docLists.get(termList.indexOf(stemmingForKeywords(singleQuery)));
            System.out.println("Document ID's for single search "+ docLists.get(termList.indexOf(stemmingForKeywords(singleQuery))));
            for (int i = 0; i < temp.size(); i++)
                System.out.println(documentDetails.get(temp.get(i)-1));
        }
        else{
            System.out.println(singleQuery  +" : Keyword not found for single search");
        }

    }
    /**
     * Search algorithm for two keywords using AND operation
     * @param keyOne and KeyTwo : keyword to be searched
     *
     */
    private static void searchDoubleAndResult(String keyOne, String keyTwo) {
        ArrayList<Integer>searchTwo = new ArrayList<Integer>();
        String resultOne = stemmingForKeywords(keyOne);
        String resultTwo = stemmingForKeywords(keyTwo);
        if (termList.contains(resultOne) && termList.contains((resultTwo))) {
            ArrayList<Integer> listOne = docLists.get(termList.indexOf(resultOne));
            ArrayList<Integer> listTwo = docLists.get(termList.indexOf(resultTwo));
            for(int i = 0,j=0;i<listOne.size()&&j<listTwo.size();){
                if(listOne.get(i) == listTwo.get(j)){
                    searchTwo.add(listOne.get(i));
                    i++;
                    j++;
                }
                else if(listOne.get(i)<listTwo.get((j))){
                    i++;
                }
                else
                    j++;
            }

            System.out.println("Document ID's for Two keywords with AND operation "+ searchTwo);
            for (int i = 0; i < searchTwo.size(); i++)
                System.out.println(documentDetails.get(searchTwo.get(i)-1));
        }

        else{
            System.out.println("Keywords not found");
        }
    }

    /**
     * Search algorithm for two keywords using OR operation
     * @param keyOne and KeyTwo : keyword to be searched
     *
     */
    private static void searchDoubleOrResult(String keyOne, String keyTwo) {
        ArrayList<Integer>searchOr = new ArrayList<>();
        String resultOne = stemmingForKeywords(keyOne);
        String resultTwo = stemmingForKeywords(keyTwo);
        if(termList.contains(resultOne)|| termList.contains(resultTwo)){
            ArrayList<Integer> listOrOne = docLists.get(termList.indexOf(resultOne));
            ArrayList<Integer> listOrTwo = docLists.get(termList.indexOf(resultTwo));
            if(listOrOne!=null &&listOrTwo!=null) {
                int i,j;
                for ( i = 0, j = 0; i < listOrOne.size() && j < listOrTwo.size(); ) {
                    if (listOrOne.get(i) == listOrTwo.get(j)) {
                        searchOr.add(listOrOne.get(i));
                        i++;
                        j++;
                    } else if (listOrOne.get(i) < listOrTwo.get(j)) {
                        searchOr.add(listOrOne.get(i));
                        i++;
                    }
                }
                while(i<listOrOne.size()){
                    searchOr.add(listOrOne.get(i));
                    i++;
                }
                while(j<listOrTwo.size()){
                    searchOr.add(listOrTwo.get(j));
                    j++;
                }

            }
            System.out.println("Document ID for two keywords with OR operation: "+searchOr);
            for (int i = 0; i < searchOr.size(); i++)
                System.out.println(documentDetails.get(searchOr.get(i)-1));
        }

        else{
            System.out.println("Keyword not found");
        }
    }
    /**
     * stemming the keywords for single and double keyword search
     * @param keyOne : keyword to be stemmed
     *
     */
    private static String stemmingForKeywords(String keyOne) {
        Stemmer st = new Stemmer();
        st.add(keyOne.toCharArray(),keyOne.length());
        st.stem();
        return st.toString();
    }
    /**
     * Search algorithm for three or more keywords using AND operation
     * @param searchString : a string containing keyword to be searched
     *
     */
    private static void searchThreeOrMore(String searchString) {
        String[] search= searchString.split(" ");
        ArrayList<String> at=stemming_new(search);
        ArrayList<ArrayList<Integer>> postingLists = new ArrayList<ArrayList<Integer>>();
        for(int i=0;i<at.size();i++){
            ArrayList<Integer>temp = new ArrayList<>();
            if(termList.contains(at.get(i))) {
                temp = docLists.get(termList.indexOf(at.get(i)));
                postingLists.add(temp);
            }
            else{
                System.out.println("Keyword not found");
            }
        }
        SortingPostingLists(postingLists); //sorting multiple posting lists based on size
        mergeForPostingLists(postingLists);

    }
    /**
     * Stemming the string for three or more keywords
     * @param search : a string containing keyword to be stemmed
     * @return : searchResults- final stemmed keywords of the string
     *
     */
    private static ArrayList<String> stemming_new(String[] search) {
        ArrayList<String> searchResults = new ArrayList<>();
        Stemmer st = new Stemmer();
        for(String word: search){
            st.add(word.toCharArray(), word.length());
            st.stem();
            searchResults.add(st.toString());
        }
        return searchResults;
    }

    /**
     * sorting posting lists in order to reduce the number of comparission
     * @param postingLists - contains arrayList of arrayList of postingLists.
     * @return postingLists - returning sorted posting lists after applying bubble sort
     *
     */
    private static ArrayList<ArrayList<Integer>> SortingPostingLists(ArrayList<ArrayList<Integer>> postingLists) {
        for(int i=0;i<postingLists.size()-1;i++){
            int min = i;
            for(int j=i+1;j<postingLists.size();j++){
                if(postingLists.get(j).size()<postingLists.get(i).size()) {
                    min = j;
                    ArrayList<Integer> temp = postingLists.get(min);
                    postingLists.set(min, postingLists.get(i));
                    postingLists.set(i, temp);
                }
            }
        }
        return postingLists;
    }
    /**
     * Logic for merging two or more posting Lists
     * @param postingLists - contains sorted posting lists.
     *
     *
     */
    private static void mergeForPostingLists(ArrayList<ArrayList<Integer>> postingLists) {
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();
        first = postingLists.get(0);
        second = postingLists.get(1);
        for (int k = 0, l = 0; k < first.size() && l < second.size(); ) {
            if (first.get(k) == second.get(l)) {
                temp.add(first.get(k));
                k++;
                l++;
            } else if (first.get(k) < second.get((l))) {
                k++;
            } else
                l++;
        }
        for(int i=2;i<postingLists.size();i++){
            ArrayList<Integer> temp2 = postingLists.get(i);
            ArrayList<Integer>init = new ArrayList<>();
            for(int k=0,j=0;k<temp.size() && j<temp2.size();){
                if(temp.get(k)==temp2.get(j)){
                    init.add(temp.get(k));
                    j++;
                    k++;
                }
                else if(temp.get(k)<temp2.get(j)){
                    k++;
                }
                else
                    j++;
            }
            temp=init;
        }
        System.out.println("Document ID for three or more keywords :" +temp);
        for (int i = 0; i < temp.size(); i++){
            System.out.println(documentDetails.get(temp.get(i)-1));
        }

    }
    public static void main(String args[]) {
        String dir = "Lab1_Data/";
        File f = new File(dir);
        File[] files = f.listFiles();
        ArrayList<String> fileResults = readFileFromPath(files); // reading multiple files from directory
        List<String[]> tokens = tokenCreation(fileResults); // creating tokens from individual files
        String stopWordsFile = "stopwords.txt";
        File ft = new File(stopWordsFile);
        String[] stopList = stopWordsFile(ft); // creating stopwords and applying bubble sort
        List<ArrayList<String>> stemr = searchAndRemoveStopWords(stopList, tokens); //stemming and removing stopwords
        System.out.println(invertedIndex(stemr)); //creating inverted index

        //search test case for single keyword search
        searchSingleString("plot");
        searchSingleString("couples");
        searchSingleString("siddharth");

        //search test case for two keywords with AND operation
        searchDoubleAndResult("plot", "accident");
        searchDoubleAndResult("teen", "movie");
        searchDoubleAndResult("Siddharth","Chauhan");

        //search test case for two keywords with OR operation
        searchDoubleOrResult("teen","plot");
        searchDoubleOrResult("Siddharth","chauhan");

        //search test for three or more terms with AND operation
        searchThreeOrMore("teen plot accident");
        searchThreeOrMore("watch movie find teen");
        searchThreeOrMore("provide danger live fall");
        searchThreeOrMore("plot watch movie film attempt audience character video plai");

    }

}


