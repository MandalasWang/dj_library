package com.djcps.library.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
import com.djcps.library.model.vo.PageVo;
import com.djcps.library.service.BorrowingBooksRecordService;

import javax.annotation.Resource;

/**
 * @author djsxs
 *
 */
@RequestMapping("/borrowing")
@RestController
public class BorrowingController {

	@Resource
	@Qualifier("borrowingBooksRecordService")
	private BorrowingBooksRecordService borrowingBooksRecordService;

	/**
	 * 返回所有用户借书记录页面
	 * 
	 * @return
	 */
	@RequestMapping("/allBorrowBooksRecordPage")
	public RetResult<PageVo> allBorrowingBooksRecordPage(@RequestParam("pageNum") int pageNum) {
		PageVo pVo = borrowingBooksRecordService.selectAllBorrowingRecord(pageNum);
		return RetResponse.makeOKRsp(pVo);
	}
	
	
}
