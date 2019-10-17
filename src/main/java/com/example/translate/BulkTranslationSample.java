package com.example.translate;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.spi.v2.TranslateRpc;
import com.google.cloud.translate.spi.v2.TranslateRpc.Option;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class BulkTranslationSample {

  public static void main(String... args) throws Exception {

    String GOOGLE_APPLICATION_CREDENTIALS_JSON_PATH = "E:\\files\\x.json";
    String SOURCE_FILE = "E:\\workspaces\\veroozi\\admin-ui\\src\\main\\resources\\messages_en.properties";
    String DESTINATION_FILE = "E:\\workspaces\\veroozi\\admin-ui\\src\\main\\resources\\messages_en_no.properties";
    String targetLocale = "no";
    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream(
            GOOGLE_APPLICATION_CREDENTIALS_JSON_PATH)).createScoped(
            Lists.newArrayList());
    // Instantiates a client
    Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build()
        .getService();
    List<String> keyValues = Files.readAllLines(Paths.get(SOURCE_FILE));
    //.replaceAll("\r\n","<br>")
    // Translates some text into targetLocale
    StringBuilder translatedValue;
    for (String keyValue : keyValues) {
      String[] keyValueArray = keyValue.split("=", 2);
      if (keyValueArray.length > 1) {
        Translation translation =
            translate.translate(
                keyValueArray[1],
                TranslateOption.sourceLanguage("en"),
                TranslateOption.targetLanguage(targetLocale), TranslateOption.format("text"));
        translatedValue = new StringBuilder();
        translatedValue.append(keyValueArray[0]).append("=")
            .append(translation.getTranslatedText()
                .replaceAll("% s", " %s"))
            .append("\r\n");
      } else {
        translatedValue = new StringBuilder();
        translatedValue.append(keyValue).append("\r\n");
      }
      System.out.print(translatedValue);
      Files.write(Paths.get(DESTINATION_FILE),
          translatedValue.toString().getBytes("utf-8"),
          StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    }
    System.out.printf("translation completed.");
  }
}
