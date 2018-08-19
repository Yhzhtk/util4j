package yh.util.io;

import jxl.write.WritableCellFormat;

/**
 * 存储xls对象,必须要初始化fileName,colName和content三个值才能用WriteXls写入文件
 * @author yh
 * 2011-11-19 14:26
 */
public class ExcelBean {

	private String fileName;//xls文件名
	private String []colName;//列名集
	private String [][]content;//所有内容二位数字，一维是行，二维是列
	
	private String sheetName="sheet1";//sheet名
	private int[] colWidth;//列宽集
	private int colNum=0;//列数
	private int rowNum=0;//行数
	private WritableCellFormat[] formats;//格式集
	private int[][] contentFormat;//内容对应于格式集的编号
	private int[] colNameFormat;//列名对应于格式集的编号
	
	public ExcelBean() {
		super();
	}

	public ExcelBean(String fileName, String sheetName, String[] colName,
			int[] colWidth, String[][] content, int colNum, int rowNum,
			WritableCellFormat[] formats, int[][] contentFormat,
			int[] colNameFormat) {
		super();
		this.fileName = fileName;
		this.sheetName = sheetName;
		this.colName = colName;
		this.colWidth = colWidth;
		this.content = content;
		this.colNum = colNum;
		this.rowNum = rowNum;
		this.formats = formats;
		this.contentFormat = contentFormat;
		this.colNameFormat = colNameFormat;
	}

	/**
	 * 获取内容格式编号
	 */
	public int[][] getContentFormat() {
		return contentFormat;
	}

	/**
	 * 设置内容格式编号
	 */
	public void setContentFormat(int[][] contentFormat) {
		this.contentFormat = contentFormat;
	}

	/**
	 * 获取列名格式编号
	 */
	public int[] getColNameFormat() {
		return colNameFormat;
	}

	/**
	 * 设置列名格式编号
	 */
	public void setColNameFormat(int[] colNameFormat) {
		this.colNameFormat = colNameFormat;
	}

	/**
	 * 获得表格格式
	 */
	public WritableCellFormat[] getFormats() {
		return formats;
	}

	/**
	 * 设置表格格式，示例如下
	 * WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
     * WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 14,WritableFont.BOLD);   
     * WritableCellFormat wcf_table = new WritableCellFormat(NormalFont);  
     * wcf_table.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
     * wcf_table.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐  
     * wcf_table.setAlignment(Alignment.CENTRE);//对齐方式  
     * wcf_table.setBackground(Colour.GRAY_25);//背景颜色  
     * wcf_table.setWrap(true); // 是否换行  
	 */
	public void setFormats(WritableCellFormat[] formats) {
		this.formats = formats;
	}

	/**
	 * 获取列宽集
	 */
	public int[] getColWidth() {
		return colWidth;
	}

	/**
	 * 设置列宽集
	 */
	public void setColWidth(int[] colWidth) {
		this.colWidth = colWidth;
	}
	
	/**
	 * 获取文件名
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取列数
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * 获取行数，除列标题行
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * 获取表名
	 */
	public String getSheetName() {
		return sheetName;
	}
	/**
	 * 设置表名
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	/**
	 * 获取内容
	 */
	public String[][] getContent() {
		return content;
	}
	/**
	 * 设置内容
	 */
	public void setContent(String[][] content) {
		this.content = content;
		this.rowNum=content.length;
		if(this.rowNum>0){
			this.colNum=content[0].length;
		}
	}
	/**
	 * 获取列名集
	 */
	public String[] getColName() {
		return colName;
	}
	/**
	 * 设置列名集
	 */
	public void setColName(String[] colName) {
		this.colName = colName;
	}
	
}
