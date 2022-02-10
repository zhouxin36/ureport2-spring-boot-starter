package vip.zhouxin.ureport.font.arial;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;


/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ArialFontRegister implements FontRegister {

	public String getFontName() {
		return "Arial";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/arial/ARIAL.TTF";
	}
}
