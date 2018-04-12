public class Outcast {
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int[] distance = new int[nouns.length];
        for (int i=0; i<nouns.length; i++){
            for (int j=i; j<nouns.length; j++){
                int dist = wordnet.distance(nouns[i], nouns[j]);
                distance[i] += dist;
                if (i != j){
                    distance[j] += dist;
                }
            }
        }
        int maxDistance = 0;
        int maxIndex = 0;
        for (int i=0; i<distance.length; i++){
            if (distance[i] > maxDistance){
                maxDistance = distance[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }   // given an array of WordNet nouns, return an outcast
   // public static void main(String[] args)  // see test client below
}