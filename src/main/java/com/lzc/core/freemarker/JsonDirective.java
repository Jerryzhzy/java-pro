package com.lzc.core.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

/**
 *  freemarker自定义指令 -json
 * Created by ziyu.zhang on 2017/6/30 20:28
 */
public class JsonDirective implements TemplateDirectiveModel {

	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		Object data=params.get("data");
		JSONObject jo=JSONObject.fromObject(data);
		Iterator<Object> it = jo.keys();
		Writer out = env.getOut();
		while(it.hasNext()){
			Object key = it.next();
			Object value = jo.get(key);
			System.out.println(key+"--"+value);
			
		}
		StringBuilder builder = new StringBuilder();
		builder.append(jo.toString());
		System.out.println(builder.toString());
		out.write(builder.toString());
	}
}
