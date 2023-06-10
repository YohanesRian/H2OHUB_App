package com.tanpanama.h2ohub.GetStarted;

import java.io.Serializable;

public class level_serial implements Serializable {
    private boolean isChecked = false;
    private String level;
    private String level_desc;

    public level_serial(){

    }

    public boolean isChecked(){
        return this.isChecked;
    }

    public void setChecked(boolean checked){
        isChecked = checked;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_desc() {
        return level_desc;
    }

    public void setLevel_desc(String level_desc) {
        this.level_desc = level_desc;
    }
}
