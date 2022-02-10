package vip.zhouxin.ureport.font.timesnewroman;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class TimesNewRomanFontRegister implements FontRegister {

	public String getFontName() {
		return "Times New Roman";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/timesnewroman/TIMES.TTF";
	}
}
