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
package vip.zhouxin.ureport.core.definition.value;

import vip.zhouxin.ureport.core.chart.Chart;

/**
 * @author Jacky.gao
 * @since 2017年6月9日
 */
public class ChartValue implements Value {

	public static final String TYPE = "chart";
	private Chart chart;
	@Override
	public String getValue() {
		return null;
	}
	@Override
	public String getType() {
		return TYPE;
	}
	public void setChart(Chart chart) {
		this.chart = chart;
	}
	public Chart getChart() {
		return chart;
	}
}
