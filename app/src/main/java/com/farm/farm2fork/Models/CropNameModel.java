package com.farm.farm2fork.Models;

import com.orm.SugarRecord;

/**
 * Created by master on 31/3/18.
 */

public class CropNameModel extends SugarRecord {

    private String name;

    public CropNameModel() {

    }

    public CropNameModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
