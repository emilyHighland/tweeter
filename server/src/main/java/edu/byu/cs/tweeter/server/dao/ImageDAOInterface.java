package edu.byu.cs.tweeter.server.dao;

public interface ImageDAOInterface {
    String uploadImage(String alias, String base64Image) throws RuntimeException;
}
