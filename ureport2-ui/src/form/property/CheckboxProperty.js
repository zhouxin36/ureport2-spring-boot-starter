/**
 * Created by Jacky.Gao on 2017-10-16.
 */
import bootbox from "bootbox";
import DatasetProperty from "./DatasetProperty.js";
export default class CheckboxProperty extends DatasetProperty{
    constructor(){
        super();
        this.init();
    }
    init(){
        this.col.append(this.buildOptionsInlineGroup());
    }
    addCheckboxEditor(checkbox){
        var self=this;
        var inputGroup=$("<div class='input-group'>");
        var text=$("<input type='text' class='form-control'>");
        inputGroup.append(text);
        text.change(function(){
            var value=$(this).val();
            var json={value:value,label:value};
            var array=value.split(",");
            if(array.length===2){
                json.label=array[0];
                json.value=array[1];
            }
            checkbox.setValue(json);
        });
        if(checkbox.label===checkbox.value){
            text.val(checkbox.label);
        }else{
            text.val(checkbox.label+","+checkbox.value);
        }
        var addon=$("<span class='input-group-addon'>");
        inputGroup.append(addon);
        var del=$("<span class='pb-icon-delete'><li class='glyphicon glyphicon-trash'></li></span>");
        del.click(function(){
            if(self.current.options.length===1){
                bootbox.alert("至少要保留一个选项!");
                return;
            }
            self.current.removeOption(checkbox);
            inputGroup.remove();
        });
        addon.append(del);
        var add=$("<span class='pb-icon-add' style='margin-left: 10px'><li class='glyphicon glyphicon-plus'></span>");
        add.click(function(){
            var newOption=self.current.addOption();
            self.addCheckboxEditor(newOption);
        });
        addon.append(add);
        this.simpleOptionGroup.append(inputGroup);
    }
    refreshValue(current){
        super.refreshValue(current);
        this.simpleOptionGroup.append($("<label>选项(若显示值与实际值不同，则用“,”分隔，如“是,true”等)</label>"));
        var self=this;
        $.each(this.current.options,function(index,checkbox){
            self.addCheckboxEditor(checkbox);
        });
    }
}
