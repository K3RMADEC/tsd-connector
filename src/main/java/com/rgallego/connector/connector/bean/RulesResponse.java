package com.rgallego.connector.connector.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class RulesResponse implements Serializable {

    private List<Rule> data;

    private RulesMetadata meta;

    private List<RuleError> errors;
}
