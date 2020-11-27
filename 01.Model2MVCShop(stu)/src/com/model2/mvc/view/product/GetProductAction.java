package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {


	@Override
	public String execute(HttpServletRequest request, 
											HttpServletResponse response) throws Exception {
		System.out.println("GetProductActionΩ√¿€===");
		int prodNo =Integer.parseInt( request.getParameter("prodNo"));
		String me = (String)request.getParameter("menu");
		System.out.println("GetProductAction menu?::"+me);
		
		ProductService service = new ProductServiceImpl();
		ProductVO vo = service.getProduct(prodNo);
		
		HttpSession session = request.getSession(); 
		session.setAttribute("vo", vo);
		
		System.out.println("GetProductAction≥° =====");
		
		if(me.equals("manage")) {
			return "forward:/updateProductView.do?prodNo="+prodNo+"&menu="+me;
		}else{
			return "redirect:/product/getProduct.jsp";
		}
	
		
	}

}