package edu.byu.cs.tweeter.model.net.response;

public class SimpleResponse extends Response {

    public SimpleResponse(String message){
        super(false, message);
    }

    public SimpleResponse(){
        super(true, null);
    }
//
//    public SimpleResponse(boolean isTrue, String message){
//        super(isTrue, message);
//    }
}
