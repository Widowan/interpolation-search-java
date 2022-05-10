package dev.widowan;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        var input = Input.get(args);
        var sorted = new Quicksorter<>(input.nums()).sort().nums();
        var searcher = new InterpolationSearcher(sorted);

        // Warmup JVM to get similar speeds
        {
            searcher.search(new Random().nextInt(0, sorted.size() - 1));
        }

        {
            var startTime = System.nanoTime();
            var result = searcher.search(input.target());
            var stopTime = System.nanoTime();

            System.out.println("Лучший случай:");
            System.out.printf("""
                              Число для поиска: %d
                              Найденный индекс: %d
                              Массив: %s
                              Число по индексу: %.0f
                              Кол-во сравнений: %d
                              Время: %f с.%n
                            """,
                    input.target(),
                    result.index(),
                    sorted.size() > 11 ? "[...]" : sorted.toString(),
                    result.index() == -1 ? Double.NaN : sorted.get(result.index()),
                    result.cmpCnt(),
                    (stopTime - startTime) / 1_000_000_000D
            );
        }

        {
            int newTarget = input.target();
            while (sorted.contains(newTarget))
                newTarget += 1;


            var startTime = System.nanoTime();
            var result = searcher.search(newTarget);
            var stopTime = System.nanoTime();

            System.out.println("Худший случай:");
            System.out.printf("""
                              Число для поиска: %d
                              Найденный индекс: %d
                              Массив: %s
                              Число по индексу: %.0f
                              Кол-во сравнений: %d
                              Время: %f с.
                            """,
                    newTarget,
                    result.index(),
                    sorted.size() > 6 ? "[...]" : sorted.toString(),
                    result.index() == -1 ? Double.NaN : sorted.get(result.index()),
                    result.cmpCnt(),
                    (stopTime - startTime) / 1_000_000_000D
            );
        }
    }
}