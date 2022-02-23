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
package vip.zhouxin.ureport.core.definition.searchform;

/**
 * @author Jacky.gao
 * @since 2017年10月23日
 */
public class TextInputComponent extends InputComponent {
	public static final String TYPE = "Text";
	@Override
	String inputHtml(RenderContext context, Object pValue) {
		return "<input type='text' value=\""+pValue+"\" style=\"padding:3px;height:28px\" id='"+context.buildComponentId(this)+"'"+refreshHtml()+" name='"+getBindParameter()+"' class='form-control'>";
	}
	@Override
	public String initJs(RenderContext context) {
		String name=getBindParameter();
		return "formElements.push(" +
				"function(){" +
				"if(''==='" + name + "'){" +
				"alert('文本框未绑定查询参数名，不能进行查询操作!');" +
				"throw '文本框未绑定查询参数名，不能进行查询操作!'" +
				"}" +
				"return {" +
				"\"" + name + "\":" +
				"$('#" + context.buildComponentId(this) + "').val()" +
				"}" +
				"}" +
				");";
	}
}
