package com.soebes.performance.streams;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BenchmarkStreamConcat {


  private List<FancyStuff> fancyStuffs;

  public BenchmarkStreamConcat() {
    fancyStuffs = IntStream.rangeClosed(1, 50)
      .mapToObj(i -> new FancyStuff(new Element(i + 1), createList(i)))
      .collect(Collectors.toList());

  }

  private List<Element> createList(int factor) {
    return IntStream.rangeClosed(2, 50)
      .mapToObj(i -> new Element(i * factor))
      .collect(Collectors.toList());
  }

  @Benchmark
  public List<Element> with_new_arraylist() throws InterruptedException {
      return fancyStuffs.stream().flatMap(item -> {
          ArrayList<Element> objects = new ArrayList<>();
          objects.add(item.getElement());
          objects.addAll(item.getElements());
          return objects.stream();
      }).collect(Collectors.toList());

  }

  @Benchmark
  public List<Element> with_stream_concat() {
      return fancyStuffs.stream()
        .flatMap(fs -> Stream.concat(Stream.of(fs.getElement()), fs.getElements().stream()))
        .collect(Collectors.toList());

  }

}
