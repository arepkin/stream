package ru.liga.interning.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
public class StreamTest {

    private static final BinaryOperator<Integer> LONG_TIME_SUM = (a, b) -> {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            return 0;
        }
        return a + b;
    };

    @Test
    public void testInfiniteStream() {
        final Supplier<Integer> infiniteStreamSupplier = () -> Stream.generate(() -> 1).reduce((a, b) -> a + b).get();
        final Executable executable = () -> CompletableFuture.supplyAsync(infiniteStreamSupplier).get(1, TimeUnit.SECONDS);
        Assertions.assertThrows(TimeoutException.class, executable);
    }

    @Test
    public void testFiniteGeneratedStream() {
        Integer result = Stream.generate(() -> 1)
                .limit(10)
                .reduce((a, b) -> a + b).get();
        Assertions.assertEquals(result, 10);
    }

    @Test
    public void intermediateStatelessStream() {
        final IntPredicate evenPredicate = i -> i % 2 == 0;
        final IntPredicate oddPredicate = evenPredicate.negate();
        Integer result = IntStream.of(1, 2, 3).filter(oddPredicate).map(i -> i + 1).sum();
        Assertions.assertEquals(result, 6);
    }

    @Test
    public void intermediateStateFullStream() {
        Integer result = IntStream.of(3, 1, 3, 4, 6, 0)
                .sorted()
                .distinct()
                .sum();
        Assertions.assertEquals(result, 14);
    }

    @Test
    public void problemParallelStream() {
        Integer result =
                IntStream.of(3, 1, 3, 4, 6, 0)
                        .parallel()
                        .sorted()
                        .distinct()
                        .sum();
        Assertions.assertEquals(result, 14);
    }

    @Test //BAD Пример из JavaDoc
    public void interferenceExampleStream() {
        List<String> l = new ArrayList(Arrays.asList("one", "two"));
        Stream<String> sl = l.stream();
        l.add("three");
        String s = sl.collect(Collectors.joining(" "));
        Assertions.assertEquals(s, "one two three");
    }

    @Test //Sum Примеры из JavaDoc
    public void sumStream() {
        final List<Integer> numbers = Arrays.asList(1, 2, 3);
        int sum = 0;
        for (int x : numbers) {
            sum += x;
        }
        Assertions.assertEquals(sum, 6);
        int sum2 = numbers.stream().reduce(0, (x, y) -> x + y);
        Assertions.assertEquals(sum2, 6);
        int sum3 = numbers.stream().reduce(0, Integer::sum);
        Assertions.assertEquals(sum3, 6);
        int sum4 = numbers.parallelStream().reduce(0, Integer::sum);
        Assertions.assertEquals(sum4, 6);
    }

    @Test
    public void compareWithIteratesTest() {
        //ищем самое короткое слово в императивном стиле и в декларативном
        List<String> words = Arrays.asList("lesson", "oracle", "java", "homework", "students");
        int minimalWordsSize1 = searchMinimalWordsSizeImperative(words);
        int minimalWordsSize2 = searchMinimalWordsSizeDeclarative(words);
        Assertions.assertEquals(minimalWordsSize1, minimalWordsSize2);
    }

    private int searchMinimalWordsSizeImperative(List<String> words) {
        String shortWord = words == null || words.size() > 0 ? words.get(0) : "";
        for (String word : words) {
            if (word.length() < shortWord.length()) {
                shortWord = word;
            }
        }
        return shortWord.length();
    }

    private int searchMinimalWordsSizeDeclarative(List<String> words) {
        return words.stream().mapToInt(String::length).min().orElse(0);
    }

    @Test
    public void stringsFilterTest() {
        Stream<String> words = Stream.of("abc", "cat", "", "apple", "tree");
        List<String> list = words.filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());
        Assertions.assertEquals(list, Arrays.asList("abc", "cat", "apple", "tree"));
    }

    @Test
    public void stringsMapTest() {
        Stream<String> words1 = Stream.of("abc", "cat", "", "apple", "tree");
        List<Integer> list = words1.map(String::length).collect(Collectors.toList());
        Assertions.assertEquals(list, Arrays.asList(3, 3, 0, 5, 4));
        Stream<String> words2 = Stream.of("abc", "cat", "", "apple", "tree");
        List<Integer> list2 = words2.map(word -> word.length()).collect(Collectors.toList());
        Assertions.assertEquals(list2, Arrays.asList(3, 3, 0, 5, 4));
    }

    @Test
    public void takeAndDropTest() {
        //takeWhile может превратить бесконечный поток в конечный
        Stream<Integer> numbers = Stream.generate(() -> new Random().nextInt(10));
        final List<Integer> list = numbers.takeWhile(n -> n > 0).collect(Collectors.toList());
        Assertions.assertNotNull(list);

        Stream<String> words = Stream.of("abc", "cat", "", "apple", "tree");
        List<String> list2 = words.dropWhile(s -> s.length() <= 3).collect(Collectors.toList());
        Assertions.assertEquals(list2, Arrays.asList("apple", "tree"));
    }

    @Test
    public void limitTest() {
        //ещё один способ превратить бесконечный поток в конечный
        Stream<Integer> numbers = Stream.generate(() -> new Random().nextInt(10));
        final List<Integer> list = numbers.limit(100).collect(Collectors.toList());
        Assertions.assertEquals(list.size(), 100);

        //А что с сортировкой? Ответ: сортировка бесконечного потока займет бесконечное время!
        Stream<Integer> numbersSorted = Stream.generate(() -> new Random().nextInt(10)).sorted();
//        final List<Integer> listSorted = numbersSorted.limit(100).collect(Collectors.toList());
//        Assertions.assertEquals(listSorted.size(), 100);
    }

    @Test
    public void findTest() {
        Stream<String> wordsAny = Stream.of("abc", "cat", "", "apple", "tree");
        final Optional<String> any = wordsAny.findAny();
        Assertions.assertEquals(any.get(), "abc");

        Stream<String> wordsFind = Stream.of("abc", "cat", "", "apple", "tree");
        final Optional<String> foundWords = wordsFind.findFirst();
        Assertions.assertEquals(foundWords.get(), "abc");
    }

    @Test
    public void reduceInsteadIterateTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 9, 2, 3, 9, 8, 1, 0);
        Integer sum1 = 0;
        for (Integer i : numbers) {
            sum1 += i;
        }
        Assertions.assertEquals(sum1, 35);

        Integer sum2 = numbers.stream().reduce(0, (a, b) -> a + b);
        Assertions.assertEquals(sum2, 35);
        int sum3 = numbers.stream().mapToInt(Integer::intValue).sum();
        Assertions.assertEquals(sum3, 35);
        //вариант с Optional
        Optional<Integer> sum4 = numbers.stream().reduce((a, b) -> a + b);
        Assertions.assertEquals(sum4.get(), 35);
    }

    @Test
    public void collectorsTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 9, 2, 3, 9, 8, 1, 0);
        final Set<Integer> set = numbers.stream().collect(Collectors.toSet());
        Assertions.assertEquals(set, Set.of(0, 1, 2, 3, 8, 9));
        final Long count = numbers.stream().collect(Collectors.counting());
        Assertions.assertEquals(count, 9);
        final Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Comparator.naturalOrder()));
        Assertions.assertEquals(max.get(), 9);
        final String binVals = numbers.stream().map(Integer::toBinaryString).collect(Collectors.joining(", "));
        Assertions.assertEquals(binVals, "1, 10, 1001, 10, 11, 1001, 1000, 1, 0");
        final Map<String, Set<Integer>> map = numbers.stream().collect(Collectors.groupingBy(i -> {
            if (i % 2 == 0) return "EVEN";
            else return "ODD";
        }, Collectors.toSet()));
        Assertions.assertEquals(map, Map.of("EVEN", Set.of(0, 2, 8), "ODD", Set.of(1, 3, 9)));
    }

    @Test
    public void splitIteratorTest() {
        long ctm = System.currentTimeMillis();
        Integer sum1 = Stream.iterate(1, i -> i + 1).limit(100).reduce(0, LONG_TIME_SUM);
        System.out.println("seq iterate take for " + (System.currentTimeMillis() - ctm) + "ms");
        Assertions.assertEquals(sum1, 5050);
        //вариант со SplitIterator, который позволяет реально распараллелить вычисления
        List<Integer> numbers = Stream.iterate(1, i -> i + 1).limit(100).collect(Collectors.toList());
        ctm = System.currentTimeMillis();
        final BinaryOperator<Integer> combiner = Integer::sum;
        final Integer sum2 = StreamSupport.stream(numbers.spliterator(), true).reduce(0, LONG_TIME_SUM, combiner);
        System.out.println("parallel iterate take for " + (System.currentTimeMillis() - ctm) + "ms");
        Assertions.assertEquals(sum2, 5050);
    }
}
