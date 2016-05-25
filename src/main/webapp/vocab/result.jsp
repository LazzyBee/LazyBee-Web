<%@page import="com.google.gwt.user.client.Window"%>
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
	//url = "http://lazzybee.com/";
	String score = null;
	String url_image = null;
	String data_title = null;
	String data_desc = null;
	String data_dessF = null;
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() <= 1)
		redirectHomeUrl(response);
	else {
		String encry = request.getPathInfo().replaceFirst("/", "");
		if (encry == null || encry.equals(""))
			redirectHomeUrl(response);
		else {
			DataServiceImpl service = new DataServiceImpl();
			score = service.getVocabResult_Test(encry);
			if (score == null || score.equals("")) {
				//	redirectHomeUrl(response);
				score = null;
			} else {
				/* url_image = "http://chart.apis.google.com/chart?chs=160x160&cht=p3&chtt="
				+ "Vốn%20từ%20của%20bạn%20là|"
				+ score.toString()
				+ "|từ"
				+ "&chts=0000FF,20&chf=bg,s,00000000"; */
				url_image = "http://www.lazzybee.com/mobile-resources/fb_share.png";
				data_title = "Vốn từ của bạn khoảng "
						+ score.toString() + " từ";
				int score_i = Integer.parseInt(score.replace(",", "")
						.trim());

				if (score_i < 3000) {
					data_desc = "<li>"
							+ "Vốn từ của bạn ở mức cơ bản, bạn cần bổ sung thêm từ vựng để nâng cao trình độ tiếng Anh của mình."
							+ "</li>"
							+ "<li>"
							+ "Chúng tôi khuyến nghị bạn nên tăng cường để có mức từ vựng ít nhất khoảng 7000 từ."
							+ "</li>"
							+ "<li>"
							+ "Việc sử dụng một ứng dụng học từ như LazzyBee sẽ giúp bạn tăng trưởng vốn từ vựng đều đặn và bền vững (2000 từ một năm) "
							+ "<a style='text-decoration: underline !important; color: blue;' href='http://www.lazzybee.com/blog/can_you_learn_2000_words_per_year'>chỉ với 5 phút mỗi ngày.</a>"
							+ " Bạn nên thiết lập ở level 1 trong ứng dụng LazzyBee. Chúc bạn thành công!"
							+ "</li>";
				} else if (score_i < 5000) {
					data_desc = "<li>"
							+ "Vốn từ của bạn ở mức tạm ổn, tuy nhiên, bạn sẽ vẫn gặp nhiều khó khăn khi đọc sách tiếng Anh."
							+ "</li>"
							+ "<li>"
							+ "Chúng tôi khuyến nghị bạn nên tăng cường để có mức từ vựng ít nhất khoảng 7000 từ."
							+ "</li>"
							+ "<li>"
							+ "Việc sử dụng một ứng dụng học từ như LazzyBee sẽ giúp bạn tăng trưởng vốn từ vựng đều đặn và bền vững (2000 từ một năm) "
							+ "<a style='text-decoration: underline !important; color: blue;' href='http://www.lazzybee.com/blog/can_you_learn_2000_words_per_year'>chỉ với 5 phút mỗi ngày.</a>"
							+ " Bạn nên thiết lập ở level 2 trong ứng dụng LazzyBee. Chúc bạn thành công!"
							+ "</li>";
				} else if (score_i < 7500) {
					data_desc = "<li>"
							+ "Vốn từ của bạn ở mức khá tốt, bạn có thể đọc tài liệu cũng như nghe tiếng Anh tương đối dễ dàng."
							+ "</li>"
							+ "<li>"
							+ "Nếu bạn nâng mức từ vựng lên 9000 từ, bạn sẽ thấy trình độ tiếng Anh vươn lên một tầm cao mới."
							+ "</li>"
							+ "<li>"
							+ "Sử dụng một ứng dụng học từ như LazzyBee sẽ giúp bạn tăng trưởng vốn từ vựng đều đặn và bền vững (2000 từ một năm) "
							+ "<a style='text-decoration: underline !important; color: blue;' href='http://www.lazzybee.com/blog/can_you_learn_2000_words_per_year'>chỉ với 5 phút mỗi ngày.</a>"
							+ " Bạn nên thiết lập ở level 3 trong ứng dụng LazzyBee. Chúc bạn thành công";
				} else if (score_i < 10000) {
					data_desc = "<li>"
							+ "Vốn từ của bạn rất tốt, không có nhiều người học tiếng Anh có được vốn từ vựng như bạn."
							+ "</li>"
							+ "<li>"
							+ "Việc chủ động tăng vốn từ vựng là không quá cần thiết, nhưng sử dụng một ứng dụng học từ như LazzyBee sẽ giúp bạn tiết kiệm thời gian để nhớ các từ mới."
							+ "</li>";
				} else if (score_i < 12500) {
					data_desc = "<li>"
							+ "Bạn có vốn từ tương đương với người sống ở nước nói tiếng Anh vài năm. Vốn từ này giúp bạn nghe, nói và đọc tài liệu rất dễ dàng. Xin chúc mừng."
							+ "</li>"
							+ "<li>"
							+ "Việc chủ động tăng vốn từ vựng là không quá cần thiết, nhưng sử dụng một ứng dụng học từ như LazzyBee sẽ giúp bạn tiết kiệm thời gian để nhớ các từ mới"
							+ "</li>";;
				} else if (score_i < 19000) {
					data_desc = "<li>"
							+ "Bạn có vốn từ rất lớn, gần như người bản ngữ. Xin chúc mừng!"
							+ "</li>";
				} else {
					data_desc = "<li>"
							+ "Bạn có vốn từ vựng tương đương với người bản ngữ. Xin chúc mừng!"
							+ "</li>";
				}
				data_dessF = data_desc.replaceAll("<li>", "")
						.replaceAll("</li>", "");

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
<title>Vocabulary test result</title>
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />

<meta property="og:url" content=<%=url%> />
<meta property="og:type" content="website" />
<meta property="og:title"
	content="Kiểm tra vốn từ vựng tiếng Anh - LazzyBee" />
<meta property="og:description"
	content="Vốn từ vựng tiếng Anh của bạn được ước tính khoảng <%=score%> từ" />
<meta property="og:image" content=<%=url_image%> />
<meta property="fb:app_id" content="754889477966743" />

<script type="text/javascript"
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
				<div class="main_title">Lazzybee Result</div>
			</div>
		</div>
	</div>
	<div id="main">
		<div id="content">
			<div class="mainMTestTool">
				<%
					if (score == null) {
				%>
				<div class="MTestTool_Obj1" style="padding: 10px; overflow: hidden;">
					<div class="i_testtool_info"
						style="margin-top: 5px; margin-bottom: 5px;">Liên kết không
						hợp lệ</div>
				</div>
				<div style="text-align: center; margin: 10px 0px;">
					<a class="btn_replay" href="/testvocab/">Test từ</a>
				</div>
				<%
					} else {
				%>
				<div class="MTestTool_Obj1" style="padding: 10px; overflow: hidden;">
					<div class="i_testtool_info"
						style="margin-top: 5px; margin-bottom: 5px;">Vốn từ của bạn
						khoảng</div>
				</div>
				<div style="text-align: center; margin-bottom: 40px;">
					<div class="box">
						<div class="result_score"><%=score%></div>
					</div>
				</div>

				<div class="MTestTool_Obj1">
					<span style="font-weight: bold">Kết quả:</span>
					<ul style="width: 90%; line-height: 1.6">
						<%=data_desc%>
					</ul>
				</div>

				<br />

				<p style="font-style: italic; font-size: inherit; margin: 0px 5px;">
					(*)Bài kiểm tra này có sai số <a
						style="text-decoration: underline !important; color: blue;"
						href="http://www.lazzybee.com/blog/how_accurate_the_test_is">khoảng
						10%</a> . Để tìm hiểu chi tiết về các hoạt động, bạn có thể tìm hiểu
					bài viết chi tiết về cách thức kiểm tra <a
						style="text-decoration: underline !important; color: blue;"
						href="http://www.lazzybee.com/blog/how_the_test_works">ở đây</a>.
				</p>

				<br />
				<script src='http://connect.facebook.net/en_US/all.js'></script>

				<div class="todo">
					<!-- <div id="shareResult" style="display: inline-block; margin: 0"></div> -->

					<div id="shareResult" style="display: inline-block; margin: 0"
						href="<%=url%>" data-image="<%=url_image%>"
						data-title="<%=data_title%>" data-desc="<%=data_dessF%>"
						class="fb_share">
						<img class="fb_f" src="/mobile-resources/fb_f.png"> Chia sẻ
						FB
					</div>
					<a class="btn_replay" href="/testvocab/">Test lại</a>

				</div>
				<br />
				<div class="permalink">
					Để lưu trữ hoặc chia sẻ kết quả của bạn, sử dụng liên kết sau đây:
					<a href="<%=url%>"> <%=url%>
					</a>
				</div>
				<br />

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
				<%
					}
				%>

				<p
					style="padding: 10px; background-color: lemonchiffon; line-height: 1.5; font-size: inherit;">
					Lazzy Bee là ứng dụng học từ vựng chuyên nghiệp, giúp bạn tăng vốn
					từ vựng bền vững 2000 từ một năm <a
						style="text-decoration: underline !important; color: blue;"
						href="http://www.lazzybee.com/blog/can_you_learn_2000_words_per_year">chỉ
						với 5 phút mỗi ngày.</a>. Vui lòng đọc <a
						style="text-decoration: underline !important; color: blue;"
						href="http://www.lazzybee.com/blog/did_you_read_the_manual">hướng
						dẫn sử dụng</a> và học theo đúng phương pháp học để đảm bảo hiệu quả
					tối đa.
				</p>
				
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
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>Born2Go©2015</center>
	</div>
</body>
</html>