<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@ page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%@ page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>

<%!
	//Global function
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");	
		try {
			response.getWriter().print("<h1>NOT_FOUND</h1>");
		} catch(IOException e) {}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setHeader("Location", site);
	}
%>

<%
	//Global variable
	java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/YYYY HH:mm");
	Blog blog;
	Picture blog_avatar;
	
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() < 1) {
		redirectHomeUrl(response);
		return;
	} 
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "");
		DataServiceImpl dataService = new DataServiceImpl();
		blog = dataService.findBlogByTitle(blogTitle);
		if (blog == null) {
			redirectHomeUrl(response);
			return;
		} 
		else {
			if(blog.getAvatar() != null)
				blog_avatar = dataService.findPicture(blog.getAvatar());
		}
	}
%>		

<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="../lazzybee.css">

<link rel="icon" type="image/png" href="../favicon.png" />

<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

<title>Lazzy Bee</title>

<script type="text/javascript" language="javascript"
	src="../lazzybee/lazzybee.nocache.js"></script>
<script src="../resources/ckeditor/ckeditor.js"></script>

<script type="text/javascript">
	function handleClientLoad() {
		if (typeof GWTExport != 'undefined') {
			var lazzyBeeExport = new GWTExport.LazzyBeeExport();
			lazzyBeeExport.handleClientLoad();
		}
	}
	function exporterOnLoad() {
		var lazzyBeeExport = new GWTExport.LazzyBeeExport();
		lazzyBeeExport.handleClientLoad();
	}
</script>

<script
	src="https://apis.google.com/js/client.js?onload=handleClientLoad"></script>
<script src="https://connect.facebook.net/en_US/all.js"></script>

</head>

<body style="overflow: hidden;">

	<div id="fb-root"></div>
	<script>
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

	<div id="header">
		<div class="header_logo">
			<img alt="" src="../resources/1435838158_Mushroom - Bee.png"
				style="height: 100%; margin-right: 5px;"> <span
				style="position: relative; top: -4px; color: rgb(234, 253, 116);">Lazzy
				Bee</span>
		</div>
		<div class="header_menu">
			<!-- <a class="header_menu_item">Bộ Flash Cards</a> -->
			<a href="/vdict/" class="header_menu_item"
				style="color: rgb(234, 253, 116) !important;">Thư Viện</a> <a
				id="menu_editor" href="/editor/" class="header_menu_item">Soạn
				Thảo</a>
		</div>
		<div class="header_accPro">
			<div id="menu_login"></div>
		</div>
		<div id="wt_noticebox"></div>
	</div>

	<div id="bodyy">
		<div id="wt_dictionary_slide" class="slidebar"></div>

		<div id="content" class="content">
			<script type="text/javascript">
				var ec_height = window.innerHeight - 40;
				document.getElementById("content").setAttribute("style",
						"height:" + ec_height + "px");
			</script>

			<div id="wt_dictionary"
				style="padding: 20px 30px 30px 30px; width: 600px; float: left;">
				<div style="text-align: left">
					<span style="font-size: 20px;color: #0e74af;font-weight: bold;"><%= blog.getTitle().replaceAll("_", " ") %></span>
					<%-- <%if(blog_avatar != null) { %>
					<br/>
					<img style="margin-top:20px" alt="" src="<%= blog_avatar.getServeUrl()%>">
					<% } %> --%>
					<span style="padding:10px;margin-top:15px;background: #f2f1f1;width: 100%;display: block;">Bài viết được tạo: <%= df.format(new Date(blog.getCreateDate())) %></span>
				</div>
				<br/>
				<div style="margin-bottom:30px"><%= blog.getContent() %></div>
			</div>

			<div id="right_panel">
				<div>
					<img alt="" src="/resources/right-panel-pic.jpg"
						style="width: 100%; height: 280px;">
				</div>
				<div style="float: left; margin-right: 10px; margin-top: 15px;">
					<img alt="" src="/resources/appstore.png"
						style="width: 140px; height: 50px; cursor: pointer;">
				</div>
				<div>
					<img alt="" src="/resources/googleplay.jpg"
						style="width: 140px; height: 50px; cursor: pointer; margin-top: 15px;">
				</div>
				<div class="advertise" style="margin-top: 25px; height: 100px">
					<span style="font-size: 15px; font-weight: bold;">Connect
						with Born2Go!</span> <br /> <br />
					<div style="float: left" class="fb-like"
						data-href="http://www.lazzybee.com/" data-width="260"
						data-layout="button_count" data-action="like"
						data-show-faces="true" data-share="false"></div>
					<a	target="_blank" href="https://www.facebook.com/lazzybees?fref=ts"
						style="float: left; color: #09f; cursor: pointer; margin-left: 20px; text-decoration: none;"><i
						class="fa fa-hand-o-right fa-lg"></i> Follow LazzyBee </a>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
