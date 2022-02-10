package vip.zhouxin.ureport.font.impact;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ImpactFontRegister implements FontRegister {

	public String getFontName() {
		return "Impact";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/impact/IMPACT.TTF";
	}
}
