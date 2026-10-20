package com.web.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.web.dao.MemberDao;

@WebListener
public class AdminInitializer implements ServletContextListener {

    // 웹 어플리케이션 초기화 시 실행되는 메서드
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // MemberDao 객체 생성
        MemberDao memberDao = new MemberDao();
        // Admin 계정을 초기화하는 메서드 호출
        memberDao.initializeAdmin();
    }

    // 웹 어플리케이션 종료 시 실행되는 메서드
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 어플리케이션 종료 시 처리할 작업 (필요시)
    }
}
