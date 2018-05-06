import java.util.HashMap;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private HashMap<Integer,String> id2noun;
    private HashMap<String, Bag<Integer>> noun2id;
    private Digraph relationDigraph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        id2noun = new HashMap<>();
        noun2id = new HashMap<>();
        In Synsets = new In(synsets);
        In Hypernyms = new In(hypernyms);
        int root = 0;
        while (Synsets.hasNextLine()) {
            String[] line = Synsets.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            id2noun.put(id, line[1]);

            for (String noun : line[1].split(" ")) {
                if (noun2id.get(noun) == null) {
                    noun2id.put(noun, new Bag<>());
                }
                noun2id.get(noun).add(id);
            }
        }

        relationDigraph = new Digraph(id2noun.size());
        while (Hypernyms.hasNextLine()) {
            String[] line = Hypernyms.readLine().split(",");
            int synId = Integer.parseInt(line[0]);
            for (int i =1;i<line.length;i++) {
                relationDigraph.addEdge(synId, Integer.parseInt(line[i]));
            }
        }

        DirectedCycle checker = new DirectedCycle(relationDigraph);
        if (checker.hasCycle()) {
            throw new IllegalArgumentException("Not acyclic");
        }

        for (int i = 0; i < relationDigraph.V();i++){
            if(!relationDigraph.adj(i).iterator().hasNext())
                root++;
        }
        if(root!=1){
            throw new IllegalArgumentException("Not a rooted DAG");
        }

        sap = new SAP(relationDigraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun2id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return noun2id.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException ();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        Bag<Integer> synsetA = noun2id.get(nounA);
        Bag<Integer> synsetB = noun2id.get(nounB);
        return sap.length(synsetA, synsetB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA)||!isNoun(nounB))
            throw new IllegalArgumentException();
        Bag<Integer> idsA = noun2id.get(nounA);
        Bag<Integer> idsB = noun2id.get(nounB);
        SAP sap = new SAP(relationDigraph);
        int ancestor = sap.ancestor(idsA,idsB);
        return id2noun.get(ancestor);
    }

    // do unit testing of this class

}
