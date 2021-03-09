package com.rgallego.connector.connector.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RulesRequest {

    private List<Rule> add;

    private DeleteRule delete;
}
