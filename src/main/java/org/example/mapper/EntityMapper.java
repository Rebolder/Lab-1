package org.example.mapper;

import org.example.dto.BoardGameRequest;
import org.example.dto.BoardGameResponse;
import org.example.dto.PublisherRequest;
import org.example.dto.PublisherResponse;
import org.example.model.BoardGame;
import org.example.model.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper {

    Publisher toPublisher(PublisherRequest request);

    PublisherResponse toResponse(Publisher publisher);

    @Mapping(target = "publisher", source = "publisher")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "slug", source = "request.slug")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "minPlayers", source = "request.minPlayers")
    @Mapping(target = "maxPlayers", source = "request.maxPlayers")
    @Mapping(target = "playTimeMinutes", source = "request.playTimeMinutes")
    BoardGame toBoardGame(BoardGameRequest request, Publisher publisher);

    @Mapping(source = "publisher.id", target = "publisherId")
    @Mapping(source = "publisher.name", target = "publisherName")
    BoardGameResponse toResponse(BoardGame game);

    void apply(PublisherRequest request, @MappingTarget Publisher publisher);

    @Mapping(target = "publisher", source = "publisher")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "slug", source = "request.slug")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "minPlayers", source = "request.minPlayers")
    @Mapping(target = "maxPlayers", source = "request.maxPlayers")
    @Mapping(target = "playTimeMinutes", source = "request.playTimeMinutes")
    void apply(BoardGameRequest request, Publisher publisher, @MappingTarget BoardGame game);
}
