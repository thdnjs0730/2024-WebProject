package com.web.controller;
import com.web.common.JdbcConnectUtil;
import java.io.IOException;
import java.nio.file.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/uploads/*")
public class UploadServlet extends HttpServlet {
    public static Path directory() throws IOException {
        Path dir = Paths.get(JdbcConnectUtil.setting("UPLOAD_DIR", System.getProperty("user.home") + "/.pet-adoption/uploads")).toAbsolutePath();
        Files.createDirectories(dir); return dir;
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = req.getPathInfo();
        if (name == null || !name.matches("/[a-f0-9-]+\\.(png|jpg|gif)")) { res.sendError(404); return; }
        Path file = directory().resolve(name.substring(1));
        if (!Files.isRegularFile(file)) { res.sendError(404); return; }
        res.setContentType(name.endsWith("png") ? "image/png" : name.endsWith("gif") ? "image/gif" : "image/jpeg");
        res.setContentLengthLong(Files.size(file)); Files.copy(file, res.getOutputStream());
    }
}
