package dev.widowan;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Input {
    enum InputMethod {
        stdio,
        random,
    }

    public static Result get(String[] args) {
        // Regex for -h, -help, --h, --help
        if (args.length == 0 || args[0].matches("(-)?-h(elp)?")) {
            System.out.println(
                    "Введите число для поиска и входной массив, либо аргумент --random n, где n - длина массива;");
            System.exit(1);
        }


        InputMethod inputMethod;
        var	argsList = Arrays.asList(args);
        int idx;
        int len = 0;
        // Deciding whether input is random or static
        if ((idx = argsList.indexOf("--random")) != -1) {
            inputMethod = InputMethod.random;

            try {
                len = Integer.parseInt(argsList.get(idx+1));
            } catch (Exception e) {
                System.out.println("Введите --random n");
                System.exit(1);
            }

        } else inputMethod = InputMethod.stdio;

        return (inputMethod == InputMethod.random)
                ? getRandomInput(len)
                : getStdioInput(argsList);
    }

    private static Result getRandomInput(int len) {
        var rand = new Random();
        var randInput = rand
                .ints(len)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        return new Result(
                randInput.get(rand.nextInt(0, len)),
                randInput
        );
    }

    private static Result getStdioInput(List<String> args) {
        return new Result(
                Integer.parseInt(args.get(0)),
                args.stream().skip(1)
                        .map(Integer::parseInt)
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    public record Result(int target, List<Integer> nums) {}
}
