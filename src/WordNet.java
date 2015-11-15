import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

   private SAP sap;
   private HashMap<Integer, String> id2synset;
   private HashMap<String, Bag<Integer>> noun2id;
   private Digraph wordgraph;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms)
   {
       id2synset = new HashMap<Integer, String>();
       noun2id = new HashMap<String, Bag<Integer>>();
       readSynsets(synsets);
       readHypernyms(hypernyms);
       checkcycle(wordgraph);
       checkrooted(wordgraph);
       
       sap = new SAP(wordgraph);
   }
   
   private void checkcycle(Digraph g)
   {
       DirectedCycle cycle = new DirectedCycle(g);
       if (cycle.hasCycle()) throw new java.lang.IllegalArgumentException();
   }
   
   private void checkrooted(Digraph g)
   {
       int num = 0;
       for (int i = 0; i < g.V(); i++)
       {
           if (!g.adj(i).iterator().hasNext())
               num++;
       }
       
       if (num != 1)
           throw new java.lang.IllegalArgumentException();
   }
   
   private void readSynsets(String fileSynsets)
   {
       In input = new In(fileSynsets);
       Bag<Integer> bag;

       while (input.hasNextLine()) 
       {
           String[] tokens = input.readLine().split(",");
           int id = Integer.parseInt(tokens[0]);
           id2synset.put(id, tokens[1]);
           
           for (String noun: tokens[1].split(" "))
           {
               if (!noun2id.containsKey(noun))
               {
                   bag = new Bag<Integer>();
                   bag.add(id);
                   noun2id.put(noun, bag);
               }
               else
               {
                   bag = noun2id.get(noun);
                   bag.add(id);
               }
           }
       }
   }
   
   private void readHypernyms(String fileHypernyms)
   {
       In input = new In(fileHypernyms);
       wordgraph = new Digraph(id2synset.size());
       
       while (input.hasNextLine())
       {
           String[] tokens = input.readLine().split(",");
           int id = Integer.parseInt(tokens[0]);
           for (int i = 1; i < tokens.length; i++)
           {
               wordgraph.addEdge(id, Integer.parseInt(tokens[i]));
           }
       }
   }

   // returns all WordNet nouns
   public Iterable<String> nouns()
   {
       return noun2id.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word)
   {
       if (word == null) throw new java.lang.NullPointerException();
       return noun2id.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB)
   {
       if (nounA == null || nounB == null) throw new java.lang.NullPointerException();
       if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
       return sap.length(noun2id.get(nounA), noun2id.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB)
   {
       if (nounA == null || nounB == null) throw new java.lang.NullPointerException();
       if (!(isNoun(nounA) && isNoun(nounB))) throw new IllegalArgumentException();
       return id2synset.get(sap.ancestor(noun2id.get(nounA), noun2id.get(nounB)));
   }

   // do unit testing of this class
   public static void main(String[] args) {
       WordNet wordNet = new WordNet(args[0], args[1]);

       while (!StdIn.isEmpty()) {
           String nounA = StdIn.readString();
           String nounB = StdIn.readString();

           if (!wordNet.isNoun(nounA)) {
               StdOut.printf("%s is not a noun!\n", nounA);
               continue;
           }

           if (!wordNet.isNoun(nounB)) {
               StdOut.printf("%s is not a noun!\n", nounB);
               continue;
           }

           int distance = wordNet.distance(nounA, nounB);
           String ancestor = wordNet.sap(nounA, nounB);

           StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
       }
   }
}