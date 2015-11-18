<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@ page contentType="text/html; charset=UTF-8"%>
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
	Picture blog_avatar = null;
	if (request.getPathInfo() == null
	|| request.getPathInfo().length() <= 1)
		redirectHomeUrl(response);
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "");
		if (blogTitle == null || blogTitle.equals(""))
	redirectHomeUrl(response);
		else {
	DataServiceImpl service = new DataServiceImpl();
	Blog currentBlog = service.findBlogByTitle(blogTitle);
	if (currentBlog == null)
		redirectHomeUrl(response);
	else {
		if (currentBlog.getAvatar() != null)
	blog_avatar = service.findPicture(currentBlog.getAvatar());
	SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy");
	String title = currentBlog.getShowTitle();
	String pathPicture = "";
	String content = currentBlog.getContent();
	content = content.replaceAll("<p>&nbsp;</p>", "");
	String dateCreate = "Ngày tạo "+ dateFormat.format(new Date(currentBlog.getCreateDate()));
	List<Blog> blogs_exsist = new ArrayList<Blog>();
	blogs_exsist = service.getBlogsOlder(currentBlog);
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
<!-- for view mobile -->
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<title><%=title%></title>
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />
<script type="text/javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js" async></script>
<script src="https://connect.facebook.net/en_US/all.js" async></script>
</head>
<body>

	<!-- Google Tag Manager -->
	<noscript>
		<iframe src="//www.googletagmanager.com/ns.html?id=GTM-KZBFX5"
			height="0" width="0" style="display: none; visibility: hidden"></iframe>
	</noscript>
	<script async>
		(function(w, d, s, l, i) {
			w[l] = w[l] || [];
			w[l].push({
				'gtm.start' : new Date().getTime(),
				event : 'gtm.js'
			});
			var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l='
					+ l
					: '';
			j.async = true;
			j.src = '//www.googletagmanager.com/gtm.js?id=' + i + dl;
			f.parentNode.insertBefore(j, f);
		})(window, document, 'script', 'dataLayer', 'GTM-KZBFX5');
	</script>
	<!-- End Google Tag Manager -->

	<div id="fb-root"></div>
	<script async>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.5&appId=754889477966743";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script>

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
			</div>
			<div class="toppest">
				<div class="main_title">Lazzybee</div>
			</div>
		</div>
	</div>
	<div id="main">
		<div id="content">
			<div class="nameBlog">
				<h1><%=title%></h1>
			</div>
			<div class="publishdate"><%=dateCreate%></div>
			<%
				if (blog_avatar != null) {
			%>
			<div class="avatarBlog" id="avatarBlog">
				<img alt="" src="<%=blog_avatar.getServeUrl()%>" height="200px">
			</div>
			<%
				}
			%>
			<div class="mCenter">
				<div><%=content%></div>
				<br />
			</div>
			<div class="fb-comments" data-width="100%"
				data-href="http://www.lazzybee.com/blog/<%=currentBlog.getTitle()%>"
				data-numposts="5" data-colorscheme="light"
				data-order-by="reverse_time" data-version="v2.3"></div>
			<br /> <br />

			<%
				if (blogs_exsist.size() > 0) {
			%>
			<div class="fon39">
				<h5>Các bài đã đăng</h5>
			</div>
			<ul class="blogs_exist">
				<%
					for (int i = 0; i < blogs_exsist.size(); i++) {
										Blog blog_exist = blogs_exsist.get(i);
										String hrefShow = "/blog/"
												+ blog_exist.getTitle();
										String name_blog = blog_exist.getShowTitle();
				%>
				<li><a style="text-decoration: none; color: #333;"
					href=<%=hrefShow%>><%=name_blog%></a></li>

				<%
					}
				%>
			</ul>
			<%
				}
			%>
			<%
				}
					}
				}
			%>
			<h2 class="mblog_install_app">
				Tải ứng dụng <a href="http://www.lazzybee.com/">Lazzybee</a> cho <a
					href="https://itunes.apple.com/us/app/lazzy-bee/id1035545961?ls=1&mt=8"
					style="cursor: none;">iOS</a> và <a
					href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee"
					style="cursor: none;">Android</a>
			</h2>
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go©2015</center>
	</div>
</body>
</html>