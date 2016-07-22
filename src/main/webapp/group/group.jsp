<%@page import="java.io.IOException"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%@page import="com.born2go.lazzybee.gdatabase.shared.GroupVoca"%>
<%@page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	GroupVoca group = null;
	boolean show_n = false;
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() <= 1) {
	} else {
		String ids = request.getPathInfo().replaceAll("/", "");
		try {
			long id = Long.parseLong(ids);
			DataServiceImpl dataService = new DataServiceImpl();
			group = dataService.findGroupVoca(id);
			if (group == null)
				show_n = true;
		} catch (Exception e) {
			group = null;
			show_n = true;
		}

	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LazzyBee - Học tiếng anh, từ vựng với flashcard</title>
<meta name="description"
	content="Lazzy Bee cung cấp ứng dụng học tiếng Anh, từ vựng hiệu quả. Giúp bạn xây dựng vốn từ vựng mọi lúc, mọi nơi chỉ với 5 phút mỗi ngày.">
<meta name="keywords"
	content="Lazzy,Bee,học tiếng anh,hoc tieng anh,từ vựng,tu vung">

<link rel="icon" type="image/png" href="../favicon.png" />

<link type="text/css" rel="stylesheet" href="../lazzybee.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<script type="text/javascript" language="javascript"
	src="../lazzybee/lazzybee.nocache.js"></script>

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
	src="https://apis.google.com/js/client.js?onload=handleClientLoad"
	async></script>
<script src="https://connect.facebook.net/en_US/all.js" async></script>
</head>
<body style="overflow: hidden;">

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

	<div id="header">
		<div class="header_logo">
			<a href="/" style="text-decoration: none"> <img alt="Lazzy Bee"
				src="../resources/1435838158_Mushroom - Bee.png"
				style="height: 100%; margin-right: 5px;"> <span
				style="position: relative; top: -4px; color: rgb(234, 253, 116);">Lazzy
					Bee</span>
			</a>
		</div>
		<div class="header_menu">
			<!-- <a class="header_menu_item">Bộ Flash Cards</a> -->
			<a href="/vdict/" class="header_menu_item"
				style="color: rgb(234, 253, 116) !important;">Thư Viện</a> <a
				href="/blog/user_guide" class="header_menu_item">Hướng dẫn</a> <a
				id="menu_editor" href="/editor/" class="header_menu_item">Soạn
				Thảo</a> <a href="/blog/feedback" class="header_menu_item">Ý kiến
				phản hồi</a>
		</div>
		<div class="header_accPro">
			<div id="menu_login"></div>
		</div>
		<div id="wt_noticebox"></div>
	</div>

	<div id="bodyy">
		<div id="wt_dictionary_slide_dummy" class="slidebar">
			<div class="slidebar_background">
				<br /> <br /> <span class="slidebar_anchor">Tra cứu từ vựng</span>
				<br /> <br /> <br /> <span class="slidebar_anchor">Kiểm
					tra vốn từ</span> <br /> <br /> <br /> <span class="slidebar_trademark">Born2Go
					© 2016</span>
			</div>
		</div>

		<div id="wt_dictionary_slide" class="slidebar" style="display: none"></div>

		<div id="content" class="content">

			<script type="text/javascript">
				var ec_height = window.innerHeight - 40;
				document.getElementById("content").setAttribute("style",
						"height:" + ec_height + "px");
				document.getElementById("wt_dictionary_slide_dummy")
						.setAttribute("style", "height:" + ec_height + "px");
			</script>

			<%
				if (group != null) {
			%>
			<div id="wt_dictionary"
				style="padding: 20px 30px 30px 30px; width: 600px; float: left;">

				<div style="text-align: left">
					<div
						style="overflow: hidden; margin-bottom: 20px; padding: 10px; margin-top: 15px; background: #f2f1f1; width: 100%; display: block; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;">
						<span style="float: left; margin-top: 3px;" id="creatorGroup">Nhóm từ được tạo bởi: <%=group.getCreator()%></span> <a id="groupViewEdit"
							style="float:right; margin-top: 2px; margin-left: 20px;" title="Soạn thảo"
							href="/editor/#group/<%=group.getId()%>"><i
							class="fa fa-pencil-square-o fa-lg"></i></a>

					</div>
					<h1>Mô tả</h1>
					<div style="margin-bottom: 30px"><%=group.getDescription()%></div>
				</div>
				<div style="font-size: 17px; font-weight: bold; color: #0e74af;">Danh
					sách các từ</div>
				<div style="margin-bottom: 30px"><%=group.getListVoca()%></div>
			</div>
			<%
				} else {
			%>
			<div id="wt_grouplist"
				style="padding: 20px 30px 30px 30px; width: 600px">
				<%
					if (show_n == true) {
				%>
				<div class="notice_u">Không tìm thấy dữ liệu</div>
				<%
					}
				%>
			</div>
			<%
				}
			%>

			<div id="right_panel">
				<div style="text-align: center;">
					<img alt="Lazzy Bee" src="/resources/2015-11-01.png"
						style="width: 100%; height: 300px;">
				</div>
				<div style="float: left; margin-right: 10px; margin-top: 15px;">
					<a
						href="https://itunes.apple.com/us/app/lazzy-bee/id1035545961?ls=1&mt=8">
						<img alt="App hoc tieng anh, tu vung"
						src="/resources/appstore.png"
						style="width: 140px; height: 50px; cursor: pointer;">
					</a>
				</div>
				<div>
					<a
						href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee">
						<img alt="App hoc tieng anh, tu vung"
						src="/resources/googleplay.jpg"
						style="width: 140px; height: 50px; cursor: pointer; margin-top: 15px;">
					</a>
				</div>
				<div class="advertise" style="margin-top: 25px; height: 100px">
					<span style="font-size: 15px; font-weight: bold;">Connect
						with LazzyBee!</span> <br /> <br />
					<div style="float: left" class="fb-like"
						data-href="http://www.lazzybee.com/" data-width="260"
						data-layout="button_count" data-action="like"
						data-show-faces="true" data-share="false"></div>
					<a target="_blank"
						href="https://www.facebook.com/lazzybees?fref=ts"
						style="float: left; color: #09f; cursor: pointer; margin-left: 20px; text-decoration: none;"><i
						class="fa fa-hand-o-right fa-lg"></i> Follow LazzyBee </a>
				</div>
			</div>
		</div>
	</div>

</body>
</html>