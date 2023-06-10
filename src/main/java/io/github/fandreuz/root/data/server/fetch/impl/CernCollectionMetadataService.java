package io.github.fandreuz.root.data.server.fetch.impl;

import io.github.fandreuz.root.data.server.fetch.FetchException;
import io.github.fandreuz.root.data.server.fetch.MetadataService;
import io.github.fandreuz.root.data.server.model.collection.CollectionMetadata;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link MetadataService} for CERN Open data collections.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
final class CernCollectionMetadataService implements MetadataService<CollectionMetadata> {

   // Uniform Resource Name (URN)
   // schema:namespace
   private static final String UID_PATTERN = "cern-open-data:%s";
   private static final Pattern DOI_PATTERN = Pattern.compile("DOI:([0-9.]+/[0-9A-Z.]+)");

   @Override
   public CollectionMetadata buildMetadata(@NonNull String collectionId, @NonNull Path file) {
      Document document;
      try {
         document = Jsoup.parse(file.toFile(), "UTF-8");
      } catch (Exception exception) {
         throw new FetchException("An error occurred while parsing the collection page", exception);
      }

      var citeInfo = extractCiteInfo(document);

      return CollectionMetadata.builder() //
            .id(String.format(UID_PATTERN, collectionId)) //
            .name(collectionId) //
            .description(extractDescription(document)) //
            .experimentName(extractExperimentName(document)) //
            .type(extractCollectionType(document)) //
            .tag(extractCollectionTag(document)) //
            .citeText(citeInfo.getLeft()) //
            .doi(citeInfo.getRight()) //
            .build();
   }

   private String extractExperimentName(@NonNull Document document) {
      return document.getElementsByClass("badge-experiment").text().trim();
   }

   private String extractCollectionType(@NonNull Document document) {
      return document.getElementsByClass("badge-subtype").text().trim();
   }

   private String extractCollectionTag(@NonNull Document document) {
      return document.getElementsByClass("badge-tag").text().trim();
   }

   // Cite text and DOI
   private Pair<String, String> extractCiteInfo(@NonNull Document document) {
      String text = document.getElementsByClass("cite-paragraph").text();
      Matcher doiMatcher = DOI_PATTERN.matcher(text);
      if (doiMatcher.find()) {
         String doiText = doiMatcher.group(1);
         return Pair.of(text.replaceFirst("DOI:" + Pattern.quote(doiText), ""), doiText);
      }
      return Pair.of(text, "");
   }

   private String extractDescription(@NonNull Document document) {
      try {
         return document.getElementsMatchingOwnText("Description").first() // <h2>Description</h2>
               .parent() //
               .getElementsByTag("p") //
               .text() //
               .trim();
      } catch (Exception exception) {
         return "";
      }
   }
}
