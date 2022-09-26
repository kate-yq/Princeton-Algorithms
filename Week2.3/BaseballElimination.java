import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final int N; // number of teams
    private final int[] wins, loses, remainings;
    private ArrayList<String> teams;
    private final int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        this.N = in.readInt();
        this.teams = new ArrayList<String>();
        this.wins = new int[this.N];
        this.loses = new int[this.N];
        this.remainings = new int[this.N];
        this.games = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            String team = in.readString();
            teams.add(team);
            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remainings[i] = in.readInt();
            for (int j = 0; j < this.N; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return this.N;
    }

    // all teams
    public Iterable<String> teams() {
        return this.teams;
    }

    // PRIVATE check if contain the team
    // private boolean contain(String team) {
    //     for (String t : this.teams) {
    //         if (t == team) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // number of wins for given team
    public int wins(String team) {
        // if (!contain(team)) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        int i = teams.indexOf(team);
        return this.wins[i];
    }

    // number of losses for given team
    public int losses(String team) {
        // if (!contain(team)) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        int i = teams.indexOf(team);
        return this.loses[i];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        // if (!contain(team)) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        int i = teams.indexOf(team);
        return this.remainings[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        // if ((!contain(team1)) || (!contain(team2))) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        int i = teams.indexOf(team1);
        int j = teams.indexOf(team2);
        return this.games[i][j];

    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        // if ((!contain(team))) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        int t = teams.indexOf(team);

        // situation 1 - Trivial elimination.
        for (int i = 0; i < this.N; i++) {
            if (wins[t] + remainings[t] < wins[i]) {
                return true;
            }
        }
        // situation 2 - nonTrivial elimination.
        FlowNetwork leagues = new FlowNetwork(this.N * (this.N - 1) / 2 + 2);
        for (int i = 0; i < this.N; i++) {
            if (i == t) {
                continue;
            }
            FlowEdge out = new FlowEdge(i, t, wins[t] + remainings[t] - wins[i]);
            leagues.addEdge(out);
        }
        int j = this.N + 1;
        int resourse = 0;
        for (int a = 0; a < this.N; a++) {
            if (a == t) {
                continue;
            }
            for (int b = a + 1; b < this.N; b++) {
                if (b == t) {
                    continue;
                }
                FlowEdge in = new FlowEdge(this.N, j, games[a][b]);
                leagues.addEdge(in);
                resourse = resourse + games[a][b];
                FlowEdge bridgeA = new FlowEdge(j, a, Double.POSITIVE_INFINITY);
                FlowEdge bridgeB = new FlowEdge(j, b, Double.POSITIVE_INFINITY);
                leagues.addEdge(bridgeA);
                leagues.addEdge(bridgeB);
                j++;
            }
        }
        FordFulkerson findelimination = new FordFulkerson(leagues, this.N, t);
        if (findelimination.value() == resourse) {
            return false;
        } else {
            return true;
        }
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        // if ((!contain(team))) {
        //     throw new IllegalArgumentException("does not exist this team");
        // }
        ArrayList<String> eliminateby = new ArrayList<String>();
        int t = teams.indexOf(team);

        // situation 1 - Trivial elimination.
        int maxwin = 0;
        int maxw = 0;
        for (int i = 0; i < this.N; i++) {
            if (wins[i] > maxwin) {
                maxwin = wins[i];
                maxw = i;
            }
        }
        if (wins[t] + remainings[t] < maxwin) {
            eliminateby.add(teams.get(maxw));
            return eliminateby;
        }

        // situation 2 - nonTrivial elimination.
        FlowNetwork leagues = new FlowNetwork(this.N * (this.N - 1) / 2 + 2);
        for (int i = 0; i < this.N; i++) {
            if (i == t) {
                continue;
            }
            FlowEdge out = new FlowEdge(i, t, wins[t] + remainings[t] - wins[i]);
            leagues.addEdge(out);
        }
        int j = this.N + 1;
        int resourse = 0;
        for (int a = 0; a < this.N; a++) {
            if (a == t) {
                continue;
            }
            for (int b = a + 1; b < this.N; b++) {
                if (b == t) {
                    continue;
                }
                FlowEdge in = new FlowEdge(this.N, j, games[a][b]);
                leagues.addEdge(in);
                resourse = resourse + games[a][b];
                FlowEdge bridgeA = new FlowEdge(j, a, Double.POSITIVE_INFINITY);
                FlowEdge bridgeB = new FlowEdge(j, b, Double.POSITIVE_INFINITY);
                leagues.addEdge(bridgeA);
                leagues.addEdge(bridgeB);
                j++;
            }
        }
        FordFulkerson findelimination = new FordFulkerson(leagues, this.N, t);
        if (findelimination.value() == resourse) {
            return null;
        }
        for (int i = 0; i < this.N; i++) {
            if (i == t) {
                continue;
            }
            if (findelimination.inCut(i)) {
                eliminateby.add(teams.get(i));
            }
        }
        return eliminateby;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
