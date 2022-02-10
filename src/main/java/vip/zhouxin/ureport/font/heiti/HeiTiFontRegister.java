package vip.zhouxin.ureport.font.heiti;

import vip.zhouxin.ureport.core.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class HeiTiFontRegister implements FontRegister {

	public String getFontName() {
		return "黑体";
	}

	public String getFontPath() {
		return "vip/zhouxin/ureport/font/heiti/SIMHEI.TTF";
	}
}
