package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("UpdateProductActuin prodNo시작확인====");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("UpdateProductAction 시작=======");
		System.out.println("UpdateProductActuin prodNo확인::"+prodNo);
		
		ProductVO productVO = new ProductVO();
		
		
		productVO.setProdNo(prodNo);
		productVO.setProdName(request.getParameter("prodName"));
		productVO.setProdDetail(request.getParameter("prodDetail"));
		productVO.setManuDate(request.getParameter("manuDate"));
		int price = Integer.parseInt(request.getParameter("price"));
		productVO.setPrice(price);
		productVO.setFileName(request.getParameter("fileName"));
		
		System.out.println("updateProductAction VO"+productVO);
		ProductService service = new ProductServiceImpl();
		service.updateProduct(productVO);
		
		System.out.println("111");
		/* HttpSession session = request.getSession(); */
		//System.out.println("1111:"+(ProductVO)request.getAttribute("prodVO"));
		//String sessionNo =Integer.toString(((ProductVO)request.getAttribute("prodVO")).getProdNo());
		
		System.out.println("222222");
		/*if(sessionNo.equals(prodNo)){*/
			request.setAttribute("vo", productVO);
		//}
		
		System.out.println("UpdateProductAction 끝=======");
		
		
		return "forward:/product/getProduct.jsp";
	}

}
