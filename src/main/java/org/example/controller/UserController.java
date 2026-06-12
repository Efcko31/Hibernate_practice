package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.repository.UserRepositoryImpl;
import org.example.entity.User;

import java.io.IOException;
import java.util.Optional;

//@WebServlet("/api/users/*")
public class UserController extends HttpServlet {
    private final UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
    private final ObjectMapper objectMapper = new ObjectMapper(); //для парсинга JSON

    //GetMapping("/{id}")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // получаем то что идет после /api/users/

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        Long userId = Long.parseLong(pathInfo.substring(1));
        Optional<User> userOpt = userRepositoryImpl.findById(userId);

        if(userOpt.isPresent()) {
            response.setContentType("application/json");
            //вручную превращаем в Java-объект в JSON-строку и отправляем клиенту
            objectMapper.writeValue(response.getWriter(), userOpt.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
