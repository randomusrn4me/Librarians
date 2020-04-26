package utils;

import ui.listusers.ListUsersController;

public interface Controller {
    public void setReceivedUser(ListUsersController.User receivedUserClass);
    public void initByHand();
}
