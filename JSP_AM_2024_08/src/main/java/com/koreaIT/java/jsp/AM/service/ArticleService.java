package com.koreaIT.java.jsp.AM.service;

import java.sql.Connection;
import java.util.List;

import com.koreaIT.java.jsp.AM.dao.ArticleDao;
import com.koreaIT.java.jsp.AM.dto.Article;

public class ArticleService {

	private Connection conn;
	private ArticleDao articleDao;

	public ArticleService(Connection conn) {
		this.conn = conn;
		this.articleDao = new ArticleDao(conn);
	}

	public int getTotalCnt() {
		return articleDao.getTotalCnt();

	}

	public List<Article> getForPrintArticles(int limitFrom, int itemsInAPage)  {
		return articleDao.getForPrintArticles(limitFrom, itemsInAPage);
	}

}