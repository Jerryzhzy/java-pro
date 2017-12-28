package com.lzc.core.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.ClassUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 自定义枚举标签 map
 * Created by ziyu.zhang on 2017/6/30 20:28
 */
public class EnummapDirective implements TemplateDirectiveModel  {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		Object data=params.get("class");
		String result="";
		try {
			Class<Enum> clazz=ClassUtil.forName(data.toString());
			List<Enum> list= org.apache.commons.lang3.EnumUtils.getEnumList(clazz);
			for(Enum e:list){
				result+=",\""+e.name()+"\":\""+e.toString()+"\"";
			}
			if(!"".equals(result)) result=result.substring(1);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Writer out = env.getOut();
		out.write("{"+result+"}");
	}

}
