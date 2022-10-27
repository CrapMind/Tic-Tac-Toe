package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var currentSession = req.getSession(true);
        var field = new Field();
        var fieldData = field.getField();
        var data = field.getFieldData();

        currentSession.setAttribute("field", field);
        currentSession.setAttribute("data", data);

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);



    }

}
