package com.example.mymusic;

import java.io.Serializable;

public class MusicModel implements Serializable {

    String aPath;
    String aName;

    public MusicModel(String aPath, String aName) {
        this.aPath = aPath;
        this.aName = aName;
    }


    public String getPath() {
        return aPath;
    }

    public void setPath(String aPath) {
        this.aPath = aPath;
    }

    public String getName() {
        return aName;
    }

    public void setName(String aName) {
        this.aName = aName;
    }
}
