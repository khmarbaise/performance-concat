package com.soebes.performance.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamConcat {
  private List<FancyStuff> fancyStuffs;

  public StreamConcat(List<FancyStuff> fancyStuffs) {
    this.fancyStuffs = fancyStuffs;
  }

  void setUp() {
    fancyStuffs = IntStream.rangeClosed(1, 50)
      .mapToObj(i -> new FancyStuff(new Element(i + 1), createList(i)))
      .collect(Collectors.toList());
  }

  private List<Element> createList(int factor) {
    return IntStream.rangeClosed(2, 50)
      .mapToObj(i -> new Element(i * factor))
      .collect(Collectors.toList());
  }

  List<Element> first_variant(List<FancyStuff> fancyStuffs) {
    return fancyStuffs.stream().flatMap(item -> {
      ArrayList<Element> objects = new ArrayList<>();
      objects.add(item.getElement());
      objects.addAll(item.getElements());
      return objects.stream();
    }).collect(Collectors.toList());
  }

  List<Element> second_variant(List<FancyStuff> fancyStuffs) {
    return fancyStuffs.stream()
      .flatMap(fs -> Stream.concat(Stream.of(fs.getElement()), fs.getElements().stream()))
      .collect(Collectors.toList());
  }

  class Element {
    private int id;

    public Element(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }
  }

  class FancyStuff {
    private Element element;
    private List<Element> elements;

    public FancyStuff(Element element, List<Element> elements) {
      this.element = element;
      this.elements = elements;
    }

    public Element getElement() {
      return element;
    }

    public List<Element> getElements() {
      return elements;
    }
  }


}
