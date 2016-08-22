<%@page import="javax.sound.midi.SysexMessage"%>
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
		if(service.getListBlog(true) != null && ! service.getListBlog(true).isEmpty())
	    blogs.addAll(service.getListBlog(true));
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<title>Tra từ điển Anh Việt - LazzyBee</title>
<meta name="keywords"
	content="Lazzy, Bee, học tiếng anh, hoc tieng anh, từ vựng, tu vung, tra từ điển Anh Việt, tra tu dien Anh Viet">
<meta name="description"
	content="Lazzy Bee cung cấp ứng dụng học từ vựng tiếng Anh hiệu quả, giúp xây dựng vốn từ vựng mọi lúc, mọi nơi chỉ với 5 phút mỗi ngày.">
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />
<script type="text/javascript" language="javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js" async></script>
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
			<div id="gwt_header_mdic"></div>
			<div class="right_header_w">
				<div class="right_header">
					<div id="inputsearch" class="inputsearch"></div>
					<div class="btsearch">
						<i class="fa fa-search" id="btsearch"></i>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div id="main">
		<div id="content">
			<div id="gwt_notice"></div>
			<div id="notice_first"
				style="margin-top: 10px;; padding: 10px; background-color: lemonchiffon; line-height: 1.5; text-align: center;">Đang
				tải...</div>
			<div id="gwt_contentMdic"></div>
			<br />
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
									<img alt="download lazzybee"
									src="/mobile-resources/appstore_m.png" class="appstore">
								</a>
							</div>
						</td>
						<td>
							<div>
								<a
									href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee">
									<img alt="download lazzybee"
									src="/mobile-resources/googleplay_m.jpg" class="appstore">
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
						SimpleDateFormat df = new SimpleDateFormat("d/MM/yyyy");
							String title = null;
							Picture picture = null;
							for (int i = 0; i < blogs.size(); i++) {
								Blog blog = blogs.get(i);
								if (blog != null) {
									title = blog.getShowTitle();
									picture = service.findPicture(blog.getAvatar());
									String urlPicture = "";
									if (picture != null)
										urlPicture = picture.getServeUrl() + "=s100";
									else
										urlPicture = "/mobile-resources/lazzybee_m.png";
					%>
					<li><a class="vdict_avatar"
						href=<%="/blog/" + blog.getTitle()%> title=<%=title%>> <img
							alt=<%=title%> src="<%=urlPicture%>">
							<h3><%=title%></h3>
							<div class="ovh time">
								<i class="fa fa-clock-o">&nbsp;</i><i class="publishdate"><%=df.format(new Date(blog.getCreateDate()))%></i>
							</div>
					</a></li>
					<%
						}

							}
					%>

					<%
						}
					%>
				</ul>
			</div>
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go © 2016</center>
	</div>
</body>
</html>