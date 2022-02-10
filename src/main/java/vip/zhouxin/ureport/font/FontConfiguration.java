package vip.zhouxin.ureport.font;

import vip.zhouxin.ureport.font.arial.ArialFontRegister;
import vip.zhouxin.ureport.font.comicsansms.ComicSansMSFontRegister;
import vip.zhouxin.ureport.font.couriernew.CourierNewFontRegister;
import vip.zhouxin.ureport.font.fangsong.FangSongFontRegister;
import vip.zhouxin.ureport.font.heiti.HeiTiFontRegister;
import vip.zhouxin.ureport.font.kaiti.KaiTiFontRegister;
import vip.zhouxin.ureport.font.songti.SongTiFontRegister;
import vip.zhouxin.ureport.font.timesnewroman.TimesNewRomanFontRegister;
import vip.zhouxin.ureport.font.yahei.YaheiFontRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 字体加载
 *
 * @author xinxingzhou
 * @since 2022/1/19
 */
@Configuration
public class FontConfiguration {

    @Bean
    public ArialFontRegister arialFontRegister(){
        return new ArialFontRegister();
    }
    @Bean
    public ComicSansMSFontRegister comicSansMSFontRegister(){
        return new ComicSansMSFontRegister();
    }
    @Bean
    public CourierNewFontRegister courierNewFontRegister(){
        return new CourierNewFontRegister();
    }
    @Bean
    public FangSongFontRegister fangSongFontRegister(){
        return new FangSongFontRegister();
    }
    @Bean
    public HeiTiFontRegister heiTiFontRegister(){
        return new HeiTiFontRegister();
    }
    @Bean
    public KaiTiFontRegister kaiTiFontRegister(){
        return new KaiTiFontRegister();
    }
    @Bean
    public SongTiFontRegister songTiFontRegister(){
        return new SongTiFontRegister();
    }
    @Bean
    public TimesNewRomanFontRegister timesNewRomanFontRegister(){
        return new TimesNewRomanFontRegister();
    }
    @Bean
    public YaheiFontRegister yaheiFontRegister(){
        return new YaheiFontRegister();
    }
}
