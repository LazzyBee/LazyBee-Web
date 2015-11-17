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
		if(service.getListBlog() != null && ! service.getListBlog().isEmpty())
	blogs.addAll(service.getListBlog());
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <script type="text/javascript">
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
</script> -->
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<title>Học từ vựng tiếng Anh, từ điển - LazzyBee</title>
<meta name="description"
	content="Lazzy Bee cung cấp ứng dụng học từ vựng tiếng Anh hiệu quả, giúp xây dựng vốn từ vựng mọi lúc, mọi nơi chỉ với 5 phút mỗi ngày.">
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />
<script type="text/javascript" language="javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js"></script>
<script type="text/javascript">
	
<%@include file="/mobile-resources/jquery-1.11.3.min.js" %>
	
<%@include file="/mobile-resources/menu.js" %>
	
</script>
</head>
<body>
	<!-- <script src="/mobile-resources/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="/mobile-resources/menu.js"></script> -->
	<script type="text/javascript">
		$(document).ready(function() {
			size_li = $("#myList li").size();
			x = 2;
			$('#myList li:lt(' + x + ')').show();
			if (x == size_li || size_li == 0) {
				$('#loadMore').hide();
			} else {
				$('#loadMore').show();
			}
			$('#loadMore').click(function() {
				x = (x + 5 <= size_li) ? x + 5 : size_li;
				$('#myList li:lt(' + x + ')').show();
				if (x == size_li) {
					$('#loadMore').hide();
				}
			});

		});
	</script>
	<!-- Google Tag Manager -->
	<noscript>
		<iframe src="//www.googletagmanager.com/ns.html?id=GTM-KZBFX5"
			height="0" width="0" style="display: none; visibility: hidden"></iframe>
	</noscript>
	<script>
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
								href="/blog/user_guide">Hưỡng dẫn sử dụng</a>
						</div>
					</li>
					<li>
						<div class="m_menu_feedback">
							<i class="fa fa-comment" style="color: white;"></i> <a
								href="/blog/feedback">Ý kiến phản hồi</a>
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
							<div style="float: left; margin-right: 10px; margin-top: 15px;">
								<a
									href="https://itunes.apple.com/us/app/lazzy-bee/id1035545961?ls=1&mt=8">
									<img alt="" src="/resources/appstore.png"
									style="width: 130px; height: 50px; cursor: pointer;">
								</a>

							</div>
						</td>
						<td>
							<div>
								<a
									href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee">
									<img alt="" src="/resources/googleplay.jpg"
									style="width: 130px; height: 50px; cursor: pointer; margin-top: 15px;">
								</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="blogs" class="blogs" style="display: block;">
				<ul id="myList">
					<%
						if (blogs != null && !blogs.isEmpty() && isShortBlog == true) {
					%>

					<%
						for (int i = 0; i < blogs.size(); i++) {
								Blog blog = blogs.get(i);
								if (blog != null) {
									String hrefShow = "/mblog/" + blog.getTitle();
									String title = blog.getShowTitle();
									Long pictureId = blog.getAvatar();
									Picture picture = service.findPicture(pictureId);
									String urlPicture = "";
									if (picture != null) {
										urlPicture = picture.getServeUrl();
									}
					%>
					<li style="display: none;">
						<div>
							<div class="titleBlog">
								<h1>
									<a href=<%=hrefShow%>><%=title%></a>
								</h1>
							</div>
							<div class="imgdefault">
								<a><img alt="" class="avatar" src="<%=urlPicture%>"
									style="color: rgb(56, 119, 127);" /></a>
								<p class="mparagraptext">
									<%=title%>
								</p>
							</div>
						</div>
					</li>

					<%
						}

							}
					%>

					<%
						}
					%>
				</ul>

				<div id="loadMore">Load more</div>
			</div>

		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go©2015</center>
	</div>
</body>
</html>