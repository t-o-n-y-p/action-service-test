package ru.tonyp.extensions.injector;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class InjectorService {

  private static final Injector injector = Guice.createInjector(new ObjectMapperModule());

  public static void injectMembers(Object instance) {
    injector.injectMembers(instance);
  }

  public static <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }
}
