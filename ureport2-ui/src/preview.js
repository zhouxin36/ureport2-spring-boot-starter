/**
 * Created by Jacky.Gao on 2017-03-17.
 */
import './form/external/bootstrap-datetimepicker.css';
import {pointToMM, showLoading, hideLoading} from './Utils.js';
import {alert} from './MsgBox.js';
import PDFPrintDialog from './dialog/PDFPrintDialog.js';
import defaultI18nJsonData from './i18n/preview.json';
import en18nJsonData from './i18n/preview_en.json';
import * as utils from "./Utils";
import '../public/venderjs/bootstrap-datetimepicker.js'

(function ($) {
    $.fn.datetimepicker.dates['zh-CN'] = {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        today: "今天",
        suffix: [],
        meridiem: ["上午", "下午"]
    };
}(jQuery));

export function loadSearchForm(){
    refreshSearchFormData();
    let body = buildLocationSearchBody();
    $.ajax({
        url: window._server + "/preview/loadSearchForm",
        contentType: 'application/json',
        type:'POST',
        data:JSON.stringify(body),
        success:function(message){
            let upSearchForm = $(`#upSearchFormDiv`);
            let downSearchForm = $(`#downSearchFormDiv`);
            upSearchForm.empty();
            downSearchForm.empty();
            upSearchForm.append(message.upSearchFormHtml);
            upSearchForm.append(message.downSearchFormHtml);
        }
    });
}

export function previewInit() {
    let body = buildLocationSearchBody();
    $.ajax({
        url: window._server + "/preview",
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(body),
        success: function (message) {
            var body = $("#app");
            let upSearchForm = $(`<div id = "upSearchFormDiv"></div>`)
            upSearchForm.append(message.upSearchFormHtml)
            body.append(upSearchForm)
            if (message.error === true) {
                body.append(`<div id="_ureport_table" style="float:` + message.reportAlign + `">` + message.content + `</div>`)
                body.append(`<iframe name="_print_frame" width="0" height="0" frameborder="0" src="about:blank"></iframe>`)
                body.append(`<iframe name="_print_pdf_frame" width="0" height="0" frameborder="0" src="about:blank"></iframe>`)
                return
            }
            let param = {}
            let pageParam = {}
            window.searchFormParameters = {};
            for (let key in message.customParameters) {
                window.searchFormParameters[key] = message.customParameters[key];
                param[key] = message.customParameters[key];
                pageParam[key] = message.customParameters[key];
            }
            $('title').append(message.title)
            $('style').append(message.style)
            if (message.tools.show === true) {
                var div1 = $(`<div style="border:solid 1px #ddd;border-radius:5px;height:35px;width:100%;margin-bottom:5px;background:#f8f8f8"></div>`);
                var div2 = $(`<div style="text-align:` + message.reportAlign + `"></div>`)
                div1.append(div2)
                body.append(div1)

                if (message.tools.print === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-print"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="在线打印">\n` +
                        `            <img src="../../icons/print.svg" width="20px" height="20px">\n` +
                        `        </button>`);
                    div2.append(button)
                    button.click(function () {
                        const body = buildLocationSearchBody();
                        const url = window._server + '/preview/loadPrintPages';
                        showLoading();
                        $.ajax({
                            url,
                            type: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify(body),
                            success: function (result) {
                                $.ajax({
                                    url: window._server + '/preview/loadPagePaper' + buildLocationSearchParameters(),
                                    type: 'GET',
                                    contentType: 'application/json',
                                    success: function (paper) {
                                        hideLoading();
                                        const html = result.html;
                                        const iFrame = window.frames['_print_frame'];
                                        let styles = `<style type="text/css">`;
                                        styles += buildPrintStyle(paper);
                                        styles += $('#_ureport_table_style').html();
                                        styles += `</style>`;
                                        $(iFrame.document.body).html(styles + html);
                                        iFrame.window.focus();
                                        iFrame.window.print();
                                    },
                                    error: function (response) {
                                        hideLoading();
                                        if (response && response.responseText) {
                                            alert("服务端错误：" + response.responseText + "");
                                        } else {
                                            alert("服务端出错！");
                                        }
                                    }
                                });
                            },
                            error: function (response) {
                                hideLoading();
                                if (response && response.responseText) {
                                    alert("服务端错误：" + response.responseText + "");
                                } else {
                                    alert("服务端出错！");
                                }
                            }
                        });
                    })
                }
                if (message.tools.pdfPrint === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-pdf-direct-print"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="PDF在线打印">\n` +
                        `            <img src="../../icons/pdf-direct-print.svg" width="20px" height="20px">\n` +
                        `        </button>`);
                    div2.append(button)
                    button.click(function () {
                        showLoading();
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/pdf/show' + urlParameters + `&pageIndex=${index++}`;
                        const iframe = window.frames['_print_pdf_frame'];
                        if (!directPrintPdf) {
                            directPrintPdf = true;
                            $("iframe[name='_print_pdf_frame']").on("load", function () {
                                hideLoading();
                                iframe.window.focus();
                                iframe.window.print();
                            });
                        }
                        iframe.window.focus();
                        iframe.location.href = url;
                    })
                }
                let directPrintPdf = false, index = 0;
                const pdfPrintDialog = new PDFPrintDialog();
                if (message.tools.pdfPreviewPrint === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-pdf-print"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="PDF在线预览打印">\n` +
                        `            <img src="../../icons/pdf-print.svg" width="20px" height="20px">\n` +
                        `        </button>`)
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        $.ajax({
                            url: window._server + '/preview/loadPagePaper' + urlParameters,
                            type: 'GET',
                            contentType: 'application/json',
                            success: function (paper) {
                                pdfPrintDialog.show(paper);
                            },
                            error: function (response) {
                                hideLoading();
                                if (response && response.responseText) {
                                    alert("服务端错误：" + response.responseText + "");
                                } else {
                                    alert("服务端出错！");
                                }
                            }
                        });
                    })
                }
                if (message.tools.pdf === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-export-pdf"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="导出PDF">\n` +
                        `            <img src="../../icons/pdf.svg" width="20px" height="20px">\n` +
                        `        </button>`);
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/pdf' + urlParameters;
                        window.open(url, '_blank');
                    })
                }
                if (message.tools.word === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-export-word"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="导出WORD">\n` +
                        `            <img src="../../icons/word.svg" width="20px" height="20px">\n` +
                        `        </button>`);
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/word' + urlParameters;
                        window.open(url, '_blank');
                    })
                }
                if (message.tools.excel === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-export-excel"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="导出EXCEL">\n` +
                        `            <img src="../../icons/excel.svg" width="20px" height="20px">\n` +
                        `        </button>`)
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/excel' + urlParameters;
                        window.open(url, '_blank');
                    })
                }
                if (message.tools.pagingExcel === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-export-excel-paging"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px" title="分页导出EXCEL">\n` +
                        `            <img src="../../icons/excel-paging.svg" width="20px" height="20px">\n` +
                        `        </button>`)
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/excel/paging' + urlParameters;
                        window.open(url, '_blank');
                    })
                }
                if (message.tools.sheetPagingExcel === true) {
                    var button = $(`<button type="button" class="btn btn-default ureport-export-excel-paging-sheet"\n` +
                        `                style="display:inline-block;padding:0;background:#f8f8f8;border:none;margin:3px"\n` +
                        `                title="分页分Sheet导出EXCEL">\n` +
                        `            <img src="../../icons/excel-with-paging-sheet.svg" width="20px" height="20px">\n` +
                        `        </button>`)
                    div2.append(button)
                    button.click(function () {
                        const urlParameters = buildLocationSearchParameters();
                        const url = window._server + '/excel/sheet' + urlParameters;
                        window.open(url, '_blank');
                    })
                }
                if (message.tools.paging === true) {
                    var preview
                    if (message.pageIndex > 0) {
                        preview = '分页预览'
                    } else {
                        preview = '预览'
                    }
                    var btn = $(`<div class="btn-group"></div>`)
                    btn.append(`<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"\n` +
                        `                    style="background:#f8f8f8;border:none;color:#337ab7">\n` + preview +
                        `                <span class="caret"></span>\n` +
                        `            </button>`)
                    var ul = $(`<ul class="dropdown-menu" role="menu"></ul>`)
                    param.template=message.file;
                    pageParam.template=message.file;
                    pageParam.pageIndex=1;
                    let pr = $(`<li><a style="color:#337ab7">预览</a>\n` +
                        `                </li>`);
                    let pagePr = $(`<li><a style="color:#337ab7">分页预览</a>\n` +
                        `                </li>`);
                    pr.click(function () {
                        vueRoute('preview',window.buildQueryBody('pageIndex'))
                    })
                    pagePr.click(function () {
                        let body=window.buildQueryBody();
                        if (!body.pageIndex){
                            body.pageIndex=1
                        }
                        vueRoute('preview',body)
                    })

                    ul.append(pr)
                    ul.append(pagePr)

                    btn.append(ul)
                    div2.append(btn)
                }
                if (message.pageIndex > 0) {
                    var span = $(`<span id="pagingContainer" style="font-size:14px;margin-left:1px;color:#337ab7"></span>`)
                    span.append(`共<span id="totalPageLabel">` + message.totalPageWithCol + `</span>页`)
                    var select = $(`<select id="pageSelector" class="form-control" style="display:inline-block;width:inherit;font-size:13px;height:28px;margin-top:2px"></select>`)
                    for (let i = 1; i <= message.totalPageWithCol; i++) {
                        select.append(`<option>` + i + `</option>`)
                    }
                    span.append(select)
                    span.append(`<span id="pageLinkContainer"></span>`)
                    div2.append(span)
                }
            }
            let downSearchForm = $(`<div id = "downSearchFormDiv"></div>`)
            downSearchForm.append(message.downSearchFormHtml)
            body.append(downSearchForm)
            body.append(`<div id="_ureport_table" style="float:` + message.reportAlign + `">` + message.content + `</div>`)
            body.append(`<iframe name="_print_frame" width="0" height="0" frameborder="0" src="about:blank"></iframe>`)
            body.append(`<iframe name="_print_pdf_frame" width="0" height="0" frameborder="0" src="about:blank"></iframe>`)

            buildPaging(message.pageIndex, message.totalPageWithCol);

            if (message.intervalRefreshValue > 0) {
                _intervalRefresh(message.intervalRefreshValue, message.totalPageWithCol);
            } else {
                _buildChartDatas(JSON.parse(message.chartDatas));
            }
            var sc = document.createElement('script')
            sc.append(message.searchFormJs)
            sc.type = 'text/javascript'
            document.getElementsByTagName('head')[0].appendChild(sc)
            sc = document.createElement('script')
            sc.src = '../../venderjs/bootstrap.min.js'
            sc.type = 'text/javascript'
            document.getElementsByTagName('head')[0].appendChild(sc)
        },
        error: function (message) {
            alert("服务端错误：" + message);
        }
    });

    let language = window.navigator.language || window.navigator.browserLanguage;
    if (!language) {
        language = 'zh-cn';
    }
    language = language.toLowerCase();
    window.i18n = defaultI18nJsonData;
    if (language !== 'zh-cn') {
        window.i18n = en18nJsonData;
    }
};

var urlEncode = function (param, key, encode) {
    if(param==null) return '';
    var paramStr = '';
    var t = typeof (param);
    if (t === 'string' || t === 'number' || t === 'boolean') {
        paramStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);
    } else {
        for (var i in param) {
            var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
            paramStr += urlEncode(param[i], k, encode);
        }
    }
    return paramStr;
};
window._currentPageIndex = null;
window._totalPage = null;

window.buildLocationSearchParameters = function (exclude) {
    let urlParameters = window.location.search;
    if (urlParameters.length > 0) {
        urlParameters = urlParameters.substring(1, urlParameters.length);
    }
    if (!urlParameters){
        let splits=window.location.href.replaceAll("#/","").split("?");
        if (splits.length>=2){
            urlParameters = splits[1];
        }
    }
    let parameters = {};
    const pairs = urlParameters.split('&');
    for (let i = 0; i < pairs.length; i++) {
        const item = pairs[i];
        if (item === '') {
            continue;
        }
        const param = item.split('=');
        let key = param[0];
        if (exclude && key === exclude) {
            continue;
        }
        let value = param[1];
        parameters[key] = value;
    }
    if (window.searchFormParameters) {
        for (let key in window.searchFormParameters) {
            if (key === exclude) {
                continue;
            }
            const value = window.searchFormParameters[key];
            if (value) {
                parameters[key] = value;
            }
        }
    }
    let p = '?';
    for (let key in parameters) {
        if (p === '?') {
            p += key + '=' + parameters[key];
        } else {
            p += '&' + key + '=' + parameters[key];
        }
    }
    return p;
};
window.buildQueryBody = function (exclude) {
    refreshSearchFormData();
    let body = buildLocationSearchBody(exclude);
    if (body.customParameters!==null && body.customParameters!== undefined){
        for (let key in body.customParameters) {
            body[key]=body.customParameters[key]
        }
    }
    body.customParameters = undefined;
    return body;
}
window.refreshSearchFormData = function () {
    window.searchFormParameters={};
    for(let fun of window.formElements){
        const json=fun.call(this);
        for(let key in json){
            let value=json[key];
            if (!value){
                continue
            }
            value=encodeURI(value);
            window.searchFormParameters[key]=value;
        }
    }
}
window.buildLocationSearchBody = function (exclude) {
    let urlParameters = window.location.search;
    if (urlParameters.length > 0) {
        urlParameters = urlParameters.substring(1, urlParameters.length);
    }
    if (!urlParameters){
        let splits=window.location.href.replaceAll("#/","").split("?");
        if (splits.length>=2){
            urlParameters = splits[1];
        }
    }
    let parameters = {};
    const pairs = urlParameters.split('&');
    for (let i = 0; i < pairs.length; i++) {
        const item = pairs[i];
        if (item === '') {
            continue;
        }
        const param = item.split('=');
        let key = param[0];
        if (exclude && key === exclude) {
            continue;
        }
        let value = param[1];
        parameters[key] = value;
    }
    if (window.searchFormParameters) {
        for (let key in window.searchFormParameters) {
            if (key === exclude) {
                continue;
            }
            const value = window.searchFormParameters[key];
            if (value) {
                parameters[key] = value;
            }
        }
    }
    let map = {};
    let customParameters = {};
    for (let key in parameters) {
        if ($.inArray(key, ['template', 'pageIndex', 'title', 'tools']) !== -1) {
            map[key] = decodeURIComponent(parameters[key]);
        } else {
            customParameters[key] = decodeURIComponent(parameters[key]);
        }
    }
    map['customParameters'] = customParameters;
    return map;
};

function buildPrintStyle(paper) {
    const marginLeft = pointToMM(paper.leftMargin);
    const marginTop = pointToMM(paper.topMargin);
    const marginRight = pointToMM(paper.rightMargin);
    const marginBottom = pointToMM(paper.bottomMargin);
    const paperType = paper.paperType;
    let page = paperType;
    if (paperType === 'CUSTOM') {
        page = pointToMM(paper.width) + 'mm ' + pointToMM(paper.height) + 'mm';
    }
    const style = `
        @media print {
            .page-break{
                display: block;
                page-break-before: always;
            }
        }
        @page {
          size: ${page} ${paper.orientation};
          margin-left: ${marginLeft}mm;
          margin-top: ${marginTop}mm;
          margin-right:${marginRight}mm;
          margin-bottom:${marginBottom}mm;
        }
    `;
    return style;
};

window.buildPaging = function (pageIndex, totalPage) {
    if (totalPage === 0) {
        return;
    }
    if (!pageIndex) {
        return;
    }
    if (!window._currentPageIndex) {
        window._currentPageIndex = pageIndex;
    }
    pageIndex = window._currentPageIndex;
    if (!window._totalPage) {
        window._totalPage = totalPage;
    }

    const pageSelector = $('#pageSelector');
    pageSelector.change(function () {
        const parameters = window.buildLocationSearchBody('pageIndex');
        // let url = `/preview${parameters}&pageIndex=${$(this).val()}`;
        // window.open("/#/"+url, '_blank');
        parameters.pageIndex=$(this).val();
        vueRoute('/preview',parameters);
    });
    pageSelector.val(pageIndex);
    if (totalPage === 1) {
        return;
    }
    const parameters = window.buildQueryBody('pageIndex');
    const pagingContainer = $('#pageLinkContainer');
    pagingContainer.empty();
    if (pageIndex > 1) {
        // let url = `/preview${parameters}&pageIndex=${pageIndex - 1}`;
        parameters.pageIndex=pageIndex - 1;
        const prevPage = $(`<button type="button" class="btn btn-link btn-sm">上一页</button>`);
        pagingContainer.append(prevPage);
        prevPage.click(function () {
            // window.open("/#/"+url, '_blank');
            vueRoute('/preview',parameters);
        });
    }
    if (pageIndex < totalPage) {
        // let url = `/preview${parameters}&pageIndex=${pageIndex + 1}`;
        parameters.pageIndex=pageIndex + 1;
        const nextPage = $(`<button type="button" class="btn btn-link btn-sm">下一页</button>`);
        pagingContainer.append(nextPage);
        nextPage.click(function () {
            // window.open("/#/"+url, '_blank');
            vueRoute('/preview',parameters);
        });
    }
};

window._intervalRefresh = function (value, totalPage) {
    if (!value) {
        return;
    }
    window._totalPage = totalPage;
    const second = value * 1000;
    setTimeout(function () {
        _refreshData(second);
    }, second);
};

function _refreshData(second) {
    refreshSearchFormData();
    const body = buildLocationSearchBody();
    let url = window._server + `/preview/loadData`;
    $.ajax({
        url,
        type: 'POST',
        data: JSON.stringify(body),
        contentType: 'application/json',
        success: function (report) {
            const tableContainer = $(`#_ureport_table`);
            tableContainer.empty();
            window._totalPage = report.totalPageWithCol;
            tableContainer.append(report.content);
            _buildChartDatas(report.chartDatas);
            buildPaging(window._currentPageIndex, window._totalPage);
            if (window._currentPageIndex) {
                window._currentPageIndex++;
            }
            setTimeout(function () {
                _refreshData(second);
            }, second);
        },
        error: function (response) {
            const tableContainer = $(`#_ureport_table`);
            tableContainer.empty();
            if (response && response.responseText) {
                tableContainer.append("<h3 style='color: #d30e00;'>服务端错误：" + response.responseText + "</h3>");
            } else {
                tableContainer.append("<h3 style='color: #d30e00;'>加载数据失败</h3>");
            }
            setTimeout(function () {
                _refreshData(second);
            }, second);
        }
    });
};

window._buildChartDatas = function (chartData) {
    if (!chartData) {
        return;
    }
    for (let d of chartData) {
        let json = d.json;
        json = JSON.parse(json, function (k, v) {
            if (v.indexOf && v.indexOf('function') > -1) {
                return eval("(function(){return " + v + " })()")
            }
            return v;
        });
        _buildChart(d.id, json);
    }
};
window._buildChart = function (canvasId, chartJson) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) {
        return;
    }
    let options = chartJson.options;
    if (!options) {
        options = {};
        chartJson.options = options;
    }
    let animation = options.animation;
    if (!animation) {
        animation = {};
        options.animation = animation;
    }
    animation.onComplete = function (event) {
        const chart = event.chart;
        const base64Image = chart.toBase64Image();
        const urlParameters = window.location.search;
        const url = window._server + '/chart/storeData' + urlParameters;
        const canvas = $("#" + canvasId);
        const width = parseInt(canvas.css('width'));
        const height = parseInt(canvas.css('height'));
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "base64Data": base64Image,
                "chartId": canvasId,
                "width": width,
                "height": height,
            }),
            url
        });
    };
    const chart = new Chart(ctx, chartJson);
};

export function submitSearchForm () {
    refreshSearchFormData();
    const body = window.buildLocationSearchBody();
    let url = window._server + "/preview/loadData";
    const pageSelector = $(`#pageSelector`);
    $.ajax({
        url,
        type: 'POST',
        data: JSON.stringify(body),
        contentType: 'application/json',
        success: function (report) {
            window._currentPageIndex = 1;
            const tableContainer = $(`#_ureport_table`);
            tableContainer.empty();
            tableContainer.append(report.content);
            _buildChartDatas(report.chartDatas);
            const totalPage = report.totalPage;
            window._totalPage = totalPage;
            if (pageSelector.length > 0) {
                pageSelector.empty();
                for (let i = 1; i <= totalPage; i++) {
                    pageSelector.append(`<option>${i}</option>`);
                }
                const pageIndex = report.pageIndex || 1;
                pageSelector.val(pageIndex);
                $('#totalPageLabel').html(totalPage);
                buildPaging(pageIndex, totalPage);
            }
        },
        error: function (response) {
            if (response && response.responseText) {
                alert("服务端错误：" + response.responseText + "");
            } else {
                alert('查询操作失败！');
            }
        }
    });
};
