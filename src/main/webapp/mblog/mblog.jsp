<%@page import="com.born2go.lazzybee.gdatabase.shared.Picture"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@ page import="com.born2go.lazzybee.gdatabase.server.DataServiceImpl"%>
<%@ page import="com.born2go.lazzybee.gdatabase.shared.Blog"%>

<%
	//Check if we serve mobile or not?
	String ua = request.getHeader("User-Agent").toLowerCase();
	if (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")
			|| ua.substring(0, 4)
					.matches(
							"(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")) {

	} else {
		String blogId = request.getPathInfo().replaceAll("/", "");
		RequestDispatcher rd = getServletContext()
				.getRequestDispatcher("/blog/" + blogId);
		if (rd != null) {
			rd.forward(request, response);
		} else {
			response.setStatus(response.SC_OK);
			redirectHomeUrl(response);
		}
		return;
	}
%>

<%!//Global functions
	public void redirectHomeUrl(HttpServletResponse response) {
		String site = new String("/mvdict/");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}%>
<%
	if (request.getPathInfo() == null
			|| request.getPathInfo().length() <= 1)
		redirectHomeUrl(response);
	else {
		String blogTitle = request.getPathInfo().replaceAll("/", "");
		if (blogTitle == null || blogTitle.equals(""))
			redirectHomeUrl(response);
		else {
			DataServiceImpl service = new DataServiceImpl();
			Blog blog = service.findBlogByTitle(blogTitle);
			if (blog == null)
				redirectHomeUrl(response);
			else {

				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"d/MM/yyyy");
				String title = blog.getShowTitle();
				String url = "http://127.0.0.1:8888/mblog/" + title;
				String pathPicture = "";
				String content = blog.getContent();
				String dateCreate = "Ngày tạo "
						+ dateFormat.format(new Date(blog
								.getCreateDate()));
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
<script type="text/javascript" language="javascript"
	src="/lazzybeemobile/lazzybeemobile.nocache.js"></script>
<meta content="width=device-width, initial-scale=1.0, user-scalable=yes"
	name="viewport">
<link type="text/css" rel="stylesheet"
	href="/mobile-resources/mobile.css">
<link rel="stylesheet"
	href="/resources/font-awesome-4.2.0/css/font-awesome.min.css">
<link rel="icon" type="image/png" href="/favicon.png" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="/mobile-resources/menu.js"></script>

<title><%=title%></title>
<meta property="og:title" content="<%=title.replaceAll("\"", "\'")%>" />
<meta property="og:type" content="article" />
<meta property="og:image" content="<%=pathPicture%>" />
<meta property="og:url" content="<%=url%>" />
<%
	if (content != null) {
%>
<meta property="og:description"
	content='<%=content.replaceAll("\'", "\"")
									.replace("\n", "").replace("\r", "")%>' />
<%
	;
				}
%>
</head>

<body>
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
									href="/mblog/">Blog</a>
							</div>
						</li>
					</ul>
				</nav>
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
			<div class="mCenter">
				<div><%=content%></div>
				<br />
			</div>
			<%
				}
					}
				}
			%>
		</div>
	</div>
	<div class="mfooter" id="mfooter">
		<center>© Copyright 2015, Born2Go.</center>
	</div>
</body>
</html>