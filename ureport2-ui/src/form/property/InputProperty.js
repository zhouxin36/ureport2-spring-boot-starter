/**
 * Created by xinxingzhou on 2022/2/23.
 */
import Property from "@/form/property/Property";

export default class InputProperty extends Property{
    constructor(report){
        super();
        this.col.append(this.buildRefreshOnChange());
    }
    buildRefreshOnChange(){
        const group=$("<div class='form-group'><label>表单值改变时刷新表单（主要用于级联参数）</label></div>");
        this.buildRefreshOnChangeGroup=$(`<select class="form-control">
            <option value="false" selected>否</option>
            <option value="true">是</option>
        </select>`);
        group.append(this.buildRefreshOnChangeGroup);
        const self=this;
        this.buildRefreshOnChangeGroup.change(function(){
            const value=$(this).val();
            self.current.setRefreshOnChange(value);
        });
        return group;
    }
    refreshValue(current){
        super.refreshValue(current);
        this.buildRefreshOnChangeGroup.val(current.refreshOnChange+"");
    }
}
