package com.ubddpd.planhealth;

public class PlanHealthUtils {

	public static String getColorCode(String rgb){
        String backgroundColorCode = null;
        switch(rgb){
            case "background-color: rgb(159, 127, 69);":
                backgroundColorCode = "background-color: #9F7F45";
                break;
            case "background-color: rgb(4, 120, 194);":
                backgroundColorCode = "background-color: #0478C2";
                break;
            case "background-color: rgb(22, 43, 109);":
                backgroundColorCode = "background-color: #162B6D";
                break;
            case "background-color: rgb(152, 197, 223);":
                backgroundColorCode = "background-color: #98C5DF";
                break;
            case "background-color: rgb(42, 177, 189);":
                backgroundColorCode = "background-color: #2AB1BD";
                break;
            case "background-color: rgb(243, 137, 68);":
                backgroundColorCode = "background-color: #F38944";
                break;
            case "background-color: rgb(180, 97, 175);":
                backgroundColorCode = "background-color: #B461AF";
                break;
            case "background-color: rgb(240, 195, 182);":
                backgroundColorCode = "background-color: #F0C3B6";
                break;
        }
        return backgroundColorCode;
    }
}
