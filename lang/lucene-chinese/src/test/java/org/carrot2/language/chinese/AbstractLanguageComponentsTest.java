/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * https://www.carrot2.org/carrot2.LICENSE
 */
package org.carrot2.language.chinese;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.carrot2.language.LanguageComponents;
import org.carrot2.language.LexicalData;
import org.carrot2.language.Stemmer;
import org.carrot2.language.Tokenizer;
import org.carrot2.util.MutableCharArray;
import org.junit.Test;

public abstract class AbstractLanguageComponentsTest {
  protected final LanguageComponents components;
  private final String[][] stemmingPairs;
  private final String[] stopWords;

  AbstractLanguageComponentsTest(String language, String[] stopWords, String[][] stemmingPairs) {
    this.components = LanguageComponents.load(language);
    this.stemmingPairs = stemmingPairs;
    this.stopWords = stopWords;
  }

  /** */
  @Test
  public void testStemmerAvailable() {
    assertNotNull(components.get(Stemmer.class));
  }

  /** */
  @Test
  public void testStemming() {
    final Stemmer stemmer = components.get(Stemmer.class);
    for (String[] pair : stemmingPairs) {
      CharSequence stem = stemmer.stem(pair[0]);
      Assertions.assertThat(stem == null ? null : stem.toString()).isEqualTo(pair[1]);
    }
  }

  /** */
  @Test
  public void testCommonWords() {
    LexicalData lexicalData = components.get(LexicalData.class);
    final String[] testData = stopWords;
    for (String word : testData) {
      Assertions.assertThat(lexicalData.ignoreWord(new MutableCharArray(word))).as(word).isTrue();
    }
  }

  protected List<String> tokenize(Tokenizer tokenizer, String input) throws IOException {
    tokenizer.reset(new StringReader(input));
    MutableCharArray buffer = new MutableCharArray();
    ArrayList<String> tokens = new ArrayList<>();
    while (tokenizer.nextToken() >= 0) {
      tokenizer.setTermBuffer(buffer);
      tokens.add(buffer.toString());
    }
    return tokens;
  }
}
