package edu.byu.cs.tweeter.server.sqs.DTO;

public class UserDTO {

    private String name;
    private String alias;

    public UserDTO(){}

    public UserDTO(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
