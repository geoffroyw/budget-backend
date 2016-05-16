package io.yac.common.api.converter;

/**
 * Created by geoffroy on 07/02/2016.
 */
public interface ResourceEntityConverter<R, E> {

    R convertToResource(E entity);

    E convertToEntity(R resource, Long id);
}
