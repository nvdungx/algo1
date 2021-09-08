/* *****************************************************************************
 *  Name:              Nguyen Van Dung
 *  Coursera User ID:
 *  Last modified:     August 18, 2021
 **************************************************************************** */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length > 0) {
            List<String> input = Arrays.asList(args);
            String outStr = String.join(" and ", input);
            System.out.printf("Hello %s.\r\n", outStr);
            Collections.reverse(input);
            outStr = String.join(" and ", input);
            System.out.printf("Goodbye %s.\r\n", outStr);
        }
    }
}
