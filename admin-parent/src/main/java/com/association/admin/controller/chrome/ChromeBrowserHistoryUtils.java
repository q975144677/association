package com.association.admin.controller.chrome;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;
import org.sqlite.SQLiteException;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class ChromeBrowserHistoryUtils {
public static void main(String[] args){
    try {
        new ChromeBrowserHistoryUtils().exportChromeBHToExcel("xls");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
	/**
	 * 将浏览历史导出为Excel文件
	 * @param excelType excel的文件类型
	 * @throws IOException
	 */
	public void exportChromeBHToExcel(String excelType) throws IOException {


		String chromeSqlite = findChromeCacheLocation();
		//找不到谷歌浏览器的的缓存位置
		if(chromeSqlite == null) {
			System.out.println("找不到Chrome浏览器的缓存位置，请在Chrome浏览器中输入chrome://version查案");
		}else{

			Workbook wb = null;
			if("xls".equals(excelType)){
				wb = new HSSFWorkbook();
			}else{
				wb = new XSSFWorkbook();
			}
			Sheet sheetHistory = wb.createSheet("浏览历史");
			sheetHistory.setColumnWidth(0, 256 * 20);
			sheetHistory.setColumnWidth(1, 256 * 20);
			sheetHistory.setColumnWidth(2, 256 * 20);
			sheetHistory.setColumnWidth(3, 256 * 20);
			Sheet sheetData = wb.createSheet("统计");
			sheetData.setColumnWidth(0, 256*20);
			sheetData.setColumnWidth(1, 256*20);
			sheetData.setColumnWidth(2, 256*20);
			Cell cell = null;
			Row row = null;
			row = sheetHistory.createRow(0);
			cell = row.createCell(0);
			cell.setCellStyle(getCommonCellStyle(cell.getCellStyle()));

			cell.setCellValue("标题");
			cell = row.createCell(1);
			cell.setCellValue("url");
			cell = row.createCell(2);
			cell.setCellValue("访问时间");
			cell = row.createCell(3);
			cell.setCellValue("停留时间");

			List<ChromeBrowserHistory> histories = getChromeBrowserHistories(chromeSqlite);
			int count = histories.size();
			for(int i = 1; i <= count;i++){
				ChromeBrowserHistory chromeBrowserHistory = histories.get(i-1);

				row = sheetHistory.createRow(i);

				cell = row.createCell(0);
				cell.setCellValue(chromeBrowserHistory.getTitle());
				cell = row.createCell(1);
				String url = chromeBrowserHistory.getUrl();
				if(url != null  && url.length() > 32767){
					cell.setCellValue(chromeBrowserHistory.getUrl().substring(0, 32766));
				}else{
					cell.setCellValue(chromeBrowserHistory.getUrl());
				}

				cell = row.createCell(2);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
				cell.setCellValue(simpleDateFormat.format(chromeBrowserHistory.getVisitTime()));
				cell = row.createCell(3);
				cell.setCellValue(chromeBrowserHistory.getVisitDuration()/1000000 + "秒");
			}

			row = sheetData.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue("标题");
			cell = row.createCell(1);
			cell.setCellValue("url");
			cell = row.createCell(2);
			cell.setCellValue("访问次数");

			List<ChromeBrowserHistoryData> datas = getChromeChromeBrowserHistoryData(chromeSqlite);
			count = datas.size();
			for(int i = 1; i <= count; i++){
				ChromeBrowserHistoryData chromeBrowserHistoryData = datas.get(i-1);
				row = sheetData.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(chromeBrowserHistoryData.getTitle());
				cell = row.createCell(1);
				cell.setCellValue(chromeBrowserHistoryData.getUrl());
				cell = row.createCell(2);
				cell.setCellValue(chromeBrowserHistoryData.getVisitCount());
			}

			OutputStream outputStream = null;
			if("xls".equals(excelType)){
				outputStream = new FileOutputStream(new File(getFileName() + ".xls"));
			}else{
				outputStream = new FileOutputStream(new File(getFileName() + ".xlsx"));
			}

			wb.write(outputStream);
			outputStream.close();
			wb.close();
		}
	}

	/**
	 * 获取Excel的样式
	 * @param cellStyle
	 * @return
	 */
	private CellStyle getCommonCellStyle(CellStyle cellStyle){
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setWrapText(true);
		return cellStyle;
	}

	/**
	 * 自动寻找谷歌浏览器的默认浏览历史缓存地址
	 * @return 浏览历史缓存地址
	 */
	public String findChromeCacheLocation(){

		String chromeHistoryLocation = null;
		//加载类路径下的配置文件

        InputStream inputStream = ChromeBrowserHistoryUtils.class.getResourceAsStream("/history.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println("加载history.properties配置文件异常,使用默认路径读取");
		}
		if(properties != null){
			chromeHistoryLocation = properties.getProperty("chrome.history.location");
		}
		if(chromeHistoryLocation == null){
			//系统名称
			String osName = System.getProperty("os.name");
			//查找windows下的谷歌浏览器缓存路径
			if(osName.startsWith("Windows")){
				String userName = System.getProperty("user.name");
				chromeHistoryLocation = "C:\\Users\\" + userName +"\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\History";
			}
		}
		System.out.println("Chrome浏览器的浏览历史缓冲路径为:" + chromeHistoryLocation);
		return chromeHistoryLocation;
	}

	/**
	 * 获取随机文件名
	 * @return 文件名
	 */
	private String getFileName(){
		StringBuffer name = new StringBuffer();
		Random random = new Random();
		for(int i = 0; i < 24; i++){
			name.append(((char)('a'+ random.nextInt(25))));
		}
		return name.toString();
	}


	/**
	 * 对Chrome中日期数据进行处理，Chrome默认从1601年1月1日 00:00:00 开始，
	 * 同时时间单位为微妙
	 * @param rawDate 初始化的日期数据
	 * @return 换算成从1970 年 1 月 1 日 00:00:00开始且单位为毫秒的日期格式
	 */
	public long getChromeBrowserLastVisitTime(long rawDate){
		long diff = 0;
		for(int i = 1601; i < 1970; i++){
			//判断是否为闰年
			if((i % 100 != 0 && i % 4 == 0)
					|| (i % 100 == 0 && i % 400 == 0) ){
				diff += (366 * 24 * 60 * 60 );
			}else{
				diff += (365 * 24 * 60 * 60 );
			}
		}
		diff = diff * 1000;
		long date = rawDate / 1000 - diff;
		return date;
	}




	/**
	 * 获取Chrome的浏览历史的统计数据
	 * @param sqlite Chrome的浏览历史缓存数据位置（Sqlite位置）
	 * @return Chrome浏览历史的统计数据集合
	 */
	public List<ChromeBrowserHistoryData> getChromeChromeBrowserHistoryData(String sqlite){
		List<ChromeBrowserHistoryData> histories = new ArrayList<ChromeBrowserHistoryData>();
		Connection connection = getSqliteConnection(sqlite);
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "select url,title,visit_count from urls order by visit_count desc limit 0,100";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			System.out.println("正在读取访问数量前100的浏览历史信息");
			while(resultSet.next()){
				ChromeBrowserHistoryData chromeBrowserHistoryData = new ChromeBrowserHistoryData();
				chromeBrowserHistoryData.setUrl(resultSet.getString("url"));
				chromeBrowserHistoryData.setTitle(resultSet.getString("title"));
				chromeBrowserHistoryData.setVisitCount(resultSet.getInt("visit_count"));
				histories.add(chromeBrowserHistoryData);
			}
		}catch (SQLiteException e) {
			throw new RuntimeException("读取Chrome缓存异常，请先关闭Chrome浏览器");
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}finally{
			release(connection, statement, resultSet);
		}
		System.out.println("读取访问数量前100的浏览历史信息完成");
		return histories;
	}

	/**
	 * 读取Chrome的浏览历史
	 * @param sqlite  Chrome的浏览历史缓存数据位置（Sqlite位置）
	 * @return chrome的浏览历史的集合
	 */
	public List<ChromeBrowserHistory> getChromeBrowserHistories(String sqlite){
		int num = 0;
		Connection connection = getSqliteConnection(sqlite);
		Statement statement = null;
		ResultSet resultSet = null;
		List<ChromeBrowserHistory> histories = new ArrayList<ChromeBrowserHistory>();
		String sqlVisits = "select url,visit_time,visit_duration from visits";
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlVisits);
			System.out.println("正在读取Chrome的浏览历史");
			while(resultSet.next()){
				ChromeBrowserHistory cbh = new ChromeBrowserHistory();
				cbh.setId(resultSet.getInt("url"));
				long time = getChromeBrowserLastVisitTime(resultSet.getLong("visit_time"));
				cbh.setVisitTime(new Date(time));
				long visitDuration = resultSet.getLong("visit_duration");
				cbh.setVisitDuration(visitDuration);
				histories.add(cbh);
			}

			String sqlUrls = "select url,title from urls where id = ";
			for(ChromeBrowserHistory history : histories){
				num ++;
				statement = connection.createStatement();
				resultSet = statement.executeQuery(sqlUrls + history.getId());
				while(resultSet.next()){
					history.setUrl(resultSet.getString("url"));
					history.setTitle(resultSet.getString("title"));
				}
				if(num % 10000 == 0)	System.out.println("已经读取" + num + "条浏览历史记录");
			}
			System.out.println(num + "条浏览历史记录读取完成");
		}catch (Exception e) {
			throw new RuntimeException("读取Chrome缓存异常，请先关闭Chrome浏览器");
		} finally{
			release(connection, statement, resultSet);
		}
		return histories;
	}

	/**
	 * 获取指定Sqlite数据库的连接
	 * @param sqlite
	 * @return
	 */
	private Connection getSqliteConnection(String sqlite){
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqlite);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("缺少Sqlite的驱动程序");
		} catch (SQLException e) {
			throw new RuntimeException("Chrome浏览历史缓存位置异常");
		}
		return connection;
	}

	/**
	 * 释放数据库相关的资源
	 */
	private void release(Connection connection, Statement statement, ResultSet resultSet){
		try {
			if(resultSet != null){
				resultSet.close();
			}
			if(statement != null){
				statement.close();
			}
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}