package com.rgallego.connector.connector.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Rule {
    /**
     * Used only for responses
     */
    private String id;

    /**
     * https://developer.twitter.com/en/docs/twitter-api/tweets/filtered-stream/integrate/build-a-rule
     *
     * keyword
     * "exact phrase match"
     * # (hashtag)
     * @ (metions)
     * from: username/user ID (tweet from a user)
     * to: username/user ID (tweet in reply to a user)
     * entity:"Michael Jordan/Barcelona" (Person, Place, Product, Organization, Other)
     *
     * NON-STANDALONE
     * lang: es/en (idioma)
     * sample: 10 (devuelve el 10% de los tweets en lugar de todos)
     * is:verified (cuentas verificadas)
     */
    private String value;

    private String tag;

    public Rule(String value, String tag) {
        this.value = value;
        this.tag = tag;
    }
}
