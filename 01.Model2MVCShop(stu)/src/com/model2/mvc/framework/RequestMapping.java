package com.model2.mvc.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {			//�λ����!!!  
	
	private static RequestMapping requestMapping;
	private Map<String, Action> map;
	private Properties properties;
	
	private RequestMapping(String resources) {
		map = new HashMap<String, Action>();		//key���� String   value���� Action
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);	//properties �����̶� ����
			properties = new Properties();
			properties.load(in);		//���Ḹ�� in�� �־��༭ �о�´�
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties ���� �ε� ���� :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	public synchronized static RequestMapping getInstance(String resources){		//�λ������ �Ѹ� �ʿ��ϴ� �Ѹ��� �̴�
		if(requestMapping == null){						//�ѹ� ����������� null�� �ƴϴϱ� 
			requestMapping = new RequestMapping(resources);			//ó�� ��������� �ν��Ͻ� (�λ���� ����)
		}
		return requestMapping;		//������� �λ������ ����
	}
	
	public Action getAction(String path){
		Action action = map.get(path);		//ActionŬ������ ������ְ� ,Map��ü�� map���� �̸��� path ��  ���� �����µڿ�?
		if(action == null){
			String className = properties.getProperty(path);		//.getProperty(path)�� key(path)�� value���� �����ͼ� �ν��Ͻ������ؼ� className�� ��´�
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim();
			try{										//className (value)���� �޾� ��ü�� ������ ����
				Class c = Class.forName(className);		// Class.forName() Ŭ������ �����´ٴ°��̸�, ���ϸ��� �ƴ� ��Ű������ ������ϴ°� �´� ���⼭�� properties�� key/value������ ��ΰ� ���ֱ⋚���� 'className'�� ����
				Object obj = c.newInstance();	// �������� �Ѿ���� ���ڿ� ���� �ٸ���ü �������� ,Object obj�� Class.forName(className)
	            /*
				* ��� �̷��� ���� ���� ���� �밳
	               A a = new A(); �� ���� ��� ������Ʈ�� ��� �޸𸮻� �ö󰡰� ����
	               	���� ArrayList<A> list = new ArrayList<A>(10000); ��� 10000 ���� ���� ���,
	               AŬ�������� ��� ������ �޸𸮻� �� �ö󰡰� �ǰ���? �׷� �޸� ���ϵ� ����������?
	               	���� Ŭ������ �ϳ��� �����س��� �ʿ��� ���� set �ϰԵǸ� ������Ʈ�� �޸𸮻� �ϳ��� ����Ǽ�
	               	�޸𸮸� �� �Ե��� �ؾ��Ҷ�, ���� ����Դϴ�
	             */
				
				if(obj instanceof Action){		//instanceof�� ���� ��ü���� Ȯ��(����ȯ�� �����Ѱ�?)
					map.put(path, (Action)obj);
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class����ȯ�� ���� �߻�  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action������ ���ϴ� ���� ���� �߻� : " + ex);
			}
		}
		return action;		//Action���� ĳ���õ� obj�� �������ش�
	}
}