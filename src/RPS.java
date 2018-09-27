import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RPS
{
    public static void main (final String... args)
    {
        Stream.generate(() -> true).forEach(ignore ->
                Stream.<Object[]>of(
                        new Object[]{
                                new Scanner(System.in),
                                new String[][]{
                                        {"tie", "win", "loose"},
                                        {"loose", "tie", "win"},
                                        {"win", "loose", "tie"}
                                }
                        }
                ).map((x) -> (System.out.printf("Rock, paper, or scissors: ") != null) ?
                        (new Object[]{
                                ((Callable<String, Integer>) (s) -> (
                                        s.equalsIgnoreCase("rock")  ?  0
                                                : s.equalsIgnoreCase("paper") ?  1
                                                :  2)
                                ).call(((Scanner) x[0]).nextLine()),
                                (int) (Math.random() * 3),
                                x
                        }) : null
                ).map((x) -> System.out.printf("Computer picked %s, you %s\n\n",
                        ((int) x[1] == 0 ? "rock" : ((int) x[1] == 1 ? "paper" : "scissors")),
                        ((String[][]) ((Object[]) x[2])[1])[(int) x[1]][(int) x[0]]) != null ? 0 : 0)
                        .collect(Collectors.toList())
        );
    }

    interface Callable<Q, T>
    {
        T call (Q q);
    }
}
