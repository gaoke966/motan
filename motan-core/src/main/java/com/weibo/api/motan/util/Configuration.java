package com.weibo.api.motan.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 操作properties文件
 * 
 * @author Administrator
 * 
 */
public class Configuration {

	private Properties propertie;
	private String path;
	private FileInputStream inputFile;
	private FileOutputStream outputFile;

	/**
	 * 构造器 用途：新建一个properties文件 调用setValue（key，value）添加节点 调用saveAsFile（路径，描述）方法保存
	 */
	public Configuration() {
		propertie = new Properties();
	}

	/**
	 * 构造器（输入路径）
	 * 
	 * @param filePath
	 *            properties文件路径 调用setValue（key，value）添加节点 调用getValue（key）获取节点的值
	 *            调用clear（）方法清空properties文件 调用saveFile（）保存
	 */
	public Configuration(String filePath) {
		path = filePath;
		propertie = new Properties();
		try {
			inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
		} catch (FileNotFoundException ex) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("装载文件--->失败!");
			ex.printStackTrace();
		}
	}

	/**
	 * 通过key值获取value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		if (propertie.containsKey(key)) {
			String value = propertie.getProperty(key);// 得到某一属性的值
			return value;
		} else
			return "";
	}

	/**
	 * 清空properties文件
	 * 
	 */
	public void clear() {
		propertie.clear();
	}

	/**
	 * 输入key和value进行添加节点 ------构造器（输入路径）实例化时候 调用
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		propertie.setProperty(key, value);
	}

	/**
	 * 保存properties文件
	 * 
	 * @param description
	 */
	public void saveFile(String description) {
		try {
			outputFile = new FileOutputStream(path);
			propertie.store(outputFile, description);
			outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 另存为properties文件
	 * 
	 * @param fileName
	 * @param description
	 */
	public void saveAsFile(String fileName, String description) {
		try {
			outputFile = new FileOutputStream(fileName);
			propertie.store(outputFile, description);
			outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public Properties getProperties() {
		return propertie;
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration rc = new Configuration("d://test.properties");

		// 测试从文件读取
		String databaseName = rc.getValue("databaseName");
		String dbUserName = rc.getValue("dbUserName");
		String dbPort = rc.getValue("dbPort");
		String dbPassword = rc.getValue("dbPassword");

		System.out.println("databaseName = " + databaseName);
		System.out.println("dbUserName = " + dbUserName);
		System.out.println("dbPort = " + dbPort);
		System.out.println("dbPassword = " + dbPassword);

		// 测试保存saveFile
		rc.setValue("test", "000000");
		rc.saveFile("saveFile");

		// 测试另存为saveAsFile
		Configuration cf = new Configuration();
		cf.setValue("min", "0");
		cf.setValue("max", "99999");
		cf.saveAsFile("d://testSaveAsFile.properties", "saveAsFile");

	}
}