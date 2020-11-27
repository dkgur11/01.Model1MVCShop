package com.model2.mvc.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {			//인사부장!!!  
	
	private static RequestMapping requestMapping;
	private Map<String, Action> map;
	private Properties properties;
	
	private RequestMapping(String resources) {
		map = new HashMap<String, Action>();		//key값에 String   value값은 Action
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);	//properties 파일이랑 연결
			properties = new Properties();
			properties.load(in);		//연결만한 in을 넣어줘서 읽어온다
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties 파일 로딩 실패 :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	public synchronized static RequestMapping getInstance(String resources){		//인사부장은 한명만 필요하다 한명을 뽑는
		if(requestMapping == null){						//한번 만들어졌으면 null이 아니니까 
			requestMapping = new RequestMapping(resources);			//처음 만들었으면 인스턴스 (인사부장 생성)
		}
		return requestMapping;		//만들어진 인사부장을 리턴
	}
	
	public Action getAction(String path){
		Action action = map.get(path);		//Action클래스를 만들어주고 ,Map겍체인 map에서 이름이 path 인  값을 가져온뒤에?
		if(action == null){
			String className = properties.getProperty(path);		//.getProperty(path)은 key(path)의 value값을 가져와서 인스턴스생성해서 className에 담는다
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim();
			try{										//className (value)값을 받아 객체로 생성해 리턴
				Class c = Class.forName(className);		// Class.forName() 클래스를 가져온다는것이며, 파일명이 아닌 패키지까지 써줘야하는게 맞다 여기서는 properties에 key/value값으로 경로가 다있기떄문에 'className'로 써줌
				Object obj = c.newInstance();	// 동적으로 넘어오는 인자에 따라 다른객체 생성가능 ,Object obj는 Class.forName(className)
	            /*
				* 어떨때 이렇게 쓰는 경우라 함은 대개
	               A a = new A(); 로 했을 경우 오브젝트가 모두 메모리상에 올라가게 되죠
	               	만일 ArrayList<A> list = new ArrayList<A>(10000); 라고 10000 개를 했을 경우,
	               A클래스의의 모든 변수가 메모리상에 다 올라가게 되겠죠? 그럼 메모리 부하도 심해지겟죠?
	               	만약 클래스를 하나만 생성해놓고 필요한 값만 set 하게되면 오브젝트가 메모리상에 하나만 적재되서
	               	메모리를 덜 먹도록 해야할때, 쓰는 방법입니다
	             */
				
				if(obj instanceof Action){		//instanceof는 상위 객체인지 확인(형변환이 가능한가?)
					map.put(path, (Action)obj);
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class형변환시 오류 발생  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action정보를 구하는 도중 오류 발생 : " + ex);
			}
		}
		return action;		//Action으로 캐스팅된 obj를 리턴해준다
	}
}