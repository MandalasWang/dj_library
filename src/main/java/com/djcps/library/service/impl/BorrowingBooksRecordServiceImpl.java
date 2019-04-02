package com.djcps.library.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.djcps.library.mapper.BookMapper;
import com.djcps.library.mapper.BorrowingBooksMapper;
import com.djcps.library.model.BorrowingBooks;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.BorrowingBooksRecordService;

/**
 * @author djsxs
 *
 */
@Service("borrowingBooksRecordService")
public class BorrowingBooksRecordServiceImpl implements BorrowingBooksRecordService {
	@Resource
	private BookMapper bookMapper;

	@Resource
	private BorrowingBooksMapper borrowingBooksMapper;

	@Override
	public PageVo selectAllBorrowingRecord(int pageNum) {
		int borrowingTotalCounts = borrowingBooksMapper.getBorrowingTotalCount();
		PageVo pageVo = new PageVo();
		int pageSize = 10;
		int pageIndex = 0;
		int totalPage = 0;
		if (0 == pageNum) {
			pageIndex = 1;
			pageSize = 10;
		} else {
			pageIndex = (pageNum - 1) * pageSize;
			pageSize = pageNum * 10;
		}
		if (borrowingTotalCounts > 0) {
			totalPage = (borrowingTotalCounts - 1) / 10 + 1;
		}
		pageVo.setPageIndex(pageIndex);
		pageVo.setTotalPage(totalPage);
		pageVo.setPageSize(pageSize);
		List<BorrowingBooks> list = borrowingBooksMapper.selectAllByCondition(pageIndex, pageSize);
		pageVo.setBorrowingBookslist(list);
		return pageVo;
	}

	@Override
	public BorrowingBooks getBorrowMsgBybookId(Integer bookId,Integer userId) {
		int isreturn = 0;
		return borrowingBooksMapper.getBorrowMsgBybookIdAndUserId(bookId,userId,isreturn);
	}

}
