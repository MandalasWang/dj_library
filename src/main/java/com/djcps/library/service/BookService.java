package com.djcps.library.service;

import java.util.List;

import com.djcps.library.model.Book;
import com.djcps.library.model.vo.BookVo;
import com.djcps.library.model.vo.PageVo;

/**
 * @author djsxs
 *
 */
public interface BookService {


	/**书籍集合
	 * @return
	 */
	List<Book> listBook();

	/**根据页码查询所有书籍
	 * @param pageNum
	 * @return
	 */
	PageVo selectAllBook(int pageNum);
	
	
	/**根据上架时间查询书籍
	 * @param pageNum
	 * @return
	 */
	PageVo findBookByTheOnsaleDate(int pageNum);
	
	/**查找最近几天上架书籍
	 * @return
	 */
	List<Book> findBookByOnRecently();
	
	/**查询热门书籍
	 * @param pageNum
	 * @return
	 */
	PageVo findHotBook(int pageNum);
	
	/**条件查询书籍
	 * @param bookCondition
	 * @param pageNum
	 * @return
	 */
	PageVo getBookListBybookCondition(Book bookCondition,int pageNum);
	
	/**根据条形码查找书籍信息
	 * @param barCode
	 * @return
	 */
	Book findBookByBarCode(String barCode);
	
	/**根据书籍id获取被借书籍信息
	 * @param bookId
	 * @return
	 */
	BookVo getBorrowedMsg(Integer bookId,Integer isreturn);

	BookVo getBorrowedMsgAndroid(String barCode, Integer isreturn);

}
