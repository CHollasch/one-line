import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricConverter {

    public static void main(String[] args)
    {
        /*
            Things to know before reading:
                Stream primitives:
                    1. int sum = Stream.of(1, 2, 3, 4, 5).sum(); // sum = 15
                    2. Stream.of(1, 2, 3, 4, 5).map((num) -> String.valueOf(num)).forEach((stringVersionOfNum) -> ...)
                    3. Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList()) = an arraylist of the input integers
                I use the callback interface to simply run something later, in this case, we use it to run print
                statements after we read from the scanner and to get conversions.
         */
        // Begin by creating a stream of the single scanner pipe from system in.
        Stream.of(new Scanner(System.in))

        // forEach is just the 1 scanner...
        .forEach(
                // Build another stream...
                (scanner) -> Stream.of(
                        // Create an array for our variables,
                        //  1. Scanner
                        //  2. Print statement for the goal (print name, convert height, convert weight, or our utility header)
                        //  3. Callback function that gives the conversion callback result and output string format
                        //      The callback for name just reads the name in and returns it.
                        //  4. The string we print for the output for the given goal
                        new Object[]{scanner, "Enter your name: ", (Callback<Object[]>) () -> new Object[]{scanner.nextLine(), "\nName:\t%s\n"}},
                        new Object[]{scanner, "Enter Height in inches: ", (Callback<Object[]>) () -> new Object[]{(scanner.nextDouble() * 2.54d) / 100, "Height:\t%f meters\n"}},
                        new Object[]{scanner, "Enter Weight in pounds: ", (Callback<Object[]>) () -> new Object[]{(scanner.nextDouble() * 0.45359237d), "Weight:\t%f kilograms\n"}},
                        new Object[]{null, "\nMetric Calculation"}

        // Since our stream above is in order, we can process them in order.
        // We map each input array to an output array that has
        //  1. Callback that returns the conversion result from system in
        //  2. String we want to print and format for output
        ).map((arr) ->
                // This callback is created as a shortcut to print the "Enter .....", the == null ? always returns true.
                ((Callback<Integer>) () -> (System.out.printf("%s", arr[1].toString()) == null ? 1 : 1)).t() != 0 ?
                (
                        // Since the above callback always returns 1, it is always not equal to 0, and thus the line
                        // below always executes.

                        // If our input array length is only 2, we are supposed to print the output header.
                        // Otherwise, return the callback for the output format and result.
                        (arr.length == 2 ? null : ((Callback<Object[]>) arr[2]).t())
                ) : null
        )
        // By creating a collector, we run the above map all at once before remapping below.
        // If we didn't do the collect, a map above would pipe directly to the map below and run out of order.
        .collect(Collectors.toList())
        .stream()
        .map((obj) ->
                // The input object is only null if it was the header utility print, so we have to handle it.
                (obj == null
                        ? null
                        // Print the given output format with the converted result.
                        : System.out.printf((String) obj[1], obj[0]))
        )
        // Useless count call to make the map above actually run (basically we need to call it to run anything)
        .count());
    }

    interface Callback<T> {
        T t();
    }
}