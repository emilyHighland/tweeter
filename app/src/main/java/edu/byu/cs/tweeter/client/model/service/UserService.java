package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.LogoutTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.RegisterTask;
import edu.byu.cs.tweeter.client.model.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.observer.UserObserver;

// for service classes: use observer pattern to notify presenter that tasks are done
public class UserService {

    private Service service;

    public UserService(){
        service = new Service();
    }

    public interface getUserObserver extends UserObserver { }

    public void getUser(String alias, getUserObserver observer){
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                alias, new GetUserHandler(observer));
        service.runTask(getUserTask);
    }


    /**
     * LOGIN
     */
    public interface LoginObserver extends AuthenticateObserver { }

    public void login(String alias, String password, LoginObserver observer){
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        service.runTask(loginTask);
    }


    /**
     * REGISTER
     */
    public interface RegisterObserver extends AuthenticateObserver { }

    public void register(String firstName, String lastName, String alias, String password, String image,
                         RegisterObserver observer){
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, image,
                new RegisterHandler(observer));
        service.runTask(registerTask);
    }

    /**
     * LOGOUT
     */
    public interface LogoutObserver extends SimpleNotificationObserver { }

    public void logout(UserService.LogoutObserver observer){
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(),
                new LogoutHandler(observer));
        service.runTask(logoutTask);
    }
}
