package com.example.userloginsystem;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener(value = "/login")
public class LoginInitialize implements ServletContextListener {
    public LoginInitialize() {}

    @Override
    public void contextInitialized(ServletContextEvent sce){
        AccountManager manager = new AccountManager();
        ServletContext servletContext  = sce.getServletContext();
        servletContext.setAttribute(AccountManager.ROLE, manager);
    }
}
