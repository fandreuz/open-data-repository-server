package io.github.fandreuz.open.data.server.model.collection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Comparator;

/**
 * Collection metadata.
 *
 * @author fandreuz
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CollectionMetadata implements Comparable<CollectionMetadata> {

   private static final Comparator<CollectionMetadata> COMPARATOR = Comparator.nullsLast( //
         Comparator.comparing( //
               CollectionMetadata::getId, //
               Comparator.nullsLast(Comparator.naturalOrder()) //
         ) //
   );

   @NonNull
   private final String id;

   @NonNull
   private final String name;
   @Nonnull
   private final String experimentName;
   // Collision, derived or simulated. Didn't want to provide an enum to make it
   // more flexible.
   @Nullable
   private Long eventsCount;
   @Nonnull
   private final String type;
   @NonNull
   private final String keyword;
   @NonNull
   private final String tag;
   @NonNull
   private final String citeText;
   @NonNull
   private final String doi;
   @NonNull
   private final String license;

   // DataCite
   @Nonnull
   private final String creator;
   @NonNull
   private final String title;
   @NonNull
   private final String publisher;
   @NonNull
   private final Integer publicationYear;

   // DataCite additional
   @NonNull
   private final String language;
   @NonNull
   private final String subject;
   @NonNull
   private final String description;
   @NonNull
   private final String geoLocation;
   @NonNull
   private final String fundingReference;

   /**
    * Construct a metadata instance from a database object.
    *
    * @param collectionMetadataDO
    *            database object.
    * @return a dataset instance.
    */
   public static CollectionMetadata fromDatabaseObject(CollectionMetadataDO collectionMetadataDO) {
      return CollectionMetadata.builder() //
            .id(collectionMetadataDO.getId()) //
            .name(collectionMetadataDO.getName()) //
            .experimentName(collectionMetadataDO.getExperimentName()) //
            .eventsCount(collectionMetadataDO.getEventsCount()) //
            .type(collectionMetadataDO.getType()) //
            .keyword(collectionMetadataDO.getKeyword()) //
            .tag(collectionMetadataDO.getTag()) //
            .citeText(collectionMetadataDO.getCiteText()) //
            .doi(collectionMetadataDO.getDoi()) //
            .license(collectionMetadataDO.getLicense()) //
            .creator(collectionMetadataDO.getCreator()) //
            .title(collectionMetadataDO.getTitle()) //
            .publisher(collectionMetadataDO.getPublisher()) //
            .publicationYear(collectionMetadataDO.getPublicationYear()) //
            .language(collectionMetadataDO.getLanguage()) //
            .subject(collectionMetadataDO.getSubject()) //
            .description(collectionMetadataDO.getDescription()) //
            .geoLocation(collectionMetadataDO.getGeoLocation()) //
            .fundingReference(collectionMetadataDO.getFundingReference()) //
            .build();
   }

   /**
    * Transform this instance in a database object.
    *
    * @return a database object.
    */
   public CollectionMetadataDO asDatabaseObject() {
      return CollectionMetadataDO.builder() //
            .id(getId()) //
            .name(getName()) //
            .experimentName(getExperimentName()) //
            .eventsCount(getEventsCount()) //
            .type(getType()) //
            .keyword(getKeyword()) //
            .tag(getTag()) //
            .citeText(getCiteText()) //
            .doi(getDoi()) //
            .license(getLicense()) //
            .creator(getCreator()) //
            .title(getTitle()) //
            .publisher(getPublisher()) //
            .publicationYear(getPublicationYear()) //
            .language(getLanguage()) //
            .subject(getSubject()) //
            .description(getDescription()) //
            .geoLocation(getGeoLocation()) //
            .fundingReference(getFundingReference()) //
            .build();
   }

   @Override
   public int compareTo(@NonNull CollectionMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }
}
