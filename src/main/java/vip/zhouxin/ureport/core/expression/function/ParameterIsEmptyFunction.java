package vip.zhouxin.ureport.core.expression.function;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class ParameterIsEmptyFunction extends ParameterFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context,
                          Cell currentCell) {
		Object obj = super.execute(dataList, context, currentCell);
		if(obj==null || obj.toString().trim().equals("")){
			return true;
		}
		return false;
	}
	@Override
	public String name() {
		return "emptyparam";
	}
}
