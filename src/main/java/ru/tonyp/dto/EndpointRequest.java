package ru.tonyp.dto;

import lombok.Builder;

/*
 Строковый тип используется для проверки невалидных значений.
 Валидные значения берутся из типа EndpointRequestAction.
*/
@Builder
public record EndpointRequest(String token, String action) {}
