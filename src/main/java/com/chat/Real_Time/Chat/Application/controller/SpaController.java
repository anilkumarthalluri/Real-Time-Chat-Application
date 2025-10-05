package com.chat.Real_Time.Chat.Application.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This controller handles server-side routing for the single-page application (SPA).
 * It ensures that both the root path and any deep-link URL (e.g., /login, /signup)
 * are correctly forwarded to the main index.html page, allowing the client-side
 * JavaScript router to handle the view.
 */
@Controller
public class SpaController implements ErrorController {

    /**
     * Explicitly handles requests to the root path of the application.
     *
     * @return A forward directive to the main index.html page.
     */
    @RequestMapping("/")
    public String handleRootPath() {
        return "forward:/index.html";
    }

    /**
     * Handles server-side errors, specifically forwarding 404 Not Found errors
     * to the SPA's entry point. This is the "catch-all" for deep links.
     *
     * @param request The incoming HTTP request.
     * @return A forward directive to the main index.html page.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // For 404 Not Found errors, forward to the SPA's entry point.
            if (statusCode == 404) {
                return "forward:/index.html";
            }
        }

        // For all other errors, you could forward to a generic error page,
        // but for an SPA, forwarding to the entry point is a safe default.
        return "forward:/index.html";
    }
}
