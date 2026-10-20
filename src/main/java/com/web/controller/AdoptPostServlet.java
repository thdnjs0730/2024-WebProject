package com.web.controller;

import com.web.dao.AdoptDao;
import com.web.dto.AdoptDto;
import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet({"/adopt", "/adoptDetail.do", "/editAdopt.do", "/deleteAdopt.do", "/editAdoptForm.do", "/latestAdopt.do"})
@MultipartConfig(maxFileSize = 5242880, maxRequestSize = 6291456)
public class AdoptPostServlet extends HttpServlet {
    private final AdoptDao dao = new AdoptDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            switch (req.getServletPath()) {
                case "/adopt":
                    if (req.getParameter("postId") != null) { detail(req, res, false); return; }
                    req.getRequestDispatcher("/adoption-list.jsp").forward(req, res); return;
                case "/adoptDetail.do": detail(req, res, false); return;
                case "/editAdopt.do": detail(req, res, true); return;
                case "/latestAdopt.do":
                    req.setAttribute("posts", dao.getLatestPosts(6));
                    req.getRequestDispatcher("/adoption-latest.jsp").forward(req, res); return;
                default: res.sendError(405);
            }
        } catch (NumberFormatException e) { res.sendError(400, "유효한 게시글 번호가 필요합니다."); }
          catch (SQLException e) { throw new ServletException(e); }
    }

    private AdoptDto find(HttpServletRequest req, HttpServletResponse res, boolean owner) throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("postId"));
        AdoptDto post = dao.getPostById(id);
        if (post == null) { res.sendError(404); return null; }
        HttpSession session = req.getSession(false);
        String user = session == null ? null : (String) session.getAttribute("userId");
        if (owner && !Objects.equals(user, post.getUserId())) { res.sendError(403); return null; }
        return post;
    }

    private void detail(HttpServletRequest req, HttpServletResponse res, boolean edit) throws SQLException, ServletException, IOException {
        AdoptDto post = find(req, res, edit);
        if (post == null) return;
        req.setAttribute("post", post);
        HttpSession session = req.getSession(false);
        req.setAttribute("id", session == null ? null : session.getAttribute("userId"));
        req.getRequestDispatcher(edit ? "/adoption-edit.jsp" : "/adoption-detail.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = req.getServletPath();
        if (!Set.of("/adopt", "/editAdoptForm.do", "/deleteAdopt.do").contains(route)) { res.sendError(405); return; }
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) { res.sendRedirect(req.getContextPath() + "/auth.jsp"); return; }
        String uploaded = null;
        try {
            AdoptDto post = route.equals("/adopt") ? new AdoptDto() : find(req, res, true);
            if (post == null) return;
            if (route.equals("/deleteAdopt.do")) {
                dao.deletePost(post.getId()); removePhoto(post.getPhoto());
                res.sendRedirect(req.getContextPath() + "/adopt"); return;
            }
            String title = req.getParameter("title"), description = req.getParameter("description"), type = req.getParameter("pet_type");
            if (title == null || title.isBlank() || title.length() > 255 || description == null || description.isBlank() || description.length() > 15000 || !Set.of("강아지", "고양이", "기타").contains(type == null ? "" : type)) {
                res.sendError(400, "제목, 내용, 반려동물 종류를 확인해 주세요."); return;
            }
            Part photo = req.getPart("photo");
            String oldPhoto = post.getPhoto();
            if (photo != null && photo.getSize() > 0) {
                uploaded = savePhoto(photo); post.setPhoto(uploaded);
            } else if (route.equals("/adopt")) { res.sendError(400, "사진이 필요합니다."); return; }
            post.setTitle(title); post.setDescription(description); post.setPetType(type);
            post.setUserId((String) session.getAttribute("userId"));
            if (route.equals("/adopt")) {
                dao.insertPost(post); res.sendRedirect(req.getContextPath() + "/adopt");
            } else {
                dao.updatePost(post);
                if (uploaded != null) removePhoto(oldPhoto);
                res.sendRedirect(req.getContextPath() + "/adoptDetail.do?postId=" + post.getId());
            }
        } catch (NumberFormatException e) { res.sendError(400, "유효한 게시글 번호가 필요합니다."); }
          catch (IllegalArgumentException | IllegalStateException e) { if (uploaded != null) removePhoto(uploaded); res.sendError(400, "5MB 이하의 PNG/JPEG/GIF 사진을 선택해 주세요."); }
          catch (SQLException e) { if (uploaded != null) removePhoto(uploaded); throw new ServletException(e); }
    }

    private String savePhoto(Part photo) throws IOException {
        // Decode and re-encode image data: do not trust the original file name or MIME header.
        String format;
        java.awt.image.BufferedImage image;
        try (InputStream in = photo.getInputStream(); javax.imageio.stream.ImageInputStream data = ImageIO.createImageInputStream(in)) {
            Iterator<javax.imageio.ImageReader> readers = ImageIO.getImageReaders(data);
            if (!readers.hasNext()) throw new IllegalArgumentException("Invalid image");
            javax.imageio.ImageReader reader = readers.next();
            try {
                reader.setInput(data);
                format = reader.getFormatName().toLowerCase(Locale.ROOT);
                if (!Set.of("png", "jpeg", "gif").contains(format) || (long) reader.getWidth(0) * reader.getHeight(0) > 16000000) throw new IllegalArgumentException("Image too large or unsupported");
                image = reader.read(0);
            } finally { reader.dispose(); }
        }
        String name = UUID.randomUUID() + "." + (format.equals("jpeg") ? "jpg" : format);
        Path target = UploadServlet.directory().resolve(name);
        if (!ImageIO.write(image, format, target.toFile())) throw new IOException("No image writer");
        return name;
    }
    private void removePhoto(String name) throws IOException {
        if (name != null && name.matches("[a-f0-9-]+\\.(png|jpg|gif)")) Files.deleteIfExists(UploadServlet.directory().resolve(name));
    }
}
