package com.example.state;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class State {
    @Id
    private String space;

    private Integer populate;

    private String keeper;

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public Integer getPopulate() {
        return populate;
    }

    public void setPopulate(Integer populate) {
        this.populate = populate;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }
}
