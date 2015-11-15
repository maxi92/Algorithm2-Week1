
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
               result = noun;
       }
       return result;
   }
   //public static void main(String[] args)  // see test client below
}