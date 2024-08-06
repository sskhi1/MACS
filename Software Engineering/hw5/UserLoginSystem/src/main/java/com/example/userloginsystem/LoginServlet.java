package com.example.userloginsystem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("index.jsp").forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        AccountManager accountManager = (AccountManager) getServletContext().getAttribute(AccountManager.ROLE);
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        if(accountManager.isCorrectPassword(username, password)){
            httpServletRequest.setAttribute("username", username);
            httpServletRequest.getRequestDispatcher("welcome success.jsp").forward(httpServletRequest, httpServletResponse);
        }else{
            httpServletRequest.getRequestDispatcher("try again.jsp").forward(httpServletRequest, httpServletResponse);
        }
    }
}
