/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package vip.zhouxin.ureport.core;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.config.UreportProperties;
import vip.zhouxin.ureport.core.definition.datasource.BuildinDatasource;
import vip.zhouxin.ureport.core.definition.datasource.GenerateBuildinDatasource;
import vip.zhouxin.ureport.core.exception.ConvertException;
import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.model.Report;
import vip.zhouxin.ureport.core.provider.image.ImageProvider;
import vip.zhouxin.ureport.core.utils.DateUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Jacky.gao
 * @since 2016年11月12日
 */
public class Utils implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	private static Collection<BuildinDatasource> buildinDatasources;
	private static Collection<ImageProvider> imageProviders;
	private static boolean debug;

	public Utils() {
	}

	public Utils(boolean debug) {
		Utils.debug = debug;
	}



	public static boolean isDebug() {
		return Utils.debug;
	}
	
	public static void logToConsole(String msg){
		if(Utils.debug){
			System.out.println(msg);
		}
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static Collection<BuildinDatasource> getBuildinDatasources() {
		if (buildinDatasources == null){
			buildinDatasources= new ArrayList<>();
			Collection<GenerateBuildinDatasource> datasources = Optional.of(applicationContext.getBeansOfType(GenerateBuildinDatasource.class)).map(Map::values).orElse(Collections.emptyList());
			datasources.forEach(e-> buildinDatasources.addAll(e.generate()));
			UreportProperties properties = applicationContext.getBean(UreportProperties.class);
			Optional.of(properties).map(UreportProperties::getDataSources).orElse(Collections.emptyList()).forEach(e->{
				DruidDataSource druidDataSource;
				druidDataSource = new DruidDataSource();
				druidDataSource.setUrl(e.getJdbcUrl());
				druidDataSource.setUsername(e.getUsername());
				druidDataSource.setPassword(e.getPassword());
				druidDataSource.setDriverClassName(e.getDriverName());
				buildinDatasources.add(new BuildinDatasource() {
					@Override
					public String name() {
						return e.getTitle();
					}

					@Override
					public Connection getConnection() {
						try {
							return druidDataSource.getConnection();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
			});
		}
		return buildinDatasources;
	}

	public static Collection<BuildinDatasource> resetBuildinDatasources(){
		buildinDatasources = null;
		return getBuildinDatasources();
	}
	
	public static Collection<ImageProvider> getImageProviders() {
		return imageProviders;
	}

	
	public static Connection getBuildinConnection(String name){
		for(BuildinDatasource datasource:buildinDatasources){
			if(name.equals(datasource.name())){
				return datasource.getConnection();
			}
		}
		return null;
	}
	
	public static List<Cell> fetchTargetCells(Cell cell, Context context, String cellName){
		while(!context.isCellPocessed(cellName)){
			context.getReportBuilder().buildCell(context, null);
		}
		List<Cell> leftCells=fetchCellsByLeftParent(context,cell, cellName);
		List<Cell> topCells=fetchCellsByTopParent(context,cell, cellName);
		if(leftCells!=null && topCells!=null){
			int leftSize=leftCells.size(),topSize=topCells.size();
			if(leftSize==1 || topSize==0){
				return leftCells;
			}
			if(topSize==1 || leftSize==0){
				return topCells;
			}
			if(leftSize==0 && topSize==0){
				return new ArrayList<Cell>();
			}
			List<Cell> list=new ArrayList<Cell>();
			if(leftSize<=topSize){
				for(Cell c:leftCells){
					if(topCells.contains(c)){
						list.add(c);
					}
				}
			}else{
				for(Cell c:topCells){
					if(leftCells.contains(c)){
						list.add(c);
					}
				}
			}
			return list;
		}else if(leftCells!=null && topCells==null){
			return leftCells;
		}else if(leftCells==null && topCells!=null){
			return topCells;
		}else{
			Report report=context.getReport();
			return report.getCellsMap().get(cellName);
		}
	}

	private static List<Cell> fetchCellsByLeftParent(Context context, Cell cell, String cellName){
		Cell leftParentCell=cell.getLeftParentCell();
		if(leftParentCell==null){
			return null;
		}
		if(leftParentCell.getName().equals(cellName)){
			List<Cell> list=new ArrayList<Cell>();
			list.add(leftParentCell);
			return list;
		}
		Map<String,List<Cell>> childrenCellsMap=leftParentCell.getRowChildrenCellsMap();
		List<Cell> targetCells=childrenCellsMap.get(cellName);
		if(targetCells!=null){
			return targetCells;
		}
		return fetchCellsByLeftParent(context,leftParentCell,cellName);
	}
	
	private static List<Cell> fetchCellsByTopParent(Context context, Cell cell, String cellName){
		Cell topParentCell=cell.getTopParentCell();
		if(topParentCell==null){
			return null;
		}
		if(topParentCell.getName().equals(cellName)){
			List<Cell> list=new ArrayList<Cell>();
			list.add(topParentCell);
			return list;
		}
		Map<String,List<Cell>> childrenCellsMap=topParentCell.getColumnChildrenCellsMap();
		List<Cell> targetCells=childrenCellsMap.get(cellName);
		if(targetCells!=null){
			return targetCells;
		}
		return fetchCellsByTopParent(context,topParentCell,cellName);
	}
	
	public static Object getProperty(Object obj,String property){
		if(obj==null)return null;
		try{
			if(obj instanceof Map && property.indexOf(".")==-1){
				Map<?,?> map=(Map<?,?>)obj;
				return map.get(property);
			}
			return PropertyUtils.getProperty(obj, property);
		}catch(Exception ex){
			throw new ReportComputeException(ex);
		}
	}
	
	public static Date toDate(Object obj){
		if(obj instanceof Date){
			return (Date)obj;
		}else if(obj instanceof String){
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
			try{
				return sd.parse(obj.toString());
			}catch(Exception ex){
				sd=new SimpleDateFormat(DateUtils.STANDARD_DATE_FORMAT);
				try{
					return sd.parse(obj.toString());					
				}catch(Exception e){
					throw new ReportComputeException("Can not convert "+obj+" to Date.");
				}
			}
		}
		throw new ReportComputeException("Can not convert "+obj+" to Date.");
	}
	public static BigDecimal toBigDecimal(Object obj){
		if(obj==null){
			return null;
		}
		if(obj instanceof BigDecimal){
			return (BigDecimal)obj;
		}else if(obj instanceof String){
			if(obj.toString().trim().equals("")){
				return new BigDecimal(0);
			}
			try{
				String str=obj.toString().trim();
				return new BigDecimal(str);				
			}catch(Exception ex){
				throw new ConvertException("Can not convert "+obj+" to BigDecimal.");
			}
		}else if(obj instanceof Number){
			Number n=(Number)obj;
			return new BigDecimal(n.doubleValue());
		}
		throw new ConvertException("Can not convert "+obj+" to BigDecimal.");
	}
	
	public void setDebug(boolean debug) {
		Utils.debug = debug;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		Utils.applicationContext=applicationContext;
		imageProviders= new ArrayList<>();
		imageProviders.addAll(applicationContext.getBeansOfType(ImageProvider.class).values());
	}
}
