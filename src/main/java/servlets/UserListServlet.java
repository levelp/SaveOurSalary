package servlets;

import dao.UserDAO;
import model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Вывод списка пользователей
 */
public class UserListServlet extends javax.servlet.http.HttpServlet {
    static EntityManagerFactory emf;
    UserDAO userDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        emf= Persistence.createEntityManagerFactory("Production-MySQL");
        userDAO= new UserDAO(emf);

        try {
            userDAO.startConnection();

            // Выводим список пользователей
            for (User u : userDAO.listALL()) {
                out.println(u.getId() + " login = " + u.getLogin());
            }

        } finally {
            userDAO.closeConnection();
        }

        emf.close();
    }
}