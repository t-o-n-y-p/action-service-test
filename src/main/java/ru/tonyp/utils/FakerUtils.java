package ru.tonyp.utils;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import net.datafaker.Faker;

@Singleton
public class FakerUtils {

  private final Faker faker = new Faker();

  @Getter
  private final List<String> allGeneratedTokens = Collections.synchronizedList(new ArrayList<>());

  public String getToken() {
    return generateByTemplate("[A-F0-9]{31}[A-F2-9]");
  }

  public String getAuthErrorToken() {
    return generateByTemplate("[A-F0-9]{31}0");
  }

  public String getActionErrorToken() {
    return generateByTemplate("[A-F0-9]{31}1");
  }

  private String generateByTemplate(String regex) {
    String result = faker.regexify(regex);
    allGeneratedTokens.add(result);
    return result;
  }
}
