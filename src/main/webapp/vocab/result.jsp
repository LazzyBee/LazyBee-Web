<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/mvdict/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	String title = "Test your voca result";
	String url = request.getRequestURL().toString();
	String score = null;
	String url_image = null;
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() <= 1)
		redirectHomeUrl(response);
	else {
		String encry = request.getPathInfo().replaceAll("/", "");
		if (encry == null || encry.equals(""))
			redirectHomeUrl(response);
		else {
			DataServiceImpl service = new DataServiceImpl();
			score = service.getVocabResult_Test(encry);
			if (score == null || score.equals(""))
				redirectHomeUrl(response);
			else
				 url_image = "http://chart.apis.google.com/chart?chs=400x200&cht=p3&chtt="
							+ "Vốn%20từ%20của%20bạn%20là|"
							+ score.toString()
							+ "|từ"
							+ "&chts=0000FF,50&chf=bg,s,00000000";

		}
	}
	 
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

<meta property="og:url" content=<%=url%> />
<meta property="og:type" content="website" />
<meta property="og:title" content=<%=title%> />
<meta property="og:description" content="Kết quả làm bài test của bạn trên lazzybee" />
<meta property="og:image"
	content=<%=url_image %> />
<meta property="fb:app_id" content="754889477966743" />

<script type="text/javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js" async></script>
</head>
<body onload="onFBReady();">
	<!-- Load fb sdk -->
	<div id="fb-root"></div>
	<script>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.3&appId=754889477966743";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script>
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
			<div class="mainMTestTool">
				<div class="MTestTool_Obj1" style="padding: 10px; overflow: hidden;">
					<div class="i_testtool_info" style="margin-top: 5px;">Tổng số
						từ vựng của bạn được tính toán là</div>
				</div>
				<div style="text-align: center; margin-bottom: 40px;">
					<div class="box">
						<div class="result"><%=score%></div>
					</div>
				</div>
				<div style="text-align: center; margin-bottom: 40px;">
					<div class="fb-share-button" data-href=<%=url%>
						data-layout="button_count" data-mobile-iframe="true"></div>
				</div>
			</div>

		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go©2015</center>
	</div>
</body>
</html>