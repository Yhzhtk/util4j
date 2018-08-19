package yh.util.io;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 写xls文件类
 * @author yh
 * 2011-12-19 14:17
 */
public class WriteXls {

	private static boolean WriteOk = false;

	/**
	 * 判断是否写入成功
	 * @return
	 */
	public static boolean isWriteOk() {
		return WriteOk;
	}

	/**
	 * 设置写文件状态，如果设置为true，则调用writeXlsToSucess方法是会跳出。
	 * @param writeOk
	 */
	public static void setWriteOk(boolean writeOk) {
		WriteOk = writeOk;
	}
	
	/**
	 * 写xls文件，其中列标题为加粗12号，其余的为正常
	 * @param excel 需要写入文件的excel对象
	 */
	public static void writeXls(ExcelBean excel) {
		try {
			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(new File(excel
					.getFileName()));
			/** **********创建工作表************ */
			WritableSheet sheet = workbook.createSheet(excel.getSheetName(), 0);

			// //设置行高
			// sheet.setRowView(0, 600, false);
			// //设置页边距
			// sheet.getSettings().setRightMargin(0.5);
			// //设置页脚
			// sheet.setFooter("", "", "测试页脚");

			//
			// //合并单元格,注意mergeCells(col0,row0,col1,row1) --列从0开始,col1为你要合并到第几列,行也一样
			// //sheet.mergeCells(0, 0, 5, 0);

			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
					WritableFont.BOLD);
			WritableCellFormat boldFormat = new WritableCellFormat(BoldFont);

			boolean colWidth=(excel.getColWidth()!=null&&(excel.getColWidth().length==excel.getColNum()))?true:false;
			
			/** *********设置列宽 和 列名**************** */
			for (int c = 0; c < excel.getColNum(); c++) {
				if(colWidth){
					sheet.setColumnView(0, excel.getColWidth()[c]); // 第i列宽
				}
				sheet.addCell(new Label(c, 0, excel.getColName()[c],
								boldFormat)); // 第i列名
			}

			for (int r = 0; r < excel.getRowNum(); r++) {
				for (int c = 0; c < excel.getColNum(); c++) {
					sheet
							.addCell(new Label(c, r + 1,
									excel.getContent()[r][c]));
				}
			}

			/** **********以上所写的内容都是写在缓存中的，下一句将缓存的内容写到文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			System.out.println("导出Excel文件成功");

			WriteOk = true;
		} catch (Exception e) {
			WriteOk = false;
			e.printStackTrace();
		}
	}

	/**
	 * 以excel的格式写xls文件
	 * @param excel 需要写入文件的excel对象
	 * @param useFormat 是否写格式，true表示按给定格式写入，false则按默认格式
	 */
	public static void writeXls(ExcelBean excel, boolean useFormat) {
		try {
			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(new File(excel
					.getFileName()));
			/** **********创建工作表************ */
			WritableSheet sheet = workbook.createSheet(excel.getSheetName(), 0);
			
			boolean colWidth=(excel.getColWidth()!=null&&(excel.getColWidth().length==excel.getColNum()))?true:false;
			
			/** *********设置列宽 和 列名**************** */
			for (int c = 0; c < excel.getColNum(); c++) {
				if(colWidth){
					sheet.setColumnView(0, excel.getColWidth()[c]); // 第i列宽
				}
				
				if(useFormat){
				sheet.addCell(new Label(c, 0, excel.getColName()[c])); // 第c列名
				}
				else{
					sheet.addCell(new Label(c, 0, excel.getColName()[c],
							excel.getFormats()[excel.getColNameFormat()[c]])); // 第c列名
				}
			}

			if (useFormat) {//使用格式写入数据
				for (int r = 0; r < excel.getRowNum(); r++) {
					for (int c = 0; c < excel.getColNum(); c++) {
						sheet.addCell(new Label(c, r + 1,excel.getContent()[r][c], 
								excel.getFormats()[excel.getContentFormat()[r][c]]));
					}
				}
			} else {
				for (int r = 0; r < excel.getRowNum(); r++) {
					for (int c = 0; c < excel.getColNum(); c++) {
						sheet.addCell(new Label(c, r + 1,
								excel.getContent()[r][c]));
					}
				}
			}

			/** **********以上所写的内容都是写在缓存中的，下一句将缓存的内容写到文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			System.out.println("导出Excel文件成功");

			WriteOk = true;
		} catch (Exception e) {
			WriteOk = false;
			e.printStackTrace();
		}
	}

	/**
	 * 防止文件写入被占用而采取的一种方法，如果文件被占用，会提示，延时5s后再写一次，当文件占用关闭再写入，知道写xls成功。
	 * @param excel 需要写入文件的excel对象
	 */
	public static void writeXlsToSucess(ExcelBean excel,boolean useFormat) {
		try {
			if(useFormat){
					writeXls(excel,true);
			}
			else{
				writeXls(excel);
			}
		} catch (Exception e) {
		} finally {
			if (!WriteXls.WriteOk) {// 保存错误，重复导出直到正确
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				writeXlsToSucess(excel,useFormat);
			}
		}
	}
}
