package edu.byu.cs.tweeter.server.handlers.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.server.dao.DAOModule;

public class InjectorConfig {

    private static InjectorConfig config;

    private InjectorConfig() {}

    public static InjectorConfig getInstance(){
        if (config == null){
            config = new InjectorConfig();
        }
        return config;
    }


    public Injector getInjector(){
        return Guice.createInjector(new DAOModule());
    }
}
