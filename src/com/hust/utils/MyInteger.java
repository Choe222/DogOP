package com.hust.utils;
public class MyInteger {
    int value;

    public MyInteger(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }
    
    public void decrease(int d) {
    	this.value -= d;
    }
    
    public void increase(int d) {
    	this.value += d;
    }
}
