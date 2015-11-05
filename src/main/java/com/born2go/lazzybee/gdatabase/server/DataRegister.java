package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {

	public DataRegister() {
		super();
		ObjectifyService.register(Voca.class);
		ObjectifyService.register(VocaPreview.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Blog.class);
		ObjectifyService.register(Picture.class);
		
//		User user = new User();
//		user.setAdmin(true);
//		user.setFacebook_id("536617819821814_F");
//		user.setUserName("Lệ Nguyễn");
//		ofy().save().entity(user);
		
		// Blog blog = new Blog();
		// blog.setTitle("Tạo blog mới");
		// blog.setCreateDate(new Date().getTime());
		// blog.setContent("thêm vài dòng vào đây cho nó dài");
		// ofy().save().entity(blog);

		// for (int i = 1; i <= 30; i++) {
		// Voca v = new Voca();
		// v.setQ("routine");
		// v.setLevel("3");
		// v.setA("{\"q\":\"a\", \"pronoun\":\"/a\", \"packages\":{}}");
		// v.setL_vn("<span class=\"tl\">danh từ</span><ul> <li>lề thói hằng ngày; công việc thường làm hằng ngày</li> <li>thủ tục; lệ thường  <ul>   <li><span class=\"ec\">these questions are asked as a matter of routine:</span><span class=\"exm\"> người ra đặt những câu hỏi này theo thủ tục</span></li>  </ul></li> <li>(sân khấu) tiết mục nhảy múa; tiết mục khôi hài</li></ul><span class=\"tl\">Kinh tế</span><ul> <li>chương trình thông dụng</li> <li>công việc</li> <li>công việc đều đặn hàng ngày</li> <li>công việc thường làm hàng ngày</li> <li>lề thói đã quen</li> <li>lệ thường</li> <li>quy lệ thường ngày</li> <li>thường lệ</li> <li>thường trình</li> <li>việc làm thường ngày</li></ul><span class=\"tl\">Kỹ thuật</span><ul> <li>biểu đồ</li> <li>chương trình con</li> <li>dãy</li> <li>kế hoạch</li> <li>tiện ích</li></ul><span class=\"tl\">Lĩnh vực: toán &amp; tin</span><ul> <li>đoạn chương trình</li></ul>");
		// v.setL_en(" <span class=\"tl\">Noun</span><br><span class=\"mean\">an unvarying or habitual method of procedure</span><br><span class=\"mean\">a set sequence of steps, part of larger computer program</span><br><span class=\"mean\">a short theatrical performance that is part of a longer program</span><br><span class=\"ex\"> \"he did his act three times every evening\"</span><br><span class=\"ex\"> \"she had a catchy little routine\"</span><br><span class=\"ex\"> \"it was one of the best numbers he ever did\"</span><br><br><span class=\"tl\">Adjective</span><br><span class=\"mean\">found in the ordinary course of events</span><br><span class=\"ex\"> \"a placid everyday scene\"</span><br><span class=\"ex\"> \"it was a routine day\"</span><br><span class=\"ex\"> \"there's nothing quite like a real...train conductor to add color to a quotidian commute\"- Anita Diamant</span><br><span class=\"mean\">occurring at fixed times or predictable intervals</span><br><span class=\"ex\"> \"made her routine trip to the store\"</span><br>");
		// v.setPackages(",common,");
		// v.setCheck(false);
		// ofy().save().entity(v);
		// }
	}
}
