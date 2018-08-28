package com.example.translate;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BulkTranslationSample {
  public static void main(String... args) throws Exception {

    String GOOGLE_APPLICATION_CREDENTIALS_JSON_PATH = "E:\\files\\Bulk-Translations-xxxxxxxx.json";
    String SOURCE_FILE = "E:\\files\\emailmessages_en.properties";
    String DESTINATION_FILE = "E:\\files\\emailmessages_ja.properties";
    String targetLocale = "ja";
    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream(
            GOOGLE_APPLICATION_CREDENTIALS_JSON_PATH)) .createScoped(
                Lists.newArrayList());
    // Instantiates a client
    Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
    String content = new String(Files.readAllBytes(Paths.get(SOURCE_FILE))).replaceAll("\r\n","<br>");
    // Translates some text into Japanese
    Translation translation =
        translate.translate(
            content,
            TranslateOption.sourceLanguage("en"),
            TranslateOption.targetLanguage(targetLocale));

    Files.write(Paths.get(DESTINATION_FILE),
        translation.getTranslatedText().replaceAll("<br> ","\n").replaceAll(" =","=").getBytes("utf-8"),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    System.out.printf("translation completed.");
  }
}
