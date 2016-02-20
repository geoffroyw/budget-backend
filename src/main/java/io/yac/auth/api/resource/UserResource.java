package io.yac.auth.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.yac.auth.user.model.User;

/**
 * Created by geoffroy on 20/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResource {

    @JsonProperty
    //Hack since we cannot make jackson unwrap root properties because of katharsis
    private JsonUser user;


    public UserResource() {
        this.user = new JsonUser();
    }

    public UserResource(User user) {
        this.user = new JsonUser();
        this.user.id = user.getId();
        this.user.email = user.getEmail();
        this.user.login = user.getLogin();
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public char[] getPassword() {
        return user.getPassword();
    }

    public String getLogin() {
        return user.getLogin();
    }

    public char[] getPasswordConfirm() {
        return user.getPasswordConfirm();
    }

    public JsonUser getUser() {
        return user;
    }

    public void setUser(JsonUser user) {
        this.user = user;
    }

    public static class JsonUser {
        @JsonProperty
        private Long id;
        @JsonProperty
        private String email;
        @JsonProperty
        private char[] password;
        @JsonProperty
        private char[] passwordConfirm;
        @JsonProperty
        private String login;

        public JsonUser() {
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public char[] getPassword() {
            return password;
        }

        public String getLogin() {
            return login;
        }

        public char[] getPasswordConfirm() {
            return passwordConfirm;
        }
    }
}
