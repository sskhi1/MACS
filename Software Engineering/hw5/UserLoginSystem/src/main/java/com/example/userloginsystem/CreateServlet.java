package com.example.userloginsystem;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Login", value = "/Create")
public class CreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("create.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        AccountManager accountManager = (AccountManager) getServletContext().getAttribute(AccountManager.ROLE);
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");

        httpServletRequest.setAttribute("username", username);
        if(!accountManager.accountExists(username)){
            accountManager.createNewAccount(username, password);
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("welcome success.jsp");
            rd.forward(httpServletRequest, httpServletResponse);
        }else{
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("already in use.jsp");
            rd.forward(httpServletRequest, httpServletResponse);
        }
    }
}
