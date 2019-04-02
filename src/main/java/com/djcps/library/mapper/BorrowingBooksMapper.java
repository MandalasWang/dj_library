package com.djcps.library.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.djcps.library.model.BorrowingBooks;

/**
 * @author djsxs
 *
 */
public interface BorrowingBooksMapper {
    
    /**按条件查询所有借书记录并分页显示
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<BorrowingBooks> selectAllByCondition(@Param("pageIndex")int pageIndex, 
    		                      @Param("pageSize")int pageSize);

	/**借书
	 * @param books
	 * @return
	 */
	int borrowBook(BorrowingBooks books);

	/**还书
	 * @param i
	 * @param brrowingbookid
	 * @return
	 */
	int retunBook(@Param(value="isreturn")int isreturn,
			@Param(value="brrowingbookid")Integer brrowingbookid);

	/**获取所有借书记录总数
	 * @return
	 */
	int getBorrowingTotalCount();

	/**根据id获取借书记录信息
	 * @param id
	 * @return
	 */
	BorrowingBooks getBorrowMsgByid(@Param("id")Integer id);

	/**根据bookId更新最新借书日期
	 * @param books
	 * @return
	 */
	int updatelastdateByid(BorrowingBooks books);
	
	/**根据bookId获取借书信息
	 * @param bookId
	 * @return
	 */
	BorrowingBooks getBorrowMsgBybookId(@Param("bookId")Integer bookId,@Param(value="isreturn")Integer isreturn);
	
	
	/**通过userId获取user的借书信息条数
	 * @param userId
	 * @return
	 */
	int getBorrowingBookTotalCountsByUserId(@Param("userId")Integer userId);
	

	/**通过userId获取user的借书信息详情
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List<BorrowingBooks> getBorrowingBookMsgByUserId(@Param("userId")Integer userId,@Param("pageIndex")int pageIndex, 
            @Param("pageSize")int pageSize);

	/**通过bookId获取借书人信息
	 * @param bookId
	 * @return
	 */
	BorrowingBooks getBorrowRecordByBookId(@Param("bookId")Integer bookId);

	/**根据书籍是否归还删除借书信息
	 * @return
	 */
	int delBorrowingRecordByisreturn();
	
	int updateReturnDateByid(@Param("brrowingbookid")Integer brrowingbookid,@Param("returnDate")Date returnDate);

	/**根据bookId和userId获取借书信息
	 * @param bookId
	 * @return
	 */
	BorrowingBooks getBorrowMsgBybookIdAndUserId(@Param("bookId")Integer bookId,@Param(value="userId")Integer userId,@Param(value="isreturn")Integer isreturn);

	int deleteBorrowingBooks(@Param("bookId")Integer bookId);
}