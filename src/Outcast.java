import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {
   
   WordNet wordnet;
   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
   {
       this.wordnet = wordnet;
   }
   
   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
   {
       int maxdist = 0;
       int dist; 
       String result = nouns[0];
       
       for(String noun : nouns)
       {
           dist = 0;
           for(String arrnoun : nouns)
           {
               if(arrnoun != noun)
               {
                   
                   dist += wordnet.distance(noun, arrnoun);
               }
           }
           
           if (dist > maxdist)
               {
                   result = noun;
                   maxdist = dist;
               }
       }
       return result;
   }
   public static void main(String[] args) {
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int t = 2; t < args.length; t++) {
           In in = new In(args[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
   }
}