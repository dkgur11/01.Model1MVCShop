package com.model2.mvc.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.util.HttpUtil;


public class ActionServlet extends HttpServlet {		//controller
	
	private RequestMapping mapper;

	@Override
	public void init() throws ServletException {			
		super.init();
		String resources=getServletConfig().getInitParameter("resources");	//servletConfig는 web.xml에 있는 servlet 태그안에 정보를 추출하는 메서드 .getInitParameter은 web.xml 에 적어논 정보에서 인자값으로 받은걸 가져옴
		mapper=RequestMapping.getInstance(resources); //RequestMapping의  getInatance메소드에 위의 리소스를 인자로 넘겨주고 Init 메소드 실행유무혹인하여 저장뒤.RequestMapping 의 객체인 Mapper에 저장해줌
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
																									throws ServletException, IOException {
		
		String url = request.getRequestURI();				//프로젝트+파일경로 가져옴    /edu/logon.do
		String contextPath = request.getContextPath();		//프로젝트path만 가져옴		/edu
		String path = url.substring(contextPath.length());	//		/login.do	url(프로젝트+파일경로).contextPath(프로젝트 path만가져온)의 길이만큼만 파싱해서 path에 준다
		System.out.println(path);
		
		try{															//인사부장한테  위에서 파싱한 Action을 찾아라!
			Action action = mapper.getAction(path); //Action클래스의 객체를 만들어 준뒤,RequestMapping 클래스의 객체인 mapper로 getAction메소드를 path넘겨주고 실행
			action.setServletContext(getServletContext());	//getServletContext()는 클래스의 주소값을 가져오지만 아무것도 쓰지않고 그 자체만 사용한다면 web.xml에 설정해둔 값을 설정?수행?해주는 역할
			/*
			ServletConfig는 해당 서블릿에서만 사용할 수 있지만 Web App 내에서 공통적인 내용을 가져다 사용하려면 ServletContext를 사용할 수 있다.
			ServletContext는 ServletConfig와 마찬가지로 web.xml을 사용하며, 따라서 바로 사용하려면 String만 사용할 수 있다.
			ervlet 커뮤니케이션를 위한 Method 집합을 의미합니다. MIME Type 정보를 얻거나, 요청을 제공받거나, Log파일을 기록하는 것을 예로 들 수 있습니다.
			모든 Servlet에서 하나의 객체를 공유할 때 사용,모든 Servlet들의 ServletConfig를 포함
			DB를 사용하는 서블릿들이 호출될때마다 DB 커넥션을 생성하지 않고, 웹 어플리케이션이 시작될 때 생성해서 ServletContext에 저장하면 필요한 서블릿에서 커넥션 인스턴스를 꺼내 쓸 수 있다.
			*/
			
			
			String resultPage=action.execute(request, response);
			String result=resultPage.substring(resultPage.indexOf(":")+1);
			System.out.println(result);
			if(resultPage.startsWith("forward:"))
				HttpUtil.forward(request, response, result);
			else
				HttpUtil.redirect(response, result);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}