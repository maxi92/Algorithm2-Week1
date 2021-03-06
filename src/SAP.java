import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SAP {

    private Digraph G;
    private BFSAP vsap, wsap;
    
    private class BFSAP implements Iterable<Integer> {
        private boolean[] marked;
        private int[] distance;
        private Queue<Integer> queue;    //queue for BFS
        private Queue<Integer> cvertex; //the vertex that connected with v, including v itself, this is for iterator
        
        public BFSAP(Digraph G) 
        {
            marked = new boolean[G.V()];
            distance = new int[G.V()];
            queue = new Queue<Integer>();
            cvertex = new Queue<Integer>();
        }
        
        public Iterator<Integer> iterator() {
            return cvertex.iterator();
        }
        
        public void bfs(int v)
        {
            marked[v] = true;
            distance[v] = 0;
            queue.enqueue(v);
            cvertex.enqueue(v);
            while (!queue.isEmpty())
            {
                int t = queue.dequeue();
                for (int w: G.adj(t))
                {
                    if (!marked[w])
                    {
                        distance[w] = distance[t] + 1;
                        marked[w] = true;
                        queue.enqueue(w);
                        cvertex.enqueue(w);
                    }
                }
            }
        }
        
        public void bfs(Iterable<Integer> v)
        {
            for (int w : v)
            {
                marked[w] = true;
                distance[w] = 0;
                queue.enqueue(w);
                cvertex.enqueue(w);
            }
            while (!queue.isEmpty())
            {
                int t = queue.dequeue();
                for (int w: G.adj(t))
                {
                    if (!marked[w])
                    {
                        distance[w] = distance[t] + 1;
                        marked[w] = true;
                        queue.enqueue(w);
                        cvertex.enqueue(w);
                    }
                }
            }
        }
        
        private boolean isConnected(int v)
        {
            return marked[v];
        }
        
        private int dist(int v)
        {
            return this.distance[v];
        }
    }
    
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G)
   {
       if (G == null)
           throw new java.lang.NullPointerException();
       this.G = new Digraph(G);
       this.vsap = new BFSAP(G);
       this.wsap = new BFSAP(G);
   }
   
   private void validcheck(int v)
   {
       if (v < 0 || v > G.V()-1)
           throw new java.lang.IndexOutOfBoundsException();
   }
   
   private void validcheck(Iterable<Integer> v)
   {
       for (int w : v)
       {
           validcheck(w);
       }
   }

   
   private int calc(int v, int w, boolean type)
   {
       vsap = new BFSAP(G);
       wsap = new BFSAP(G);
       vsap.bfs(v);
       wsap.bfs(w);
       int mindist = -1;
       int ancestor = -1;
       boolean flag = false;
       
       int result = 0;
       
       for (int t : vsap)
       {
           if (vsap.isConnected(t) && wsap.isConnected(t))
           {
               if (!flag)
               {
                   mindist = vsap.distance[t] + wsap.distance[t];
                   ancestor = t;
                   flag = true;
                   continue;
               }
               if (vsap.distance[t] + wsap.distance[t] < mindist)
               {
                   mindist = vsap.distance[t]+wsap.distance[t];
                   ancestor = t;
               }
           }
       }
       
       if (type) result = mindist;
       if (!type) result = ancestor;
       return result;
   }
   
   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w)
   {
       validcheck(v);
       validcheck(w);
       return calc(v, w, true);
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w)
   {
       validcheck(v);
       validcheck(w);
       return calc(v, w, false);
   }

   private int calc(Iterable<Integer> v, Iterable<Integer> w, boolean type)
   {
       vsap = new BFSAP(G);
       wsap = new BFSAP(G);
       vsap.bfs(v);
       wsap.bfs(w);
       int mindist = -1;
       int ancestor = -1;
       boolean flag = false;
       
       int result = 0;
       
       for (int t : vsap)
       {
           if (vsap.isConnected(t) && wsap.isConnected(t))
           {
               if (!flag)
               {
                   mindist = vsap.distance[t] + wsap.distance[t];
                   ancestor = t;
                   flag = true;
                   continue;
               }
               if (vsap.distance[t] + wsap.distance[t] < mindist)
               {
                   mindist = vsap.distance[t] + wsap.distance[t];
                   ancestor = t;
               }
           }
       }
       
       if (type) result = mindist;
       if (!type) result = ancestor;
       return result;
   }
   
   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w)
   {
       validcheck(v);
       validcheck(w);
       return calc(v, w, true);
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
   {
       validcheck(v);
       validcheck(w);
       return calc(v, w, false);
   }

   // do unit testing of this class
   public static void main(String[] args) {
       In in = new In(args[0]);
       Digraph G = new Digraph(in);
       SAP sap = new SAP(G);
       while (!StdIn.isEmpty()) {
           int v = StdIn.readInt();
           int w = StdIn.readInt();
           int length   = sap.length(v, w);
           int ancestor = sap.ancestor(v, w);
           StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
   }
}
