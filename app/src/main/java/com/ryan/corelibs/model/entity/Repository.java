package com.ryan.corelibs.model.entity;

import java.io.Serializable;
import java.util.Date;

public class Repository implements Serializable {
    public Long id;
    public String name;
    public String description;
    public Owner owner;
    public int stargazers_count;
    public String language;
    public Date updated_at;
}
