/* HashTableChained.java */

package dict;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

import java.lang.Math;
import list.*;

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
    public int sizeHash;
    public int buckets;
    public DList[] table;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
      sizeHash = 0;
      double bucketEstimate = sizeEstimate;
      if (isPrime(((int)bucketEstimate))) {
          buckets = (int) (bucketEstimate *1.1);
      }
      else {
          while (!isPrime(((int)bucketEstimate))) {
              bucketEstimate++;
          }
          buckets = (int) bucketEstimate;
      }
      table = new DList[buckets];
      for (int i=0; i<buckets; i++) {
          table[i] = new DList();
      }
  }
    
    public void resize() {
        try {
            int bucketEstimate=0;
            double hashS = sizeHash, buck = buckets;
            if (hashS/buck>.95) {
                bucketEstimate = ((int) (buck*2)+1);              //make new bucket number
            }
            while (!isPrime(((int)bucketEstimate))) {
                bucketEstimate++;
                int oldbuck = buckets;                      //store old bucket number
                buckets = bucketEstimate;                   //set new bucket number
                Object[] oldTable = table;                  //save old table
                table = new DList[buckets];                 //set new table
                sizeHash = 0;                               //reset hashtable size
                //WE NEED TO GO THROUGH AND MAKE SURE THAT THE NEW TABLE IS FILLED
                //THEN WE CAN APPEND
                for (int i=0; i<buckets; i++) {
                    table[i] = new DList();
                }
                int i;
                for (i=0; i<oldbuck; i++) {
                    if (table[i]!=null) {
                        DListNode curr =  (DListNode) table[i].front();
                        for (int j=0; j<table[i].length(); j++)  {
                            insert( ((Entry) curr.item()).key(), ((Entry) curr.item()).value());
                            curr = (DListNode) curr.next();
                        }
                    }
                }
            }
        }
        catch (InvalidNodeException e1) {
            System.out.println("ERROR IN resize");
            System.out.println(e1);
        }
    }
    
    public static boolean isPrime(int n) {
        int divisor = 2;
        while (divisor < Math.sqrt(n)) {
            if (n % divisor == 0) {
                return false;
            }                            
            divisor++;                  
        }
        return true;
    }
    

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
      sizeHash = 0;
      buckets = 101;
      table = new DList[buckets];
      for (int i=0; i<buckets; i++) {
          table[i] = new DList();
      }
    // Your solution here.
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
      if (code%buckets<0) {
          return (code%buckets)+buckets;
      }
    return code%buckets;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return sizeHash;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return (size()==0);
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
      Entry inserter=new Entry();
      inserter.key = key;
      inserter.value = value;
      table[compFunction(key.hashCode())].insertFront(inserter);
      sizeHash++;
      resize();
      return inserter;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
      try {
          // Replace the following line with your solution.
          List currList = table[compFunction(key.hashCode())];
          ListNode curr = table[compFunction(key.hashCode())].front();
          if (currList.length()>0) {
              for (int i=0; i<currList.length(); i++) {
                  if (((Entry)curr.item()).key.equals(key)) {
                      return (Entry) curr.item();
                  }
                  
                  else {
                      curr = curr.next();
                  }
              }
          }
          return null;
      }
      catch (InvalidNodeException e1) {
          System.out.println("ERROR IN resize");
          System.out.println(e1);
      }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
      try {
          // Replace the following line with your solution.
          List currList = table[compFunction(key.hashCode())];
          ListNode curr = table[compFunction(key.hashCode())].front();
          if (currList.length()>0) {
              for (int i=0; i<currList.length(); i++) {
                  if (((Entry)curr.item()).key.equals(key)) {
                      Entry result = new Entry();
                      result.key = key;
                      result.value = ((Entry) curr.item()).value;
                      curr.remove();
                      sizeHash--;
                      resize();
                      return result;
                  }
                  
                  else {
                      curr = curr.next();
                  }
              }
              resize();
              return null;
          }
          resize();
          return null;
      }
      catch (InvalidNodeException e1) {
          System.out.println("ERROR IN remove");
          System.out.println(e1);
      }
      return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
      try {
          // Your solution here.
          while (size()>0) {
              for (DList cell : table) {
                  if (cell.length() !=0) {
                      ListNode curr = cell.front();
                      do {
                          ListNode temp = curr;
                          curr = curr.next();
                          temp.remove();
                          sizeHash--;
                      } while (curr!=cell.back());
                  }
              }
          }
      }
      catch (InvalidNodeException e1) {
          System.out.println("ERROR IN remove");
          System.out.println(e1);
      }
  }
    public static void main(String[] argv) {
        HashTableChained test = new HashTableChained();
        System.out.println("buckets should be 101 "+test.buckets);
        HashTableChained test2 = new HashTableChained(50);
        System.out.println("buckets should be prime less than 50 "+test2.buckets);
        HashTableChained test3 = new HashTableChained(100);
        System.out.println("buckets should be prime lt 100 "+test3.buckets);
        HashTableChained test4 = new HashTableChained(125);
        System.out.println("buckets should be prime less than 125 "+test.buckets);
        
        test.insert(new Integer(1),new Integer(1));
        test.insert(new Integer(2),new Integer(1));
        test.insert(new Integer(4),new Integer(1));
        System.out.println("Should be 3 "+test.size());
        System.out.println("Should be a value "+test.find(new Integer(4)));
        System.out.println("Should be null "+test.find(new Integer(5)));
        System.out.println("Should be null "+test.remove(new Integer(6)));
        System.out.println("Should be a value "+test.remove(new Integer(2)));
        System.out.println("Should be 2 "+test.size());
        test.makeEmpty();
        System.out.println("Should be null "+test.find(new Integer(4)));
        System.out.println("Size should be 0 "+test.size());
        
        
    }
    
}
