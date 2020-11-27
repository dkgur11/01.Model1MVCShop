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
		String resources=getServletConfig().getInitParameter("resources");	//servletConfig�� web.xml�� �ִ� servlet �±׾ȿ� ������ �����ϴ� �޼��� .getInitParameter�� web.xml �� ����� �������� ���ڰ����� ������ ������
		mapper=RequestMapping.getInstance(resources); //RequestMapping��  getInatance�޼ҵ忡 ���� ���ҽ��� ���ڷ� �Ѱ��ְ� Init �޼ҵ� ��������Ȥ���Ͽ� �����.RequestMapping �� ��ü�� Mapper�� ��������
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
																									throws ServletException, IOException {
		
		String url = request.getRequestURI();				//������Ʈ+���ϰ�� ������    /edu/logon.do
		String contextPath = request.getContextPath();		//������Ʈpath�� ������		/edu
		String path = url.substring(contextPath.length());	//		/login.do	url(������Ʈ+���ϰ��).contextPath(������Ʈ path��������)�� ���̸�ŭ�� �Ľ��ؼ� path�� �ش�
		System.out.println(path);
		
		try{															//�λ��������  ������ �Ľ��� Action�� ã�ƶ�!
			Action action = mapper.getAction(path); //ActionŬ������ ��ü�� ����� �ص�,RequestMapping Ŭ������ ��ü�� mapper�� getAction�޼ҵ带 path�Ѱ��ְ� ����
			action.setServletContext(getServletContext());	//getServletContext()�� Ŭ������ �ּҰ��� ���������� �ƹ��͵� �����ʰ� �� ��ü�� ����Ѵٸ� web.xml�� �����ص� ���� ����?����?���ִ� ����
			/*
			ServletConfig�� �ش� ���������� ����� �� ������ Web App ������ �������� ������ ������ ����Ϸ��� ServletContext�� ����� �� �ִ�.
			ServletContext�� ServletConfig�� ���������� web.xml�� ����ϸ�, ���� �ٷ� ����Ϸ��� String�� ����� �� �ִ�.
			ervlet Ŀ�´����̼Ǹ� ���� Method ������ �ǹ��մϴ�. MIME Type ������ ��ų�, ��û�� �����ްų�, Log������ ����ϴ� ���� ���� �� �� �ֽ��ϴ�.
			��� Servlet���� �ϳ��� ��ü�� ������ �� ���,��� Servlet���� ServletConfig�� ����
			DB�� ����ϴ� �������� ȣ��ɶ����� DB Ŀ�ؼ��� �������� �ʰ�, �� ���ø����̼��� ���۵� �� �����ؼ� ServletContext�� �����ϸ� �ʿ��� �������� Ŀ�ؼ� �ν��Ͻ��� ���� �� �� �ִ�.
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