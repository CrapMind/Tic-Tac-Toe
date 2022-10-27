package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        var currentSession = req.getSession();
        var field = extractField(currentSession);
        var index = getSelectedIndex(req);
        var currentSign = field.getField().get(index);

        if (Sign.EMPTY != currentSign) {
            var dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
        }

        field.getField().put(index, Sign.CROSS);

        if (checkWin(resp, currentSession, field)) {
            return;
        }

        var emptyFieldIndex = field.getEmptyFieldIndex();

        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            if (checkWin(resp, currentSession, field)) {
                return;
            }
        }

        else {
            currentSession.setAttribute("draw", true);

            var data = field.getFieldData();
            currentSession.setAttribute("data", data);

            resp.sendRedirect("/index.jsp");
            return;
        }

        var data = field.getFieldData();

        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);

        resp.sendRedirect("/index.jsp");
    }

    private int getSelectedIndex(HttpServletRequest request) {
        var click = request.getParameter("click");
        var isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession currentSession) {
        var fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }

    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        var winner = field.checkWin();

        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            currentSession.setAttribute("winner", winner);

            var data = field.getFieldData();
            currentSession.setAttribute("data", data);

            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }
}
