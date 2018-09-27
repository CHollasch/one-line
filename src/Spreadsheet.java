import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Connor Hollasch
 * @since Sep 27, 12:28 AM
 */
public class Spreadsheet
{
    public static void main (final String... args)
    {
        Stream.of(new ArrayList<>(Arrays.asList(
                false,

                // String to Function ID
                ((CB<String, Integer>) (x) -> x.equalsIgnoreCase("set") ? 2 : x.equalsIgnoreCase("clear") ? 3
                        : x.equalsIgnoreCase("print") ? 4 : -1),

                // Set a cell
                ((CB<Object[], Void>) (dat) ->
                        Stream.of(new AbstractMap.SimpleEntry<>((String[]) dat[0], (ArrayList) dat[1])).map(d ->
                                Stream.of(new AbstractMap.SimpleEntry<>(
                                        Integer.parseInt(d.getKey()[1]), Integer.parseInt(d.getKey()[2])
                                )).map(xy ->
                                        Stream.of(d.getKey().length > 3 ? d.getKey()[3] : null)
                                                .map((data) ->
                                                        (((String[][]) d.getValue().get(0))
                                                                [xy.getKey()][xy.getValue()] = data) == null ? null :
                                                                (
                                                                        ((CB) d.getValue().get(5))
                                                                                .call(d.getValue()) == null ?
                                                                                System.out.printf(
                                                                                        "Set (%s, %s) to %s\n",
                                                                                        xy.getKey(),
                                                                                        xy.getValue(),
                                                                                        data
                                                                                ) == null ? null : null : null
                                                                ))
                                                .count()
                                ).count()
                        ).count() == -1 ? null : null),

                // Clear all cells
                ((CB<Object[], Void>) (dat) -> IntStream.range(0, ((String[][]) ((ArrayList) dat[1]).get(0)).length)
                        .mapToObj(i ->
                                IntStream.range(0, ((String[][]) ((ArrayList) dat[1]).get(0))[0].length).mapToObj(q ->
                                        ((String[][]) ((ArrayList) dat[1]).get(0))[i][q] = null
                                ).count()
                        ).count() == -1 ? null : ((CB) ((ArrayList) dat[1]).get(5)).call(dat[1]) == null ? null : null),

                // Print cells
                ((CB<Object[], Void>) (dat) -> (IntStream.range(0,
                        ((int) ((ArrayList) dat[1]).get(6) + 3) * ((String[][]) (((ArrayList) dat[1]).get(0))).length
                ).mapToObj(r ->
                        System.out.printf("-")
                ).count() != -1 ?
                        System.out.printf("\n") == null ? null :
                                IntStream.range(0, ((String[][]) ((ArrayList) dat[1]).get(0)).length).mapToObj(i ->
                                        IntStream.range(0, ((String[][]) ((ArrayList) dat[1]).get(0))[0].length)
                                                .mapToObj(q ->
                                                        Stream.of(((String[][]) ((ArrayList) dat[1]).get(0))[i][q]).map(item ->
                                                                item == null ? "" : item
                                                        ).map(at ->
                                                                System.out.printf(" %s", at) == null ? null :
                                                                        IntStream.range(0, (int)
                                                                                ((ArrayList) dat[1]).get(6) - at.length() + 1)
                                                                                .mapToObj(ignore -> System.out.printf(" "))
                                                                                .count() != -1 ?
                                                                                System.out.printf("|") : null).count()
                                                ).count() != -1 ?
                                                System.out.printf("\n") : null
                                ).count() : null) == -1 ? null :
                        (IntStream.range(0, ((int) ((ArrayList) dat[1]).get(6) + 3)
                                * ((String[][]) (((ArrayList) dat[1]).get(0))).length).mapToObj(r ->
                                System.out.printf("-")
                        ).count()) == -1 ? null :
                                System.out.printf("\n") == null ? null : null
                ),

                // Find longest string in table
                ((CB<ArrayList, Void>) (dat) ->
                        IntStream.range(0, ((String[][]) dat.get(0)).length).mapToObj(i ->
                                IntStream.range(0, ((String[][]) dat.get(0))[0].length).mapToObj(q ->
                                        ((String[][]) dat.get(0))[i][q] == null ? null :
                                                ((String[][]) dat.get(0))[i][q].length() > (int) dat.get(7)
                                                        ? dat.set(7,
                                                        ((String[][]) dat.get(0))[i][q].length()) : null
                                ).count()
                        ).count() == -1 ? null : (dat.set(6, dat.get(7)) == null ? null : dat.set(7, 0) == null
                                ? null : null)
                ),

                // Index 6 & 7 (6 = longest string letter, 7 = temp calculation variable for said length)
                5,
                0
        ))).map(vars ->
                Stream.of(new Scanner(System.in)).map(x ->
                        Stream.generate(() -> 1).map((unused) ->
                                vars.get(0) instanceof Boolean ?

                                        // Initialize spreadsheet width and height.
                                        Stream.of(1).map($a ->
                                                (System.out.printf(
                                                        "Enter a width and height for the spreadsheet: "
                                                ) != null) ? x.nextLine() : null
                                        ).map(widthHeight -> ((boolean) vars.set(0, null) ? null : (
                                                vars.set(0,
                                                        new String[Integer.parseInt(widthHeight.split(" ")[0])]
                                                                [Integer.parseInt(widthHeight.split(" ")[1])]
                                                )
                                        ))).count() :

                                        // Program loop post-init
                                        Stream.generate(() -> (System.out.printf("Enter command: ") != null) ?
                                                (x.nextLine()) : null).map(line ->
                                                line.split(" ", 4)
                                        ).map(split -> new Object[]{((CB<String, Integer>) vars.get(1))
                                                .call(split[0]), split})
                                                .map(raw ->
                                                        ((int) raw[0] == -1) ? System.out.printf("No such command!\n")
                                                                == null ? null : null :
                                                                ((CB) vars.get((int) raw[0])).call(
                                                                        new Object[]{raw[1], vars}
                                                                ) == null ? ((int) raw[0] != 4 ? ((CB) vars.get(4))
                                                                        .call(new Object[]{raw[1], vars}) : null) : null
                                                ).count()
                        ).count()
                ).count()
        ).count();
    }

    public interface CB<Param, Return>
    {
        Return call (Param param);
    }
}