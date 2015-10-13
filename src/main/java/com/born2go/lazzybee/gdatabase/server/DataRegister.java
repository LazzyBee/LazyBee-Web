package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {
	
	public DataRegister() {
		super();
		ObjectifyService.register(Voca.class);
		ObjectifyService.register(VocaPreview.class);
		ObjectifyService.register(User.class);
		
//		for(int i = 1; i<=30; i++) {
//			Voca v = new Voca();
//			v.setQ("routine");
//			v.setLevel("3");
//			v.setA("{\"q\":\"a\", \"pronoun\":\"/a\", \"packages\":{}}");
//			v.setL_vn("<span class=\"tl\">danh từ</span><ul> <li>lề thói hằng ngày; công việc thường làm hằng ngày</li> <li>thủ tục; lệ thường  <ul>   <li><span class=\"ec\">these questions are asked as a matter of routine:</span><span class=\"exm\"> người ra đặt những câu hỏi này theo thủ tục</span></li>  </ul></li> <li>(sân khấu) tiết mục nhảy múa; tiết mục khôi hài</li></ul><span class=\"tl\">Kinh tế</span><ul> <li>chương trình thông dụng</li> <li>công việc</li> <li>công việc đều đặn hàng ngày</li> <li>công việc thường làm hàng ngày</li> <li>lề thói đã quen</li> <li>lệ thường</li> <li>quy lệ thường ngày</li> <li>thường lệ</li> <li>thường trình</li> <li>việc làm thường ngày</li></ul><span class=\"tl\">Kỹ thuật</span><ul> <li>biểu đồ</li> <li>chương trình con</li> <li>dãy</li> <li>kế hoạch</li> <li>tiện ích</li></ul><span class=\"tl\">Lĩnh vực: toán &amp; tin</span><ul> <li>đoạn chương trình</li></ul>");
//			v.setPackages(",common,");
// 			v.setCheck(true);
//			v.setL_en("E");
//			v.setL_vn("V");
//			ofy().save().entity(v);
//		}
	}

}
