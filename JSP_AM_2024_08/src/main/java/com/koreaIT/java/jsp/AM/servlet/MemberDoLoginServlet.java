package com.koreaIT.java.jsp.AM.servlet;

import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.koreaIT.java.jsp.AM.exception.SQLErrorException;
import com.koreaIT.java.jsp.AM.util.DBUtil;
import com.koreaIT.java.jsp.AM.util.SecSql;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MemberDoLoginServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// DB 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 x");
			e.printStackTrace();
		}

		String url = "jdbc:mysql://127.0.0.1:3306/24_08_JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

		String user = "root";
		String password = "";

		Connection conn = null;
		

		try {
			conn = DriverManager.getConnection(url, user, password);
			request.setCharacterEncoding("UTF-8");

			String loginId = request.getParameter("loginId");
			String loginPw = request.getParameter("loginPw");

			SecSql sql = SecSql.from("SELECT *");
			sql.append("FROM `member`");
			sql.append("WHERE loginId = ?;", loginId);

			Map<String, Object> memberRow = DBUtil.selectRow(conn, sql);

			if (memberRow.isEmpty()) {
				response.getWriter().append(String.format(
						"<script>alert('%s는 없는 아이디입니다');location.replace('../member/login'); </script>", loginId));
				return;
			}

			if (memberRow.get("loginPw").equals(loginPw) == false) {
				response.getWriter().append(String.format(
						"<script>alert('비밀번호가 틀렸습니다');location.replace('../member/login'); </script>", loginId));
				return;
			}

			HttpSession session = request.getSession();
			session.setAttribute("loginedMemberId", memberRow.get("id"));
			session.setAttribute("loginedMemberLoginId", memberRow.get("loginId"));

			response.getWriter()
					.append(String.format("<script>alert('%s님 환영합니다'); location.replace('../article/list');</script>",
							memberRow.get("name")));

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SQLErrorException e) {
			e.getOrigin().printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
