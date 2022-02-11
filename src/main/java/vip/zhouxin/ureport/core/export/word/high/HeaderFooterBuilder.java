/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package vip.zhouxin.ureport.core.export.word.high;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.util.CollectionUtils;
import vip.zhouxin.ureport.core.build.paging.HeaderFooter;
import vip.zhouxin.ureport.core.model.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jacky.gao
 * @since 2017年4月17日
 */
public class HeaderFooterBuilder {
	public void build(XWPFDocument document, CTSectPr sectPr, Report report){
		//HeaderFooterDefinition headerDef=report.getHeader();
		//HeaderFooterDefinition footerDef=report.getFooter();
		
		HeaderFooter header=new HeaderFooter();
		HeaderFooter footer=new HeaderFooter();
		XWPFHeaderFooterPolicy headerFooterPolicy=null;
		if(header!=null){
			List<XWPFParagraph> list=buildXWPFParagraph(header, document);
			XWPFParagraph[] newparagraphs = new XWPFParagraph[list.size()];
			list.toArray(newparagraphs);
			headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);									
			headerFooterPolicy.createHeader(STHdrFtr.DEFAULT, newparagraphs);
		}
		if(footer!=null){
			List<XWPFParagraph> list=buildXWPFParagraph(footer, document);
			XWPFParagraph[] newparagraphs = new XWPFParagraph[list.size()];
			list.toArray(newparagraphs);
			/*if(headerFooterPolicy==null){
				headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);
			}*/
			headerFooterPolicy.createFooter(STHdrFtr.DEFAULT, newparagraphs);
		}
	}
	
	private List<XWPFParagraph> buildXWPFParagraph(HeaderFooter hf, XWPFDocument document){
		CTP ctp = null;
		List<XWPFParagraph> paras=new ArrayList<XWPFParagraph>();
		XWPFParagraph para=null;		
		XWPFRun r1 = null;
		String left=hf.getLeft();
		String center=hf.getCenter();
		String right=hf.getRight();
		SimpleDateFormat dateSD=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeSD=new SimpleDateFormat("HH:MM:ss");
		Date D=new Date();
		String date=dateSD.format(D);
		String time=timeSD.format(D);
		if(StringUtils.isNotBlank(left)){
			ctp = CTP.Factory.newInstance();				
			para=new XWPFParagraph(ctp, document);
			para.setAlignment(ParagraphAlignment.LEFT);
			doBuild(hf, paras, para, left, date, time);
		}
		if(StringUtils.isNotBlank(center)){
			ctp = CTP.Factory.newInstance();				
			para=new XWPFParagraph(ctp, document);
			para.setAlignment(ParagraphAlignment.CENTER);
			doBuild(hf, paras, para, center, date, time);
		}
		if(StringUtils.isNotBlank(right)){
			ctp = CTP.Factory.newInstance();				
			para=new XWPFParagraph(ctp, document);
			para.setAlignment(ParagraphAlignment.RIGHT);
			doBuild(hf, paras, para, right, date, time);
		}
		return paras;
	}

	private void doBuild(HeaderFooter hf, List<XWPFParagraph> paras, XWPFParagraph para, String left, String date, String time) {
		XWPFRun r1;
		paras.add(para);
		List<String> list=splitHeaderFooterContent(left);
		for (String text : list) {
			switch (text) {
				case "$[PAGE]": {
					r1 = para.createRun();
					CTFldChar fldChar = r1.getCTR().addNewFldChar();
					fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));
					CTText ctText = r1.getCTR().addNewInstrText();
					ctText.setStringValue("PAGE  \\* MERGEFORMAT");
					ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
					doCtrpr(hf, r1);
					fldChar = r1.getCTR().addNewFldChar();
					fldChar.setFldCharType(STFldCharType.Enum.forString("end"));
					break;
				}
				case "$[PAGES]": {
					r1 = para.createRun();
					CTFldChar fldChar = r1.getCTR().addNewFldChar();
					fldChar.setFldCharType(STFldCharType.Enum.forString("begin"));
					CTText ctText = r1.getCTR().addNewInstrText();
					ctText.setStringValue("NUMPAGES  \\* MERGEFORMAT ");
					ctText.setSpace(SpaceAttribute.Space.Enum.forString("preserve"));
					doCtrpr(hf, r1);
					fldChar = r1.getCTR().addNewFldChar();
					fldChar.setFldCharType(STFldCharType.Enum.forString("end"));
					break;
				}
				case "$[DATE]": {
					r1 = para.createRun();
					r1.setText(date);
					doCtrpr(hf, r1);
					break;
				}
				case "$[TIME]": {
					r1 = para.createRun();
					r1.setText(time);
					doCtrpr(hf, r1);
					break;
				}
				default: {
					r1 = para.createRun();
					r1.setText(text);
					doCtrpr(hf, r1);
					break;
				}
			}
		}
	}


	private void doCtrpr(HeaderFooter hf, XWPFRun r1) {
		if (hf.getFontSize() > 1) {
			r1.setFontSize(hf.getFontSize());
		}
		CTRPr rpr = r1.getCTR().isSetRPr() ? r1.getCTR().getRPr() : r1.getCTR().addNewRPr();
		List<CTFonts> rFontsList = rpr.getRFontsList();
		CTFonts fonts;
		if (CollectionUtils.isEmpty(rFontsList)){
			fonts = rpr.addNewRFonts();
		}else {
			fonts = rFontsList.get(0);
		}
		String fontName = hf.getFontFamily();
		if (fontName != null) {
			fonts.setAscii(fontName);
			fonts.setEastAsia(fontName);
			fonts.setHAnsi(fontName);
		}
		if (hf.isBold()) {
			r1.setBold(true);
		}
		if (hf.isItalic()) {
			r1.setItalic(true);
		}
	}

	private List<String> splitHeaderFooterContent(String info){
		Pattern pattern = Pattern.compile("\\$\\[PAGE\\]|\\$\\[PAGES\\]|\\$\\[DATE\\]|\\$\\[TIME\\]");
		Matcher matcher = pattern.matcher(info);
		List<String> list= new ArrayList<>();
		int start=0;
		while (matcher.find()) {
			String text=matcher.group();
			int pos=info.indexOf(text);
			String str=info.substring(start,pos);
			start=pos+text.length();
			list.add(str);
			list.add(text);
		}
		if(start<info.length()){
			list.add(info.substring(start));
		}
		return list;
	}
	
}
