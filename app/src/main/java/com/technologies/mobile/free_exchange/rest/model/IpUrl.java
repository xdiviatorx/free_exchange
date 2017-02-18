package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 13.01.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpUrl {

    @JsonProperty("RESPONSE")
    String url;

    public String getUrl() {
        return url;
    }

    public String getHost(){
        int begin = 0;
        for( int i = 0; i < url.length(); i++ ){
            if( url.charAt(i) == '/' && url.charAt(i+1) == '/' ){
                begin = i+2;
                break;
            }
        }
        return url.substring(begin);
    }
}
