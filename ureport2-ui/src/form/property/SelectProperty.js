/**
 * Created by Jacky.Gao on 2017-10-20.
 */
import bootbox from "bootbox";
import DatasetProperty from "@/form/property/DatasetProperty";
export default class SelectProperty extends DatasetProperty{
    constructor(report){
        super();
    }
    refreshValue(editor){
        super.refreshValue(editor);
        this.simpleOptionGroup.append($("<label>固定值选项(若显示值与实际值不同，则用“,”分隔，如“是,true”等)</label>"));
        var self=this;
        $.each(editor.options,function(index,option){
            self.addOptionEditor(option);
        });
    }
    addOptionEditor(option){
        var inputGroup=$("<div class='input-group'>");
        var input=$("<input class='form-control' type='text'>");

        if(option.label===option.value){
            input.val(option.label);
        }else{
            input.val(option.label+","+option.value);
        }

        input.change(function(){
            var value=$(this).val();
            var json={value:value,label:value};
            var array=value.split(",");
            if(array.length===2){
                json.label=array[0];
                json.value=array[1];
            }
            option.setValue(json);
        });
        inputGroup.append(input);
        var addon=$("<span class='input-group-addon'>");
        inputGroup.append(addon);
        var self=this;
        var del=$("<span class='pb-icon-delete'><li class='glyphicon glyphicon-trash'></li></span>");
        del.click(function(){
            if(self.current.options.length===1){
                bootbox.alert("至少要保留一个列表选项!");
                return;
            }
            self.current.removeOption(option);
            inputGroup.remove();
        });
        addon.append(del);
        var add=$("<span class='pb-icon-add' style='margin-left: 10px'><li class='glyphicon glyphicon-plus'></span>");
        add.click(function(){
            var newOption=self.current.addOption();
            self.addOptionEditor(newOption);
        });
        addon.append(add);
        this.simpleOptionGroup.append(inputGroup);
    }
}
