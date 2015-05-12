package com.talentica.smsahu.chatroom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.talentica.smsahu.chatroom.core.Template;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

/*import io.dropwizard.db.DataSourceFactory;*/

public class ChatRoomConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    /*@Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();*/
    
    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    private Map<String, String> authenticationCachePolicy = Collections.emptyMap();

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    /*@JsonProperty("authenticationCachePolicy")
    public Map<String,String> getAuthenticationCachePolicy(){
        return authenticationCachePolicy;
    }*/

    public Template buildTemplate() {
        return new Template(template, defaultName);
    }

    /*@JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }*/
}
