package org.jfu.test.weld.event;

public class LoggedInEvent {
    
    private String userName;
    private String role;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "LoggedInEvent [userName=" + userName + ", role=" + role + "]";
    }
    
    
}
