package edu.ntnu.idatt2105.backend.mapper;

import edu.ntnu.idatt2105.backend.dto.bookmark.ToggleBookmarkRequest;
import edu.ntnu.idatt2105.backend.model.Bookmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * a mapper for mapping between bookmark model and dto.
 */
@Mapper
public interface BookmarkMapper {

  BookmarkMapper INSTANCE = Mappers.getMapper(BookmarkMapper.class);

  /**
   * maps addItemRequest dto to bookmark model.
   *
   * @param toggleBookmarkRequest the dto
   * @return the bookmark model
   */
  @Mapping(target = "itemID", source = "itemID")
  Bookmark bookmarkRequestToBookmark(ToggleBookmarkRequest toggleBookmarkRequest);
}
