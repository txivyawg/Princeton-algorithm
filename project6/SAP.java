import edu.princeton.cs.algs4.Digraph;


public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new NullPointerException();
        this.G = new Digraph(G);
    }

    private Boolean isInValid(int v){
        return v < 0 || v > G.V()-1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if(isInValid(v) || isInValid(w))
            throw new IllegalArgumentException ();

        int ancestor = ancestor(v, w);
        if(ancestor == -1)
            return -1;

        DeluxeDFS bfsv = new DeluxeDFS(G, v);
        DeluxeDFS bfsw = new DeluxeDFS(G, w);

        return bfsv.disTo(ancestor) + bfsw.disTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if(isInValid(v) || isInValid(w))
            throw new IllegalArgumentException ();

        DeluxeDFS bfsv = new DeluxeDFS(G, v);
        DeluxeDFS bfsw = new DeluxeDFS(G, w);

        boolean[] ancestorv = bfsv.getMarked() ;
        boolean[] ancestorw = bfsw.getMarked();
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i =0; i < G.V() ;i++) {
            if (ancestorv[i] && ancestorw[i]) {
                int checkDistance = bfsv.disTo(i) + bfsw.disTo(i);
                if (checkDistance < distance) {
                    distance = checkDistance;
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if(v ==null || w == null)
            throw new NullPointerException();
        for(Integer id: v){
            if(isInValid(id))
                throw new IllegalArgumentException ();
        }
        for(Integer id: w){
            if(isInValid(id))
                throw new IllegalArgumentException ();
        }

        int ancestor = ancestor(v, w);
        if(ancestor == -1)
            return -1;

        DeluxeDFS bfsv = new DeluxeDFS(G, v);
        DeluxeDFS bfsw = new DeluxeDFS(G, w);

        return bfsv.disTo(ancestor) + bfsw.disTo(ancestor);

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if(v ==null || w == null)
            throw new NullPointerException();
        for(Integer id: v){
            if(isInValid(id))
                throw new IllegalArgumentException ();
        }
        for(Integer id: w){
            if(isInValid(id))
                throw new IllegalArgumentException ();
        }

        DeluxeDFS bfsv = new DeluxeDFS(G, v);
        DeluxeDFS bfsw = new DeluxeDFS(G, w);

        boolean[] ancestorv = bfsv.getMarked() ;
        boolean[] ancestorw = bfsw.getMarked();
        int distance = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i =0; i < G.V() ;i++) {
            if (ancestorv[i] && ancestorw[i]) {
                int checkDistance = bfsv.disTo(i) + bfsw.disTo(i);
                if (checkDistance < distance) {
                    distance = checkDistance;
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    // do unit testing of this class
   // public static void main(String[] args)
}
