package com.laptrinhjavaweb.news.mapper.mongo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.RiderDocument;

@Mapper(componentModel = "spring")
public interface RiderMapper {

    @Mapping(target = "zone", ignore = true)
    void updateRider(RiderInput riderInput, @MappingTarget RiderDocument riderDocument);
}
