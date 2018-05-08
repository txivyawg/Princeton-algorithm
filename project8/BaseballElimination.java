import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;

public class BaseballElimination {
    private int Num;
    private int[] wins,losses,remaining;
    private int[][] games;
    private String[] teams;
    private HashMap<String, Integer> nameToNum;
    private HashSet<String> teamSet;
    private FordFulkerson maxFlowSolver;

    public BaseballElimination(String filename) {
        In in = new In(filename);

        Num = in.readInt();
        teams = new String[Num];
        wins = new int[Num];
        losses = new int[Num];
        remaining = new int[Num];
        games = new int[Num][Num];
        nameToNum = new HashMap<>();

        for (int i = 0; i < Num; i++) {
            teams[i] = in.readString();
            nameToNum.put(teams[i], i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < Num; j++) {
                games[i][j] = in.readInt();
            }
        }
    }                    // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return Num;
    }                        // number of teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }                                // all teams
    public int wins(String team) {
        if (!nameToNum.containsKey(team)) throw new IllegalArgumentException();

        return wins[nameToNum.get(team)];
    }                      // number of wins for given team
    public int losses(String team) {
        if (!nameToNum.containsKey(team)) throw new IllegalArgumentException();

        return losses[nameToNum.get(team)];
    }                    // number of losses for given team
    public int remaining(String team) {
        if (!nameToNum.containsKey(team)) throw new IllegalArgumentException();

        return remaining[nameToNum.get(team)];
    }                 // number of remaining games for given team
    public int against(String team1, String team2) {
        if (!nameToNum.containsKey(team1) ||!nameToNum.containsKey(team2)) throw new IllegalArgumentException();

        return games[nameToNum.get(team1)][nameToNum.get(team2)];
    }    // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        if(!nameToNum.containsKey(team)) throw new IllegalArgumentException();

        teamSet = new HashSet<>();
        int x = nameToNum.get(team);
        FlowNetwork network = constructFlowNetwork(x);

        int s = network.V() - 2;
        int t = network.V() - 1;
        maxFlowSolver = new FordFulkerson(network, s, t);

        for (FlowEdge e : network.adj(s))
            if (e.flow() < e.capacity())
                return true;
        return !teamSet.isEmpty();

    }              // is given team eliminated?

    private FlowNetwork constructFlowNetwork(int x) {
        FlowNetwork network = new FlowNetwork(2 + Num +  Num * (Num - 1) /2);
        int s = Num +  Num * (Num - 1) /2;
        int t = s + 1;
        int game = 0;

        for (int v = 0; v < Num; v++) {
            if (v == x) continue;

            int capacity = wins[x] + remaining[x] - wins[v];
            if (capacity < 0)
                teamSet.add(teams[v]);
            FlowEdge e = new FlowEdge(v, t, Math.max(capacity, 0));
            network.addEdge(e);
        }

        for (int i = 0; i < Num; i++) {
            for (int j = 0; j < Num; j++) {
                if (i == x || j == x || i >= j) continue;

                int v = Num + (game++);
                network.addEdge(new FlowEdge(s, v, games[i][j]));
                network.addEdge(new FlowEdge(v, i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(v, j, Double.POSITIVE_INFINITY));
            }
        }

        return network;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if(!nameToNum.containsKey(team)) throw new IllegalArgumentException();

        int x = nameToNum.get(team);
        if (!isEliminated(team))
            return null;
        else {
            for (int v = 0; v < Num; v++)
                if (v != x && maxFlowSolver.inCut(v))
                    teamSet.add(teams[v]);
            return teamSet;
        }
    }  // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }

}
