package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.Product;
import org.example.repository.ProductRepository;

import java.io.IOException;
import java.util.List;

public class ProductServlet extends HttpServlet {
    private final ProductRepository productRepository = new ProductRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF8");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Product> products = productRepository.findAll();
            objectMapper.writeValue(response.getWriter(), products);
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));
        productRepository.findById(id).ifPresentOrElse(
                product -> {
                    try {
                        objectMapper.writeValue(response.getWriter(), product);
                    } catch (Exception ignored) {
                    }
                }, () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND)

        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product newProduct = objectMapper.readValue(request.getInputStream(), Product.class);
        productRepository.save(newProduct);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product productToUpdate = objectMapper.readValue(request.getInputStream(), Product.class);
        productRepository.update(productToUpdate);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        productRepository.deleteById(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
