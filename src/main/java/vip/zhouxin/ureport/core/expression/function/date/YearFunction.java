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
package vip.zhouxin.ureport.core.expression.function.date;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.Calendar;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年1月22日
 */
public class YearFunction extends CalendarFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Calendar c = buildCalendar(dataList);
		int year=c.get(Calendar.YEAR);
		return year;
	}

	@Override
	public String name() {
		return "year";
	}
}