package ru.tonyp.dto;

import lombok.Builder;

@Builder
public record EndpointResponse(EndpointResponseResult result, String message) {}
