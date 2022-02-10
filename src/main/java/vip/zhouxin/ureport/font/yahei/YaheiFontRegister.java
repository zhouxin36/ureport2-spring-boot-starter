package vip.zhouxin.ureport.font.yahei;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class YaheiFontRegister implements FontRegister {

	public String getFontName() {
		return "微软雅黑";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/yahei/msyh.ttc";
	}
}
