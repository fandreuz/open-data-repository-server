package io.github.fandreuz.open.data.server.model.collection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Collection metadata.
 *
 * @author fandreuz
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CollectionMetadataDO {

   @NonNull
   private String id;
   @NonNull
   private String name;
   @Nonnull
   private String experimentName;
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
   @Nonnull
   private String creator;
   @NonNull
   private String title;
   @NonNull
   private String publisher;
   @NonNull
   private Integer publicationYear;
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

   public CollectionMetadataDO() {
      // Required by the serialization layer
   }
}
