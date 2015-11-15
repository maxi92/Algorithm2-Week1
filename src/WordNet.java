import java.util.HashMap;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {

   SAP sap;
   HashMap<Integer,String> id2synset;
   HashMap<String,Bag<Integer>> noun2id;
   Digraph wordgraph;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms)
   {
       id2synset = new HashMap<Integer, String>();
       noun2id = new HashMap<String, Bag<Integer>>();
       readSynsets(synsets);
       readHypernyms(hypernyms);
       DirectedCycle cycle = new DirectedCycle(wordgraph);
       if (cycle.hasCycle()) throw new java.lang.IllegalArgumentException();
       
       sap = new SAP(wordgraph);
   }
   
   private void readSynsets(String Filesynsets)
   {
       In input = new In(Filesynsets);
       Bag<Integer> bag;

       while (input.hasNextLine()) 
       {
           String[] tokens = input.readLine().split(",");
           int id = Integer.parseInt(tokens[0]);
           id2synset.put(id, tokens[1]);
           
           for(String noun: tokens[1].split(" "))
           {
               if(!noun2id.containsKey(noun))
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
   
   private void readHypernyms(String Filehypernyms)
   {
       In input = new In(Filehypernyms);
       wordgraph = new Digraph(id2synset.size());
       
       while(input.hasNextLine())
       {
           String tokens[] = input.readLine().split(",");
           int id = Integer.parseInt(tokens[0]);
           for (int i=1; i<tokens.length; i++)
           {
               wordgraph.addEdge(Integer.parseInt(tokens[i]), id);
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
       return noun2id.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB)
   {
       if(isNoun(nounA) || isNoun(nounB) != true) throw new IllegalArgumentException();
       return sap.length(noun2id.get(nounA), noun2id.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB)
   {
       if(isNoun(nounA) || isNoun(nounB) != true) throw new IllegalArgumentException();
       return this.id2synset.get(sap.ancestor(this.noun2id.get(nounA), this.noun2id.get(nounB)));
   }

   // do unit testing of this class
   //public static void main(String[] args)
}