package com.ggbook.model;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "serial", "rawtypes" })
public abstract class BaseModel<M extends Model> extends Model<M> {

	/**
	 * 页面数据和数据库数据对冲
	 * @param dataBaseObject 数据库数据
	 * @param paramObject 页面数据
	 * @return
	 */
	protected M correct(M dataBaseObject, M paramObject) {
		if(dataBaseObject==null){
			return paramObject;
		}

		for(String key : dataBaseObject._getAttrNames()){
			if(paramObject.get(key)!=null){
				continue;
			}
			paramObject.put(key, dataBaseObject.get(key));
		}
		return paramObject;
	}

	/**
	 * 设置查询
	 * @param params
	 * @param paras
	 * @param sqls
	 * @param keys
	 */
	public void setSql(JSONObject params, List<Object> paras, StringBuffer sqls, Map<String, Object> keys) {
		for(String key : keys.keySet()){

			Object value = params.get(key);
			if (value == null && key.contains(".")) {
				value = params.get(key.split("\\.")[1]);
			}
			if(value==null){
				continue;
			}

			Object type = keys.get(key);
			if(type.equals(Number.class)){
				BigDecimal number = new BigDecimal(StrKit.isBlank(""+value)?"0":(""+value));
				if(number.doubleValue() > 0.00d){
					paras.add(value);
					sqls.append("AND "+key+" = ? ");
				}
			}else if(type.equals(String.class)){
				String string = params.getString(key);
				if(!StrKit.isBlank(string)){
					paras.add(value);
					sqls.append("AND "+key+" REGEXP ? ");
				}
			}
		}
	}

	/**
	 * replace into table
	 * @return
	 */
	public int replaceSave(){
		StringBuffer sql = new StringBuffer();
		Table table = TableMapping.me().getTable(DbKit.getUsefulClass(this.getClass()));
		String tableName = table.getName();
		sql.append("replace into `").append(tableName).append("`(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values (");
		Iterator i$ = this.getAttrs().entrySet().iterator();

		List<Object> paras = new ArrayList<>();
		while(i$.hasNext()) {
			Map.Entry e = (Map.Entry)i$.next();
			String key = (String)e.getKey();
			if(!table.hasColumnLabel(key)){
				continue;
			}
			if(paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(key).append("`");
			temp.append("?");
			paras.add(e.getValue());
		}

		sql.append(temp.toString()).append(")");
		return Db.update(sql.toString(), paras.toArray());
	}

	/**
	 * replace into table
	 * @param list
	 * @return
	 */
	public int[] batchReplaceSave(List<M> list){
		if(list==null || list.isEmpty()){
			return new int[0];
		}
		int size = list.size();
		M one = list.get(0);
		Table table = TableMapping.me().getTable(DbKit.getUsefulClass(one.getClass()));
		String tableName = table.getName();

		StringBuffer sql = new StringBuffer();
		sql.append("replace into `").append(tableName).append("`(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values (");
		Iterator i$ = one._getAttrsEntrySet().iterator();
		List<Object> _paras = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		while(i$.hasNext()) {
			Map.Entry e = (Map.Entry)i$.next();
			String key = (String)e.getKey();
			if(!table.hasColumnLabel(key)){
				continue;
			}
			if(_paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(key).append("`");
			temp.append("?");
			_paras.add(e.getValue());
			keys.add(key);
		}
		sql.append(temp.toString()).append(")");

		Object[][] paras = new Object[size][];
		for(int i=0; i<size; i++){
			M m = list.get(i);
			List<Object> para = new ArrayList<>();
			for(String key : keys){
				para.add(m.get(key));
			}
			paras[i] = para.toArray();
		}

		return Db.batch(sql.toString(), paras, size);
	}

}
