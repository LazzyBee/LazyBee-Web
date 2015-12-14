<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@ page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>
<%@ page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>

<%
	//Check if we serve mobile or not?
	String ua=request.getHeader("User-Agent").toLowerCase();
	if(ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {
		String blogId = request.getPathInfo().replaceAll("/", "");
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/mblog/"+ blogId);
		if (rd != null){
			rd.forward(request, response);
		}
		else {
			response.setStatus(response.SC_OK);
			redirectHomeUrl(response);
		} 
		return;
	}
%>

<%!//Global function
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/");
		try {
			response.getWriter().print("<h1>NOT_FOUND</h1>");
		} catch (IOException e) {
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setHeader("Location", site);
	}%>

<%
	//Global variable
	java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/YYYY HH:mm");
	Picture blog_avatar = null;
	Blog blog;
	Blog previous_blog;
	Blog next_blog;
	List<Blog> blogs_older  ;
	if (request.getPathInfo() == null
	|| request.getPathInfo().length() < 1) {
		redirectHomeUrl(response);
		return;
	} 
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "").toLowerCase();
		DataServiceImpl dataService = new DataServiceImpl();
		blog = dataService.findBlogByTitle(blogTitle);
		previous_blog = dataService.getPreviousBlog(blog);
		next_blog = dataService.getNextBlog(blog);
		if (blog == null) {
	redirectHomeUrl(response);
	return;
		} 
		else {
		if(blog.getAvatar() != null)
		blog_avatar = dataService.findPicture(blog.getAvatar());
		}
		blogs_older = new ArrayList<Blog>();
		blogs_older = dataService.getBlogsOlder(blog);
	}
%>

<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<link rel="icon" type="image/png" href="../favicon.png" />

<link type="text/css" rel="stylesheet" href="../lazzybee.css">
<link rel="stylesheet" href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">

<title><%=blog.getShowTitle()%></title>

<meta property="og:type" content=website />
<%
if(blog_avatar != null) {
%>
<meta property="og:image" content="<%=blog_avatar.getServeUrl()%>" />
<%
}  else {
%>
<meta property="og:image" content="http://www.lazzybee.com/resources/2015-11-01.png" />
<%
}
%>
<meta property="og:title" content="<%=blog.getShowTitle().replaceAll("\"", "\'")%>" />
<meta property="og:url" content="http://www.lazzybee.com/blog/<%=blog.getTitle()%>" />
<meta property="fb:app_id" content="754889477966743" />

<script type="text/javascript" language="javascript" src="../lazzybee/lazzybee.nocache.js"></script>

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

<script src="https://apis.google.com/js/client.js?onload=handleClientLoad" async></script>
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
	<!-- <script>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.5&appId=754889477966743";
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script> -->

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
			<a href="/" style="text-decoration: none">
			<img alt="" src="../resources/1435838158_Mushroom - Bee.png"
				style="height: 100%; margin-right: 5px;"> <span
				style="position: relative; top: -4px; color: rgb(234, 253, 116);">Lazzy
				Bee</span>
			</a>
		</div>
		<div class="header_menu">
			<!-- <a class="header_menu_item">Bộ Flash Cards</a> -->
			<%
				if(blog.getTitle().equals("feedback")) {
			%>
			<a href="/vdict/" class="header_menu_item">Thư Viện</a> <a
				href="/blog/user_guide" class="header_menu_item">Hướng dẫn</a> <a
				id="menu_editor" href="/editor/" class="header_menu_item">Soạn
				Thảo</a> <a style="color: rgb(234, 253, 116) !important;"
				href="/blog/feedback" class="header_menu_item">Ý kiến phản hồi</a>
			<%
				} else if(blog.getTitle().equals("user_guide")) {
			%>
			<a href="/vdict/" class="header_menu_item">Thư Viện</a> <a
				style="color: rgb(234, 253, 116) !important;"
				href="/blog/user_guide" class="header_menu_item">Hướng dẫn</a> <a
				id="menu_editor" href="/editor/" class="header_menu_item">Soạn
				Thảo</a> <a href="/blog/feedback" class="header_menu_item">Ý kiến
				phản hồi</a>
			<%
				} else {
			%>
			<a style="color: rgb(234, 253, 116) !important;" href="/vdict/"
				class="header_menu_item">Thư Viện</a> <a href="/blog/user_guide"
				class="header_menu_item">Hướng dẫn</a> <a id="menu_editor"
				href="/editor/" class="header_menu_item">Soạn Thảo</a> <a
				href="/blog/feedback" class="header_menu_item">Ý kiến phản hồi</a>
			<%
				}
			%>
		</div>
		<div class="header_accPro">
			<div id="menu_login"></div>
		</div>
		<div id="wt_noticebox"></div>
	</div>

	<div id="bodyy">
		<div id="wt_dictionary_slide_dummy" class="slidebar">
			<div class="slidebar_background">
				<br/> <br/>
				<span class="slidebar_anchor">Tra cứu từ vựng</span>
				<br/> <br/> <br/>
				<span class="slidebar_anchor">Kiểm tra vốn từ</span>
				<br/> <br/> <br/>
				<span class="slidebar_trademark">Born2Go © 2015</span>
			</div>
		</div>
	
		<div id="wt_dictionary_slide" class="slidebar" style="display: none"></div>

		<div id="content" class="content">
		
			<script type="text/javascript">
				var ec_height = window.innerHeight - 40;
				document.getElementById("content").setAttribute("style",
						"height:" + ec_height + "px");
				document.getElementById("wt_dictionary_slide_dummy").setAttribute("style",
						"height:" + ec_height + "px");
			</script>

			<div id="wt_dictionary"
				style="padding: 20px 30px 30px 30px; width: 600px; float: left;">
				<div style="text-align: left">
					<h1><%=blog.getShowTitle()%></h1>
					<%-- <% if(blog_avatar != null) { %>
					<img style="margin-top:20px; width: 100%; margin: auto; display: block;" alt="" src="<%= blog_avatar.getServeUrl()%>">
					<% } %> --%>
					<div
						style="overflow: hidden; margin-bottom: 20px; padding: 10px; margin-top: 15px; background: #f2f1f1; width: 100%; display: block; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;">
						<span style="float: left; margin-top: 3px;">Bài viết được
							tạo: <%=df.format(new Date(blog.getCreateDate()))%></span> <a
							id="blogViewEdit" style="display: none;" title="Soạn thảo"
							href="/editor/#blog/<%=blog.getId()%>"><i
							class="fa fa-pencil-square-o fa-lg"></i></a>
						<div style="float: right">
							<div class="fb-like"
								data-href="http://www.lazzybee.com/blog/<%=blog.getTitle()%>"
								data-layout="button_count" data-action="like"
								data-show-faces="true" data-share="true"></div>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 30px"><%=blog.getContent()%></div>

				<%-- <div>
					<%if(previous_blog != null) {%>
					<a style="float: left; cursor: pointer;" href="/blog/<%= previous_blog.getTitle() %>"><i class="fa fa-angle-double-left"></i> Trang trước</a>
					<% } else { %>
					<a style="float: left; cursor: default; color: silver !important"><i class="fa fa-angle-double-left"></i> Trang trước</a>
					<% } %>
					
					<%if(next_blog != null) {%>
					<a style="float: right; cursor: pointer;" href="/blog/<%= next_blog.getTitle() %>">Trang sau <i class="fa fa-angle-double-right"></i></a>
					<% } else { %>
					<a style="float: right; cursor: default; color: silver !important"">Trang sau <i class="fa fa-angle-double-right"></i></a>
					<% } %>
				</div> --%>
				<%
					if (blogs_older.size() > 0) {
				%>
				<div class="fon39">
					<h5>Các bài đã đăng</h5>
				</div>
				<ul class="blogs_exist">
					<%
						for (int i = 0; i < blogs_older.size(); i++) {
								Blog blog_exist = blogs_older.get(i);
								String hrefShow = "/blog/" + blog_exist.getTitle();
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

				<br /> <br />
				<div class="fb-comments" data-width="100%"
					data-href="http://www.lazzybee.com/blog/<%=blog.getTitle()%>"
					data-numposts="5" data-colorscheme="light"
					data-order-by="reverse_time" data-version="v2.3"></div>
				<br /> <br />
			</div>

			<div id="right_panel">
				<div style="text-align: center;">
					<img alt="" src="/resources/2015-11-01.png"
						style="width: 100%; height: 300px;">
				</div>
				<div style="float: left; margin-right: 10px; margin-top: 15px;">
					<a
						href="https://itunes.apple.com/us/app/lazzy-bee/id1035545961?ls=1&mt=8">
						<img alt="" src="/resources/appstore.png"
						style="width: 140px; height: 50px; cursor: pointer;">
					</a>
				</div>
				<div>
					<a
						href="https://play.google.com/store/apps/details?id=com.born2go.lazzybee">
						<img alt="" src="/resources/googleplay.jpg"
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
