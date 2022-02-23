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

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年10月23日
 */
public class SelectInputComponent extends DatasetComponent {
    public static final String TYPE = "Select";

    @Override
    String inputHtml(RenderContext context, Object pValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select style=\"padding:3px;height:28px\" id='").append(context.buildComponentId(this))
                .append("' name='")
                .append(getBindParameter())
                .append("' ")
                .append(refreshHtml())
                .append(" class='form-control'>");
        List<Option> options = generateOption(context);

        for (Option option : options) {
            String value = option.getValue();
            String selected = value.equals(pValue) ? "selected" : "";
            sb.append("<option value='").append(value).append("' ").append(selected).append(">").append(option.getLabel()).append("</option>");
        }
        if (pValue.equals("")) {
            sb.append("<option value='' selected></option>");
        }
        sb.append("</select>");
        return sb.toString();
    }

    @Override
    public String initJs(RenderContext context) {
        String name = getBindParameter();
        return "formElements.push(" +
                "function(){" +
                "if(''==='" + name + "'){" +
                "alert('列表框未绑定查询参数名，不能进行查询操作!');" +
                "throw '列表框未绑定查询参数名，不能进行查询操作!'" +
                "}" +
                "return {" +
                "\"" + name + "\":" +
                "$('#" + context.buildComponentId(this) + "').val()" +
                "}" +
                "}" +
                ");";
    }
}
