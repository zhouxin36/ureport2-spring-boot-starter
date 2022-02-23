/**
 * Created by Jacky.Gao on 2017-01-25.
 */
import Tool from './Tool.js';
import SaveDialog from '../dialog/SaveDialog.js';
import {alert, confirm} from '../MsgBox.js';
import {resetDirty, tableToJson, tableToXml} from '../Utils.js';

export default class SaveTool extends Tool{
    execute(){
    }

    buildButton(){
        const group=$(`<div class="btn-group"></div>`);
        const mainBtn=$(`<button type="button" class="btn btn-default dropdown-toggle" style="border:none;border-radius:0;background: #f8f8f8;padding: 6px 8px;" data-toggle="dropdown" title="${window.i18n.tools.save.save}">
            <i class="ureport ureport-save" style="color: #0e90d2;"></i>
            <span class="caret"></span>
        </button>`);
        const ul=$(`<ul class="dropdown-menu" role="menu"></ul>`);
        const save=$(`<li id="__save_btn" class="disabled">
                <a>
                    <i class="ureport ureport-save" style="color: #0e90d2;"></i> ${window.i18n.tools.save.save}
                </a>
            </li>`);
        ul.append(save);
        const saveDialog=new SaveDialog();
        const _this=this;
        save.click(function(){
            if(window._reportFile){
                $.ajax({
                    url:window._server+"/designer/saveReportFile",
                    contentType: 'application/json',
                    data:JSON.stringify(tableToJson(window._reportFile,_this.context)),
                    type:'POST',
                    success:function(){
                        alert(`${window.i18n.tools.save.successSave}`);
                        resetDirty();
                    },
                    error:function(response){
                        if(response && response.responseText){
                            alert("服务端错误："+response.responseText+"");
                        }else{
                            alert(`${window.i18n.tools.save.failSave}`);
                        }
                    }
                });
            }else{
                saveDialog.show(tableToJson(window._reportFile,_this.context),_this.context);
            }
        });
        const saveAs=$(`<li>
                <a>
                    <i class="glyphicon glyphicon-floppy-disk" style="color: #0e90d2;font-size: 16px"></i> ${window.i18n.tools.save.saveAs}
                </a>
            </li>`);
        ul.append(saveAs);
        saveAs.click(function(){
            const content=tableToXml(_this.context);
            saveDialog.show(content,_this.context);
        });

        group.append(mainBtn);
        group.append(ul);
        return group;
    }


    getTitle(){
        return `${window.i18n.tools.save.save}`;
    }
    getIcon(){
        return `<i class="ureport ureport-save" style="color: #0e90d2"></i>`;
    }
}
