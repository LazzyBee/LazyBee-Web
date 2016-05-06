<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/mvdict/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	String url = request.getRequestURL().toString();
	String score = null;
	String url_image = null;
	String data_title = null;
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
			{
				/* url_image = "http://chart.apis.google.com/chart?chs=160x160&cht=p3&chtt="
				+ "Vốn%20từ%20của%20bạn%20là|"
				+ score.toString()
				+ "|từ"
				+ "&chts=0000FF,20&chf=bg,s,00000000"; */
				url_image="http://www.lazzybee.com/mobile-resources/fb_share.png";
				data_title = "Vốn từ của bạn khoảng " + score.toString() + " từ";
			}

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
<title>Test your voca result</title>
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />

<meta property="og:url" content=<%=url%> />
<meta property="og:type" content="website" />
<meta property="og:title" content="Test your voca result" />
<meta property="og:description"
	content="Kết quả làm bài test của bạn trên lazzybee" />
<meta property="og:image" content=<%=url_image%> />
<meta property="fb:app_id" content="754889477966743" />

<script type="text/javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js" async></script>
</head>
<body>
	<!-- Load fb sdk -->
	<div id="fb-root"></div>
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
					<div class="i_testtool_info"
						style="margin-top: 5px; margin-bottom: 5px;">Vốn từ của bạn
						khoảng</div>
				</div>
				<div style="text-align: center; margin-bottom: 40px;">
					<div class="box">
						<div class="result"><%=score%></div>
					</div>
				</div>

				<br />
				<div>Bài kiểm tra này có sai số khoảng 10%. Để tìm hiểu chi
					tiết về các hoạt động, bạn có thể tìm hiểu các bài viết khác.</div>

				<br />
				<script src='http://connect.facebook.net/en_US/all.js'></script>

				<div class="todo">
					<!-- <div id="shareResult" style="display: inline-block; margin: 0"></div> -->

					<div id="shareResult" style="display: inline-block; margin: 0"
						href="<%=url %>"
						data-image="<%=url_image %>"
						data-title="<%=data_title %>"
						data-desc="Kết quả làm bài test trên ứng dụng LazzyBee" class="fb_share">
						<img class="fb_f" src="/mobile-resources/fb_f.png"> Chia sẻ FB
					</div>
					<a class="btn_replay" href="/testvocab/">Test lại</a>

				</div>

				<script type="text/javascript">
					FB.init({
						appId : "754889477966743",
						status : true,
						cookie : true
					});
					(function(d, debug) {
						var js, id = 'facebook-jssdk', ref = d
								.getElementsByTagName('script')[0];
						if (d.getElementById(id)) {
							return;
						}
						js = d.createElement('script');
						js.id = id;
						js.async = true;
						js.src = "//connect.facebook.net/en_US/all"
								+ (debug ? "/debug" : "") + ".js";
						ref.parentNode.insertBefore(js, ref);
					}(document, /*debug*/false));

					function postToFeed(title, desc, url, image) {
						var obj = {
							method : 'feed',
							link : url,
							picture : image,
							name : title,
							description : desc
						};
						function callback(response) {
						}
						FB.ui(obj, callback);
					}

					var fbShareBtn = document.querySelector('.fb_share');
					fbShareBtn
							.addEventListener(
									'click',
									function(e) {
										e.preventDefault();
										var title = fbShareBtn
												.getAttribute('data-title'), desc = fbShareBtn
												.getAttribute('data-desc'), url = fbShareBtn
												.getAttribute('href'), image = fbShareBtn
												.getAttribute('data-image');
										postToFeed(title, desc, url, image);

										return false;
									});
				</script>
				<br />


				<!--  <script src='http://connect.facebook.net/en_US/all.js'></script>
				<p>
					<a onclick='postToFeed(); return false;'>Post to Feed</a>
				</p>
				<p id='msg'></p>
				<script>
					FB.init({
						appId : "754889477966743",
						status : true,
						cookie : true
					});

					function postToFeed() {

						// calling the API ...
						var obj = {
							method : 'feed',
							redirect_uri : 'localhost:8888/vocab/g/9XogRWLLj6rCFFISgStQ==',
							link : 'http://www.lazzybee.com/vocab/pgEO5ow16ZxJKptu7gv1dg==',
							picture : 'http://fbrell.com/f8.jpg',
							name : 'Facebook Dialogs',
							caption : 'Reference Documentation',
							description : 'Using Dialogs to interact with people.'
						};

						function callback(response) {
							document.getElementById('msg').innerHTML = "Post ID: "
									+ response['post_id'];
						}

						FB.ui(obj, callback);
					}
				</script> -->

			</div>

		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go©2015</center>
	</div>
</body>
</html>