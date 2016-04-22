package com.born2go.lazzybee.gdatabase.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class TestVocaServlet
 */
public class TestVocaServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");

		Document doc = Jsoup.connect("http://testyourvocab.com").get();

		Element wordlist = doc.select("table.wordlist").first();
		resp.getWriter().println(
				"<form action=\"TestVocaServlet\" method=\"post\">");
		resp.getWriter().println(wordlist);

		Element table = doc.select("table.wordlist").first();
		Element row = table.select("tr").first();
		Elements tds = row.select("td");
		HashMap<String, String> hmap = new HashMap<String, String>();
		for (int i = 0; i < tds.size(); i++) {
			Elements childrenTd = tds.get(i).select("label");
			for (int j = 0; j < childrenTd.size(); j++) {
				String key = childrenTd.get(j).attr("for");
				String value = childrenTd.get(j).text();
				hmap.put(key, value);
				 
			}
		}

		 

		// Temporay submit button
		resp.getWriter()
				.println(
						"<button class=\"submit\" type=\"submit\" name=\"continue\">continue</button> </form>");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		// Check if we have user-id cookie

		// No cookie, so dit is the step 1

		// Sample data
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("action", "step_two");

		hmap.put("word-162", "0");
		hmap.put("word-163", "0");
		hmap.put("word-164", "0");
		hmap.put("word-165", "0");
		hmap.put("word-168", "0");
		hmap.put("word-170", "0");
		hmap.put("word-171", "1");
		hmap.put("word-173", "1");
		hmap.put("word-174", "1");
		hmap.put("word-175", "1");
		hmap.put("word-176", "0");
		hmap.put("word-177", "0");
		hmap.put("word-178", "0");
		hmap.put("word-180", "0");
		hmap.put("word-182", "0");
		hmap.put("word-181", "0");
		hmap.put("word-183", "0");
		hmap.put("word-166", "0");
		hmap.put("word-167", "0");
		hmap.put("word-169", "0");
		hmap.put("word-172", "0");
		hmap.put("word-179", "0");

		// Connection
		Connection conn = Jsoup
				.connect("http://testyourvocab.com/step_two?user=6490247")
				.followRedirects(true).data(hmap);

		Document doc = conn.post();
		Element wordlist = doc.select("table.wordlist").first();

		Cookie cookie = new Cookie("user-id", conn.response().url().getPath()
				+ "?" + conn.response().url().getQuery());
		resp.addCookie(cookie);
		resp.getWriter().println(wordlist);
		// Print out the querydata which contains userid
		System.out.println(conn.response().url().getQuery());

	}

}
