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

   private static final Comparator<CollectionMetadata> COMPARATOR = Comparator.comparing(CollectionMetadata::getId);

   @NonNull
   private String id;

   @NonNull
   private String name;
   @Nonnull
   private String experimentName;
   // Collision, derived or simulated. Didn't want to provide an enum to make it
   // more flexible.
   @Nullable
   private Long eventsCount;
   @Nonnull
   private String type;
   @NonNull
   private String keyword;
   @NonNull
   private String tag;
   @NonNull
   private String citeText;
   @NonNull
   private String doi;
   @NonNull
   private String license;

   // DataCite
   @Nonnull
   private String creator;
   @NonNull
   private String title;
   @NonNull
   private String publisher;
   @NonNull
   private Integer publicationYear;

   // DataCite additional
   @NonNull
   private String language;
   @NonNull
   private String subject;
   @NonNull
   private String description;
   @NonNull
   private String geoLocation;
   @NonNull
   private String fundingReference;

   public CollectionMetadata() {
      // Required by the serialization layer
   }

   @Override
   public int compareTo(@NonNull CollectionMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }
}
