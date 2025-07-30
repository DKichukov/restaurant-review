package com.project.restaurant_review.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Field(type = FieldType.Keyword)
    private String stringNumber;

    @Field(type = FieldType.Text)
    private String streetName;

    @Field(type = FieldType.Text)
    private String unit;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String state;

    @Field(type = FieldType.Keyword)
    private String postalCode;

    @Field(type = FieldType.Text)
    private String country;
}
