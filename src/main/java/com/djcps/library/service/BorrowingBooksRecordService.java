package com.djcps.library.service;

import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.vo.PageVo;

/**
 * @author djsxs
 *
 */
public interface BorrowingBooksRecordService {

	/**根据页码查询所有借书记录
	 * @param pageNum
	 * @return
	 */
	PageVo selectAllBorrowingRecord(int pageNum);
	
	BorrowingBooks getBorrowMsgBybookId(Integer bookId,Integer userId);

}
