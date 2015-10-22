<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%@ page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/mvdict/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() <= 1)
		redirectHomeUrl(response);
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "");
		if (blogTitle == null || blogTitle.equals(""))
			redirectHomeUrl(response);
		else {
			DataServiceImpl service = new DataServiceImpl();
			Blog blog = service.findBlogByTitle(blogTitle);
			if (blog == null)
				redirectHomeUrl(response);
			else {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"d/MM/yyyy");
				String title = blog.getTitle();
				String url = "http://127.0.0.1:8888/mblog/" + title;
				String pathPicture = "";
				String content = blog.getContent();

				String dateCreate = "Ngày tạo "
						+ dateFormat.format(new Date(blog
								.getCreateDate()));
				Long avatar = blog.getAvatar();
				String urlAvatar = "";
%>

<!doctype html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->
<html>
<head>
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

<title><%=title%></title>
<meta property="og:title" content="<%=title.replaceAll("\"", "\'")%>" />
<meta property="og:type" content="article" />
<meta property="og:image" content="<%=pathPicture%>" />
<meta property="og:url" content="<%=url%>" />
<%
	if (content != null) {
%>
<meta property="og:description"
	content='<%=content.replaceAll("\'", "\"")
									.replace("\n", "").replace("\r", "")%>' />
<%
	;
				}
%>
</head>

<body>
	<!-- OPTIONAL: include this if you want history support -->
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
					</ul>
				</nav>
			</div>
			<div class="toppest">
				<div class="main_title">Lazzybee</div>
			</div>
		</div>
	</div>
	<div id="main">
		<div id="content">
			<div class="nameBlog">
				<h1>The difference between count and noncount nouns</h1>
			</div>
			<div class="publishdate"><%=dateCreate%></div>
			<%
				if (urlAvatar != "") {
			%>
			<div class="avatarBlog" id="avatarBlog">
				<img alt="" src="<%=urlAvatar%>" height="200px">
			</div>
			<%
				}
			%>

			<div class="mCenter">
				<div><%=content%></div>
				<br />
			</div>
			<%
				}
					}
				}
			%>
		</div>
	</div>
</body>
</html>