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
package vip.zhouxin.ureport.core.build.compute;

import vip.zhouxin.ureport.core.build.BindData;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.definition.value.SimpleValue;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class SimpleValueCompute implements ValueCompute {

	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list= new ArrayList<>();
		list.add(new BindData(cell.getValue().getValue(),null,null));
		return list;
	}

	@Override
	public String type() {
		return SimpleValue.TYPE;
	}
}
