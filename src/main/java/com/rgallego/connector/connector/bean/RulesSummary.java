package com.rgallego.connector.connector.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RulesSummary {

    private int created;

    private int not_created;

    private int deleted;

    private int not_deleted;

    private int valid;

    private int invalid;
}
