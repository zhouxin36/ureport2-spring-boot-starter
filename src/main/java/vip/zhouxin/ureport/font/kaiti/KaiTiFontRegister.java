package vip.zhouxin.ureport.font.kaiti;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class KaiTiFontRegister implements FontRegister {

	public String getFontName() {
		return "楷体";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/kaiti/SIMKAI.TTF";
	}
}
