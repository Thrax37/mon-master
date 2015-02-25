package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utils<T> {

	public List<List<T>> distribute(List<T> list, int y) {
	    int x = list.size();
	    int nLists = (int) Math.ceil((double)x/y);

	    // Create result lists
	    List<List<T>> result = new ArrayList<>();
	    for (int j = 0; j < nLists; j++)
	        result.add(new ArrayList<T>());
	    List<List<T>> outputLists = new ArrayList<>(result);

	    // Create item count store
	    Map<T, Integer> itemCounts = new HashMap<>();
	    for (T item : list)
	        itemCounts.put(item, 0);

	    // Populate results
	    Random random = new Random();
	    for (int i = 0; i < x; i++) {
	        // Add a random item (from the remaining eligible items)
	        // to a random list (from the remaining eligible lists)
	        T item = list.get(0);
	        List<T> outputList = null;
	        do {
	            outputList = outputLists.get(random.nextInt(outputLists.size()));
	        } while (outputList.contains(item));
	        outputList.add(item);

	        // Manage eligible output lists
	        if (outputList.size() >= y)
	            outputLists.remove(outputList);

	        // Manage eligible items
	        list.remove(item);
	    }

	    return result;
	}
	
	public List<List<T>> distributeAll(List<T> list, int y, int z) {
	    int x = list.size();
	    int nLists = (int) Math.ceil((double)(x*z)/y);

	    // Create result lists
	    List<List<T>> result = new ArrayList<>();


	    // Create item count store
	    Map<T, Integer> itemCounts = new HashMap<>();
	    for (T item : list){
	        itemCounts.put(item, 0);
	    }
	    
	    // Populate results
	    Random random = new Random();
	    ArrayList<T> copyList=new ArrayList<T>(); //create a copy of the List of Ts. 
	    for (int i = 0; i < nLists; i++) {
	       
	        //the copyList is reduced for each T i pick out of it, so i can assure, that no item is used twice in a result, and several item arent picked 5 times and other 0 times, to prevent a error in the end.
	        ArrayList<T> tempList=new ArrayList<T>();
	        //if there are less items in the copyList, than fitting in the resultlist, refill it
	        if (copyList.size()<y && list.size()>1){
	            for (T item : list){
	                if (!copyList.contains(item)) copyList.add(item);
	                else {
	                    tempList.add(item);
	                    int itemCount = itemCounts.get(item).intValue()+1;
	                    if (itemCount >= z) {
	                        list.remove(item);
	                        copyList.remove(item);
	                    }
	                    else
	                        itemCounts.put(item, itemCount);
	                }
	            }
	        }
	        // as long als the tempList isnt filled and there are items in the list to assign, add Ts to the tempList
	        while (tempList.size()<y && list.size()>0) {
	            random=new Random();
	            T item = copyList.get(random.nextInt(copyList.size()));
	            if (!tempList.contains(item)){
	                tempList.add(item);
	                copyList.remove(item);
	                int itemCount = itemCounts.get(item).intValue()+1;
	                if (itemCount >= z)
	                    list.remove(item);
	                else
	                    itemCounts.put(item, itemCount);
	            }
	        }
	        result.add(tempList);
	    }
	    return result;
	}

	public List<List<T>> distributeLists(List<T> items, int y, int z) {
	    // Create list of items * z
	    List<T> allTs = new ArrayList<>();
	    for (int i = 0; i < z; i++)
	        allTs.addAll(items);
	    Collections.shuffle(allTs);

	    // Randomly shuffle list
	    List<List<T>> result = new ArrayList<>();
	    int totalTs = items.size()*z;
	    for (int i = 0; i < totalTs; i += y)
	        result.add(new ArrayList<T>(allTs.subList(i, Math.min(totalTs, i+y))));

	    // Swap items in lists until lists are unique
	    for (List<T> resultList : result) {
	        // Find duplicates
	        List<T> duplicates = new ArrayList<>(resultList);
	        for (T item : new HashSet<T>(resultList))
	            duplicates.remove(item);

	        for (T duplicate : duplicates) {
	             // Swap duplicate for item in another list
	            for (List<T> swapCandidate : result) {
	                if (swapCandidate.contains(duplicate))
	                    continue;
	                List<T> candidateReplacements = new ArrayList<>(swapCandidate);
	                candidateReplacements.removeAll(resultList);
	                if (candidateReplacements.size() > 0) {
	                    T replacement = candidateReplacements.get(0);
	                    resultList.add(resultList.indexOf(duplicate), replacement);
	                    resultList.remove(duplicate);
	                    swapCandidate.add(swapCandidate.indexOf(replacement), duplicate);
	                    swapCandidate.remove(replacement);
	                    break;
	                }
	            }
	        }
	    }

	    return result;
	}
}
