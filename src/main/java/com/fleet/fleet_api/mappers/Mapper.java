package com.fleet.fleet_api.mappers;

import java.util.List;

public interface Mapper<E, R, C> {
    R toResponse(E entity);
    E toEntity(C request);

    default List<R> toResponseList(List<E> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
