package utils;

import ui.listusers.ListUsersController;

public interface Controller {
    public User receivedUserClass = null;
    public void setReceivedUser(User receivedUserClass);
    public void initByHand();
    public void closeWindow();
}
