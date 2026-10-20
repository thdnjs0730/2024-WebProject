package com.web.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import com.web.dao.AdoptDao;
import com.web.dao.MemberDao;
import com.web.dto.AdoptDto;
import com.web.dto.MemberDto;
import java.util.List;

@WebServlet({"/adopt", "/adoptDetail.do", "/editAdopt.do", "/deleteAdopt.do", "/editAdoptForm.do", "/latestAdopt.do"})
@MultipartConfig
public class AdoptPostServlet extends HttpServlet {
    private AdoptDao adoptDao;

    @Override
    public void init() {
        adoptDao = new AdoptDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();
        if ("/adopt".equals(action)) {
            handleAdopt(request, response);
        } else if("/deleteAdopt.do".equals(action)) {
            handleDelete(request, response);
        } else if("/editAdoptForm.do".equals(action)) {
            handleEditAdoptForm(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        if ("/adopt".equals(action)) {
            handleGetAdopt(request, response);
        } else if ("/adoptDetail.do".equals(action)) {
            handleAdoptDetail(request, response);
        } else if ("/editAdopt.do".equals(action)) {
            handleEditAdopt(request, response);
        } else if ("/latestAdopt.do".equals(action)) {
            handleGetLatestPosts(request, response);
        }
    }

    // 입양 게시글 등록 처리 메서드
    public void handleAdopt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("loginForm.jsp");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String petType = request.getParameter("pet_type");
        Part filePart = request.getPart("photo");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // 타임스탬프를 사용하여 고유한 파일명 생성
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String newFileName = timeStamp + "_" + fileName;

        // 파일 업로드 디렉토리 설정 및 생성
        String uploadDir = getServletContext().getRealPath("/") + "uploads";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String filePath = uploadDir + File.separator + newFileName;
        File file = new File(filePath);
        filePart.write(filePath);

        AdoptDto post = new AdoptDto();
        post.setTitle(title);
        post.setDescription(description);
        post.setPetType(petType);
        post.setUserId(userId);
        post.setPhoto(newFileName);
        post.setUserId(userId);

        try {
            adoptDao.insertPost(post);
        } catch (SQLException e) {
            throw new ServletException("게시글 등록 중 오류 발생", e);
        }

        response.sendRedirect("adopt");
    }

    // 입양 게시글 목록 조회 처리 메서드
    public void handleGetAdopt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String postId = request.getParameter("postId");
        if (postId != null) {
            try {
                AdoptDto post = adoptDao.getPostById(Integer.parseInt(postId));
                if (post != null) {
                    request.setAttribute("post", post);
                    request.setAttribute("id", userId);
                    request.getRequestDispatcher("/adoptDetail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("adopt?error=notfound");
                }
            } catch (SQLException e) {
                throw new ServletException("게시글 조회 중 오류 발생", e);
            }
        } else {
            List<AdoptDto> posts;
            try {
                posts = adoptDao.getAllPosts();
            } catch (SQLException e) {
                throw new ServletException("게시글 조회 중 오류 발생", e);
            }
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("/adopt.jsp").forward(request, response);
        }
    }

    // 입양 게시글 상세 조회 처리 메서드
    public void handleAdoptDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        int postId = Integer.parseInt(request.getParameter("postId"));
        AdoptDto post = new AdoptDto();
        try {
            post = adoptDao.getPostById(postId);
        } catch (SQLException e) {
            throw new ServletException("게시글 조회 중 오류 발생", e);
        }

        request.setAttribute("post", post);
        request.setAttribute("id", userId);
        request.getRequestDispatcher("/adoptDetail.jsp").forward(request, response);
    }

    // 최신 입양 게시글 조회 처리 메서드
    public void handleGetLatestPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<AdoptDto> posts;
        try {
            posts = adoptDao.getLatestPosts(6);
        } catch (SQLException e) {
            throw new ServletException("게시글 조회 중 오류 발생", e);
        }
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/latestAdopt.jsp").forward(request, response);
    }
    
    // 입양 게시글 수정 폼 요청 처리 메서드
    public void handleEditAdopt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("loginForm.jsp");
            return;
        }

        int postId = Integer.parseInt(request.getParameter("postId"));
        AdoptDto post = new AdoptDto();
        
        try {
            post = adoptDao.getPostById(postId);
        } catch(SQLException e) {
            throw new ServletException("게시글 조회 중 오류 발생", e);
        }
        request.setAttribute("post", post);
        request.getRequestDispatcher("/adoptEdit.jsp").forward(request, response);
    }

    // 입양 게시글 수정 처리 메서드
    public void handleEditAdoptForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("loginForm.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("postId"));

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String petType = request.getParameter("pet_type");
        Part filePart = request.getPart("photo");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // 파일명에 타임스탬프 추가하여 고유한 이름 생성
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String newFileName = timeStamp + "_" + fileName;

        String uploadDir = getServletContext().getRealPath("/") + "uploads";
        File uploadDirFile = new File(uploadDir);

        // 업로드 디렉토리가 존재하지 않으면 생성
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String filePath = uploadDir + File.separator + newFileName;

        // 파일 저장
        File file = new File(filePath);
        filePart.write(filePath);

        AdoptDto post = new AdoptDto();
        post.setId(id);
        post.setTitle(title);
        post.setDescription(description);
        post.setPetType(petType);
        post.setUserId(userId);
        post.setPhoto(newFileName);
        post.setUserId(userId);

        try {
            adoptDao.updatePost(post);
        } catch (SQLException e) {
            throw new ServletException("게시글 등록 중 오류 발생", e);
        }

        response.sendRedirect("/adoptDetail.do?postId=" + id);
    }

    // 입양 게시글 삭제 처리 메서드
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));

        try {
            adoptDao.deletePost(postId);
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "게시글 삭제가 완료되었습니다.");
            response.sendRedirect("/adopt");
        } catch(SQLException e) {
            throw new ServletException("게시글 삭제 중 오류 발생", e);
        }
    }
}
