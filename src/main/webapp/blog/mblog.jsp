<%@page import="java.io.Console"%>
<%@page import="com.google.gwt.user.client.Window"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%@ page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%@ page import="java.io.IOException"%>
<%-- <%!//Global function
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");
		try {
			response.getWriter().print("<h1>NOT_FOUND</h1>");
		} catch (IOException e) {
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setHeader("Location", site);
	}%> --%>
<%
	DataServiceImpl service = new DataServiceImpl();
	Picture blog_avatar = null;
   	String title = "";
   	Blog currentBlog = null;
   	boolean show_n = false;
	if (request.getPathInfo() == null || request.getPathInfo().length() <= 1)
	{
	
	}
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "");
	    currentBlog = service.findBlogByTitle(blogTitle);
		if (currentBlog != null){
				if (currentBlog.getAvatar() != null)
				blog_avatar = service.findPicture(currentBlog.getAvatar());
				title = currentBlog.getShowTitle();
   
		}
		  else{
	  show_n = true;
		 	/* redirectHomeUrl(response);
	return; */
		}  
		 
	}
	// System.out.print("vao day " + currentBlog);
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
<meta name="description"
	content="Lazzy Bee cung cấp ứng dụng học tiếng Anh, từ vựng hiệu quả. Giúp bạn xây dựng vốn từ vựng mọi lúc, mọi nơi chỉ với 5 phút mỗi ngày.">
<meta name="keywords"
	content="Lazzy,Bee,học tiếng anh,hoc tieng anh,từ vựng,tu vung, tra từ điển Anh Việt, tra tu dien Anh Viet">
<meta content="<%=title%>">
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
 <%
	if(currentBlog != null) {
%>
<title><%=title%></title>
<%
	} else {
%>
<title>LazzyBee - Học tiếng anh, từ vựng với flashcard</title>
<%
	}
%>
<%
	if(currentBlog != null) {
%>
<meta property="og:type" content=website />
<%
	if(blog_avatar != null) {
%>
<meta property="og:image" content="<%=blog_avatar.getServeUrl()%>" />
<%
	}  else {
%>
<meta property="og:image"
	content="http://www.lazzybee.com/resources/2015-11-01.png" />
<%
	}
%>
<meta property="og:title"
	content="<%=currentBlog.getShowTitle().replaceAll("\"", "\'")%>" />
<meta property="og:url"
	content="http://www.lazzybee.com/blog/<%=title%>" />
<meta property="fb:app_id" content="754889477966743" />
<meta property="fb:pages" content="1012100435467230" />

<link rel="canonical" href="http://www.lazzybee.com/blog/<%=currentBlog.getTitle()%>">
<%
	}
%>
<link rel="icon" type="image/png" href="/favicon.png" />
<script type="text/javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js" async></script>
<!-- <script src="https://connect.facebook.net/en_US/all.js" async></script> -->
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
				<i class="fa fa-bars fa-lg" id="menuBtn"></i>
			</div>
			<div class="toppest">
				<div class="main_title">Lazzybee Blogs</div>
			</div>
		</div>
	</div>
	<div id="main">
		<div id="content">
		
		
			<%
			if(currentBlog != null){
			%>
			<article>
			<header>
			<div class="nameBlog">
				<h1><%=title%></h1>
			</div>
			</header>
			<%
				SimpleDateFormat df = new SimpleDateFormat("d/MM/yyyy");
			DateFormat dfFb = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");
			%>
			<i class="fa fa-clock-o">&nbsp;</i><i class="publishdate"><%=df.format(new Date(currentBlog.getCreateDate()))%></i>
			<div  style="float: left; margin-top: 3px;">Bài viết được tạo: 
						<time class="op-published" datetime="<%=dfFb.format(new Date(currentBlog.getCreateDate()))%>"><%=df.format(new Date(currentBlog.getCreateDate()))%></time> 
						</div>

			<%
				if (blog_avatar != null) {
			%>
			<div class="avatarBlog" id="avatarBlog">
				<img alt="hoc cung lazzybee"
					src="<%=blog_avatar.getServeUrl() + "=s200"%>" height="200px">
			</div>
			<%
				}
			%>
			<div class="mCenter">
				<div><%=currentBlog.getContent()%></div>
				<br />
			</div>
			 
			<footer>

       				<aside>LazzyBee Team</aside>

       				<small>Born2Go © 2016</small>
     		</footer>
           </article>
			<%
			List<Blog> blogs_exsist = new ArrayList<Blog>();

			blogs_exsist = service.getBlogsOlder(currentBlog);
				if (blogs_exsist.size() > 0) {
			%>
			<div class="fon39">
				<h5>Các bài đã đăng</h5>
			</div>
			<ul class="blogs_exist">
				<%
					for (int i = 0; i < blogs_exsist.size(); i++) {
				Blog blog_exist = blogs_exsist.get(i);
				%>
				<li><a style="color: #004175; line-height: 2;"
					href=<%="/blog/" + blog_exist.getTitle()%>><%=blog_exist.getShowTitle()%></a></li>
				<%
					}
				%>
			</ul>
			<%
				}
			%>
			<script>
				function onFBReady() {
					var fbApi = new faceBookAPI();
					fbApi.OnloadFaceBook();
				}
			</script>
			<script type="text/javascript">
				function showmap(id, linkid) {
					var divid = document.getElementById(id);
					var toggleLink = document.getElementById(linkid);
					if (divid.style.display == 'block') {
						toggleLink.innerHTML = 'Hiển thị comments FB';
						divid.style.display = 'none';
					} else {
						toggleLink.innerHTML = 'Ẩn comments FB';
						divid.style.display = 'block';
						onFBReady();
					}
				}
			</script>
			<!-- hide/show fb-comments -->
			<a id="showComment" href="#" class="MTestTool_Obj11"
				onclick="showmap('fb_comments', this.id);">Hiển thị comment</a>
			<div id="fb_comments" style="display: none">
				<div class="fb-comments" data-width="100%"
					data-href="http://www.lazzybee.com/blog/<%=title%>"
					data-numposts="5" data-colorscheme="light"
					data-order-by="reverse_time" data-version="v2.5"></div>
			</div>
			<h2 class="mblog_install_app">
				Tải ứng dụng <a href="http://www.lazzybee.com/">Lazzybee</a> cho <a
					href="https://itunes.apple.com/us/app/lazzy-bee/id1035545961?ls=1&mt=8"
					style="cursor: none;">iOS</a> và <a
					href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee"
					style="cursor: none;">Android</a>
			</h2>

			<br /> <br />
			<%
				} else{
			%>
			 
		
		<%} %>
			<%
				if(currentBlog == null){
			%>
			<div class="blogs" style="display: block;">
				<%
					if (show_n == true) {
				%>
				<div class="notice_u">Không tìm thấy dữ liệu</div>
				<%
					}
				%>

				<div class="fon39" style="border: none">
					<h5>Tất cả các bài đã đăng</h5>
				</div>
				<ul id="myList">
					<%
						List<Blog> blogs = new ArrayList<Blog>();
					    List<Blog> blogs_exist = service.getListBlog(false);
					  if(blogs_exist != null && ! blogs_exist.isEmpty())
						  blogs.addAll(blogs_exist);
						SimpleDateFormat df = new SimpleDateFormat("d/MM/yyyy");
						String title_b = null;
						Picture picture = null;
						if(blogs != null && !blogs.isEmpty())
						for (int i = 0; i < blogs.size(); i++) {
								Blog blog = blogs.get(i);
								if (blog != null) {
									title_b = blog.getShowTitle();
									picture = service.findPicture(blog.getAvatar());
									String urlPicture = "";
									if (picture != null)
											urlPicture = picture.getServeUrl() + "=s100";
									else
											urlPicture = "/mobile-resources/lazzybee_m.png";																			
					%>
					<li><a class="vdict_avatar"
						href=<%="/blog/" + blog.getTitle()%> title=<%=title%>> <img
							alt=<%=title_b%> src="<%=urlPicture%>">
							<h3><%=title_b%></h3>
							<div class="ovh time">
								<i class="fa fa-clock-o">&nbsp;</i><i class="publishdate"><%=df.format(new Date(blog.getCreateDate()))%></i>
							</div>
					</a></li>
					<%
						}

						}
					%>


				</ul>
			</div>
			<%
				} %>
			
			
		
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go © 2016</center>
	</div>
</body>
</html>