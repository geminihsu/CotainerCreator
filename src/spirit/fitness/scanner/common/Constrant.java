package spirit.fitness.scanner.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;



public class Constrant {
	//public static String webUrl = "http://172.16.2.96:5000";
	public static String webUrl = "http://172.16.2.6";
	public static String weburlprefix = "FGService/api/";
	

	
	
	
	//Zone mapping Table key : zone number, value : location number
    public static int ZONE_CODE_LEN_LIMIT = 3;
    public static int ZONE_CODE_1 = 1;
    public static int ZONE_CODE_2 = 2;
    public static int ZONE_CODE_3 = 3;
    public static int ZONE_CODE_4 = 4;
    public static int ZONE_CODE_5 = 5;
    public static int ZONE_CODE_6 = 6;
    public static int ZONE_CODE_7 = 7;
    
    //Zone 1 range
    public static int ZONE_CODE_1_MIN = 0;
    public static int ZONE_CODE_1_MAX = 69;
    //Zone 2 range
    public static int ZONE_CODE_2_MIN = 701;
    public static int ZONE_CODE_2_MAX = 992;

    //Zone 3 range
    public static int ZONE_CODE_3_A = 881;
    public static int ZONE_CODE_3_B = 891;
    public static int ZONE_CODE_3_C = 901;
    public static int ZONE_CODE_3_D = 911;
    //Zone 4 range
    public static int ZONE_CODE_4_ONE = 888;
    
    //Rework range
    public static int ZONE_CODE_5_ONE = 555;
    
    //QC range
    public static int ZONE_CODE_6_ONE = 666;
    
    //Scrapped range
    public static int ZONE_CODE_7_ONE = 777;
    
    //Shipping
    public static int ZONE_CODE_SHIPPING = 999;
    
    
    //Java form color define
    public static Color BACKGROUN_COLOR = new java.awt.Color(235, 240, 255);
    public static Color FRAME_BORDER_BACKGROUN_COLOR = new java.awt.Color(94, 134, 193);
    public static Color TABLE_COLOR = new java.awt.Color(255, 255, 255);
    public static Color DISPALY_ITEMS_TABLE_COLOR = new java.awt.Color(255, 255, 240);
    public static Color BUTTON_PANEL_BACKGROUND_COLOR = new java.awt.Color(230, 230, 250);
    public static Color BUTTON_BACKGROUN_COLOR = new java.awt.Color(255, 255, 240);
    
     
    
    
}
