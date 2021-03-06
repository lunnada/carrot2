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
package org.carrot2.language;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.carrot2.text.preprocessing.LabelFormatter;
import org.carrot2.text.preprocessing.LabelFormatterImpl;
import org.carrot2.util.ResourceLookup;

public class TestsLanguageComponentsFactoryVariant2 implements LanguageComponentsProvider {
  public static final String NAME = "_tests_language_variant2_";

  private static final class LexicalDataImpl implements LexicalData {
    @Override
    public boolean ignoreWord(CharSequence word) {
      return word.toString().contains("stop");
    }

    @Override
    public boolean ignoreLabel(CharSequence formattedLabel) {
      return formattedLabel.toString().startsWith("stoplabel");
    }
  }

  @Override
  public Set<String> languages() {
    return Collections.singleton(NAME);
  }

  @Override
  public Map<Class<?>, Supplier<?>> load(String language, ResourceLookup resourceLookup) {
    LinkedHashMap<Class<?>, Supplier<?>> components = new LinkedHashMap<>();
    components.put(Stemmer.class, this::createStemmer);
    components.put(Tokenizer.class, ExtendedWhitespaceTokenizer::new);
    components.put(LexicalData.class, LexicalDataImpl::new);
    components.put(LabelFormatter.class, () -> new LabelFormatterImpl(" "));
    return components;
  }

  @Override
  public Map<Class<?>, Supplier<?>> load(String language) throws IOException {
    return load(language, null);
  }

  private Stemmer createStemmer() {
    return (word) -> {
      if (word.length() > 2) {
        return word.subSequence(0, word.length() - 2);
      } else {
        return null;
      }
    };
  }

  @Override
  public String name() {
    return getClass().getName();
  }
}
