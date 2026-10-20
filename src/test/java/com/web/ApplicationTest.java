package com.web;

import com.web.common.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/** Boots a real JSP/Servlet container and exercises HTTP with independent user sessions. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTest {
    static Tomcat server;
    static String base;
    static HttpClient owner, other, guest, admin;
    static int postId;
    static String photo;
    static String contextPath = System.getProperty("test.context", "/pet-adoption");
    static HttpClient client() {
        return HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).build();
    }
    static HttpResponse<String> get(HttpClient c, String route) throws Exception {
        return c.send(HttpRequest.newBuilder(URI.create(base + route)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
    static HttpResponse<String> form(HttpClient c, String route, String... pairs) throws Exception {
        List<String> body = new ArrayList<>();
        for (int i=0;i<pairs.length;i+=2) body.add(URLEncoder.encode(pairs[i], StandardCharsets.UTF_8)+"="+URLEncoder.encode(pairs[i+1],StandardCharsets.UTF_8));
        return c.send(HttpRequest.newBuilder(URI.create(base+route)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(String.join("&",body))).build(),HttpResponse.BodyHandlers.ofString());
    }
    static HttpResponse<String> multipart(HttpClient c, String route, boolean picture) throws Exception {
        String boundary="TestBoundary123";
        java.io.ByteArrayOutputStream out=new java.io.ByteArrayOutputStream();
        for (String[] field : new String[][]{{"title","입양 <script>alert(1)</script>"},{"description","건강한 반려동물"},{"pet_type","강아지"}}) {
            out.write(("--"+boundary+"\r\nContent-Disposition: form-data; name=\""+field[0]+"\"\r\n\r\n"+field[1]+"\r\n").getBytes(StandardCharsets.UTF_8));
        }
        if(picture) {
            out.write(("--"+boundary+"\r\nContent-Disposition: form-data; name=\"photo\"; filename=\"pet.png\"\r\nContent-Type: image/png\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(2,2,java.awt.image.BufferedImage.TYPE_INT_RGB);
            javax.imageio.ImageIO.write(image,"png",out); out.write("\r\n".getBytes());
        }
        out.write(("--"+boundary+"--\r\n").getBytes());
        return c.send(HttpRequest.newBuilder(URI.create(base+route)).header("Content-Type","multipart/form-data; boundary="+boundary).POST(HttpRequest.BodyPublishers.ofByteArray(out.toByteArray())).build(),HttpResponse.BodyHandlers.ofString());
    }
    static void status(int expected, HttpResponse<String> response) {
        assertEquals(expected,response.statusCode(),response.body());
    }
    @BeforeAll static void start() throws Exception {
        String mysql=System.getenv("TEST_DB_URL");
        System.setProperty("DB_URL",mysql==null?"jdbc:h2:mem:adoption;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;NON_KEYWORDS=USER":mysql);
        System.setProperty("DB_USER",mysql==null?"sa":System.getenv("TEST_DB_USER"));
        System.setProperty("DB_PASSWORD",mysql==null?"":System.getenv("TEST_DB_PASSWORD"));
        System.setProperty("ADMIN_ID","testadmin"); System.setProperty("ADMIN_PASSWORD","admin-test-123");
        System.setProperty("UPLOAD_DIR",Paths.get("target/test-uploads").toAbsolutePath().toString());
        try(Connection db=JdbcConnectUtil.getConnection(); Statement s=db.createStatement()) {
            // TEST_DB_URL must refer to a fresh, dedicated test database.
            for(String sql:Files.readString(Paths.get("database/schema.sql")).replaceAll("(?m)^--.*$","").split(";")) if(!sql.isBlank()) s.execute(sql);
        }
        server=new Tomcat(); server.setBaseDir(Paths.get("target/tomcat-test").toAbsolutePath().toString()); server.setPort(0); server.getConnector();
        Context ctx=server.addWebapp(contextPath,Paths.get("src/main/webapp").toAbsolutePath().toString());
        ctx.setParentClassLoader(ApplicationTest.class.getClassLoader());
        ctx.setAddWebinfClassesResources(true);
        org.apache.catalina.webresources.StandardRoot resources=new org.apache.catalina.webresources.StandardRoot(ctx);
        resources.addPreResources(new org.apache.catalina.webresources.DirResourceSet(resources,"/WEB-INF/classes",Paths.get("target/classes").toAbsolutePath().toString(),"/"));
        ctx.setResources(resources);
        server.start(); base="http://localhost:"+server.getConnector().getLocalPort()+contextPath;
        owner=client(); other=client(); guest=client(); admin=client();
    }
    @AfterAll static void stop() throws Exception { if(server!=null){server.stop();server.destroy();} }

    @Test @Order(1) void publicPagesAndAccessControl() throws Exception {
        for(String route:List.of("/","/index.jsp","/auth.jsp","/about.jsp","/adopt","/adoption-list.jsp","/latestAdopt.do","/assets/css/home.css","/assets/js/home.js","/assets/images/logo-black.png")) status(200,get(guest,route));
        for(String route:List.of("/myPage.do","/account.jsp","/adminPage.do","/admin.jsp","/adoption-create.jsp","/message-compose.jsp")) status(302,get(guest,route));
        status(404,get(guest,"/adoptDetail.do?postId=99999"));
        status(400,get(guest,"/adoptDetail.do?postId=invalid"));
    }
    @Test @Order(2) void registrationLoginAndPasswordHash() throws Exception {
        status(200,form(owner,"/register.do","id","bad","password","tiny","email","wrong","nickname",""));
        for(String id:List.of("owner123","other123")) status(302,form(guest,"/register.do","id",id,"password","password-123","email",id+"@example.com","nickname",id));
        status(200,form(guest,"/register.do","id","owner123","password","password-123","email","a@example.com","nickname","duplicate"));
        status(200,form(owner,"/login.do","id","owner123","password","incorrect"));
        status(302,form(owner,"/login.do","id","owner123","password","password-123"));
        status(302,form(other,"/login.do","id","other123","password","password-123"));
        status(302,form(admin,"/login.do","id","testadmin","password","admin-test-123"));
        try(Connection db=JdbcConnectUtil.getConnection(); Statement s=db.createStatement();ResultSet rs=s.executeQuery("SELECT password FROM users WHERE id='owner123'")){
            assertTrue(rs.next()); assertNotEquals("password-123",rs.getString(1)); assertTrue(Passwords.matches("password-123",rs.getString(1)));
        }
        status(403,get(owner,"/adminPage.do")); status(403,get(owner,"/admin.jsp")); status(200,get(admin,"/adminPage.do"));
        status(200,get(owner,"/myPage.do")); status(200,get(owner,"/message-compose.jsp?toUser=other123"));
    }
    @Test @Order(3) void adoptionCreateReadSearchAndOwnership() throws Exception {
        status(302,multipart(owner,"/adopt",true));
        try(Connection db=JdbcConnectUtil.getConnection();Statement s=db.createStatement();ResultSet rs=s.executeQuery("SELECT id,photo FROM adoptionposts")){assertTrue(rs.next());postId=rs.getInt(1);photo=rs.getString(2);}
        var detail=get(guest,"/adoptDetail.do?postId="+postId); status(200,detail);assertTrue(detail.body().contains("&lt;script&gt;"));assertFalse(detail.body().contains("<script>alert(1)</script>"));
        status(200,get(guest,"/uploads/"+photo)); status(404,get(guest,"/uploads/forbidden.jsp"));
        status(200,get(owner,"/editAdopt.do?postId="+postId));
        status(403,get(other,"/editAdopt.do?postId="+postId));
        status(403,form(other,"/deleteAdopt.do?postId="+postId));
        status(403,multipart(other,"/editAdoptForm.do?postId="+postId,false));
        status(302,multipart(owner,"/editAdoptForm.do?postId="+postId,false));
        status(200,get(guest,"/adoption-list.jsp?petType="+URLEncoder.encode("강아지",StandardCharsets.UTF_8)));
        status(200,get(guest,"/adoption-list.jsp?searchQuery=owner&searchType=nickname"));
        status(200,get(guest,"/adoption-list.jsp?searchQuery=owner&searchType=unknown"));
        assertTrue(get(guest,"/latestAdopt.do").body().contains("&lt;script&gt;"));
    }
    @Test @Order(4) void messageInboxOutboxAndProfileUpdate() throws Exception {
        status(400,form(owner,"/sendMessage.do","to","missing","message","hello"));
        var sent=form(owner,"/sendMessage.do","to","other123","message","hello <b>friend</b>");status(302,sent);assertEquals(contextPath+"/myPage.do",sent.headers().firstValue("Location").orElse(""));
        assertTrue(get(owner,"/myPage.do").body().contains("hello &lt;b&gt;friend&lt;/b&gt;"));
        assertTrue(get(other,"/myPage.do").body().contains("hello &lt;b&gt;friend&lt;/b&gt;"));
        status(302,form(owner,"/edit.do","edit-password","","edit-email","updated@example.com","edit-username","수정한 닉네임"));
        assertTrue(get(owner,"/myPage.do").body().contains("updated@example.com"));
        status(302,form(owner,"/edit.do","edit-password","new-password-123","edit-email","updated@example.com","edit-username","수정한 닉네임"));
        status(302,get(owner,"/logout.do")); status(302,get(owner,"/myPage.do"));
        status(302,form(owner,"/login.do","id","owner123","password","new-password-123"));
    }
    @Test @Order(5) void deleteAndAdminSearch() throws Exception {
        status(200,get(admin,"/adminPage.do?action=searchMembers&keyword=owner"));
        status(200,get(admin,"/adminPage.do?action=searchPosts&keyword=test"));
        status(302,form(owner,"/deleteAdopt.do?postId="+postId)); status(404,get(guest,"/adoptDetail.do?postId="+postId));
        status(404,get(guest,"/uploads/"+photo));
        status(400,form(admin,"/adminPage.do","id","testadmin","action","DeleteMember"));
        status(200,form(admin,"/adminPage.do","id","other123","action","DeleteMember"));
        try(Connection db=JdbcConnectUtil.getConnection();Statement s=db.createStatement();ResultSet rs=s.executeQuery("SELECT COUNT(*) FROM messages")){assertTrue(rs.next());assertEquals(0,rs.getInt(1));}
    }
}
