package user.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import user.dao.PatientDao;
import user.domain.*;
/**
 * Servlet implementation class patientServlet
 */
public class patientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private PatientDao userDAO;

    public void init() {
        userDAO = new PatientDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        } catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException, InstantiationException, IllegalAccessException {
        List<Patient> listUser = userDAO.listAllPatients();
        request.setAttribute("listPatients", listUser);
        for(int i=0;i<listUser.size();i++){
            System.out.println(listUser.get(i));
        } 
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsps/PatientLists.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsps/patientForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Patient existingUser = userDAO.getPatient(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsps/patientForm.jsp");
        request.setAttribute("patient", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, InstantiationException, IllegalAccessException {
        String first = request.getParameter("first");
        String last = request.getParameter("last");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        int phone = Integer.parseInt(request.getParameter("phone"));
        
        Patient newPatient = new Patient(first, last, age, gender, address, phone);
        userDAO.insertPatient(newPatient);
        response.sendRedirect("list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, InstantiationException, IllegalAccessException {
        int id = Integer.parseInt(request.getParameter("id"));
        String first = request.getParameter("first");
        String last = request.getParameter("last");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        int phone = Integer.parseInt(request.getParameter("phone"));
        Patient newPatient = new Patient(id, first, last, age, gender, address, phone);
        userDAO.updatePatient(newPatient);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, InstantiationException, IllegalAccessException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deletePatient(id);
        response.sendRedirect("list");

    }

}
