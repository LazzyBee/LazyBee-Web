<%@page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%@page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	boolean isShortBlog = false;
    List<Blog> blogs = new ArrayList<Blog>();
    DataServiceImpl service = new DataServiceImpl();
	if (request.getPathInfo() == null
	|| request.getPathInfo().length() <= 1) {
		isShortBlog = true;
		
		blogs.clear();
		if(service.findBlogs() != null && ! service.findBlogs().isEmpty())
	blogs.addAll(service.findBlogs());
		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	if (navigator.userAgent.match(/Android/i)
			|| navigator.userAgent.match(/webOS/i)
			|| navigator.userAgent.match(/iPhone/i)
			|| navigator.userAgent.match(/iPad/i)
			|| navigator.userAgent.match(/iPod/i)
			|| navigator.userAgent.match(/BlackBerry/i)
			|| navigator.userAgent.match(/Windows Phone/i)) {

	} else {
		var ourLocation = window.location.hash;
		window.location = "/vdict/" + ourLocation;
	}
</script>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script type="text/javascript" language="javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js"></script>
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="/mobile-resources/menu.js"></script>

<title>Insert title here</title>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>
	<div class="header_w">
		<div class="header">
			<div class="left_header">
				<i class="fa fa-bars fa-lg" style="color: white;" id="menuBtn"></i>
				<nav id="menu">
				<ul>
					<li>
						<div class="m_menu_seach">
							<i class="fa fa-search fa-1x" style="color: white;"></i> <a
								href="/mvdict/">Từ điển</a>
						</div>
					</li>

					<li>
						<div class="m_menu_help">
							<i class="fa fa-question fa-lg" style="color: white;"></i> <a
								href="/mtest/">Kiểm tra vốn từ</a>
						</div>
					</li>
					<li>
						<div class="m_menu_blog">
							<i class="fa fa-book" style="color: white;"></i> <a
								href="/mblog/">Blog</a>
						</div>
					</li>
				</ul>
				</nav>
			</div>
			<div id="gwt_header_mdic"></div>
			<div class="right_header_w">
				<div class="right_header">
					<div id="inputsearch" class="inputsearch"></div>
					<div class="btsearch" id="btsearch"></div>
				</div>
			</div>
		</div>
	</div>
	<div id="main">
		<div id="content">
			<div id="gwt_contentMdic"></div>
			<div id="notfoundVoca"></div>
			<div id="mdic_introduction" style="display: block;">
				<div
					style="padding: 10px; background-color: lemonchiffon; line-height: 1.5;">
					<span>Phương pháp học thông qua flashcard chỉ có trên phiên
						bản mobile. Bạn hãy cài đặt App mobile để học từ vựng tốt hơn.</span>
				</div>
				<table class="table-app" cellspacing="5">
					<tr>
						<td>
							<div style="float: left; margin-top: 15px;">
								<img alt="" src="/resources/appstore.png"
									style="width: 130px; height: 50px; cursor: pointer;">
							</div>
						</td>
						<td>
							<div>
								<img alt="" src="/resources/googleplay.jpg"
									style="width: 130px; height: 50px; cursor: pointer; margin-top: 15px;">
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="blogs" class="blogs" style="display: block;">
				<div class="headerBlogs">
					<h3>Những bài viết liên quan</h3>
				</div>
				<%
					if (blogs != null && !blogs.isEmpty() && isShortBlog == true) {
						for (int i = 0; i < blogs.size(); i++) {
							Blog blog = blogs.get(i);
							if (blog != null) {

								String hrefShow = "/mblog/" + blog.getTitle();
								String shortContent;
								String des = blog.getContent();
								if (des != null && des.length() != 0) {
									if (des.length() < 200)
										shortContent = des;
									else
										shortContent = des.substring(0, 200) + "...";
								}
								shortContent = "Thêm vài dòng vào đây cho nó thật dài xem nào...";

								String title = blog.getTitle().replaceAll("_", " ");
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"d/MM/yyyy");
								String dateCreate = "Ngày tạo "
										+ dateFormat.format(new Date(blog
												.getCreateDate()));
								Long pictureId = blog.getAvatar();
								Picture picture = service.findPicture(pictureId);
								String urlPicture = "";
								if (picture != null) {
									urlPicture = picture.getServeUrl();
								}
				%>
				<ul>
					<li>
						<div class="titleBlog">
							<h1>
								<a href=<%=hrefShow%>><%=title%></a>
							</h1>
						</div>
						<div class="publishdate">
							<%=dateCreate%></div>
						<div class="imgdefault">
							<a><img alt="" class="avatar" src="<%=urlPicture%>"
								style="color: rgb(56, 119, 127);" /></a>
							<p class="mparagraptext">
								<%=shortContent%>
							</p>
						</div>
					</li>
				</ul>
				<%
					}
						}
					}
				%>
			</div>
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>© Copyright 2015, Born2Go.</center>
	</div>
</body>
</html>