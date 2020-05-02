package com.gmail.nowak.wjw.popularmovies.data.repository;

public class Event<Boolean> {

    private boolean hasBeenHandled = false;
    private java.lang.Boolean responseStatus;

    public Event(java.lang.Boolean status){
        responseStatus=status;
    }

    public java.lang.Boolean getStatusIfNotHandled(){
        if(hasBeenHandled){
            return java.lang.Boolean.FALSE;
        } else {
            return responseStatus;
        }
    }

    public boolean peekStatus(){
        return responseStatus;
    }
}
