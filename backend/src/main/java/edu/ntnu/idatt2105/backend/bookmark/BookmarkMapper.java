package edu.ntnu.idatt2105.backend.bookmark;

import edu.ntnu.idatt2105.backend.bookmark.model.Bookmark;
import edu.ntnu.idatt2105.backend.user.dto.ToggleBookmarkRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookmarkMapper {

  BookmarkMapper INSTANCE = Mappers.getMapper(BookmarkMapper.class);

  @Mapping(target = "itemID", source = "itemID")
  Bookmark bookmarkRequestToBookmark(ToggleBookmarkRequest toggleBookmarkRequest);
}
