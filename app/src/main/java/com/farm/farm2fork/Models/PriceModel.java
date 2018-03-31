package com.farm.farm2fork.Models;

/**
 * Created by master on 24/2/17.
 */

public class PriceModel {


    private String market;
    private String state;
    private String arrivals;
    private String origins;
    private String variety;
    private String grade;
    private String minprice;
    private String maxprice;
    private String modalprice;

    public String getArrivals() {
        return arrivals;
    }

    public String getGrade() {
        return grade;
    }

    public String getMarket() {
        return market;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public String getMinprice() {
        return minprice;
    }

    public String getModalprice() {
        return modalprice;
    }

    public String getOrigins() {
        return origins;
    }

    public String getState() {
        return state;
    }

    public String getVariety() {
        return variety;
    }
}
