package com.model2.mvc.service.product.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
		// TODO Auto-generated constructor stub
	}
	public ProductVO findProduct(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();
		System.out.println("findProduct 시작==========");
		String sql = "select * from PRODUCT where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		System.out.println("findProduct sql:"+sql);
		ResultSet rs = stmt.executeQuery();
		
		ProductVO productVO = null;
		while(rs.next()) {
			productVO = new ProductVO();
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
		}
		con.close();
		
		System.out.println("findProduct 끝==========");
		return productVO;
		
	
	}
	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception{
	
		Connection con = DBUtil.getConnection();
		System.out.println("getProductList 시작====!!!" + searchVO.getSearchKeyword());
		String sql = "select * from PRODUCT ";
		if(searchVO.getSearchCondition() != null) {
			if(searchVO.getSearchCondition().equals("0")) {
				sql += "where PROD_NO LIKE "+searchVO.getSearchKeyword();
			}else if(searchVO.getSearchCondition().equals("1")) {
				sql += "where PROD_NAME LIKE '%"+searchVO.getSearchKeyword()+"%'";
			}else if(searchVO.getSearchCondition().equals("2")){
				sql += "where PRICE LIKE "+searchVO.getSearchKeyword();
			}
		}
		sql +=" order by PROD_NO";
		
		PreparedStatement stmt = 
				con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		
		ResultSet rs = stmt.executeQuery();
		System.out.println("getProductList sql:" +sql);
		rs.last();
		int total = rs.getRow();
		System.out.println("로우의 수:" + total);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(total));
		
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() +1);
		System.out.println("searchVO.getPage():" +searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());
		
		List<ProductVO> list = new ArrayList<ProductVO>();
		if(total > 0) {
			for(int i =0; i < searchVO.getPageUnit(); i++) {
				ProductVO vo = new ProductVO();
				vo.setProdNo(rs.getInt("PROD_NO"));
				vo.setProdName(rs.getString("PROD_NAME"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				
				list.add(vo);
				if(!rs.next())
					break;
			}
		}
		System.out.println("list.size() :" +list.size());
		map.put("list", list);
		System.out.println("map().size() : " +map.size());
		
		con.close();
			
		System.out.println("getProductList 끝====");

		
		return map;
	}
	
	public void insertProduct(ProductVO productVO)throws Exception{
		System.out.println("insertProduct 시작=======");
		Connection con= DBUtil.getConnection();
		
		String sql = "insert into PRODUCT values(seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		System.out.println("sql시작");
		stmt.setString(1, productVO.getProdName());
		System.out.println("sql이름");
		
		stmt.setString(2, productVO.getProdDetail());
		System.out.println("sql상세");
		
		stmt.setString(3, productVO.getManuDate());
		System.out.println("sql제조일자");
		
		stmt.setInt(4,productVO.getPrice());
		System.out.println("sql가격");
		
		stmt.setString(5, productVO.getFileName());
		System.out.println("sql이미지");
		
		stmt.executeUpdate();
		
		System.out.println("insertProduct sql:"+sql);
		System.out.println("insertProduct 끝=======");
		
		con.close();
	}
	
	public void updateProduct(ProductVO productVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		System.out.println("updateProduct 시작======");
		String sql = 
				"update PRODUCT set PROD_NAME=?, PROD_DETAIL=?, MANUFACTURE_DAY=?, PRICE=?, IMAGE_FILE=? where PROD_NO=?";
		
		System.out.println("updateProduct sql:"+sql);
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.setInt(6, productVO.getProdNo());
		System.out.println("DAO안나요면"+productVO);
		System.out.println("updateProduct 끝======");
		con.close();
	}

}
