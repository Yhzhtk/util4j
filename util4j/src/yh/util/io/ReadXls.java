package yh.util.io;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 读取xls文件，返回ExcelBean
 * 
 * @author yh
 * 2011-12-19 18:34
 */
public class ReadXls {

	/**
	 * 获取xls内容，返回ExcelBean对象
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static ExcelBean getXlsBeans(String fileName) {

		ExcelBean bean = new ExcelBean();

		Workbook workbook = null;
		try {
			try {
				workbook = Workbook.getWorkbook(new File(fileName));
			} catch (BiffException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		int cols = sheet.getColumns();

		// 格式处理
		// List<CellFormat> formats=new ArrayList<CellFormat>();

		String[] colName = new String[cols];// 列名称
		// int[] colNameFormat=new int[cols];//列名对应于格式集的编号
		for (int i = 0; i < cols; i++) {
			colName[i] = sheet.getCell(i, 0).getContents();
			// colNameFormat[i]=addColNameFormat(formats,sheet.getCell(i,0).getCellFormat());
		}
		bean.setColName(colName);// 设置列名
		// bean.setColNameFormat(colNameFormat);

		String content[][] = new String[rows - 1][cols];// 第一行归为名称行
		// int[][] contentFormat=new int[rows-1][cols];//内容对应于格式集的编号
		for (int r = 1; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				content[r - 1][c] = sheet.getCell(c, r).getContents();// 内容
				// contentFormat[r-1][c]=addColNameFormat(formats,sheet.getCell(c,r).getCellFormat());//格式
			}
		}
		bean.setContent(content);
		// bean.setContentFormat(contentFormat);

		/*
		 * 设置格式集 WritableCellFormat[] wFormat=new WritableCellFormat[formats.size()]; for(int i=0;i<wFormat.length;i++
		 * ){ wFormat[i]=(WritableCellFormat) formats.get(i); } bean.setFormats(wFormat);
		 */

		workbook.close();

		return bean;
	}

	/**
	 * 添加单元格格式
	 * 
	 * @param formats
	 *            格式集
	 * @param cellFormat
	 *            当前格式
	 * @return 当前格式所在的编号
	 * 
	 *         public static int addColNameFormat(List<CellFormat> formats,CellFormat cellFormat){ int
	 *         num=formats.size(); for(int i=0;i<num;i++){ if(formats.get(i).equals(cellFormat)) return i; }
	 *         formats.add(cellFormat); return num; }
	 */
}